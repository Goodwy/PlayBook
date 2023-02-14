package voice.settings.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
internal fun SeekTimeRow(seekTimeInSeconds: Int, forward: Boolean = true, openSeekTimeDialog: () -> Unit) {
  ListItem(
    modifier = Modifier
      .clickable {
        openSeekTimeDialog()
      }
      .fillMaxWidth(),
    colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.inverseOnSurface),
    /*leadingContent = {
      Icon(
        modifier = Modifier
          .scale(scaleX = if (!forward) -1f else 1F, scaleY = 1f)
          .rotate(degrees = -45F),
        imageVector = Icons.Rounded.Refresh,
        contentDescription = stringResource(R.string.pref_seek_time),
      )
    },*/
    headlineText = {
      Text(text = stringResource(R.string.pref_seek_time))
    },
    supportingText = {
      Text(
        text = if (forward) stringResource(R.string.pref_forward) else stringResource(R.string.pref_rewind),
      )
    },
    trailingContent = {
      Text(
        modifier = Modifier.alpha(0.6f),
        text = LocalContext.current.resources.getQuantityString(
          R.plurals.seconds,
          seekTimeInSeconds,
          seekTimeInSeconds,
        ),
        fontSize = 14.sp,
      )
    },
  )
}

@Composable
internal fun SeekAmountDialog(
  currentSeconds: Int,
  forward: Boolean = true,
  onSecondsConfirmed: (Int) -> Unit,
  onDismiss: () -> Unit,
) {
  val title = if (forward) stringResource(R.string.pref_forward) else stringResource(R.string.pref_rewind)
  TimeSettingDialog(
    title = title,
    currentSeconds = currentSeconds,
    minSeconds = 3,
    maxSeconds = 60,
    textPluralRes = R.plurals.seconds,
    onSecondsConfirmed = onSecondsConfirmed,
    onDismiss = onDismiss,
  )
}
