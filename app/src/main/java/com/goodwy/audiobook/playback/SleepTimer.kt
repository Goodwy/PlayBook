package com.goodwy.audiobook.playback

import de.paulwoitaschek.flowpref.Pref
import com.goodwy.audiobook.common.pref.PrefKeys
import com.goodwy.audiobook.playback.player.FADE_OUT_DURATION
import com.goodwy.audiobook.data.repo.BookRepository
import com.goodwy.audiobook.playback.playstate.PlayStateManager
import com.goodwy.audiobook.playback.playstate.PlayStateManager.PlayState.Playing
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton
import kotlin.time.Duration
import kotlin.time.milliseconds
import kotlin.time.minutes
import kotlin.time.seconds

@Singleton
class SleepTimer
@Inject constructor(
  @Named(PrefKeys.CURRENT_BOOK)
  private val currentBookIdPref: Pref<UUID>,
  private val repo: BookRepository,
  private val playStateManager: PlayStateManager,
  private val shakeDetector: ShakeDetector,
  @Named(PrefKeys.SHAKE_TO_RESET)
  shakeToResetPref: Pref<Boolean>,
  @Named(PrefKeys.SLEEP_TIME)
  private val sleepTimePref: Pref<Int>,
  @Named(PrefKeys.SLEEP_TIMER_CURRENT_CHAPTER)
  private val sleepTimeCurrentChapterPref: Pref<Int>,
  private val playerController: PlayerController
) {
  private val scope = MainScope()
  private val shakeToResetEnabled by shakeToResetPref
  private val sleepTime: Duration get() = sleepTimePref.value.minutes

  private val _leftSleepTime = ConflatedBroadcastChannel(Duration.ZERO)
  private var leftSleepTime: Duration
    get() = _leftSleepTime.value
    set(value) {
      _leftSleepTime.offer(value)
    }
  val leftSleepTimeFlow: Flow<Duration> get() = _leftSleepTime.asFlow()

  fun sleepTimerActive(): Boolean = sleepJob?.isActive == true && leftSleepTime > Duration.ZERO

  private var sleepJob: Job? = null

  fun setActive(enable: Boolean, countdown: Boolean) {
    Timber.i("enable=$enable")
    if (enable) {
      if (countdown) {
        start()
      } else {
        startCurrentChapter()
      }
    } else {
      cancel()
    }
  }

  private fun start() {
    Timber.i("Starting sleepTimer. Pause in $sleepTime.")
    leftSleepTime = sleepTime
    sleepJob?.cancel()
    sleepJob = scope.launch {
      launch { restartTimerOnShake() }
      startSleepTimerCountdown()
      val shakeToResetTime = 30.seconds
      Timber.d("Wait for $shakeToResetTime for a shake")
      withTimeout(shakeToResetTime) {
        shakeDetector.detect().first()
        Timber.i("Shake detected. Reset sleep time")
        playerController.play()
        start()
      }
      Timber.i("exiting")
    }
  }

  private suspend fun restartTimerOnShake() {
    if (shakeToResetEnabled) {
      shakeDetector.detect()
        .collect {
          Timber.i("Shake detected. Reset sleep time")
          if (leftSleepTime > Duration.ZERO) {
            playerController.cancelFadeout()
            leftSleepTime = sleepTime
          } else {
            playerController.play()
            start()
          }
        }
    }
  }

  private suspend fun startSleepTimerCountdown() {
    val interval = 500.milliseconds
    var fadeOutSent = false
    while (leftSleepTime > Duration.ZERO) {
      suspendUntilPlaying()
      delay(interval)
      leftSleepTime = (leftSleepTime - interval).coerceAtLeast(Duration.ZERO)
      if (leftSleepTime <= FADE_OUT_DURATION && !fadeOutSent) {
        fadeOutSent = true
        playerController.fadeOut()
      }
    }
   // playerController.pause()
  }

  private fun startCurrentChapter() {
    Timber.i("Starting sleepTimer. Pause in $sleepTime.")
    leftSleepTime = 1000.minutes
    sleepJob?.cancel()
    sleepJob = scope.launch {
      startSleepTimerEndOfCurrentChapter()
      val shakeToResetTime = 30.seconds
      Timber.d("Wait for $shakeToResetTime for a shake")
      withTimeout(shakeToResetTime) {
        shakeDetector.detect().first()
        Timber.i("Shake detected. Reset sleep time end of current chapter")
        playerController.play()
        startCurrentChapter()
      }
      Timber.i("exiting")
    }
  }

  private suspend fun startSleepTimerEndOfCurrentChapter() {
    val book = repo.bookById(currentBookIdPref.value)
    val currentChapter = book?.content?.currentChapterIndex
    val interval = 500.milliseconds
    while (currentChapter == sleepTimeCurrentChapterPref.value) {
      val book = repo.bookById(currentBookIdPref.value)
      val current = book?.content?.currentChapterIndex
      sleepTimeCurrentChapterPref.value = current!!
      suspendUntilPlaying()
      delay(interval)
      //leftSleepTime = (leftSleepTime - interval).coerceAtLeast(Duration.ZERO)
    }
    playerController.pause()
    leftSleepTime = Duration.ZERO
  }

  private suspend fun suspendUntilPlaying() {
    if (playStateManager.playState != Playing) {
      Timber.i("Not playing. Wait for Playback to continue.")
      playStateManager.playStateFlow()
        .filter { it == Playing }
        .first()
      Timber.i("Playback continued.")
    }
  }

  private fun cancel() {
    sleepJob?.cancel()
    leftSleepTime = Duration.ZERO
  }
}
