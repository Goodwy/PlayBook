package com.goodwy.audiobook.features.bookOverview

import com.goodwy.audiobook.features.bookOverview.list.BookOverviewModel
import com.goodwy.audiobook.features.bookOverview.list.header.BookOverviewCategory

sealed class BookOverviewState {

  data class Content(
    val playing: Boolean,
    val currentBookPresent: Boolean,
    val categoriesWithContents: Map<BookOverviewCategory, BookOverviewCategoryContent>,
    val columnCount: Int
  ) : BookOverviewState() {

    val useGrid = columnCount > 1
  }

  object Loading : BookOverviewState()

  object NoFolderSet : BookOverviewState()
}

data class BookOverviewCategoryContent(
  val books: List<BookOverviewModel>,
  val hasMore: Boolean
)
