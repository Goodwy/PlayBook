package voice.settings.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import voice.common.R as CommonR

@Composable
internal fun ColorThemeRow(enabled: Boolean, openColorThemeDialog: () -> Unit) {
  val alpha = if (enabled) 1f else 0.6f
  Box(
    modifier = Modifier
      .height(42.dp)
      .alpha(alpha)
      .clickable {
        openColorThemeDialog()
      }
      .fillMaxWidth()
      .background(color = MaterialTheme.colorScheme.inverseOnSurface),
  ) {
    Row(
      modifier = Modifier
        .height(42.dp)
        .padding(start = 16.dp, end = 14.dp, bottom = 2.dp)
        .fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(text = stringResource(CommonR.string.pref_basic_color))
      Icon(
        modifier = Modifier.size(36.dp),
        imageVector = Icons.Rounded.Circle,
        tint = MaterialTheme.colorScheme.primary,
        contentDescription = stringResource(id = CommonR.string.pref_basic_color)
      )
    }
  }
}

@Composable
internal fun ColorThemeDialog(
  checkedItemId: Int,
  onDismiss: () -> Unit,
  onSelected: (Int) -> Unit
) {
  HsvColorDialog(
    title = stringResource(CommonR.string.pref_basic_color),
    checkedItemId = checkedItemId,
    onDismiss = onDismiss,
    onSelected = onSelected
  )
}
