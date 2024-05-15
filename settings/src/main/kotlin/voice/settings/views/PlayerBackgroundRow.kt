package voice.settings.views

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import voice.common.constants.PLAYER_BACKGROUND_BLUR_COVER
import voice.common.constants.PLAYER_BACKGROUND_BLUR_COVER_2
import voice.common.constants.PLAYER_BACKGROUND_THEME
import voice.common.R as CommonR

@Composable
internal fun PlayerBackgroundRow(playerBackground: Int, openPlayerBackgroundDialog: () -> Unit) {
  SettingsRow(
    title = stringResource(CommonR.string.pref_player_background_title),
    value = when (playerBackground) {
      PLAYER_BACKGROUND_BLUR_COVER -> stringResource(CommonR.string.pref_player_background_blurred_cover) + " #1"
      PLAYER_BACKGROUND_BLUR_COVER_2 -> stringResource(CommonR.string.pref_player_background_blurred_cover) + " #2"
      else -> stringResource(CommonR.string.pref_player_background_theme)
    },
    paddingTop = 8.dp,
    click = openPlayerBackgroundDialog
  )
}

@Composable
internal fun PlayerBackgroundDialog(
  checkedItemId: Int,
  onDismiss: () -> Unit,
  onSelected: (Int) -> Unit
) {
  val items = arrayListOf(
    RadioItem(PLAYER_BACKGROUND_THEME, stringResource(CommonR.string.pref_player_background_theme), ImageVector.vectorResource(CommonR.drawable.ic_square)),
    RadioItem(PLAYER_BACKGROUND_BLUR_COVER, stringResource(CommonR.string.pref_player_background_blurred_cover) + " #1", ImageVector.vectorResource(CommonR.drawable.ic_square_blur)),
    RadioItem(PLAYER_BACKGROUND_BLUR_COVER_2, stringResource(CommonR.string.pref_player_background_blurred_cover) + " #2", ImageVector.vectorResource(CommonR.drawable.ic_square_blur))
  )
  RadioButtonDialog(
    title = stringResource(CommonR.string.pref_player_background_title),
    items = items,
    checkedItemId = checkedItemId,
    onDismiss = onDismiss,
    onSelected = onSelected
  )
}
