package voice.bookOverview.overview

import androidx.annotation.StringRes
import voice.data.Book
import voice.data.BookComparator
import java.util.concurrent.TimeUnit.SECONDS
import voice.strings.R as StringsR

enum class BookOverviewCategory(
  @StringRes val nameRes: Int,
  val comparator: Comparator<Book>,
) {
  CURRENT(
    nameRes = StringsR.string.book_header_current,
    comparator = BookComparator.ByLastPlayed,
  ),
  CURRENT_BY_NAME(
    nameRes = StringsR.string.book_header_current,
    comparator = BookComparator.ByName,
  ),
  CURRENT_BY_AUTHOR(
    nameRes = StringsR.string.book_header_current,
    comparator = BookComparator.ByAuthor,
  ),
  NOT_STARTED(
    nameRes = StringsR.string.book_header_not_started,
    comparator = BookComparator.ByName,
  ),
  NOT_STARTED_BY_AUTHOR(
    nameRes = StringsR.string.book_header_not_started,
    comparator = BookComparator.ByAuthor,
  ),
  NOT_STARTED_BY_LAST(
    nameRes = StringsR.string.book_header_not_started,
    comparator = BookComparator.ByLastAdded,
  ),
  FINISHED(
    nameRes = StringsR.string.book_header_completed,
    comparator = BookComparator.ByLastPlayed,
  ),
  FINISHED_BY_NAME(
    nameRes = StringsR.string.book_header_completed,
    comparator = BookComparator.ByName,
  ),
  FINISHED_BY_AUTHOR(
    nameRes = StringsR.string.book_header_completed,
    comparator = BookComparator.ByAuthor,
  ),
}

val Book.category: BookOverviewCategory
  get() {
    return if (position == 0L) {
      BookOverviewCategory.NOT_STARTED
    } else {
      if (position >= duration - SECONDS.toMillis(5)) {
        BookOverviewCategory.FINISHED
      } else {
        BookOverviewCategory.CURRENT
      }
    }
  }

val Book.categoryByLast: BookOverviewCategory
  get() {
    return if (position == 0L) {
      BookOverviewCategory.NOT_STARTED_BY_LAST
    } else {
      if (position >= duration - SECONDS.toMillis(5)) {
        BookOverviewCategory.FINISHED
      } else {
        BookOverviewCategory.CURRENT
      }
    }
  }

val Book.categoryByName: BookOverviewCategory
  get() {
    return if (position == 0L) {
      BookOverviewCategory.NOT_STARTED
    } else {
      if (position >= duration - SECONDS.toMillis(5)) {
        BookOverviewCategory.FINISHED_BY_NAME
      } else {
        BookOverviewCategory.CURRENT_BY_NAME
      }
    }
  }

val Book.categoryByAuthor: BookOverviewCategory
  get() {
    return if (position == 0L) {
      BookOverviewCategory.NOT_STARTED_BY_AUTHOR
    } else {
      if (position >= duration - SECONDS.toMillis(5)) {
        BookOverviewCategory.FINISHED_BY_AUTHOR
      } else {
        BookOverviewCategory.CURRENT_BY_AUTHOR
      }
    }
  }
