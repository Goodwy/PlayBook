package voice.settings.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import voice.settings.R

@Composable
internal fun AppVersion(appVersion: String, openAbout: () -> Unit) {
  ListItem(
    modifier = Modifier
      .clickable {
        openAbout()
      }
      .fillMaxWidth(),
    colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.inverseOnSurface),
    headlineText = {
      Text(
        text = stringResource(R.string.pref_about_title),
        //color = LocalContentColor.current.copy(alpha = 0.5F),
      )
    },
    supportingText = {
      Text(
        text = stringResource(R.string.pref_app_version) + " " + appVersion,
        color = LocalContentColor.current.copy(alpha = 0.5F),
      )
    },
    trailingContent = { Icon(imageVector = Icons.Rounded.ChevronRight, stringResource(id = R.string.pref_about_title)) }
  )
}
