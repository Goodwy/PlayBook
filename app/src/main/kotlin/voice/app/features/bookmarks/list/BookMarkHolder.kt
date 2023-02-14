package voice.app.features.bookmarks.list

import android.text.format.DateUtils
import android.view.ViewGroup
import voice.app.R
import voice.app.databinding.BookmarkRowLayoutBinding
import voice.app.uitools.ViewBindingHolder
import voice.common.formatTime
import voice.data.Bookmark
import voice.data.Chapter
import voice.data.markForPosition
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes

class BookMarkHolder(
  parent: ViewGroup,
  private val listener: BookmarkClickListener,
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
    val currentChapter = chapters.single { it.id == bookmark.chapterId }
    val bookmarkTitle = bookmark.title
    binding.title.text = when {
      bookmark.setBySleepTimer -> {
        val justNowThreshold = 1.minutes
        if (ChronoUnit.MILLIS.between(bookmark.addedAt, Instant.now()).milliseconds < justNowThreshold) {
          itemView.context.getString(R.string.bookmark_just_now)
        } else {
          DateUtils.getRelativeDateTimeString(
            itemView.context,
            bookmark.addedAt.toEpochMilli(),
            justNowThreshold.inWholeMilliseconds,
            2.days.inWholeMilliseconds,
            0,
          )
        }
      }
      bookmarkTitle != null && bookmarkTitle.isNotEmpty() -> bookmarkTitle
      else -> currentChapter.markForPosition(bookmark.time).name
    }
    binding.title.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, if (bookmark.setBySleepTimer) R.drawable.ic_sleep else 0, 0)
    binding.time.text = formatTime(bookmark.time)
    val size = chapters.size
    val index = chapters.indexOf(currentChapter)
    binding.chapter.text = itemView.context.getString(
      R.string.format_bookmarks_n_of,
      index + 1,
      size
    )
    val justNowThreshold = 1.minutes
    binding.date.text = if (ChronoUnit.MILLIS.between(bookmark.addedAt, Instant.now()).milliseconds < justNowThreshold) {
      itemView.context.getString(R.string.bookmark_just_now)
    } else {
      DateUtils.getRelativeDateTimeString(
        itemView.context,
        bookmark.addedAt.toEpochMilli(),
        justNowThreshold.inWholeMilliseconds,
        2.days.inWholeMilliseconds,
        0,
      )
    }
  }
}
