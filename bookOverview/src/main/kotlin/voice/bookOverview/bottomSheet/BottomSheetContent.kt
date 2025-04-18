package voice.bookOverview.bottomSheet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
internal fun BottomSheetContent(
  state: EditBookBottomSheetState,
  onItemClick: (BottomSheetItem) -> Unit,
) {
  Column(Modifier.padding(top = 0.dp, bottom = 0.dp)) {
    state.items.forEach { item ->
      val isLastItem = state.items.last() == item
      Column(
        modifier = Modifier
          .wrapContentHeight()
          .clickable {
            onItemClick(item)
          }
          .padding(horizontal = 16.dp)
          .fillMaxWidth(),
        verticalArrangement = Arrangement.Bottom
      ) {
        Row(
          modifier = Modifier
            .heightIn(min = 42.dp),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Icon(
            imageVector = item.icon,
            contentDescription = stringResource(item.titleRes),
            modifier = Modifier.size(24.dp).alpha(0.8f),
            tint = if (item.red) MaterialTheme.colorScheme.error else LocalContentColor.current
          )
          Spacer(modifier = Modifier.size(16.dp))
          Text(
            text = stringResource(item.titleRes),
            color = if (item.red) MaterialTheme.colorScheme.error else Color.Unspecified
          )
        }
        if (!isLastItem) {
          HorizontalDivider(
            modifier = Modifier.padding(start = 40.dp),
            thickness = Dp.Hairline
          )
        }
      }
    }
  }
}
