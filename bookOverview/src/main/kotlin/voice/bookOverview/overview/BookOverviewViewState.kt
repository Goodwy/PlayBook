package voice.bookOverview.overview

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableMap
import voice.common.BookId
import voice.common.compose.ImmutableFile
import voice.bookOverview.search.BookSearchViewState
import kotlinx.collections.immutable.persistentMapOf
import voice.common.constants.MINI_PLAYER_PLAYER

@Immutable
data class BookOverviewViewState(
  val books: ImmutableMap<BookOverviewCategory, List<BookOverviewItemViewState>>,
  val layoutMode: BookOverviewLayoutMode,
  val playButtonState: PlayButtonState?,
  val showAddBookHint: Boolean,
  val showMigrateHint: Boolean,
  val showMigrateIcon: Boolean,
  val showSearchIcon: Boolean,
  val isLoading: Boolean,
  val searchActive: Boolean,
  val searchViewState: BookSearchViewState,
  val showStoragePermissionBugCard: Boolean,
  val paddings: String,
  val title: String,
  val chapterName: String,
  val cover: ImmutableFile?,
  val currentBook: BookId?,
  val miniPlayerStyle: Int,
  val useGestures: Boolean,
  val useHapticFeedback: Boolean,
  val useMenuIconsPref: Boolean,
) {

  companion object {
    val Loading = BookOverviewViewState(
      books = persistentMapOf(),
      layoutMode = BookOverviewLayoutMode.List,
      playButtonState = null,
      showAddBookHint = false,
      showMigrateHint = false,
      showMigrateIcon = false,
      showSearchIcon = false,
      isLoading = true,
      searchActive = false,
      searchViewState = BookSearchViewState.EmptySearch(
        suggestedAuthors = emptyList(),
        recentQueries = emptyList(),
        query = "",
      ),
      showStoragePermissionBugCard = false,
      paddings = "0;0;0;0",
      title = "",
      chapterName = "",
      cover = null,
      currentBook = null,
      miniPlayerStyle = MINI_PLAYER_PLAYER,
      useGestures = true,
      useHapticFeedback = true,
      useMenuIconsPref = false,
    )
  }

  enum class PlayButtonState {
    Playing,
    Paused,
  }
}
