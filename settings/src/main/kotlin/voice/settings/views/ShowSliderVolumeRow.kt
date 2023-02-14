package voice.settings.views

import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.app.ActivityCompat.recreate
import voice.settings.R

@Composable
internal fun ShowSliderVolumeRow(showSlider: Boolean, toggle: () -> Unit) {
  ListItem(
    modifier = Modifier
      .clickable {
        toggle()
      }
      .fillMaxWidth(),
    colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.inverseOnSurface),
    headlineText = {
      Text(text = stringResource(R.string.pref_show_slider_volume_title))
    },
    trailingContent = {
      Switch(
        checked = showSlider,
        onCheckedChange = {
          toggle()
        },
      )
    },
  )
}
