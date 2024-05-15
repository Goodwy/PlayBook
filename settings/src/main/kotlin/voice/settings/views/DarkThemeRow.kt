package voice.settings.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import voice.strings.R as StringsR

@Composable
internal fun DarkThemeRow(
  useDarkTheme: Boolean,
  toggle: () -> Unit
) {
  ListItem(
    modifier = Modifier
      .height(48.dp)
      .clickable {
        toggle()
      }
      .fillMaxWidth(),
    colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.inverseOnSurface),
    headlineContent = {
      Text(text = stringResource(StringsR.string.pref_theme_dark))
    },
    trailingContent = {
      Switch(
        checked = useDarkTheme,
        onCheckedChange = {
          toggle()
        },
      )
    },
  )
}
