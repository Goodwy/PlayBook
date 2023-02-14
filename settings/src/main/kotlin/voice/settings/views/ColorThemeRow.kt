package voice.settings.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Circle
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import voice.settings.R

@Composable
internal fun ColorThemeRow(enabled: Boolean, openColorThemeDialog: () -> Unit) {
  val alpha = if (enabled) 1f else 0.6f
  ListItem(
    modifier = Modifier
      .alpha(alpha)
      .clickable {
        openColorThemeDialog()
      }
      .fillMaxWidth(),
    colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.inverseOnSurface),
    headlineText = {
      Text(text = stringResource(R.string.pref_basic_color))
    },
    trailingContent = {
      Icon(
        modifier = Modifier.size(42.dp),
        imageVector = Icons.Rounded.Circle,
        tint = MaterialTheme.colorScheme.primary,
        contentDescription = stringResource(id = R.string.pref_basic_color)
      )
    },
  )
}

@Composable
internal fun ColorThemeDialog(
  checkedItemId: Int,
  onDismiss: () -> Unit,
  onSelected: (Int) -> Unit
) {
  HsvColorDialog(
    title = stringResource(R.string.pref_basic_color),
    checkedItemId = checkedItemId,
    onDismiss = onDismiss,
    onSelected = onSelected
  )
}
