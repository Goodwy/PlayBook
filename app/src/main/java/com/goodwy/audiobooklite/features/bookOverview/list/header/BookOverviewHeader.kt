package com.goodwy.audiobooklite.features.bookOverview.list.header

import android.view.ViewGroup
import androidx.core.view.isInvisible
import com.goodwy.audiobooklite.R
import com.goodwy.audiobooklite.features.bookOverview.list.BookOverviewHeaderModel
import com.goodwy.audiobooklite.misc.recyclerComponent.AdapterComponent
import com.goodwy.audiobooklite.uitools.ExtensionsHolder
import kotlinx.android.synthetic.main.book_overview_header.*

typealias OpenCategoryListener = (BookOverviewCategory) -> Unit

class BookOverviewHeaderHolder(parent: ViewGroup, listener: OpenCategoryListener) :
  ExtensionsHolder(parent, R.layout.book_overview_header) {

  private var boundCategory: BookOverviewCategory? = null

  init {
    showAll.setOnClickListener {
      boundCategory?.let(listener)
    }
  }

  fun bind(model: BookOverviewHeaderModel) {
    boundCategory = model.category
    text.setText(model.category.nameRes)
    showAll.isInvisible = !model.hasMore
  }
}

class BookOverviewHeaderComponent(private val listener: OpenCategoryListener) :
  AdapterComponent<BookOverviewHeaderModel, BookOverviewHeaderHolder>(BookOverviewHeaderModel::class) {

  override fun onCreateViewHolder(parent: ViewGroup) =
    BookOverviewHeaderHolder(parent, listener)

  override fun onBindViewHolder(model: BookOverviewHeaderModel, holder: BookOverviewHeaderHolder) {
    holder.bind(model)
  }
}
