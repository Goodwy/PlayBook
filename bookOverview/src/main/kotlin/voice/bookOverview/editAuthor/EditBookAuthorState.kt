package voice.bookOverview.editAuthor

import voice.common.BookId

internal data class EditBookAuthorState(
  val author: String,
  val bookId: BookId,
) {

  val confirmButtonEnabled: Boolean = author.trim().isNotEmpty()
}
