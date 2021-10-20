package com.goodwy.audiobooklite.features.bookOverview.list

import androidx.annotation.FloatRange
import com.goodwy.audiobooklite.crashreporting.CrashReporter
import com.goodwy.audiobooklite.data.Book
import com.goodwy.audiobooklite.features.bookOverview.list.header.BookOverviewCategory

sealed class BookOverviewItem

data class BookOverviewHeaderModel(
  val category: BookOverviewCategory,
  val hasMore: Boolean
) : BookOverviewItem()

data class BookOverviewModel(
  val name: String,
  val author: String?,
  val transitionName: String,
  @FloatRange(from = 0.0, to = 1.0)
  val progress: Float,
  val book: Book,
  val durationTimeInMs: Long,
  val positionTimeInMs: Long,
  val remainingTimeInMs: Long,
  val playedTimeInPer: Long,
  val isCurrentBook: Boolean,
  val useGridView: Boolean,
  val showProgressBar: Boolean,
  val showDivider: Boolean
) : BookOverviewItem() {

  constructor(book: Book, isCurrentBook: Boolean, useGridView: Boolean, showProgressBar: Boolean, showDivider: Boolean) : this(
    name = book.name,
    author = book.author,
    transitionName = book.coverTransitionName,
    book = book,
    progress = book.progress(),
    durationTimeInMs = book.durationTimeInMs(),
    positionTimeInMs = book.positionTimeInMs(),
    remainingTimeInMs = book.remainingTimeInMs(),
    playedTimeInPer = book.playedTimeInPer(),
    isCurrentBook = isCurrentBook,
    useGridView = useGridView,
    showProgressBar = showProgressBar,
    showDivider = showDivider
  )

  fun areContentsTheSame(other: BookOverviewModel): Boolean {
    val oldBook = book
    val newBook = other.book
    return oldBook.id == newBook.id &&
        oldBook.content.position == newBook.content.position &&
        name == other.name &&
        author == other.author &&
        isCurrentBook == other.isCurrentBook &&
        useGridView == other.useGridView
  }

  fun areItemsTheSame(other: BookOverviewModel): Boolean {
    return book.id == other.book.id
  }
}

private fun Book.progress(): Float {
  val globalPosition = content.position
  val totalDuration = content.duration
  val progress = globalPosition.toFloat() / totalDuration.toFloat()
  if (progress < 0F) {
    CrashReporter.logException(
      AssertionError(
        "Couldn't determine progress for book=$this. Progress is $progress, " +
            "globalPosition=$globalPosition, totalDuration=$totalDuration"
      )
    )
  }
  return progress.coerceIn(0F, 1F)
}

private fun Book.durationTimeInMs(): Long {
  return content.duration
}

private fun Book.positionTimeInMs(): Long {
  return content.position
}

private fun Book.remainingTimeInMs(): Long {
  return content.duration - content.position
}

private fun Book.playedTimeInPer(): Long {
  return (content.position * 100) / content.duration
}
