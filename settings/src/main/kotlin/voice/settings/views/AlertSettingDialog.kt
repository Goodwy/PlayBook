package voice.settings.views

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import voice.strings.R as StringsR
import voice.common.R as CommonR

@Composable
fun AlertSettingDialog(
  title: String? = null,
  text: String,
  onConfirmed: () -> Unit,
  onDismiss: () -> Unit,
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      if (title != null) Text(text = title)
    },
    text = {
      Text(text = text)
    },
    confirmButton = {
      TextButton(
        onClick = {
          onConfirmed()
          onDismiss()
        },
      ) {
        Text(stringResource(CommonR.string.ok))
      }
    },
    dismissButton = {
      TextButton(
        onClick = {
          onDismiss()
        },
      ) {
        Text(stringResource(StringsR.string.dialog_cancel))
      }
    },
  )
}
