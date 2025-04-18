package voice.settings.views

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import voice.strings.R as StringsR
import voice.common.R as CommonR

@Composable
internal fun SeekTimeRow(
  seekTimeInSeconds: Int,
  forward: Boolean = true,
  openSeekTimeDialog: () -> Unit
) {
  SettingsRow(
    title = stringResource(StringsR.string.pref_seek_time),
    subtitle = if (forward) stringResource(CommonR.string.pref_forward) else stringResource(CommonR.string.pref_rewind),
    value = LocalContext.current.resources.getQuantityString(
      StringsR.plurals.seconds,
      seekTimeInSeconds,
      seekTimeInSeconds,
    ),
    paddingTop = 8.dp,
    click = openSeekTimeDialog
  )
}

@Composable
internal fun SeekAmountDialog(
  currentSeconds: Int,
  forward: Boolean = true,
  onSecondsConfirm: (Int) -> Unit,
  onDismiss: () -> Unit,
) {
  val title = if (forward) stringResource(CommonR.string.pref_forward) else stringResource(CommonR.string.pref_rewind)
  val defaultSeconds = if (forward) 30 else 20
  TimeSettingDialog(
    title = title,
    currentSeconds = currentSeconds,
    minSeconds = 1,
    maxSeconds = 60,
    defaultSeconds = defaultSeconds,
    textPluralRes = StringsR.plurals.seconds,
    onSecondsConfirm = onSecondsConfirm,
    onDismiss = onDismiss,
  )
}
