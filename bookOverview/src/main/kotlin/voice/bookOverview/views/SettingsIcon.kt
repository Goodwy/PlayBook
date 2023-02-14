package voice.bookOverview.views

import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.sp
import voice.bookOverview.R

@Composable
internal fun SettingsIcon(
  onSettingsClick: () -> Unit,
  icon: Boolean = true,
) {
  if (icon)
    IconButton(onSettingsClick) {
      Icon(
        //imageVector = Icons.Rounded.Settings,
        painter = painterResource(id = R.drawable.ic_settings),
        contentDescription = stringResource(R.string.action_settings),
        tint = MaterialTheme.colorScheme.primary //MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
  else
    TextButton(onClick = onSettingsClick) {
      Text(
        text = stringResource(R.string.settings).toUpperCase(LocaleList.current),
        fontSize = 14.sp,
        color = MaterialTheme.colorScheme.primary,
        //style = MaterialTheme.typography.bodyLarge,
        //letterSpacing = 0.sp,
        maxLines = 1,
      )
    }
}
