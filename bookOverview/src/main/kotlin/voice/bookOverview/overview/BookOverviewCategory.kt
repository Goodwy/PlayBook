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
  CURRENT_BY_LAST(
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

  NOT_STARTED_BY_LAST(
    nameRes = StringsR.string.book_header_not_started,
    comparator = BookComparator.ByLastAdded,
  ),
  NOT_STARTED_BY_NAME(
    nameRes = StringsR.string.book_header_not_started,
    comparator = BookComparator.ByName,
  ),
  NOT_STARTED_BY_AUTHOR(
    nameRes = StringsR.string.book_header_not_started,
    comparator = BookComparator.ByAuthor,
  ),

  FINISHED_BY_LAST(
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

//Last
val Book.categoryAllLast: BookOverviewCategory
  get() {
    return if (position == 0L) {
      BookOverviewCategory.NOT_STARTED_BY_LAST
    } else {
      if (position >= duration - SECONDS.toMillis(5)) {
        BookOverviewCategory.FINISHED_BY_LAST
      } else {
        BookOverviewCategory.CURRENT_BY_LAST
      }
    }
  }

val Book.categoryLastLastName: BookOverviewCategory
  get() {
    return if (position == 0L) {
      BookOverviewCategory.NOT_STARTED_BY_LAST
    } else {
      if (position >= duration - SECONDS.toMillis(5)) {
        BookOverviewCategory.FINISHED_BY_LAST
      } else {
        BookOverviewCategory.CURRENT_BY_NAME
      }
    }
  }

val Book.categoryLastLastAuthor: BookOverviewCategory
  get() {
    return if (position == 0L) {
      BookOverviewCategory.NOT_STARTED_BY_LAST
    } else {
      if (position >= duration - SECONDS.toMillis(5)) {
        BookOverviewCategory.FINISHED_BY_LAST
      } else {
        BookOverviewCategory.CURRENT_BY_AUTHOR
      }
    }
  }

val Book.categoryLastNameLast: BookOverviewCategory
  get() {
    return if (position == 0L) {
      BookOverviewCategory.NOT_STARTED_BY_LAST
    } else {
      if (position >= duration - SECONDS.toMillis(5)) {
        BookOverviewCategory.FINISHED_BY_NAME
      } else {
        BookOverviewCategory.CURRENT_BY_LAST
      }
    }
  }

val Book.categoryLastNameName: BookOverviewCategory
  get() {
    return if (position == 0L) {
      BookOverviewCategory.NOT_STARTED_BY_LAST
    } else {
      if (position >= duration - SECONDS.toMillis(5)) {
        BookOverviewCategory.FINISHED_BY_NAME
      } else {
        BookOverviewCategory.CURRENT_BY_NAME
      }
    }
  }

val Book.categoryLastNameAuthor: BookOverviewCategory
  get() {
    return if (position == 0L) {
      BookOverviewCategory.NOT_STARTED_BY_LAST
    } else {
      if (position >= duration - SECONDS.toMillis(5)) {
        BookOverviewCategory.FINISHED_BY_NAME
      } else {
        BookOverviewCategory.CURRENT_BY_AUTHOR
      }
    }
  }

val Book.categoryLastAuthorLast: BookOverviewCategory
  get() {
    return if (position == 0L) {
      BookOverviewCategory.NOT_STARTED_BY_LAST
    } else {
      if (position >= duration - SECONDS.toMillis(5)) {
        BookOverviewCategory.FINISHED_BY_AUTHOR
      } else {
        BookOverviewCategory.CURRENT_BY_LAST
      }
    }
  }

val Book.categoryLastAuthorName: BookOverviewCategory
  get() {
    return if (position == 0L) {
      BookOverviewCategory.NOT_STARTED_BY_LAST
    } else {
      if (position >= duration - SECONDS.toMillis(5)) {
        BookOverviewCategory.FINISHED_BY_AUTHOR
      } else {
        BookOverviewCategory.CURRENT_BY_NAME
      }
    }
  }

val Book.categoryLastAuthorAuthor: BookOverviewCategory
  get() {
    return if (position == 0L) {
      BookOverviewCategory.NOT_STARTED_BY_LAST
    } else {
      if (position >= duration - SECONDS.toMillis(5)) {
        BookOverviewCategory.FINISHED_BY_AUTHOR
      } else {
        BookOverviewCategory.CURRENT_BY_AUTHOR
      }
    }
  }

//Name
val Book.categoryAllName: BookOverviewCategory
  get() {
    return if (position == 0L) {
      BookOverviewCategory.NOT_STARTED_BY_NAME
    } else {
      if (position >= duration - SECONDS.toMillis(5)) {
        BookOverviewCategory.FINISHED_BY_NAME
      } else {
        BookOverviewCategory.CURRENT_BY_NAME
      }
    }
  }

val Book.categoryNameNameLast: BookOverviewCategory
  get() {
    return if (position == 0L) {
      BookOverviewCategory.NOT_STARTED_BY_NAME
    } else {
      if (position >= duration - SECONDS.toMillis(5)) {
        BookOverviewCategory.FINISHED_BY_NAME
      } else {
        BookOverviewCategory.CURRENT_BY_LAST
      }
    }
  }

val Book.categoryNameNameAuthor: BookOverviewCategory
  get() {
    return if (position == 0L) {
      BookOverviewCategory.NOT_STARTED_BY_NAME
    } else {
      if (position >= duration - SECONDS.toMillis(5)) {
        BookOverviewCategory.FINISHED_BY_NAME
      } else {
        BookOverviewCategory.CURRENT_BY_AUTHOR
      }
    }
  }

val Book.categoryNameLastLast: BookOverviewCategory
  get() {
    return if (position == 0L) {
      BookOverviewCategory.NOT_STARTED_BY_NAME
    } else {
      if (position >= duration - SECONDS.toMillis(5)) {
        BookOverviewCategory.FINISHED_BY_LAST
      } else {
        BookOverviewCategory.CURRENT_BY_LAST
      }
    }
  }

val Book.categoryNameLastName: BookOverviewCategory
  get() {
    return if (position == 0L) {
      BookOverviewCategory.NOT_STARTED_BY_NAME
    } else {
      if (position >= duration - SECONDS.toMillis(5)) {
        BookOverviewCategory.FINISHED_BY_LAST
      } else {
        BookOverviewCategory.CURRENT_BY_NAME
      }
    }
  }

val Book.categoryNameLastAuthor: BookOverviewCategory
  get() {
    return if (position == 0L) {
      BookOverviewCategory.NOT_STARTED_BY_NAME
    } else {
      if (position >= duration - SECONDS.toMillis(5)) {
        BookOverviewCategory.FINISHED_BY_LAST
      } else {
        BookOverviewCategory.CURRENT_BY_AUTHOR
      }
    }
  }

val Book.categoryNameAuthorName: BookOverviewCategory
  get() {
    return if (position == 0L) {
      BookOverviewCategory.NOT_STARTED_BY_NAME
    } else {
      if (position >= duration - SECONDS.toMillis(5)) {
        BookOverviewCategory.FINISHED_BY_AUTHOR
      } else {
        BookOverviewCategory.CURRENT_BY_NAME
      }
    }
  }

val Book.categoryNameAuthorLast: BookOverviewCategory
  get() {
    return if (position == 0L) {
      BookOverviewCategory.NOT_STARTED_BY_NAME
    } else {
      if (position >= duration - SECONDS.toMillis(5)) {
        BookOverviewCategory.FINISHED_BY_AUTHOR
      } else {
        BookOverviewCategory.CURRENT_BY_LAST
      }
    }
  }

val Book.categoryNameAuthorAuthor: BookOverviewCategory
  get() {
    return if (position == 0L) {
      BookOverviewCategory.NOT_STARTED_BY_NAME
    } else {
      if (position >= duration - SECONDS.toMillis(5)) {
        BookOverviewCategory.FINISHED_BY_AUTHOR
      } else {
        BookOverviewCategory.CURRENT_BY_AUTHOR
      }
    }
  }

//Author
val Book.categoryAllAuthor: BookOverviewCategory
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

val Book.categoryAuthorAuthorLast: BookOverviewCategory
  get() {
    return if (position == 0L) {
      BookOverviewCategory.NOT_STARTED_BY_AUTHOR
    } else {
      if (position >= duration - SECONDS.toMillis(5)) {
        BookOverviewCategory.FINISHED_BY_AUTHOR
      } else {
        BookOverviewCategory.CURRENT_BY_LAST
      }
    }
  }

val Book.categoryAuthorAuthorName: BookOverviewCategory
  get() {
    return if (position == 0L) {
      BookOverviewCategory.NOT_STARTED_BY_AUTHOR
    } else {
      if (position >= duration - SECONDS.toMillis(5)) {
        BookOverviewCategory.FINISHED_BY_AUTHOR
      } else {
        BookOverviewCategory.CURRENT_BY_NAME
      }
    }
  }

val Book.categoryAuthorLastAuthor: BookOverviewCategory
  get() {
    return if (position == 0L) {
      BookOverviewCategory.NOT_STARTED_BY_AUTHOR
    } else {
      if (position >= duration - SECONDS.toMillis(5)) {
        BookOverviewCategory.FINISHED_BY_LAST
      } else {
        BookOverviewCategory.CURRENT_BY_AUTHOR
      }
    }
  }

val Book.categoryAuthorLastLast: BookOverviewCategory
  get() {
    return if (position == 0L) {
      BookOverviewCategory.NOT_STARTED_BY_AUTHOR
    } else {
      if (position >= duration - SECONDS.toMillis(5)) {
        BookOverviewCategory.FINISHED_BY_LAST
      } else {
        BookOverviewCategory.CURRENT_BY_LAST
      }
    }
  }

val Book.categoryAuthorLastName: BookOverviewCategory
  get() {
    return if (position == 0L) {
      BookOverviewCategory.NOT_STARTED_BY_AUTHOR
    } else {
      if (position >= duration - SECONDS.toMillis(5)) {
        BookOverviewCategory.FINISHED_BY_LAST
      } else {
        BookOverviewCategory.CURRENT_BY_NAME
      }
    }
  }

val Book.categoryAuthorNameAuthor: BookOverviewCategory
  get() {
    return if (position == 0L) {
      BookOverviewCategory.NOT_STARTED_BY_AUTHOR
    } else {
      if (position >= duration - SECONDS.toMillis(5)) {
        BookOverviewCategory.FINISHED_BY_NAME
      } else {
        BookOverviewCategory.CURRENT_BY_AUTHOR
      }
    }
  }

val Book.categoryAuthorNameLast: BookOverviewCategory
  get() {
    return if (position == 0L) {
      BookOverviewCategory.NOT_STARTED_BY_AUTHOR
    } else {
      if (position >= duration - SECONDS.toMillis(5)) {
        BookOverviewCategory.FINISHED_BY_NAME
      } else {
        BookOverviewCategory.CURRENT_BY_LAST
      }
    }
  }

val Book.categoryAuthorNameName: BookOverviewCategory
  get() {
    return if (position == 0L) {
      BookOverviewCategory.NOT_STARTED_BY_AUTHOR
    } else {
      if (position >= duration - SECONDS.toMillis(5)) {
        BookOverviewCategory.FINISHED_BY_NAME
      } else {
        BookOverviewCategory.CURRENT_BY_NAME
      }
    }
  }

//Old
val Book.category: BookOverviewCategory
  get() {
    return if (position == 0L) {
      BookOverviewCategory.NOT_STARTED_BY_NAME
    } else {
      if (position >= duration - SECONDS.toMillis(5)) {
        BookOverviewCategory.FINISHED_BY_LAST
      } else {
        BookOverviewCategory.CURRENT_BY_LAST
      }
    }
  }
