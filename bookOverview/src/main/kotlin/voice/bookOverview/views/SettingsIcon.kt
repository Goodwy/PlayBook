package voice.bookOverview.views

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.sp
import voice.strings.R as StringsR
import voice.common.R as CommonR

@Composable
internal fun SettingsIcon(
  onSettingsClick: () -> Unit,
  useIcon: Boolean = true,
) {
  if (useIcon)
    IconButton(onSettingsClick) {
      Icon(
        //imageVector = Icons.Rounded.Settings,
        painter = painterResource(id = CommonR.drawable.ic_settings),
        contentDescription = stringResource(StringsR.string.action_settings),
        tint = MaterialTheme.colorScheme.primary //MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
  else
    TextButton(onClick = onSettingsClick) {
      Text(
        text = stringResource(CommonR.string.settings).toUpperCase(LocaleList.current),
        fontSize = 14.sp,
        color = MaterialTheme.colorScheme.primary,
        //style = MaterialTheme.typography.bodyLarge,
        //letterSpacing = 0.sp,
        maxLines = 1,
      )
    }
}
