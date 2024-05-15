package voice.settings.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun SettingsRow(
  title: String,
  subtitle: String? = null,
  value: String? = null,
  enabled: Boolean = true,
  showChevron: Boolean = false,
  color: Color = MaterialTheme.colorScheme.inverseOnSurface,
  paddingStart: Dp = 16.dp,
  paddingEnd: Dp = 12.dp,
  paddingTop: Dp = 6.dp,
  paddingBottom: Dp = 6.dp,
  click: () -> Unit
) {
  val alpha = if (enabled) 1f else 0.6f
  Box(
    contentAlignment = Alignment.Center,
    modifier = Modifier
      .heightIn(min = 42.dp)
      .fillMaxWidth()
      .alpha(alpha)
      .clickable {
        click()
      }
      .background(color = color),
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(start = paddingStart, end = paddingEnd, top = paddingTop, bottom = paddingBottom),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column(
        modifier = Modifier
          .weight(1f),
        verticalArrangement = Arrangement.Center
      ) {
        Text(text = title, lineHeight = 15.sp,)
        if (subtitle != null) Text(
          modifier = Modifier.alpha(0.6f),
          text = subtitle,
          fontSize = 12.sp,
          lineHeight = 12.sp)
      }
      Spacer(modifier = Modifier.size(16.dp))
      Row(
        modifier = Modifier.widthIn(min = 24.dp, max = 200.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
      ) {
        if (value != null) {
          Text(
            modifier = Modifier
              .alpha(0.6f)
              .padding(end = if (showChevron) 0.dp else 4.dp),
            text = value,
            maxLines = 3,
            lineHeight = 15.sp,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.End
          )
        }
        if (showChevron) Icon(
          imageVector = Icons.Rounded.ChevronRight,
          contentDescription = title)
      }
    }
  }
}
