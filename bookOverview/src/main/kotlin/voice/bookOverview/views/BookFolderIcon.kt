package voice.bookOverview.views

import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FolderOpen
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.sp
import voice.bookOverview.R

@Composable
internal fun BookFolderIcon(
  withHint: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  icon: Boolean = true,
) {
  Box {
    if (icon) {
      IconButton(modifier = modifier, onClick = onClick) {
        Icon(
          imageVector = Icons.Rounded.FolderOpen,
          contentDescription = stringResource(R.string.audiobook_folders_title),
          tint = MaterialTheme.colorScheme.primary //MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }
    else {
      TextButton(modifier = modifier, onClick = onClick) {
        Text(
          text = stringResource(R.string.folders).toUpperCase(LocaleList.current),
          fontSize = 14.sp,
          color = MaterialTheme.colorScheme.primary,
          //letterSpacing = 0.sp,
          maxLines = 1,
        )
      }
    }
    if (withHint) {
      AddBookHint()
    }
  }
}
