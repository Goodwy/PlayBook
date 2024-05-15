package voice.settings.views

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ViewList
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material.icons.rounded.ScreenRotationAlt
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import voice.common.R as CommonR

@Composable
internal fun GridModeRow(currentGridMode: Int, openGridModeDialog: () -> Unit) {
  SettingsRow(
    title = stringResource(CommonR.string.pref_grid_view),
    value = when (currentGridMode) {
      0 -> stringResource(CommonR.string.pref_layout_list)
      1 -> stringResource(CommonR.string.pref_layout_grid)
      else -> stringResource(CommonR.string.pref_follow_system)
    },
    paddingTop = 8.dp,
    click = openGridModeDialog
  )
}

@Composable
internal fun GridModeDialog(
  checkedItemId: Int,
  onDismiss: () -> Unit,
  onSelected: (Int) -> Unit
) {
  val items = arrayListOf(
    RadioItem(0, stringResource(CommonR.string.pref_layout_list), Icons.AutoMirrored.Rounded.ViewList),
    RadioItem(1, stringResource(CommonR.string.pref_layout_grid), Icons.Rounded.GridView),
    RadioItem(2, stringResource(CommonR.string.pref_follow_system), Icons.Rounded.ScreenRotationAlt),
  )
  RadioButtonDialog(
    title = stringResource(CommonR.string.pref_grid_view),
    items = items,
    checkedItemId = checkedItemId,
    onDismiss = onDismiss,
    onSelected = onSelected
  )
}
