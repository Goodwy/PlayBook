package voice.settings.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
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
internal fun PlayButtonStyleRow(currentGridMode: Int, openPlayButtonStyleDialog: () -> Unit) {
  ListItem(
    modifier = Modifier
      .clickable {
        openPlayButtonStyleDialog()
      }
      .fillMaxWidth(),
    colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.inverseOnSurface),
    headlineText = {
      Text(text = stringResource(R.string.pref_play_buttons_style_title))
    },
    trailingContent = {
      Text(
        modifier = Modifier.alpha(0.6f),
        text = when (currentGridMode) {
          0 -> stringResource(R.string.pref_rewind_buttons_style_classic)
          1 -> stringResource(R.string.pref_rewind_buttons_style_rounded)
          else -> stringResource(R.string.pref_rewind_buttons_style_square)
        },
        fontSize = 14.sp,
      )
    },
  )
}

@Composable
internal fun PlayButtonStyleDialog(
  checkedItemId: Int,
  onDismiss: () -> Unit,
  onSelected: (Int) -> Unit
) {
  val classic = RadioItem(0, stringResource(R.string.pref_rewind_buttons_style_classic), Icons.Rounded.PlayArrow)
  val round = RadioItem(1, stringResource(R.string.pref_rewind_buttons_style_rounded), Icons.Rounded.PlayCircle)
  val square = RadioItem(2, stringResource(R.string.pref_rewind_buttons_style_square), ImageVector.vectorResource(R.drawable.ic_play_square))
  val items = arrayListOf(classic, round, square)
  RadioButtonDialog(
    title = stringResource(R.string.pref_play_buttons_style_title),
    items = items,
    checkedItemId = checkedItemId,
    onDismiss = onDismiss,
    onSelected = onSelected
  )
}
