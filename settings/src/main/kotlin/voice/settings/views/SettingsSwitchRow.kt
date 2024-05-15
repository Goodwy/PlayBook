package voice.settings.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Help
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun SettingsSwitchRow(
  title: String,
  subtitle: String? = null,
  initSwitch: Boolean,
  toggle: () -> Unit,
  showFaq: Boolean = false,
  faq: () -> Unit = {},
  paddingTop: Dp = 4.dp,
  paddingBottom: Dp = 4.dp,
) {
  Box(
    contentAlignment = Alignment.Center,
    modifier = Modifier
      .heightIn(min = 42.dp)
      .fillMaxWidth()
      .clickable {
        toggle()
      }
      .background(color = MaterialTheme.colorScheme.inverseOnSurface),
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(start = 16.dp, end = 14.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column(
        modifier = Modifier
          .weight(1f)
          .padding(top = paddingTop, bottom = paddingBottom),
        verticalArrangement = Arrangement.Center
      ) {
        Text(text = title, lineHeight = 15.sp,)
        if (subtitle != null) Text(
          modifier = Modifier.alpha(0.6f),
          text = subtitle,
          fontSize = 12.sp,
          lineHeight = 12.sp)
      }
      Spacer(modifier = Modifier.size(6.dp))
      Row(
        modifier = Modifier.widthIn(min = 24.dp, max = 200.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
      ) {
        if (showFaq) {
          IconButton(
            onClick = {
              faq()
            },
            content = {
              Icon(
                imageVector = Icons.AutoMirrored.Rounded.Help,
                contentDescription = title,
                tint = LocalContentColor.current.copy(alpha = 0.8f),
                )
            }
          )
          Spacer(modifier = Modifier.size(6.dp))
        }
        Switch(
          checked = initSwitch,
          onCheckedChange = {
            toggle()
          },
        )
      }
    }
  }
}
