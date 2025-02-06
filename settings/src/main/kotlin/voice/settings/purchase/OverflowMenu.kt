package voice.settings.purchase

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.ripple.rememberRipple
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import voice.strings.R as StringsR
import voice.common.R as CommonR

@Composable
internal fun OverflowMenu(
  showStoreChange: Boolean,
  useGooglePlay: Boolean,
  onChangeStore: () -> Unit,
  onRefreshPurchase: () -> Unit,
  onUrlClick: () -> Unit,
) {
  Row(verticalAlignment = Alignment.CenterVertically) {
    val context = LocalContext.current
    if (showStoreChange) {
      val store = if (useGooglePlay) "Google Play" else "RuStore"
      val storeIcon = if (useGooglePlay) CommonR.drawable.ic_google_play else CommonR.drawable.ic_rustore
      Box(
        modifier = Modifier
          .combinedClickable(
            onClick = { onChangeStore() },
            onLongClick = { Toast.makeText(
              context,
              store,
              Toast.LENGTH_SHORT,
            ).show() },
            indication = rememberRipple(bounded = false, radius = 20.dp),
            interactionSource = remember { MutableInteractionSource() },
          ),
        contentAlignment = Alignment.Center,
      ) {
        Icon(
          painter = painterResource(id = storeIcon),
          contentDescription = store,
        )
      }
      Spacer(modifier = Modifier.size(24.dp))
    }
    val restorePurchase = stringResource(id = CommonR.string.restore_purchase)
    Box(
      modifier = Modifier
        .combinedClickable(
          onClick = { onRefreshPurchase() },
          onLongClick = { Toast.makeText(
            context,
            restorePurchase,
            Toast.LENGTH_SHORT,
          ).show() },
          indication = rememberRipple(bounded = false, radius = 20.dp),
          interactionSource = remember { MutableInteractionSource() },
        ),
      contentAlignment = Alignment.Center,
    ) {
      Icon(
        painter = painterResource(id = CommonR.drawable.ic_restore_purchase),
        contentDescription = restorePurchase,
      )
    }
    Spacer(modifier = Modifier.size(8.dp))
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
//      ListItem(
//        modifier = Modifier.clickable(
//          onClick = {
//            expanded = false
//            onRefreshPurchase()
//          },
//        ),
//        headlineContent = {
//          Text(text = stringResource(id = CommonR.string.restore_purchase))
//        },
//      )
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
