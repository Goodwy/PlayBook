package voice.settings.purchase

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import voice.strings.R as StringsR
import voice.common.R as CommonR

@Composable
internal fun OverflowMenu(
  showStoreChange: Boolean,
  useGooglePlay: Boolean,
  isRuStore: Boolean,
  onChangeStore: () -> Unit,
  onRefreshPurchase: () -> Unit,
  onUrlClick: () -> Unit,
) {
  Row {
    if (showStoreChange) {
      if (useGooglePlay) {
        IconButton(onClick = { onChangeStore() }) {
          Icon(
            painter = painterResource(id = CommonR.drawable.ic_google_play),
            contentDescription = "Google Play",
          )
        }
      } else {
        IconButton(onClick = { onChangeStore() }) {
          Icon(
            painter = painterResource(id = CommonR.drawable.ic_rustore),
            contentDescription = "RuStore",
          )
        }
      }
    }
    var expanded by remember { mutableStateOf(false) }
    IconButton( onClick = { expanded = !expanded }) {
      Icon(
        imageVector = Icons.Rounded.MoreVert,
        contentDescription = stringResource(id = StringsR.string.more),
      )
    }
    DropdownMenu(
      expanded = expanded,
      onDismissRequest = { expanded = false },
    ) {
      ListItem(
        modifier = Modifier.clickable(
          onClick = {
            expanded = false
            onRefreshPurchase()
          },
        ),
        headlineContent = {
          Text(text = stringResource(id = CommonR.string.restore_purchase))
        },
      )
      if (isRuStore) {
        ListItem(
          modifier = Modifier.clickable(
            onClick = {
              expanded = false
              onUrlClick()
            },
          ),
          headlineContent = {
            Text(text = stringResource(id = CommonR.string.billing_subscriptions))
          },
        )
      }
    }
  }
}
