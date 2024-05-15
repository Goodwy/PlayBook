package voice.playbackScreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.datastore.core.DataStore
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import de.paulwoitaschek.flowpref.Pref
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import voice.common.BookId
import voice.common.DispatcherProvider
import voice.common.compose.ImmutableFile
import voice.common.constants.PLAYER_BACKGROUND_THEME
import voice.common.constants.PLAY_BUTTON_ROUND
import voice.common.constants.REPEAT_ALL
import voice.common.constants.REPEAT_OFF
import voice.common.constants.REPEAT_ONE
import voice.common.constants.SKIP_BUTTON_ROUND
import voice.common.navigation.Destination
import voice.common.navigation.Navigator
import voice.common.pref.CurrentBook
import voice.common.pref.PrefKeys
import voice.data.durationMs
import voice.data.markForPosition
import voice.data.repo.BookRepository
import voice.data.repo.BookmarkRepo
import voice.logging.core.Logger
import voice.playback.PlayerController
import voice.playback.misc.Decibel
import voice.playback.misc.VolumeGain
import voice.playback.playstate.PlayStateManager
import voice.playbackScreen.batteryOptimization.BatteryOptimization
import voice.sleepTimer.SleepTimer
import voice.sleepTimer.SleepTimerViewState
import javax.inject.Named
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes

