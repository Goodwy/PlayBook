package voice.bookOverview.views.topbar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import voice.bookOverview.views.BookFolderIcon
import voice.bookOverview.views.MigrateIcon
import voice.bookOverview.views.SettingsIcon
import voice.strings.R

@Composable
internal fun ColumnScope.TopBarTrailingIcon(
  searchActive: Boolean,
//  showMigrateIcon: Boolean,
//  showMigrateHint: Boolean,
  showAddBookHint: Boolean,
//  onBookMigrationClick: () -> Unit,
//  onBoomMigrationHelperConfirmClick: () -> Unit,
  useIcon: Boolean,
  onBookFolderClick: () -> Unit,
  onSettingsClick: () -> Unit,
  onActiveChange: (Boolean) -> Unit,
) {
  AnimatedVisibility(
    visible = searchActive,
    enter = fadeIn(),
    exit = fadeOut(),
  ) {
    IconButton(onClick = { onActiveChange(false) }) {
      Icon(
        imageVector = Icons.AutoMirrored.Rounded.ArrowBackIos,
        contentDescription = stringResource(id = R.string.close),
      )
    }
  }
  AnimatedVisibility(
    visible = !searchActive,
    enter = fadeIn(),
    exit = fadeOut(),
  ) {
    Row {
//      if (showMigrateIcon) {
//        MigrateIcon(
//          onClick = onBookMigrationClick,
//          withHint = showMigrateHint,
//          onHintClick = onBoomMigrationHelperConfirmClick,
//        )
//      }

      SettingsIcon(onSettingsClick, useIcon = useIcon)
      BookFolderIcon(withHint = showAddBookHint, onClick = onBookFolderClick, useIcon = useIcon)
    }
  }
}
