package voice.playbackScreen

import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Test
import voice.common.BookId
import voice.common.DispatcherProvider
import voice.data.Book
import voice.data.BookContent
import voice.data.Bookmark
import voice.data.Chapter
import voice.data.ChapterId
import voice.pref.inmemory.InMemoryPref
import voice.sleepTimer.SleepTimer
import voice.sleepTimer.SleepTimerViewState
import java.time.Instant
import java.util.UUID
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class BookPlayViewModelTest {

  private val scope = TestScope()
  private val sleepTimerPref = InMemoryPref(15)
  private val autoSleepTimerPref = InMemoryPref(false)
  private val autoSleepTimeStartPref = InMemoryPref("22:00")
  private val autoSleepTimeEndPref = InMemoryPref("05:00")
  private val currentVolumePref = InMemoryPref(20)
  private val paddingPref = InMemoryPref("0;0;0;0")
  private val playButtonStylePref = InMemoryPref(1)
  private val playerBackgroundPref = InMemoryPref(1)
  private val repeatModePref = InMemoryPref(0)
  private val seekTimePref = InMemoryPref(20)
  private val seekTimeRewindPref = InMemoryPref(10)
  private val showSliderVolumePref = InMemoryPref(true)
  private val skipButtonStylePref = InMemoryPref(1)
  private val useGestures = InMemoryPref(true)
  private val useHapticFeedback = InMemoryPref(true)
  private val scanCoverChapter = InMemoryPref(true)
  private val book = book()
  private val sleepTimer = mockk<SleepTimer> {
    var sleepTimerActive = false
    every { sleepTimerActive() } answers { sleepTimerActive }
    coEvery { setActive(any<Duration>()) } answers {
      sleepTimerActive = true
    }
    coEvery { setActive(any<Boolean>()) } answers {
      sleepTimerActive = firstArg()
    }
  }
  private val viewModel = BookPlayViewModel(
    bookRepository = mockk {
      coEvery { get(book.id) } returns book
    },
    player = mockk(),
    sleepTimer = sleepTimer,
    autoSleepTimerPref = autoSleepTimerPref,
    autoSleepTimerStart = autoSleepTimeStartPref,
    autoSleepTimerEnd = autoSleepTimeEndPref,
    playStateManager = mockk(),
    currentBookId = mockk(),
    navigator = mockk(),
    bookmarkRepository = mockk {
      coEvery { addBookmarkAtBookPosition(book, any(), any()) } returns Bookmark(
        bookId = book.id,
        chapterId = book.currentChapter.id,
        addedAt = Instant.now(),
        setBySleepTimer = true,
        id = Bookmark.Id(UUID.randomUUID()),
        time = 0L,
        title = null,
      )
    },
    volumeGainFormatter = mockk(),
    batteryOptimization = mockk(),
    sleepTimePref = sleepTimerPref,
    bookId = book.id,
    dispatcherProvider = DispatcherProvider(scope.coroutineContext, scope.coroutineContext),
    audioVolume = mockk(),
    currentVolumePref = currentVolumePref,
    paddingPref = paddingPref,
    playButtonStylePref = playButtonStylePref,
    playerBackgroundPref = playerBackgroundPref,
    repeatModePref = repeatModePref,
    seekTimePref = seekTimePref,
    seekTimeRewindPref = seekTimeRewindPref,
    showSliderVolumePref = showSliderVolumePref,
    skipButtonStylePref = skipButtonStylePref,
    useGestures = useGestures,
    useHapticFeedback = useHapticFeedback,
    scanCoverChapter = scanCoverChapter
  )

  @Test
  fun sleepTimerValueChanging() = scope.runTest {
    fun assertDialogSleepTime(expected: Int) {
      viewModel.dialogState.value shouldBe BookPlayDialogViewState.SleepTimer(
        SleepTimerViewState(
          expected,
          autoSleepTimerPref.value,
          autoSleepTimeStartPref.value,
          autoSleepTimeEndPref.value,
        ),
      )
    }

    viewModel.toggleSleepTimer()
    assertDialogSleepTime(15)

    fun incrementAndAssert(time: Int) {
      viewModel.incrementSleepTime()
      assertDialogSleepTime(time)
    }

    fun decrementAndAssert(time: Int) {
      viewModel.decrementSleepTime()
      assertDialogSleepTime(time)
    }

    decrementAndAssert(10)
    decrementAndAssert(5)
    decrementAndAssert(4)
    decrementAndAssert(3)
    decrementAndAssert(2)
    decrementAndAssert(1)

    decrementAndAssert(1)

    incrementAndAssert(2)
    incrementAndAssert(3)
    incrementAndAssert(4)
    incrementAndAssert(5)
    incrementAndAssert(10)
    incrementAndAssert(15)
  }

  @Test
  fun sleepTimerSettingFixedValue() = scope.runTest {
    viewModel.toggleSleepTimer()
    viewModel.onAcceptSleepTime(10)
    sleepTimerPref.value shouldBe 15
    verify(exactly = 1) {
      sleepTimer.setActive(10.minutes)
    }
  }

  @Test
  fun deactivateSleepTimer() {
    viewModel.toggleSleepTimer()
    viewModel.onAcceptSleepTime(10)
    viewModel.toggleSleepTimer()
    verifyOrder {
      sleepTimer.setActive(10.minutes)
      sleepTimer.setActive(false)
    }
    sleepTimer.sleepTimerActive() shouldBe false
  }
}

private fun book(
  name: String = "TestBook",
  lastPlayedAtMillis: Long = 0L,
  addedAtMillis: Long = 0L,
): Book {
  val chapters = listOf(
    chapter(),
    chapter(),
  )
  return Book(
    content = BookContent(
      author = UUID.randomUUID().toString(),
      name = name,
      positionInChapter = 42,
      playbackSpeed = 1F,
      addedAt = Instant.ofEpochMilli(addedAtMillis),
      chapters = chapters.map { it.id },
      cover = null,
      currentChapter = chapters.first().id,
      isActive = true,
      lastPlayedAt = Instant.ofEpochMilli(lastPlayedAtMillis),
      skipSilence = false,
      showChapterNumbers = false,
      useChapterCover = false,
      repeatMode = 0,
      id = BookId(UUID.randomUUID().toString()),
      gain = 0F,
    ),
    chapters = chapters,
  )
}

private fun chapter(): Chapter {
  return Chapter(
    id = ChapterId("http://${UUID.randomUUID()}"),
    duration = 5.minutes.inWholeMilliseconds,
    fileLastModified = Instant.EPOCH,
    markData = emptyList(),
    name = "name",
    cover = null,
  )
}
