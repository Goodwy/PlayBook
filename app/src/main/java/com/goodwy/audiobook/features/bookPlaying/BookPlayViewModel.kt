package com.goodwy.audiobook.features.bookPlaying

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import de.paulwoitaschek.flowpref.Pref
import com.goodwy.audiobook.common.pref.PrefKeys
import com.goodwy.audiobook.data.Book
import com.goodwy.audiobook.data.durationMs
import com.goodwy.audiobook.data.markForPosition
import com.goodwy.audiobook.data.repo.BookRepository
import com.goodwy.audiobook.data.repo.BookmarkRepo
import com.goodwy.audiobook.playback.PlayerController
import com.goodwy.audiobook.playback.SleepTimer
import com.goodwy.audiobook.playback.playstate.PlayStateManager
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject
import javax.inject.Named
import kotlin.time.milliseconds

class BookPlayViewModel
@Inject constructor(
  private val repo: BookRepository,
  private val player: PlayerController,
  private val sleepTimer: SleepTimer,
  private val playStateManager: PlayStateManager,
  private val bookmarkRepo: BookmarkRepo,
  @Named(PrefKeys.CURRENT_BOOK)
  private val currentBookIdPref: Pref<UUID>,
  @Named(PrefKeys.REWIND_BUTTON_STYLE)
  private val rewindButtonStylePref: Pref<Int>
) {

  private val scope = MainScope()

  private val _viewEffects = BroadcastChannel<BookPlayViewEffect>(1)
  val viewEffects: Flow<BookPlayViewEffect> get() = _viewEffects.asFlow()

  lateinit var bookId: UUID

  fun viewState(): Flow<BookPlayViewState> {
    currentBookIdPref.value = bookId

    return combine(
      repo.flow(bookId).filterNotNull(), playStateManager.playStateFlow(), sleepTimer.leftSleepTimeFlow
    ) { book, playState, sleepTime ->
      val currentMark = book.content.currentChapter.markForPosition(book.content.positionInChapter)
      val hasMoreThanOneChapter = book.hasMoreThanOneChapter()
      val rewindButtonStyle = rewindButtonStylePref.value
      BookPlayViewState(
        sleepTime = sleepTime,
        playing = playState == PlayStateManager.PlayState.Playing,
        title = book.name,
        showPreviousNextButtons = hasMoreThanOneChapter,
        bookName = book.name,
        chapterName = currentMark.name.takeIf { hasMoreThanOneChapter },
        duration = currentMark.durationMs.milliseconds,
        playedTime = (book.content.positionInChapter - currentMark.startMs).milliseconds,
        cover = BookPlayCover(book),
        skipSilence = book.content.skipSilence,
        showChapterNumbers = book.content.showChapterNumbers,
        rewindButtonStylePref = rewindButtonStyle
      )
    }
  }

  private fun Book.hasMoreThanOneChapter(): Boolean {
    val chapterCount = content.chapters.sumBy { it.chapterMarks.size }
    return chapterCount > 1
  }

  fun next() {
    player.next()
  }

  fun previous() {
    player.previous()
  }

  fun playPause() {
    player.playPause()
  }

  fun rewind() {
    player.rewind()
  }

  fun fastForward() {
    player.fastForward()
  }

  fun addBookmark() {
    scope.launch {
      val book = repo.bookById(bookId) ?: return@launch
      val title = book.content.currentChapter.name

      bookmarkRepo.addBookmarkAtBookPosition(
        book = book,
        title = title,
        setBySleepTimer = false
      )
      _viewEffects.send(BookPlayViewEffect.BookmarkAdded)
    }
  }

  fun seekTo(ms: Long) {
    scope.launch {
      val book = repo.bookById(bookId) ?: return@launch
      val currentChapter = book.content.currentChapter
      val currentMark = currentChapter.markForPosition(book.content.positionInChapter)
      player.setPosition(currentMark.startMs + ms, currentChapter.file)
    }
  }

  fun toggleSleepTimer() {
    if (sleepTimer.sleepTimerActive()) {
      sleepTimer.setActive(false)
    } else {
      _viewEffects.offer(BookPlayViewEffect.ShowSleepTimeDialog)
    }
  }

  fun toggleSkipSilence() {
    scope.launch {
      val skipSilence = repo.bookById(bookId)?.content?.skipSilence
        ?: return@launch
      player.skipSilence(!skipSilence)
    }
  }

  fun toggleShowChapterNumbers() {
    scope.launch {
      val showChapterNumbers = repo.bookById(bookId)?.content?.showChapterNumbers
        ?: return@launch
      player.showChapterNumbers(!showChapterNumbers)
    }
  }
}
