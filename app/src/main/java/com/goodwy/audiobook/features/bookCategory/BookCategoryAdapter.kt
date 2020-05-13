package com.goodwy.audiobook.features.bookCategory

import androidx.recyclerview.widget.DiffUtil
import com.goodwy.audiobook.features.bookOverview.list.BookClickListener
import com.goodwy.audiobook.features.bookOverview.list.BookOverviewModel
import com.goodwy.audiobook.features.bookOverview.list.GridBookOverviewComponent
import com.goodwy.audiobook.features.bookOverview.list.ListBookOverviewComponent
import com.goodwy.audiobook.misc.recyclerComponent.CompositeListAdapter
import java.util.UUID

class BookCategoryAdapter(listener: BookClickListener) :
  CompositeListAdapter<BookOverviewModel>(Diff()) {

  init {
    addComponent(GridBookOverviewComponent(listener))
    addComponent(ListBookOverviewComponent(listener))
  }

  fun notifyCoverChanged(bookId: UUID) {
    for (i in 0 until itemCount) {
      val item = getItem(i)
      if (item.book.id == bookId) {
        notifyItemChanged(i)
        return
      }
    }
  }
}

private class Diff : DiffUtil.ItemCallback<BookOverviewModel>() {

  override fun areItemsTheSame(oldItem: BookOverviewModel, newItem: BookOverviewModel): Boolean {
    return oldItem.areItemsTheSame(newItem)
  }

  override fun areContentsTheSame(oldItem: BookOverviewModel, newItem: BookOverviewModel): Boolean {
    return oldItem.areContentsTheSame(newItem)
  }
}
