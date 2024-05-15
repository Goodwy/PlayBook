package voice.settings.views

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Contrast
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import voice.common.DARK_THEME_SETTABLE
import voice.common.constants.THEME_AUTO
import voice.common.constants.THEME_DARK
import voice.common.constants.THEME_LIGHT
import voice.common.R as CommonR

@Composable
internal fun ThemeRow(currentTheme: Int, enabled: Boolean, isWidget: Boolean = false, openThemeDialog: () -> Unit) {
  SettingsRow(
    title = if (isWidget) stringResource(CommonR.string.pref_widget) else stringResource(CommonR.string.theme),
    value = when (currentTheme) {
      THEME_LIGHT -> stringResource(CommonR.string.light_theme)
      THEME_DARK -> stringResource(CommonR.string.dark_theme)
      else -> stringResource(CommonR.string.pref_follow_system)
    },
    enabled = enabled,
    paddingTop = if (isWidget) 6.dp else 8.dp,
    paddingBottom = if (isWidget) 8.dp else 6.dp,
    click = openThemeDialog
  )
}

@Composable
internal fun ThemeDialog(
  checkedItemId: Int,
  isWidget: Boolean = false,
  onDismiss: () -> Unit,
  onSelected: (Int) -> Unit
) {
  val light = RadioItem(THEME_LIGHT, stringResource(CommonR.string.light_theme), Icons.Rounded.LightMode)
  val dark = RadioItem(THEME_DARK, stringResource(CommonR.string.dark_theme), Icons.Rounded.DarkMode)
  val system = RadioItem(THEME_AUTO, stringResource(CommonR.string.pref_follow_system), Icons.Rounded.Contrast)
  val items = if (DARK_THEME_SETTABLE || isWidget) arrayListOf(light, dark) else arrayListOf(light, dark, system)
  RadioButtonDialog(
    title = if (isWidget) stringResource(CommonR.string.pref_widget) else stringResource(CommonR.string.theme),
    items = items,
    checkedItemId = checkedItemId,
    onDismiss = onDismiss,
    onSelected = onSelected
  )
}
