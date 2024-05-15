package voice.bookOverview.editAuthor

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import voice.strings.R as StringsR
import voice.common.R as CommonR

@Composable
internal fun EditBookAuthorDialog(
  onDismissEditAuthorClick: () -> Unit,
  onConfirmEditAuthor: () -> Unit,
  viewState: EditBookAuthorState,
  onUpdateEditAuthor: (String) -> Unit,
) {
  AlertDialog(
    onDismissRequest = onDismissEditAuthorClick,
    title = {
      Text(text = stringResource(CommonR.string.edit_book_author))
    },
    confirmButton = {
      Button(
        onClick = onConfirmEditAuthor,
        enabled = viewState.confirmButtonEnabled,
      ) {
        Text(stringResource(id = StringsR.string.dialog_confirm))
      }
    },
    dismissButton = {
      TextButton(onClick = onDismissEditAuthorClick) {
        Text(stringResource(id = StringsR.string.dialog_cancel))
      }
    },
    text = {
      TextField(
        value = viewState.author,
        onValueChange = onUpdateEditAuthor,
        label = {
          Text(stringResource(CommonR.string.change_book_author))
        },
      )
    },
  )
}
