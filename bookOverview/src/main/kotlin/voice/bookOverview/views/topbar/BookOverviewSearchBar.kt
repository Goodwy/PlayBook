package voice.bookOverview.views.topbar

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import voice.bookOverview.search.BookSearchContent
import voice.bookOverview.search.BookSearchViewState
import voice.common.BookId

@Composable
internal fun ColumnScope.BookOverviewSearchBar(
  topPadding: Dp,
  horizontalPadding: Dp,
  onQueryChange: (String) -> Unit,
  onActiveChange: (Boolean) -> Unit,
  onBookMigrationClick: () -> Unit,
  onBoomMigrationHelperConfirmClick: () -> Unit,
  onBookFolderClick: () -> Unit,
  onSettingsClick: () -> Unit,
  onSearchBookClick: (BookId) -> Unit,
  searchActive: Boolean,
  showMigrateIcon: Boolean,
  showMigrateHint: Boolean,
  showAddBookHint: Boolean,
  useIcon: Boolean,
  searchViewState: BookSearchViewState,
) {
  SearchBar(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = horizontalPadding)
      .padding(top = topPadding),
    inputField = {
      SearchBarDefaults.InputField(
        query = if (searchActive) {
          searchViewState.query
        } else {
          ""
        },
        onQueryChange = onQueryChange,
        onSearch = onQueryChange,
        expanded = searchActive,
        onExpandedChange = onActiveChange,
        leadingIcon = {
          TopBarTrailingIcon(
            searchActive = searchActive,
//        showMigrateIcon = showMigrateIcon,
//        showMigrateHint = showMigrateHint,
            showAddBookHint = showAddBookHint,
//        onBookMigrationClick = onBookMigrationClick,
//        onBoomMigrationHelperConfirmClick = onBoomMigrationHelperConfirmClick,
            useIcon = useIcon,
            onBookFolderClick = onBookFolderClick,
            onSettingsClick = onSettingsClick,
            onActiveChange = onActiveChange,
          )
        },
        trailingIcon = {
          TopBarLeadingIcon(
            searchActive = searchActive,
            //onActiveChange = onActiveChange,
            showMigrateIcon = showMigrateIcon,
            showMigrateHint = showMigrateHint,
            onBookMigrationClick = onBookMigrationClick,
            onBoomMigrationHelperConfirmClick = onBoomMigrationHelperConfirmClick,
            onQueryChange = onQueryChange
          )
        },
      )
    },
    expanded = searchActive,
    onExpandedChange = onActiveChange,
    content = {
      BookSearchContent(
        viewState = searchViewState,
        contentPadding = PaddingValues(),
        onQueryChange = onQueryChange,
        onBookClick = onSearchBookClick,
      )
    },
    colors = SearchBarDefaults.colors(
      containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0f),
    ),
  )
}
