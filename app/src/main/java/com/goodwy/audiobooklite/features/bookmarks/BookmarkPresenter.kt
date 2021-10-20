package com.goodwy.audiobooklite.features.bookmarks

import de.paulwoitaschek.flowpref.Pref
import com.goodwy.audiobooklite.common.pref.PrefKeys
import com.goodwy.audiobooklite.data.Bookmark
import com.goodwy.audiobooklite.data.Chapter
import com.goodwy.audiobooklite.data.repo.BookRepository
import com.goodwy.audiobooklite.data.repo.BookmarkRepo
import com.goodwy.audiobooklite.mvp.Presenter
import com.goodwy.audiobooklite.playback.PlayerController
import com.goodwy.audiobooklite.playback.playstate.PlayStateManager
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject
import javax.inject.Named

/**
 * Presenter for the bookmark MVP
 */
class BookmarkPresenter
@Inject constructor(
  @Named(PrefKeys.CURRENT_BOOK)
  private val currentBookIdPref: Pref<UUID>,
  private val repo: BookRepository,
  private val bookmarkRepo: BookmarkRepo,
  private val playStateManager: PlayStateManager,
  private val playerController: PlayerController
) : Presenter<BookmarkView>() {

  var bookId: UUID = UUID.randomUUID()
  private val bookmarks = ArrayList<Bookmark>()
  private val chapters = ArrayList<Chapter>()

  override fun onAttach(view: BookmarkView) {
    val book = repo.bookById(bookId) ?: return

    onAttachScope.launch {
      bookmarks.clear()
      bookmarks.addAll(bookmarkRepo.bookmarks(book))
      chapters.clear()
      chapters.addAll(book.content.chapters)

      renderView()
    }
  }

  fun deleteBookmark(id: UUID) {
    scope.launch {
      bookmarkRepo.deleteBookmark(id)
      bookmarks.removeAll { it.id == id }

      renderView()
    }
  }

  fun selectBookmark(id: UUID) {
    val bookmark = bookmarks.find { it.id == id }
      ?: return

    val wasPlaying = playStateManager.playState == PlayStateManager.PlayState.Playing

    currentBookIdPref.value = bookId
    playerController.setPosition(bookmark.time, bookmark.mediaFile)

    if (wasPlaying) {
      playerController.play()
    }

    view.finish()
  }

  fun editBookmark(id: UUID, newTitle: String) {
    scope.launch {
      bookmarks.find { it.id == id }?.let {
        val withNewTitle = it.copy(
          title = newTitle
        )
        bookmarkRepo.addBookmark(withNewTitle)
        val index = bookmarks.indexOfFirst { bookmarkId -> bookmarkId.id == id }
        bookmarks[index] = withNewTitle
        if (attached) renderView()
      }
    }
  }

  fun addBookmark(name: String) {
    scope.launch {
      val book = repo.bookById(bookId) ?: return@launch
      val addedBookmark = bookmarkRepo.addBookmarkAtBookPosition(
        book = book,
        title = name,
        setBySleepTimer = false
      )
      bookmarks.add(addedBookmark)
      if (attached) renderView()
    }
  }

  private fun renderView() {
    if (attached) {
      view.render(bookmarks, chapters)
    }
  }
}
