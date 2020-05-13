package com.goodwy.audiobook.features.bookmarks.list

import android.text.format.DateUtils
import android.view.ViewGroup
import com.goodwy.audiobook.R
import com.goodwy.audiobook.data.Bookmark
import com.goodwy.audiobook.data.Chapter
import com.goodwy.audiobook.data.markForPosition
import com.goodwy.audiobook.databinding.BookmarkRowLayoutBinding
import com.goodwy.audiobook.misc.formatTime
import com.goodwy.audiobook.uitools.ViewBindingHolder

class BookMarkHolder(
  parent: ViewGroup,
  private val listener: BookmarkClickListener
) : ViewBindingHolder<BookmarkRowLayoutBinding>(parent, BookmarkRowLayoutBinding::inflate) {

  var boundBookmark: Bookmark? = null
    private set

  init {
    binding.edit.setOnClickListener { view ->
      boundBookmark?.let {
        listener.onOptionsMenuClicked(it, view)
      }
    }
    itemView.setOnClickListener {
      boundBookmark?.let { bookmark ->
        listener.onBookmarkClicked(bookmark)
      }
    }
  }

  fun bind(bookmark: Bookmark, chapters: List<Chapter>) {
    boundBookmark = bookmark
    val currentChapter = chapters.single { it.file == bookmark.mediaFile }
    val bookmarkTitle = bookmark.title
    binding.title.text = when {
      bookmark.setBySleepTimer -> {
        DateUtils.formatDateTime(itemView.context, bookmark.addedAt.toEpochMilli(), DateUtils.FORMAT_SHOW_TIME)
      }
      bookmarkTitle != null && bookmarkTitle.isNotEmpty() -> bookmarkTitle
      else -> currentChapter.markForPosition(bookmark.time).name
    }
    binding.title.setCompoundDrawablesRelativeWithIntrinsicBounds(if (bookmark.setBySleepTimer) R.drawable.ic_sleep else 0, 0, 0, 0)
    val size = chapters.size
    val index = chapters.indexOf(currentChapter)

    binding.summary.text = itemView.context.getString(
      R.string.format_bookmarks_n_of,
      index + 1,
      size
    )
    binding.time.text = formatTime(bookmark.time)
  }
}
