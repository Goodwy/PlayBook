package voice.settings.views

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FastForward
import androidx.compose.material.icons.rounded.Forward30
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import voice.common.constants.SKIP_BUTTON_CLASSIC
import voice.common.constants.SKIP_BUTTON_ROUND
import voice.common.R as CommonR

@Composable
internal fun SkipButtonStyleRow(currentGridMode: Int, openSkipButtonStyleDialog: () -> Unit) {
  SettingsRow(
    title = stringResource(CommonR.string.pref_rewind_buttons_style_title),
    value = when (currentGridMode) {
      SKIP_BUTTON_CLASSIC -> stringResource(CommonR.string.pref_rewind_buttons_style_classic)
      else -> stringResource(CommonR.string.pref_rewind_buttons_style_rounded)
    },
    click = openSkipButtonStyleDialog
  )
}

@Composable
internal fun SkipButtonStyleDialog(
  checkedItemId: Int,
  onDismiss: () -> Unit,
  onSelected: (Int) -> Unit
) {
  val items = arrayListOf(
    RadioItem(SKIP_BUTTON_CLASSIC, stringResource(CommonR.string.pref_rewind_buttons_style_classic), Icons.Rounded.FastForward),
    RadioItem(SKIP_BUTTON_ROUND, stringResource(CommonR.string.pref_rewind_buttons_style_rounded), Icons.Rounded.Forward30),
  )
  RadioButtonDialog(
    title = stringResource(CommonR.string.pref_rewind_buttons_style_title),
    items = items,
    checkedItemId = checkedItemId,
    onDismiss = onDismiss,
    onSelected = onSelected
  )
}
