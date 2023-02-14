package voice.settings.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import voice.settings.R

@Composable
internal fun AutoRewindRow(autoRewindInSeconds: Int, openAutoRewindDialog: () -> Unit) {
  ListItem(
    modifier = Modifier
      .clickable {
        openAutoRewindDialog()
      }
      .fillMaxWidth(),
    colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.inverseOnSurface),
    /*leadingContent = {
      Icon(
        imageVector = Icons.Rounded.FastRewind,
        contentDescription = stringResource(R.string.pref_auto_rewind_title),
      )
    },*/
    headlineText = {
      Text(text = stringResource(R.string.pref_auto_rewind_title))
    },
    /*supportingText = {
      Text(
        text = LocalContext.current.resources.getQuantityString(
          R.plurals.seconds,
          autoRewindInSeconds,
          autoRewindInSeconds,
        ),
      )
    },*/
    trailingContent = {
      Text(
        modifier = Modifier.alpha(0.6f),
        text = LocalContext.current.resources.getQuantityString(
          R.plurals.seconds,
          autoRewindInSeconds,
          autoRewindInSeconds,
        ),
        fontSize = 14.sp,
      )
    },
  )
}

@Composable
internal fun AutoRewindAmountDialog(
  currentSeconds: Int,
  onSecondsConfirmed: (Int) -> Unit,
  onDismiss: () -> Unit,
) {
  TimeSettingDialog(
    title = stringResource(R.string.pref_auto_rewind_title),
    currentSeconds = currentSeconds,
    minSeconds = 0,
    maxSeconds = 20,
    textPluralRes = R.plurals.seconds,
    onSecondsConfirmed = onSecondsConfirmed,
    onDismiss = onDismiss,
  )
}
