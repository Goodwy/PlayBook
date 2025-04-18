package voice.bookOverview.views.topbar

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.coroutines.delay
import voice.bookOverview.overview.BookOverviewLayoutMode
import voice.bookOverview.overview.BookOverviewViewState
import voice.bookOverview.search.BookSearchViewState
import voice.common.BookId
import voice.common.compose.VoiceTheme
import voice.common.constants.MINI_PLAYER_PLAYER
import voice.common.constants.SORTING_LAST
import voice.common.constants.SORTING_NAME
import kotlin.time.Duration.Companion.seconds

@Composable
internal fun BookOverviewTopBar(
  viewState: BookOverviewViewState,
  onBookMigrationClick: () -> Unit,
  onBoomMigrationHelperConfirmClick: () -> Unit,
  onBookFolderClick: () -> Unit,
  onSettingsClick: () -> Unit,
  onActiveChange: (Boolean) -> Unit,
  onQueryChange: (String) -> Unit,
  onSearchBookClick: (BookId) -> Unit,
) {
  Column {
    val horizontalPadding by animateDpAsState(
      targetValue = if (viewState.searchActive) 0.dp else 16.dp,
      label = "horizontalPadding",
    )
    val top = viewState.paddings.substringBefore(';').toInt()
    val topPadding = if (top == 0) 24.dp else top.dp
    BookOverviewSearchBar(
      topPadding = topPadding,
      horizontalPadding = horizontalPadding,
      onQueryChange = onQueryChange,
      onActiveChange = onActiveChange,
      onBookMigrationClick = onBookMigrationClick,
      onBoomMigrationHelperConfirmClick = onBoomMigrationHelperConfirmClick,
      onBookFolderClick = onBookFolderClick,
      onSettingsClick = onSettingsClick,
      onSearchBookClick = onSearchBookClick,
      searchActive = viewState.searchActive,
      showMigrateIcon = viewState.showMigrateIcon,
      showMigrateHint = viewState.showMigrateHint,
      showAddBookHint = viewState.showAddBookHint,
      useIcon = viewState.useMenuIconsPref,
      searchViewState = viewState.searchViewState,
    )
    var showLoading by remember { mutableStateOf(true) }
    LaunchedEffect(viewState.isLoading) {
      if (viewState.isLoading) {
        delay(3.seconds)
      }
      showLoading = viewState.isLoading
    }
    if (showLoading) {
      LinearProgressIndicator(
        Modifier
          .padding(top = 12.dp)
          .fillMaxWidth(),
      )
    }
  }
}

@Composable
@Preview
private fun BookOverviewTopBarPreview() {
  VoiceTheme(true) {
    BookOverviewTopBar(
      viewState = BookOverviewViewState(
        books = persistentMapOf(),
        layoutMode = BookOverviewLayoutMode.List,
        playButtonState = BookOverviewViewState.PlayButtonState.Paused,
        showAddBookHint = true,
        showMigrateHint = true,
        showMigrateIcon = true,
        showSearchIcon = true,
        isLoading = true,
        searchActive = false,
        searchViewState = BookSearchViewState.EmptySearch(
          suggestedAuthors = listOf(),
          recentQueries = listOf(),
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
        sortingCurrent = SORTING_LAST,
        sortingNotStarted = SORTING_NAME,
        sortingFinished = SORTING_LAST,
      ),
      onBookMigrationClick = {},
      onBoomMigrationHelperConfirmClick = {},
      onBookFolderClick = {},
      onSettingsClick = {},
      onActiveChange = {},
      onQueryChange = {},
      onSearchBookClick = {},
    )
  }
}
