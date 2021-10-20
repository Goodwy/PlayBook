package com.goodwy.audiobooklite.playback.session

import android.app.Notification
import android.app.NotificationManager
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.content.ContextCompat
import androidx.media.MediaBrowserServiceCompat
import de.paulwoitaschek.flowpref.Pref
import com.goodwy.audiobooklite.common.pref.PrefKeys
import com.goodwy.audiobooklite.data.Book
import com.goodwy.audiobooklite.data.repo.BookRepository
import com.goodwy.audiobooklite.playback.androidauto.AndroidAutoConnectedReceiver
import com.goodwy.audiobooklite.playback.androidauto.NotifyOnAutoConnectionChange
import com.goodwy.audiobooklite.playback.di.PlaybackComponentFactoryProvider
import com.goodwy.audiobooklite.playback.misc.flowBroadcastReceiver
import com.goodwy.audiobooklite.playback.notification.NotificationCreator
import com.goodwy.audiobooklite.playback.player.MediaPlayer
import com.goodwy.audiobooklite.playback.playstate.PlayStateManager
import com.goodwy.audiobooklite.playback.playstate.PlayStateManager.PauseReason
import com.goodwy.audiobooklite.playback.playstate.PlayStateManager.PlayState
import com.goodwy.audiobooklite.playback.session.headset.HeadsetState
import com.goodwy.audiobooklite.playback.session.headset.headsetStateChangeFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject
import javax.inject.Named

private const val NOTIFICATION_ID = 42

/**
 * Service that hosts the longtime playback and handles its controls.
 */
class PlaybackService : MediaBrowserServiceCompat() {

  private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

  @field:[Inject Named(PrefKeys.CURRENT_BOOK)]
  lateinit var currentBookIdPref: Pref<UUID>
  @Inject
  lateinit var player: MediaPlayer
  @Inject
  lateinit var repo: BookRepository
  @Inject
  lateinit var notificationManager: NotificationManager
  @Inject
  lateinit var notificationCreator: NotificationCreator
  @Inject
  lateinit var playStateManager: PlayStateManager
  @Inject
  lateinit var bookUriConverter: BookUriConverter
  @Inject
  lateinit var mediaBrowserHelper: MediaBrowserHelper
  @Inject
  lateinit var mediaSession: MediaSessionCompat
  @Inject
  lateinit var changeNotifier: ChangeNotifier
  @Inject
  lateinit var autoConnected: AndroidAutoConnectedReceiver
  @Inject
  lateinit var notifyOnAutoConnectionChange: NotifyOnAutoConnectionChange
  @field:[Inject Named(PrefKeys.RESUME_ON_REPLUG)]
  lateinit var resumeOnReplugPref: Pref<Boolean>
  @Inject
  lateinit var mediaController: MediaControllerCompat
  @Inject
  lateinit var callback: MediaSessionCallback

  private var isForeground = false

  override fun onCreate() {
    (application as PlaybackComponentFactoryProvider)
      .factory()
      .create(this)
      .inject(this)
    super.onCreate()

    // this is necessary because otherwise after a the service gets restarted,
    // the media session is not updated any longer.
    notificationManager.cancel(NOTIFICATION_ID)

    mediaSession.isActive = true
    mediaSession.setCallback(callback)
    sessionToken = mediaSession.sessionToken

    player.updateMediaSessionPlaybackState()

    mediaController.registerCallback(MediaControllerCallback())

    scope.launch {
      player.bookContentFlow
        .distinctUntilChangedBy { it.settings }
        .collect { content ->
          val updatedBook = repo.updateBookContent(content)
          if (updatedBook != null) {
            changeNotifier.updateMetadata(updatedBook)
            updateNotification(updatedBook)
          }
        }
    }

    scope.launch {
      notifyOnAutoConnectionChange.listen()
    }

    scope.launch {
      headsetStateChangeFlow()
        .filter { it == HeadsetState.Plugged }
        .collect {
          headsetPlugged()
        }
    }

    scope.launch {
      repo.flow()
        .map { it.size }
        .distinctUntilChanged()
        .collect {
          notifyChildrenChanged(bookUriConverter.allBooksId())
        }
    }

    scope.launch {
      flowBroadcastReceiver(IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY)).collect {
        audioBecomingNoisy()
      }
    }
  }

  private suspend fun updateNotification(book: Book): Notification {
    return notificationCreator.createNotification(book).also {
      notificationManager.notify(NOTIFICATION_ID, it)
    }
  }

  private fun headsetPlugged() {
    if (playStateManager.pauseReason == PauseReason.BECAUSE_HEADSET) {
      if (resumeOnReplugPref.value) {
        mediaController.transportControls.play()
      }
    }
  }

  private fun audioBecomingNoisy() {
    Timber.d("audio becoming noisy. playState=${playStateManager.playState}")
    if (playStateManager.playState === PlayState.Playing) {
      playStateManager.pauseReason = PauseReason.BECAUSE_HEADSET
      player.pause(true)
    }
  }

  override fun onLoadChildren(
    parentId: String,
    result: Result<List<MediaBrowserCompat.MediaItem>>
  ) {
    result.detach()
    scope.launch {
      val children = mediaBrowserHelper.loadChildren(parentId)
      result.sendResult(children)
    }
  }

  override fun onGetRoot(
    clientPackageName: String,
    clientUid: Int,
    rootHints: Bundle?
  ): BrowserRoot {
    return BrowserRoot(mediaBrowserHelper.root(), null)
  }

  override fun onDestroy() {
    scope.cancel()
    mediaSession.release()
    player.release()
    super.onDestroy()
  }

  private suspend fun updateNotification(state: PlaybackStateCompat) {
    val updatedState = state.state

    val book = repo.bookById(currentBookIdPref.value)
    val notification = if (book != null &&
      updatedState != PlaybackStateCompat.STATE_NONE
    ) {
      notificationCreator.createNotification(book)
    } else {
      null
    }

    when (updatedState) {
      PlaybackStateCompat.STATE_BUFFERING,
      PlaybackStateCompat.STATE_PLAYING -> {
        if (notification != null) {
          notificationManager.notify(NOTIFICATION_ID, notification)

          if (!isForeground) {
            ContextCompat.startForegroundService(
              applicationContext,
              Intent(applicationContext, this@PlaybackService.javaClass)
            )
            startForeground(NOTIFICATION_ID, notification)
            isForeground = true
          }
        }
      }
      else -> {
        if (isForeground) {
          stopForeground(false)
          isForeground = false

          if (updatedState == PlaybackStateCompat.STATE_NONE) {
            stopSelf()
          }

          if (notification != null) {
            notificationManager.notify(NOTIFICATION_ID, notification)
          } else {
            removeNowPlayingNotification()
          }
        }
      }
    }
  }

  private fun removeNowPlayingNotification() {
    notificationManager.cancel(NOTIFICATION_ID)
  }

  private inner class MediaControllerCallback : MediaControllerCompat.Callback() {

    override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
      mediaController.playbackState?.let { state ->
        scope.launch {
          player.updateMediaSessionPlaybackState()
          updateNotification(state)
        }
      }
    }

    override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
      state ?: return
      scope.launch {
        updateNotification(state)
      }
    }
  }
}
