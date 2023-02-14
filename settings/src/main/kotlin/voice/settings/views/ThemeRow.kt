package voice.settings.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Contrast
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
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
import voice.common.DARK_THEME_SETTABLE
import voice.settings.R

@Composable
internal fun ThemeRow(currentTheme: Int, enabled: Boolean, openThemeDialog: () -> Unit) {
  val alpha = if (enabled) 1f else 0.6f
  ListItem(
    modifier = Modifier
      .clickable {
        openThemeDialog()
      }
      .fillMaxWidth(),
    colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.inverseOnSurface),
    headlineText = {
      Text(
        modifier = Modifier.alpha(alpha),
        text = stringResource(R.string.theme))
    },
    trailingContent = {
      Text(
        modifier = Modifier.alpha(0.6f),
        text = when (currentTheme) {
          0 -> stringResource(R.string.light_theme)
          1 -> stringResource(R.string.dark_theme)
          else -> stringResource(R.string.pref_follow_system)
        },
        fontSize = 14.sp,
      )
    },
  )
}

@Composable
internal fun ThemeDialog(
  checkedItemId: Int,
  onDismiss: () -> Unit,
  onSelected: (Int) -> Unit
) {
  val light = RadioItem(0, stringResource(R.string.light_theme), Icons.Rounded.LightMode)
  val dark = RadioItem(1, stringResource(R.string.dark_theme), Icons.Rounded.DarkMode)
  val system = RadioItem(2, stringResource(R.string.pref_follow_system), Icons.Rounded.Contrast)
  val items = if (DARK_THEME_SETTABLE) arrayListOf(light, dark) else arrayListOf(light, dark, system)
  RadioButtonDialog(
    title = stringResource(R.string.theme),
    items = items,
    checkedItemId = checkedItemId,
    onDismiss = onDismiss,
    onSelected = onSelected
  )
}
