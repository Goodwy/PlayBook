package com.goodwy.audiobooklite.features.bookmarks.list

import android.text.format.DateUtils
import android.view.ViewGroup
import com.goodwy.audiobooklite.R
import com.goodwy.audiobooklite.data.Bookmark
import com.goodwy.audiobooklite.data.Chapter
import com.goodwy.audiobooklite.data.markForPosition
import com.goodwy.audiobooklite.databinding.BookmarkRowLayoutBinding
import com.goodwy.audiobooklite.misc.formatTime
import com.goodwy.audiobooklite.uitools.ViewBindingHolder

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
    itemView.setOnLongClickListener {
      boundBookmark?.let { bookmark ->
        listener.onBookmarkLongClicked(bookmark)
        true
      } ?: false
    }
  }

  fun bind(bookmark: Bookmark, chapters: List<Chapter>) {
    boundBookmark = bookmark
    val currentChapter = chapters.single { it.file == bookmark.mediaFile }
    val bookmarkTitle = bookmark.title
    binding.title.text = when {
      bookmark.setBySleepTimer -> {
        /**DateUtils.formatDateTime(itemView.context, bookmark.addedAt.toEpochMilli(), DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_NUMERIC_DATE)*/
        bookmarkTitle
      }
      bookmarkTitle != null && bookmarkTitle.isNotEmpty() -> bookmarkTitle
      else -> currentChapter.markForPosition(bookmark.time).name
    }
    binding.title.setCompoundDrawablesRelativeWithIntrinsicBounds(if (bookmark.setBySleepTimer) R.drawable.ic_sleep else 0, 0, 0, 0)
    binding.file.text = bookmark.mediaFile.toString().substringAfterLast("/")
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