class BookPlayViewModel
@AssistedInject constructor(
  @Named(PrefKeys.SEEK_TIME)
  private val seekTimePref: Pref<Int>,
  @Named(PrefKeys.SEEK_TIME_REWIND)
  private val seekTimeRewindPref: Pref<Int>,
  @Named(PrefKeys.SKIP_BUTTON_STYLE)
  private val skipButtonStylePref: Pref<Int>,
  @Named(PrefKeys.PLAY_BUTTON_STYLE)
  private val playButtonStylePref: Pref<Int>,
  @Named(PrefKeys.CURRENT_VOLUME)
  private val currentVolumePref: Pref<Int>,
  @Named(PrefKeys.SHOW_SLIDER_VOLUME)
  private val showSliderVolumePref: Pref<Boolean>,
  @Named(PrefKeys.PADDING)
  private val paddingPref: Pref<String>,
  @Named(PrefKeys.PLAYER_BACKGROUND)
  private val playerBackgroundPref: Pref<Int>,
  @Named(PrefKeys.REPEAT_MODE)
  private val repeatModePref: Pref<Int>,
  @Named(PrefKeys.SLEEP_TIME)
  private val sleepTimePref: Pref<Int>,
  @Named(PrefKeys.USE_GESTURES)
  private val useGestures: Pref<Boolean>,
  @Named(PrefKeys.USE_HAPTIC_FEEDBACK)
  private val useHapticFeedback: Pref<Boolean>,
  @Named(PrefKeys.SCAN_COVER_CHAPTER)
  private val scanCoverChapter: Pref<Boolean>,
  private val bookRepository: BookRepository,
  private val player: PlayerController,
  private val sleepTimer: SleepTimer,
  private val playStateManager: PlayStateManager,
  @CurrentBook
  private val currentBookId: DataStore<BookId?>,
  private val navigator: Navigator,
  private val bookmarkRepository: BookmarkRepo,
  private val volumeGainFormatter: VolumeGainFormatter,
  private val batteryOptimization: BatteryOptimization,
  private val audioVolume: AudioVolume,
  dispatcherProvider: DispatcherProvider,
  @Assisted
  private val bookId: BookId,
) {

  private val scope = CoroutineScope(SupervisorJob() + dispatcherProvider.main)

  private val _viewEffects = MutableSharedFlow<BookPlayViewEffect>(extraBufferCapacity = 1)
  internal val viewEffects: Flow<BookPlayViewEffect> get() = _viewEffects

  private val _dialogState = mutableStateOf<BookPlayDialogViewState?>(null)
  internal val dialogState: State<BookPlayDialogViewState?> get() = _dialogState

  @Composable
  internal fun state(): PrefViewState {
    val repeatModePref by remember { repeatModePref.flow }.collectAsState(initial = REPEAT_OFF)
    return PrefViewState(
      repeatMode = repeatModePref
    )
  }

  @Composable
  fun viewState(): BookPlayViewState? {
    player.pauseIfCurrentBookDifferentFrom(bookId)
    LaunchedEffect(Unit) {
      currentBookId.updateData { bookId }
    }

    val book = remember { bookRepository.flow(bookId).filterNotNull() }.collectAsState(initial = null).value
      ?: return null

    val playState by remember {
      playStateManager.flow
    }.collectAsState(initial = PlayStateManager.PlayState.Playing)

    val sleepTime by remember { sleepTimer.leftSleepTimeFlow }.collectAsState()
    val sleepAtEoc by remember { sleepTimer.sleepAtEocFlow }.collectAsState()
    val seekTime by remember { seekTimePref.flow }.collectAsState(initial = 0)
    val seekTimeRewind by remember { seekTimeRewindPref.flow }.collectAsState(initial = 0)
    val currentVolumePref by remember { currentVolumePref.flow }.collectAsState(initial = 0)
    val showSliderVolumePref by remember { showSliderVolumePref.flow }.collectAsState(initial = true)
    val skipButtonStyle by remember { skipButtonStylePref.flow }.collectAsState(initial = SKIP_BUTTON_ROUND)
    val playButtonStyle by remember { playButtonStylePref.flow }.collectAsState(initial = PLAY_BUTTON_ROUND)
    val paddings by remember { paddingPref.flow }.collectAsState(initial = "0;0;0;0")
    val playerBackgroundPref by remember { playerBackgroundPref.flow }.collectAsState(initial = PLAYER_BACKGROUND_THEME)
    val useGestures = useGestures.flow.collectAsState(initial = true).value
    val useHapticFeedback = useHapticFeedback.flow.collectAsState(initial = true).value
    val sleepTimePref = sleepTimePref.flow.collectAsState(initial = 15).value
    val scanCoverChapter = scanCoverChapter.flow.collectAsState(initial = true).value

    val currentMark = book.currentChapter.markForPosition(book.content.positionInChapter)
    val hasMoreThanOneChapter = book.chapters.sumOf { it.chapterMarks.count() } > 1
    val chapterMarks = book.chapters.flatMap {
      it.chapterMarks
    }
    val selectedIndex = chapterMarks.indexOf(book.currentMark)
    return BookPlayViewState(
      sleepTime = sleepTime,
      customSleepTime = sleepTimePref,
      sleepEoc = sleepAtEoc,
      playing = playState == PlayStateManager.PlayState.Playing,
      title = book.content.name,
      showPreviousNextButtons = hasMoreThanOneChapter,
      chapterName = currentMark.name.takeIf { hasMoreThanOneChapter },
      duration = currentMark.durationMs.milliseconds,
      playedTime = (book.content.positionInChapter - currentMark.startMs).milliseconds,
      cover = if (book.content.useChapterCover && scanCoverChapter) book.currentChapter.cover?.let(::ImmutableFile) else book.content.cover?.let(::ImmutableFile),
      skipSilence = book.content.skipSilence,
      showChapterNumbers = book.content.showChapterNumbers,
      useChapterCover = book.content.useChapterCover,
      scanCoverChapter = scanCoverChapter,
      playedTimeInPer = (book.position * 100) / book.duration,
      remainingTimeInMs = book.duration - book.position,
      bookDuration = book.duration,
      seekTime = seekTime,
      seekTimeRewind = seekTimeRewind,
      currentVolume = currentVolumePref,
      showSliderVolume = showSliderVolumePref,
      playbackSpeed = book.content.playbackSpeed,
      skipButtonStyle = skipButtonStyle,
      playButtonStyle = playButtonStyle,
      paddings = paddings,
      chapterMarks = chapterMarks,
      selectedIndex = selectedIndex.takeUnless { it == -1 },
      author = book.content.author,
      playerBackground = playerBackgroundPref,
      repeatModeBook = book.content.repeatMode,
      useGestures = useGestures,
      useHapticFeedback = useHapticFeedback,
    )
  }

  fun dismissDialog() {
    Logger.d("dismissDialog")
    _dialogState.value = null
  }

  fun incrementSleepTime() {
    updateSleepTimeViewState {
      val customTime = it.customSleepTime
      val newTime = when {
        customTime < 5 -> customTime + 1
        else -> customTime + 5
      }
      sleepTimePref.value = newTime
      SleepTimerViewState(newTime)
    }
  }

  fun decrementSleepTime() {
    updateSleepTimeViewState {
      val customTime = it.customSleepTime
      val newTime = when {
        customTime <= 1 -> 1
        customTime <= 5 -> customTime - 1
        else -> (customTime - 5).coerceAtLeast(5)
      }
      sleepTimePref.value = newTime
      SleepTimerViewState(newTime)
    }
  }

  fun onAcceptSleepTime(time: Int) {
    updateSleepTimeViewState {
      scope.launch {
        val book = bookRepository.get(bookId) ?: return@launch
        bookmarkRepository.addBookmarkAtBookPosition(
          book = book,
          setBySleepTimer = true,
          title = null,
        )
      }
      sleepTimer.setActive(time.minutes)
      null
    }
  }

  private fun updateSleepTimeViewState(update: (SleepTimerViewState) -> SleepTimerViewState?) {
    val current = dialogState.value
    val updated: SleepTimerViewState? = if (current is BookPlayDialogViewState.SleepTimer) {
      update(current.viewState)
    } else {
      update(SleepTimerViewState(sleepTimePref.value))
    }
    _dialogState.value = updated?.let(BookPlayDialogViewState::SleepTimer)
  }

  fun onPlaybackSpeedChanged(speed: Float) {
    _dialogState.value = BookPlayDialogViewState.SpeedDialog(speed)
    player.setSpeed(speed)
  }

  fun onVolumeGainChanged(gain: Decibel) {
    _dialogState.value = volumeGainDialogViewState(gain)
    player.setGain(gain)
  }

  fun next() {
    player.next()
  }

  fun previous() {
    player.previous()
  }

  fun onCurrentTimeClicked() {
    _dialogState.value = BookPlayDialogViewState.JumpToPosition(1.hours)
  }

  fun playPause() {
    if (playStateManager.playState != PlayStateManager.PlayState.Playing) {
      scope.launch {
        if (batteryOptimization.shouldRequest()) {
          _viewEffects.tryEmit(BookPlayViewEffect.RequestIgnoreBatteryOptimization)
          batteryOptimization.onBatteryOptimizationsRequested()
        }
      }
    }
    player.playPause()
  }

  fun rewind() {
    player.rewind()
  }

  fun fastForward() {
    player.fastForward()
  }

  /*fun onCurrentChapterClicked() {
    scope.launch {
      val book = bookRepository.get(bookId) ?: return@launch
      val chapterMarks = book.chapters.flatMap {
        it.chapterMarks
      }
      val selectedIndex = chapterMarks.indexOf(book.currentMark)
      _dialogState.value = BookPlayDialogViewState.SelectChapterDialog(
        chapters = chapterMarks,
        selectedIndex = selectedIndex.takeUnless { it == -1 },
      )
    }
  }*/

  fun onChapterClicked(index: Int) {
    scope.launch {
      val book = bookRepository.get(bookId) ?: return@launch
      var currentIndex = -1
      book.chapters.forEach { chapter ->
        chapter.chapterMarks.forEach { mark ->
          currentIndex++
          if (currentIndex == index) {
            player.setPosition(mark.startMs, chapter.id)
            _dialogState.value = null
            return@launch
          }
        }
      }
    }
  }

  fun onPlaybackSpeedIconClicked() {
    scope.launch {
      val playbackSpeed = bookRepository.get(bookId)?.content?.playbackSpeed
      if (playbackSpeed != null) {
        _dialogState.value = BookPlayDialogViewState.SpeedDialog(playbackSpeed)
      }
    }
  }

  fun onVolumeGainIconClicked() {
    scope.launch {
      val content = bookRepository.get(bookId)?.content
      if (content != null) {
        _dialogState.value = volumeGainDialogViewState(Decibel(content.gain))
      }
    }
  }

  private fun volumeGainDialogViewState(gain: Decibel): BookPlayDialogViewState.VolumeGainDialog {
    return BookPlayDialogViewState.VolumeGainDialog(
      gain = gain,
      maxGain = VolumeGain.MAX_GAIN,
      valueFormatted = volumeGainFormatter.format(gain),
    )
  }

  fun onBookmarkClicked() {
    navigator.goTo(Destination.Bookmarks(bookId))
  }

  fun onBookmarkLongClicked() {
    scope.launch {
      val book = bookRepository.get(bookId) ?: return@launch
      bookmarkRepository.addBookmarkAtBookPosition(
        book = book,
        title = null,
        setBySleepTimer = false,
      )
      _viewEffects.tryEmit(BookPlayViewEffect.BookmarkAdded)
    }
  }

  fun seekTo(position: Duration) {
    scope.launch {
      val book = bookRepository.get(bookId) ?: return@launch
      val currentChapter = book.currentChapter
      val currentMark = currentChapter.markForPosition(book.content.positionInChapter)
      if (position <= currentMark.durationMs.milliseconds) {
        player.setPosition(currentMark.startMs + position.inWholeMilliseconds, currentChapter.id)
      } else {
        Logger.d("Invalid seek with mark=$currentMark, position=$position")
      }
    }
  }

  fun onJumpToPositionTimeSelected(position: Duration) {
    _dialogState.value = null
    seekTo(position)
  }

  fun seekVolumeTo(position: Int) {
    scope.launch {
      audioVolume.volumeChangeTo(position)
      currentVolumePref.value = position
    }
  }

  fun toggleSleepTimer() {
    Logger.d("toggleSleepTimer while active=${sleepTimer.sleepTimerActive()}")
    if (sleepTimer.sleepTimerActive() || sleepTimer.sleepAtEocFlow.value) {
      sleepTimer.setActive(false)
      _dialogState.value = null
    } else {
      _dialogState.value = BookPlayDialogViewState.SleepTimer(SleepTimerViewState(sleepTimePref.value))
    }
  }

  fun toggleSkipSilence() {
    scope.launch {
      val book = bookRepository.get(bookId) ?: return@launch
      val skipSilence = book.content.skipSilence
      player.skipSilence(!skipSilence)
    }
  }

  fun toggleRepeat() {
    scope.launch {
      val book = bookRepository.get(bookId) ?: return@launch
      val repeatModeBookNew = when (book.content.repeatMode) {
        REPEAT_OFF -> REPEAT_ONE
        REPEAT_ONE -> REPEAT_ALL
        else -> REPEAT_OFF
      }
      player.setRepeat(repeatModeBookNew)
    }
  }

  fun toggleShowChapterNumbers() {
    scope.launch {
      val book = bookRepository.get(bookId) ?: return@launch
      val showChapterNumbers = book.content.showChapterNumbers
      player.showChapterNumbers(!showChapterNumbers)
    }
  }

  fun toggleUseChapterCover() {
    scope.launch {
      val book = bookRepository.get(bookId) ?: return@launch
      val useChapterCover = book.content.useChapterCover
      player.useChapterCover(!useChapterCover)
    }
  }

  @AssistedFactory
  interface Factory {
    fun create(bookId: BookId): BookPlayViewModel
  }

  fun onAcceptSleepEoc() {
    updateSleepTimeViewState {
      sleepTimer.setActive(false)
      sleepTimer.setEoc(true, bookId)
      null
    }
  }
}
