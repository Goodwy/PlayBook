package voice.bookOverview.views

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.sp
import voice.strings.R as StringsR
import voice.common.R as CommonR

@Composable
internal fun BookFolderIcon(
  withHint: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  useIcon: Boolean = true,
) {
  Box(modifier) {
    if (useIcon) {
      IconButton(onClick = onClick) {
        Icon(
          //imageVector = Icons.Rounded.FolderOpen,
          painter = painterResource(id = CommonR.drawable.ic_library),
          contentDescription = stringResource(StringsR.string.audiobook_folders_title),
          tint = MaterialTheme.colorScheme.primary //MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }
    else {
      TextButton(onClick = onClick) {
        Text(
          text = stringResource(CommonR.string.folders).toUpperCase(LocaleList.current),
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
