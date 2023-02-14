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
internal fun PlayerBackgroundRow(currentGridMode: Int, openPlayerBackgroundDialog: () -> Unit) {
  ListItem(
    modifier = Modifier
      .clickable {
        openPlayerBackgroundDialog()
      }
      .fillMaxWidth(),
    colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.inverseOnSurface),
    headlineText = {
      Text(text = stringResource(R.string.pref_player_background_title))
    },
    trailingContent = {
      Text(
        modifier = Modifier.alpha(0.6f),
        text = when (currentGridMode) {
          0 -> stringResource(R.string.pref_player_background_theme)
          else -> stringResource(R.string.pref_player_background_blurred_cover)
        },
        fontSize = 14.sp,
      )
    },
  )
}

@Composable
internal fun PlayerBackgroundDialog(
  checkedItemId: Int,
  onDismiss: () -> Unit,
  onSelected: (Int) -> Unit
) {
  val theme = RadioItem(0, stringResource(R.string.pref_player_background_theme), ImageVector.vectorResource(R.drawable.ic_square))
  val blur = RadioItem(1, stringResource(R.string.pref_player_background_blurred_cover), ImageVector.vectorResource(R.drawable.ic_square_blur))
  val items = arrayListOf(theme, blur)
  RadioButtonDialog(
    title = stringResource(R.string.pref_player_background_title),
    items = items,
    checkedItemId = checkedItemId,
    onDismiss = onDismiss,
    onSelected = onSelected
  )
}
