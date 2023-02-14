package voice.settings.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import voice.common.R
import voice.common.recomposeHighlighter

internal data class RadioItem(val id: Int, val title: String, val icon: ImageVector?)

@Composable
internal fun RadioButtonDialog(
  title: String? = null,
  items: ArrayList<RadioItem>,
  checkedItemId: Int = -1,
  onDismiss: () -> Unit,
  onSelected: (Int) -> Unit,
) {
  var selectedItem: RadioItem? by remember {
    mutableStateOf(null)
  }
  if (checkedItemId != -1) selectedItem = items[checkedItemId]
  AlertDialog(
    onDismissRequest = {
      onDismiss()
    },
    /*icon = {
      Icon(
        imageVector = Icons.Outlined.Book,
        contentDescription = stringResource(id = R.string.folder_type_dialog_title),
      )
    },*/
    confirmButton = {
      ConfirmButton(
        enabled = selectedItem != null,
        onConfirm = {
          onSelected(selectedItem!!.id)
          onDismiss()
        },
      )
    },
    dismissButton = {
      DismissButton(onDismiss)
    },
    title = {
      if (title != null) Text(
        text = title,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
      )
    },
    text = {
      Column {
        items.forEach { item ->
          ItemRow(
            item = item,
            selected = item == selectedItem,
            onSelected = {
              selectedItem = item
            },
          )
        }
      }
    },
  )
}

@Composable
private fun ConfirmButton(enabled: Boolean, onConfirm: () -> Unit) {
  TextButton(
    enabled = enabled,
    onClick = onConfirm,
  ) {
    Text(text = stringResource(id = R.string.dialog_confirm))
  }
}

@Composable
private fun DismissButton(onDismiss: () -> Unit) {
  TextButton(
    onClick = {
      onDismiss()
    },
  ) {
    Text(text = stringResource(id = R.string.dialog_cancel))
  }
}

@Composable
private fun ItemRow(
  item: RadioItem,
  selected: Boolean,
  onSelected: () -> Unit,
) {
  Row(
    modifier = Modifier.clickable(
      onClick = onSelected,
      role = Role.RadioButton,
    ),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    RadioButton(
      onClick = onSelected,
      selected = selected,
    )
    val titleSelectionText = item.title
    Text(
      modifier = Modifier.weight(1F),
      text = titleSelectionText,
      style = MaterialTheme.typography.bodyLarge,
    )
    if (item.icon != null) {
      Icon(
        modifier = Modifier.padding(end = 16.dp, start = 8.dp),
        imageVector = item.icon,
        contentDescription = titleSelectionText,
      )
    }
  }
}
