package voice.settings.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.ScreenRotationAlt
import androidx.compose.material.icons.rounded.ViewList
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import voice.common.grid.GridMode
import voice.settings.R

@Composable
internal fun GridModeRow(currentGridMode: Int, openGridModeDialog: () -> Unit) {
  ListItem(
    modifier = Modifier
      .clickable {
        openGridModeDialog()
      }
      .fillMaxWidth(),
    colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.inverseOnSurface),
    headlineText = {
      Text(text = stringResource(R.string.pref_grid_view))
    },
    trailingContent = {
      Text(
        modifier = Modifier.alpha(0.6f),
        text = when (currentGridMode) {
          0 -> stringResource(R.string.pref_layout_list)
          1 -> stringResource(R.string.pref_layout_grid)
          else -> stringResource(R.string.pref_follow_system)
        },
        fontSize = 14.sp,
      )
    },
  )
}

@Composable
internal fun GridModeDialog(
  checkedItemId: Int,
  onDismiss: () -> Unit,
  onSelected: (Int) -> Unit
) {
  val list = RadioItem(0, stringResource(R.string.pref_layout_list), Icons.Rounded.ViewList)
  val grid = RadioItem(1, stringResource(R.string.pref_layout_grid), Icons.Rounded.GridView)
  val system = RadioItem(2, stringResource(R.string.pref_follow_system), Icons.Rounded.ScreenRotationAlt)
  val items = arrayListOf(list, grid, system)
  RadioButtonDialog(
    title = stringResource(R.string.pref_grid_view),
    items = items,
    checkedItemId = checkedItemId,
    onDismiss = onDismiss,
    onSelected = onSelected
  )
}
