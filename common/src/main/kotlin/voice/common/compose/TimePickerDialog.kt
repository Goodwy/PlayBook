package voice.common.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import voice.strings.R as StringsR

@Composable
fun TimePickerDialog(
  title: String,
  initialHour: Int,
  initialMinute: Int,
  onConfirm: (TimePickerState) -> Unit,
  onDismiss: () -> Unit,
) {
  val timePickerState = TimePickerState(
    initialHour,
    initialMinute,
    is24Hour = true,
  )

  AlertDialog(
    onDismissRequest = onDismiss,
    dismissButton = {
      TextButton(onClick = { onDismiss() }) {
        Text(stringResource(id = StringsR.string.dialog_cancel))
      }
    },
    confirmButton = {
      TextButton(
        onClick = {
          onConfirm(timePickerState)
        },
      ) {
        Text(stringResource(id = StringsR.string.dialog_confirm))
      }
    },
    title = {
      Text(text = title)
    },
    text = {
      val scrollState = rememberScrollState()
      TimePicker(
        modifier = Modifier
          .verticalScroll(scrollState)
          .padding(top = 24.dp),
        state = timePickerState,
      )
    },
    properties = DialogProperties(usePlatformDefaultWidth = false)
  )
}
