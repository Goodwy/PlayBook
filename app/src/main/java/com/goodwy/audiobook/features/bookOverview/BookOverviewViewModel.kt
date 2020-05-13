package com.goodwy.audiobook.features.bookOverview

import de.paulwoitaschek.flowpref.Pref
import com.goodwy.audiobook.common.pref.PrefKeys
import com.goodwy.audiobook.data.Book
import com.goodwy.audiobook.data.repo.BookRepository
import com.goodwy.audiobook.features.BookAdder
import com.goodwy.audiobook.features.bookOverview.list.BookOverviewModel
import com.goodwy.audiobook.features.bookOverview.list.header.BookOverviewCategory
import com.goodwy.audiobook.features.gridCount.GridCount
import com.goodwy.audiobook.playback.PlayerController
import com.goodwy.audiobook.playback.playstate.PlayStateManager
import com.goodwy.audiobook.playback.playstate.PlayStateManager.PlayState
import com.goodwy.audiobook.uitools.CoverFromDiscCollector
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.util.LinkedHashMap
import java.util.UUID
import javax.inject.Inject
import javax.inject.Named

class BookOverviewViewModel
@Inject
constructor(
  private val repo: BookRepository,
  private val bookAdder: BookAdder,
  private val playStateManager: PlayStateManager,
  private val playerController: PlayerController,
  coverFromDiscCollector: CoverFromDiscCollector,
  @Named(PrefKeys.CURRENT_BOOK)
  private val currentBookIdPref: Pref<UUID>,
  @Named(PrefKeys.GRID_MODE)
  private val gridModePref: Pref<GridMode>,
  private val gridCount: GridCount
) {

  fun attach() {
    bookAdder.scanForFiles()
  }

  fun useGrid(useGrid: Boolean) {
    gridModePref.value = if (useGrid) GridMode.GRID else GridMode.LIST
  }

  val coverChanged: Flow<UUID> = coverFromDiscCollector.coverChanged()

  fun state(): Flow<BookOverviewState> {
    val bookStream = repo.flow()
    val currentBookIdStream = currentBookIdPref.flow
    val playingStream = playStateManager.playStateFlow()
      .map { it == PlayState.Playing }
      .distinctUntilChanged()
    val scannerActiveStream = bookAdder.scannerActive
    return combine(
      bookStream,
      currentBookIdStream,
      playingStream,
      scannerActiveStream,
      gridModePref.flow
    ) { books, currentBookId, playing, scannerActive, gridMode ->
      state(
        books = books,
        scannerActive = scannerActive,
        currentBookId = currentBookId,
        playing = playing,
        gridMode = gridMode
      )
    }
  }

  private fun state(
    books: List<Book>,
    scannerActive: Boolean,
    currentBookId: UUID?,
    playing: Boolean,
    gridMode: GridMode
  ): BookOverviewState {
    if (books.isEmpty()) {
      return if (scannerActive) {
        BookOverviewState.Loading
      } else {
        BookOverviewState.NoFolderSet
      }
    }

    return content(
      books = books,
      currentBookId = currentBookId,
      playing = playing,
      gridMode = gridMode
    )
  }

  private fun content(
    books: List<Book>,
    currentBookId: UUID?,
    playing: Boolean,
    gridMode: GridMode
  ): BookOverviewState.Content {
    val currentBookPresent = books.any { it.id == currentBookId }

    val amountOfColumns = gridCount.gridColumnCount(gridMode)

    val categoriesWithContents = LinkedHashMap<BookOverviewCategory, BookOverviewCategoryContent>()
    BookOverviewCategory.values().forEach { category ->
      val content = content(books, category, currentBookId, amountOfColumns)
      if (content != null) {
        categoriesWithContents[category] = content
      }
    }

    return BookOverviewState.Content(
      playing = playing,
      currentBookPresent = currentBookPresent,
      categoriesWithContents = categoriesWithContents,
      columnCount = amountOfColumns
    )
  }

  private fun content(
    books: List<Book>,
    category: BookOverviewCategory,
    currentBookId: UUID?,
    amountOfColumns: Int
  ): BookOverviewCategoryContent? {
    val booksOfCategory = books.filter(category.filter)
      .sortedWith(category.comparator)
    if (booksOfCategory.isEmpty()) {
      return null
    }
    val rows = when (category) {
      BookOverviewCategory.CURRENT -> 4
      BookOverviewCategory.NOT_STARTED -> 4
      BookOverviewCategory.FINISHED -> 2
    }
    val models = booksOfCategory.take(rows * amountOfColumns).map {
      BookOverviewModel(book = it, isCurrentBook = it.id == currentBookId, useGridView = amountOfColumns > 1)
    }
    val hasMore = models.size != booksOfCategory.size
    return BookOverviewCategoryContent(models, hasMore)
  }

  fun playPause() {
    playerController.playPause()
  }
}
