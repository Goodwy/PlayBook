package voice.settings.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.sp
import voice.settings.R

@Composable
internal fun MiniPlayerStyleRow(currentGridMode: Int, openMiniPlayerStyleDialog: () -> Unit) {
  ListItem(
    modifier = Modifier
      .clickable {
        openMiniPlayerStyleDialog()
      }
      .fillMaxWidth(),
    colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.inverseOnSurface),
    headlineText = {
      Text(text = stringResource(R.string.pref_show_mini_player_title))
    },
    trailingContent = {
      Text(
        modifier = Modifier.alpha(0.6f),
        text = when (currentGridMode) {
          0 -> stringResource(R.string.pref_mini_player_mini_player_floating)
          1 -> stringResource(R.string.pref_mini_player_floating_button)
          else -> stringResource(R.string.pref_mini_player_floating_button)
        },
        fontSize = 14.sp,
      )
    },
  )
}

@Composable
internal fun MiniPlayerStyleDialog(
  checkedItemId: Int,
  onDismiss: () -> Unit,
  onSelected: (Int) -> Unit
) {
  val player = RadioItem(0, stringResource(R.string.pref_mini_player_mini_player_floating), ImageVector.vectorResource(R.drawable.ic_mini_player))
  val round = RadioItem(1, stringResource(R.string.pref_mini_player_floating_button), Icons.Rounded.PlayCircle)
  val square = RadioItem(2, stringResource(R.string.pref_mini_player_floating_button), ImageVector.vectorResource(R.drawable.ic_play_square))
  val items = arrayListOf(player, round, square)
  RadioButtonDialog(
    title = stringResource(R.string.pref_show_mini_player_title),
    items = items,
    checkedItemId = checkedItemId,
    onDismiss = onDismiss,
    onSelected = onSelected
  )
}
