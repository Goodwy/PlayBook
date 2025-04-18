package voice.bookOverview

import io.kotest.matchers.shouldBe
import org.junit.Test
import voice.bookOverview.overview.BookOverviewCategory
import voice.bookOverview.overview.category

class BookOverviewCategoryTest {

  @Test
  fun finished() {
    val book = book().let { book ->
      val lastChapter = book.chapters.last()
      book.copy(
        content = book.content.copy(
          currentChapter = lastChapter.id,
          positionInChapter = lastChapter.duration,
        ),
      )
    }
    book.category shouldBe BookOverviewCategory.FINISHED_BY_LAST
  }

  @Test
  fun notStarted() {
    val book = book().let { book ->
      val firstChapter = book.chapters.first()
      book.copy(
        content = book.content.copy(
          currentChapter = firstChapter.id,
          positionInChapter = 0,
        ),
      )
    }
    book.category shouldBe BookOverviewCategory.NOT_STARTED_BY_NAME
  }

  @Test
  fun current() {
    val book = book().let { book ->
      book.copy(
        content = book.content.copy(
          currentChapter = book.chapters.last().id,
          positionInChapter = 0,
        ),
      )
    }
    book.category shouldBe BookOverviewCategory.CURRENT_BY_LAST
  }
}
