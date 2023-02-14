package voice.settings.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FastForward
import androidx.compose.material.icons.rounded.Forward30
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.PlayCircle
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.sp
import voice.common.grid.GridMode
import voice.settings.R

@Composable
internal fun SkipButtonStyleRow(currentGridMode: Int, openSkipButtonStyleDialog: () -> Unit) {
  ListItem(
    modifier = Modifier
      .clickable {
        openSkipButtonStyleDialog()
      }
      .fillMaxWidth(),
    colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.inverseOnSurface),
    headlineText = {
      Text(text = stringResource(R.string.pref_rewind_buttons_style_title))
    },
    trailingContent = {
      Text(
        modifier = Modifier.alpha(0.6f),
        text = when (currentGridMode) {
          0 -> stringResource(R.string.pref_rewind_buttons_style_classic)
          else -> stringResource(R.string.pref_rewind_buttons_style_rounded)
        },
        fontSize = 14.sp,
      )
    },
  )
}

@Composable
internal fun SkipButtonStyleDialog(
  checkedItemId: Int,
  onDismiss: () -> Unit,
  onSelected: (Int) -> Unit
) {
  val classic = RadioItem(0, stringResource(R.string.pref_rewind_buttons_style_classic), Icons.Rounded.FastForward)
  val round = RadioItem(1, stringResource(R.string.pref_rewind_buttons_style_rounded), Icons.Rounded.Forward30)
  val items = arrayListOf(classic, round)
  RadioButtonDialog(
    title = stringResource(R.string.pref_rewind_buttons_style_title),
    items = items,
    checkedItemId = checkedItemId,
    onDismiss = onDismiss,
    onSelected = onSelected
  )
}
