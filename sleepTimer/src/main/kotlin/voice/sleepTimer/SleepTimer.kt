package voice.sleepTimer

import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.squareup.anvil.annotations.ContributesBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import voice.common.AppScope
import voice.common.BookId
import voice.common.pref.PrefKeys
import voice.data.repo.BookRepository
import voice.logging.core.Logger
import voice.playback.PlayerController
import voice.playback.playstate.PlayStateManager
import voice.playback.playstate.PlayStateManager.PlayState.Playing
import voice.pref.Pref
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import voice.playback.session.SleepTimer as PlaybackSleepTimer

@Singleton
@ContributesBinding(AppScope::class)
class SleepTimer
@Inject constructor(
  private val playStateManager: PlayStateManager,
  private val shakeDetector: ShakeDetector,
  @Named(PrefKeys.SLEEP_TIME)
  private val sleepTimePref: Pref<Int>,
  private val playerController: PlayerController,
  private val bookRepo: BookRepository,
) : PlaybackSleepTimer {

  private val scope = MainScope()
  private val fadeOutDuration = 10.seconds

  private val _leftSleepTime = MutableStateFlow(Duration.ZERO)
  private val _sleepAtEoc = MutableStateFlow(false)
  private var leftSleepTime: Duration
    get() = _leftSleepTime.value
    set(value) {
      _leftSleepTime.value = value
    }
  private var sleepAtEoc: Boolean
    get() = _sleepAtEoc.value
    set(value) {
      _sleepAtEoc.value = value
    }
  override val leftSleepTimeFlow: StateFlow<Duration> get() = _leftSleepTime
  override val sleepAtEocFlow: StateFlow<Boolean> get() = _sleepAtEoc

  override fun sleepTimerActive(): Boolean = sleepJob?.isActive == true && leftSleepTime > Duration.ZERO

  private var sleepJob: Job? = null

  override fun setActive(enable: Boolean) {
    Logger.i("enable=$enable")
    if (enable) {
      setActive()
    } else {
      cancel()
    }
  }

  override fun setEoc(enable: Boolean, bookId: BookId) {
    Logger.i("sleep at EOC enable=$enable")
    if (enable) {
      startEoc(bookId)
    } else {
      cancel()
    }
  }

  fun setActive(sleepTime: Duration = sleepTimePref.value.minutes) {
    Logger.i("Starting sleepTimer. Pause in $sleepTime.")
    leftSleepTime = sleepTime
    playerController.setVolume(1F)
    sleepJob?.cancel()
    sleepJob = scope.launch {
      startSleepTimerCountdown()
      val shakeToResetTime = 30.seconds
      Logger.d("Wait for $shakeToResetTime for a shake")
      withTimeout(shakeToResetTime) {
        shakeDetector.detect()
        Logger.i("Shake detected. Reset sleep time")
        playerController.play()
        setActive()
      }
      Logger.i("exiting")
    }
  }

  private fun startEoc(bookId: BookId) {
    Logger.i("Starting sleepTimer. Pause at end of chapter.")
    sleepAtEoc = true
    sleepJob?.cancel()
    sleepJob = scope.launch {
      startSleepEocCountdown(bookId)
      sleepAtEoc = false
      val shakeToResetTime = 30.seconds
      Logger.d("Wait for $shakeToResetTime for a shake")
      withTimeout(shakeToResetTime) {
        shakeDetector.detect()
        Logger.i("Shake detected. Reset sleep time")
        playerController.play()
        startEoc(bookId)
      }
      Logger.i("exiting")
    }
    playerController.setVolume(1F)
  }

  private suspend fun startSleepTimerCountdown() {
    var interval = 500.milliseconds
    while (leftSleepTime > Duration.ZERO) {
      suspendUntilPlaying()
      if (leftSleepTime < fadeOutDuration) {
        interval = 200.milliseconds
        updateVolumeForSleepTime()
      }
      delay(interval)
      leftSleepTime = (leftSleepTime - interval).coerceAtLeast(Duration.ZERO)
    }
    playerController.pauseWithRewind(fadeOutDuration)
    playerController.setVolume(1F)
  }

  private suspend fun startSleepEocCountdown(bookId: BookId) {
    var book = bookRepo.get(bookId) ?: return
    val chapter = book.content.currentChapterIndex

    while (true) {
      suspendUntilPlaying()

      book = bookRepo.get(bookId) ?: return
      if (chapter != book.content.currentChapterIndex) {
        break
      }

      val timeLeft = ((book.currentChapter.duration - book.content.positionInChapter) * book.content.playbackSpeed).toLong() / 2

      delay(timeLeft.coerceAtLeast(125).coerceAtMost(5000).milliseconds)
    }
    playerController.playPause()
  }

  private fun updateVolumeForSleepTime() {
    val percentageOfTimeLeft = if (leftSleepTime == Duration.ZERO) {
      0F
    } else {
      (leftSleepTime / fadeOutDuration).toFloat()
    }.coerceIn(0F, 1F)

    val volume = 1 - FastOutSlowInInterpolator().getInterpolation(1 - percentageOfTimeLeft)
    playerController.setVolume(volume)
  }

  private suspend fun suspendUntilPlaying() {
    if (playStateManager.playState != Playing) {
      Logger.i("Not playing. Wait for Playback to continue.")
      playStateManager.flow
        .filter { it == Playing }
        .first()
      Logger.i("Playback continued.")
    }
  }

  private fun cancel() {
    sleepJob?.cancel()
    leftSleepTime = Duration.ZERO
    playerController.setVolume(1F)
    sleepAtEoc = false
  }
}
