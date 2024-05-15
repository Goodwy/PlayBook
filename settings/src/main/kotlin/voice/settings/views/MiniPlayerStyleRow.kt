package voice.settings.views


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import voice.common.constants.*
import voice.common.R as CommonR

@Composable
internal fun MiniPlayerStyleRow(miniPlayerStyle: Int, openMiniPlayerStyleDialog: () -> Unit) {
  SettingsRow(
    title = stringResource(CommonR.string.pref_show_mini_player_title),
    value = when (miniPlayerStyle) {
      MINI_PLAYER_ROUND_BUTTON -> stringResource(CommonR.string.pref_rewind_buttons_style_rounded)
      MINI_PLAYER_SQUARE_BUTTON -> stringResource(CommonR.string.pref_rewind_buttons_style_square)
      MINI_PLAYER_ROUND_AND_SQUARE -> stringResource(CommonR.string.pref_rewind_buttons_style_rounded)+"/"+stringResource(CommonR.string.pref_rewind_buttons_style_square)
      else -> stringResource(CommonR.string.pref_mini_player_mini_player_floating)
    },
    click = openMiniPlayerStyleDialog
  )
}

@Composable
internal fun MiniPlayerStyleDialog(
  checkedItemId: Int,
  onDismiss: () -> Unit,
  onSelected: (Int) -> Unit
) {
  val items = arrayListOf(
    RadioItem(MINI_PLAYER_PLAYER, stringResource(CommonR.string.pref_mini_player_mini_player_floating), ImageVector.vectorResource(CommonR.drawable.ic_mini_player)),
    RadioItem(MINI_PLAYER_ROUND_BUTTON, stringResource(CommonR.string.pref_rewind_buttons_style_rounded), Icons.Rounded.PlayCircle),
    RadioItem(MINI_PLAYER_SQUARE_BUTTON, stringResource(CommonR.string.pref_rewind_buttons_style_square), ImageVector.vectorResource(CommonR.drawable.ic_play_square)),
    RadioItem(MINI_PLAYER_ROUND_AND_SQUARE, stringResource(CommonR.string.pref_rewind_buttons_style_rounded)+"/"+stringResource(CommonR.string.pref_rewind_buttons_style_square), ImageVector.vectorResource(CommonR.drawable.ic_round_and_square)),
  )
  RadioButtonDialog(
    title = stringResource(CommonR.string.pref_show_mini_player_title),
    items = items,
    checkedItemId = checkedItemId,
    onDismiss = onDismiss,
    onSelected = onSelected
  )
}
