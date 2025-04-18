package voice.bookmark

import voice.data.Bookmark

data class BookmarkItemViewState(
  val title: String,
  val subtitle: String,
  val id: Bookmark.Id,
  val showSleepIcon: Boolean,
  val chapterNumber: String,
  val date: String,
  val useGestures: Boolean,
  val useHapticFeedback: Boolean,
)

data class BookmarkViewState(
  val bookmarks: List<BookmarkItemViewState>,
  val shouldScrollTo: Bookmark.Id?,
  val dialogViewState: BookmarkDialogViewState,
  val paddings: String,
  val sorted: Int,
  val miniPlayerStyle: Int,
)

sealed interface BookmarkDialogViewState {
  data object None : BookmarkDialogViewState
  data object AddBookmark : BookmarkDialogViewState
  data class EditBookmark(
    val id: Bookmark.Id,
    val title: String?,
  ) : BookmarkDialogViewState
}
