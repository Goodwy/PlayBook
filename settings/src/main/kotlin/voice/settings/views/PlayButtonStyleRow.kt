package voice.settings.views

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import voice.common.constants.PLAY_BUTTON_CLASSIC
import voice.common.constants.PLAY_BUTTON_ROUND
import voice.common.constants.PLAY_BUTTON_ROUND_AND_SQUARE
import voice.common.constants.PLAY_BUTTON_SQUARE
import voice.common.R as CommonR

@Composable
internal fun PlayButtonStyleRow(buttonStyle: Int, openPlayButtonStyleDialog: () -> Unit) {
  SettingsRow(
    title = stringResource(CommonR.string.pref_play_buttons_style_title),
    value = when (buttonStyle) {
      PLAY_BUTTON_ROUND -> stringResource(CommonR.string.pref_rewind_buttons_style_rounded)
      PLAY_BUTTON_SQUARE -> stringResource(CommonR.string.pref_rewind_buttons_style_square)
      PLAY_BUTTON_ROUND_AND_SQUARE -> stringResource(CommonR.string.pref_rewind_buttons_style_rounded)+"/"+stringResource(CommonR.string.pref_rewind_buttons_style_square)
      else -> stringResource(CommonR.string.pref_rewind_buttons_style_classic)
    },
    click = openPlayButtonStyleDialog
  )
}

@Composable
internal fun PlayButtonStyleDialog(
  checkedItemId: Int,
  onDismiss: () -> Unit,
  onSelected: (Int) -> Unit
) {
  val items = arrayListOf(
    RadioItem(PLAY_BUTTON_CLASSIC, stringResource(CommonR.string.pref_rewind_buttons_style_classic), Icons.Rounded.PlayArrow),
    RadioItem(PLAY_BUTTON_ROUND, stringResource(CommonR.string.pref_rewind_buttons_style_rounded), Icons.Rounded.PlayCircle),
    RadioItem(PLAY_BUTTON_SQUARE, stringResource(CommonR.string.pref_rewind_buttons_style_square), ImageVector.vectorResource(CommonR.drawable.ic_play_square)),
    RadioItem(PLAY_BUTTON_ROUND_AND_SQUARE, stringResource(CommonR.string.pref_rewind_buttons_style_rounded)+"/"+stringResource(CommonR.string.pref_rewind_buttons_style_square), ImageVector.vectorResource(CommonR.drawable.ic_round_and_square)),
  )
  RadioButtonDialog(
    title = stringResource(CommonR.string.pref_play_buttons_style_title),
    items = items,
    checkedItemId = checkedItemId,
    onDismiss = onDismiss,
    onSelected = onSelected
  )
}
