package voice.settings.views

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import voice.strings.R as StringsR

@Composable
internal fun AutoRewindRow(
  autoRewindInSeconds: Int,
  openAutoRewindDialog: () -> Unit
) {
  SettingsRow(
    title = stringResource(StringsR.string.pref_auto_rewind_title),
    value = LocalContext.current.resources.getQuantityString(
      StringsR.plurals.seconds,
      autoRewindInSeconds,
      autoRewindInSeconds,
    ),
    paddingBottom = 8.dp,
    click = openAutoRewindDialog
  )
}

@Composable
internal fun AutoRewindAmountDialog(
  currentSeconds: Int,
  onSecondsConfirmed: (Int) -> Unit,
  onDismiss: () -> Unit,
) {
  TimeSettingDialog(
    title = stringResource(StringsR.string.pref_auto_rewind_title),
    currentSeconds = currentSeconds,
    minSeconds = 0,
    maxSeconds = 20,
    defaultSeconds = 2,
    textPluralRes = StringsR.plurals.seconds,
    onSecondsConfirmed = onSecondsConfirmed,
    onDismiss = onDismiss,
  )
}
