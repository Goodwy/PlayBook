package com.goodwy.audiobooklite.features.bookCategory

import de.paulwoitaschek.flowpref.Pref
import com.goodwy.audiobooklite.common.pref.PrefKeys
import com.goodwy.audiobooklite.data.BookComparator
import com.goodwy.audiobooklite.data.repo.BookRepository
import com.goodwy.audiobooklite.features.bookOverview.GridMode
import com.goodwy.audiobooklite.features.bookOverview.list.BookOverviewModel
import com.goodwy.audiobooklite.features.bookOverview.list.header.BookOverviewCategory
import com.goodwy.audiobooklite.features.gridCount.GridCount
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.util.UUID
import javax.inject.Inject
import javax.inject.Named

class BookCategoryViewModel
@Inject constructor(
  private val repo: BookRepository,
  @Named(PrefKeys.CURRENT_BOOK)
  private val currentBookIdPref: Pref<UUID>,
  @Named(PrefKeys.GRID_MODE)
  private val gridModePref: Pref<GridMode>,
  @Named(PrefKeys.SHOW_PROGRESS_BAR)
  private val showProgressBarPref: Pref<Boolean>,
  @Named(PrefKeys.SHOW_DIVIDER)
  private val showDividerPref: Pref<Boolean>,
  private val gridCount: GridCount,
  private val comparatorPrefForCategory: @JvmSuppressWildcards Map<BookOverviewCategory, Pref<BookComparator>>
) {

  lateinit var category: BookOverviewCategory

  private fun comparatorPref(): Pref<BookComparator> = comparatorPrefForCategory.getValue(category)

  fun get(): Flow<BookCategoryState> {
    val comparatorStream = comparatorPref().flow
    return combine(
      gridModePref.flow,
      repo.flow(),
      comparatorStream
    ) { gridMode, books, comparator ->
      val gridColumnCount = gridCount.gridColumnCount(gridMode)
      val currentBookId = currentBookIdPref.value
      val showProgressBarPref = showProgressBarPref.value
      val showDividerPref = showDividerPref.value
      val models = books.asSequence()
        .filter(category.filter)
        .sortedWith(comparator)
        .map { book ->
          BookOverviewModel(
            book = book,
            isCurrentBook = book.id == currentBookId,
            useGridView = gridColumnCount > 1,
            showProgressBar = showProgressBarPref,
            showDivider = showDividerPref
          )
        }
        .toList()
      BookCategoryState(gridColumnCount, models)
    }
  }

  fun sort(comparator: BookComparator) {
    comparatorPref().value = comparator
  }

  fun bookSorting(): BookComparator {
    return comparatorPref().value
  }
}

data class BookCategoryState(
  val gridColumnCount: Int,
  val models: List<BookOverviewModel>
)
