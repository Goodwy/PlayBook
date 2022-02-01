package com.goodwy.audiobook.features.bookPlaying.selectchapter

import com.goodwy.audiobook.data.markForPosition
import com.goodwy.audiobook.data.repo.BookRepository
import com.goodwy.audiobook.features.bookOverview.list.remainingTimeInMs
import com.goodwy.audiobook.features.bookPlaying.BookPlayCover
import com.goodwy.audiobook.playback.PlayerController
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject

class SelectChapterViewModel
@Inject constructor(
  private val bookRepository: BookRepository,
  private val player: PlayerController
) {

  private val scope = MainScope()

  private val _viewEffects = BroadcastChannel<SelectChapterViewEffect>(1)
  val viewEffects: Flow<SelectChapterViewEffect> get() = _viewEffects.asFlow()

  lateinit var bookId: UUID

  fun viewState(): SelectChapterViewState {
    val book = bookRepository.bookById(bookId)

    if (book == null) {
      Timber.d("no book found for $bookId. CloseScreen")
      _viewEffects.offer(SelectChapterViewEffect.CloseScreen)
      return SelectChapterViewState(emptyList(), null, null, null, false, BookPlayCover(book!!), null, null)
    }

    val chapterMarks = book.content.chapters.flatMap {
      it.chapterMarks
    }
    val currentMark = book.content.currentChapter.markForPosition(book.content.positionInChapter)
    val selectedIndex = chapterMarks.indexOf(currentMark)
    val showChapterNumbers = book.content.showChapterNumbers
    val bookName = book.name
    val bookAuthor = book.author
    val bookDuration = book.content.duration
    val cover = BookPlayCover(book)
    val remainingTimeInMs = book.remainingTimeInMs()
    return SelectChapterViewState(chapterMarks, selectedIndex.takeUnless { it == -1 }, bookName, bookDuration, showChapterNumbers, cover, bookAuthor, remainingTimeInMs)
  }

  fun chapterClicked(index: Int) {
    scope.launch {
      val book = bookRepository.bookById(bookId) ?: return@launch
      var currentIndex = -1
      book.content.chapters.forEach { chapter ->
        chapter.chapterMarks.forEach { mark ->
          currentIndex++
          if (currentIndex == index) {
            player.setPosition(mark.startMs, chapter.file)
            _viewEffects.offer(SelectChapterViewEffect.CloseScreen)
            return@launch
          }
        }
      }
    }
  }
}
