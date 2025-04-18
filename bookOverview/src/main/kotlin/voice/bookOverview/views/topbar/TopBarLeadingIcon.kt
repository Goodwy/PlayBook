package voice.bookOverview.views.topbar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import voice.bookOverview.views.MigrateIcon
import voice.strings.R

@Composable
internal fun ColumnScope.TopBarLeadingIcon(
  searchActive: Boolean,
  //onActiveChange: (Boolean) -> Unit,
  showMigrateIcon: Boolean,
  showMigrateHint: Boolean,
  onBookMigrationClick: () -> Unit,
  onBoomMigrationHelperConfirmClick: () -> Unit,
  onQueryChange: (String) -> Unit,
) {
  AnimatedVisibility(
    visible = !searchActive,
    enter = fadeIn(),
    exit = fadeOut(),
  ) {
    Row {
      Box(Modifier.size(48.dp), contentAlignment = Alignment.Center) {
        Icon(
          imageVector = Icons.Rounded.Search,
          contentDescription = stringResource(id = R.string.search_hint),
          tint = MaterialTheme.colorScheme.primary
        )
      }
      if (showMigrateIcon) {
        MigrateIcon(
          onClick = onBookMigrationClick,
          withHint = showMigrateHint,
          onHintClick = onBoomMigrationHelperConfirmClick,
        )
      }
    }
  }
  AnimatedVisibility(
    visible = searchActive,
    enter = fadeIn(),
    exit = fadeOut(),
  ) {
    Row {
      IconButton(
        onClick = {
          onQueryChange("")
        },
      ) {
        Icon(
          imageVector = Icons.Rounded.Cancel,
          contentDescription = stringResource(id = R.string.delete),
        )
      }
    }
  }
}
