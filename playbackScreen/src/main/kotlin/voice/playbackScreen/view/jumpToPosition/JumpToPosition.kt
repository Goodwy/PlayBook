package voice.playbackScreen.view.jumpToPosition

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Circle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import voice.strings.R
import voice.common.R as CommonR
import kotlin.time.Duration

@Composable
internal fun JumpToPosition(
  onDismiss: () -> Unit,
  onPositionSelected: (Duration) -> Unit,
  modifier: Modifier = Modifier,
) {
  ModalBottomSheet(
    dragHandle = null,
    modifier = modifier,
    onDismissRequest = onDismiss,
    sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    shape = RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp),
    containerColor = MaterialTheme.colorScheme.background,
  ) {
    val focusRequester = remember { FocusRequester() }
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp),
    ) {
      Text(
        modifier = Modifier
          .align(Alignment.CenterStart)
          .padding(horizontal = 36.dp)
          .fillMaxWidth(),
        text = stringResource(id = CommonR.string.jump_to_position),
        style = MaterialTheme.typography.titleLarge,
        textAlign = TextAlign.Center,
      )
      Box(
        modifier = Modifier
          .align(Alignment.TopEnd)
          .size(32.dp)
          .combinedClickable(
            onClick = {
              onDismiss()
            },
            indication = ripple(bounded = false, radius = 20.dp),
            interactionSource = remember { MutableInteractionSource() },
          ),
        contentAlignment = Alignment.Center,
      ) {
        Icon(
          modifier = Modifier
            .size(32.dp)
            .alpha(0.2f),
          imageVector = Icons.Rounded.Circle,
          contentDescription = stringResource(id = R.string.close),
          tint = contentColorFor(MaterialTheme.colorScheme.background)
        )
        Icon(
          modifier = Modifier
            .size(32.dp)
            .padding(5.dp),
          imageVector = Icons.Rounded.Close,
          contentDescription = stringResource(id = R.string.close),
          tint = contentColorFor(MaterialTheme.colorScheme.background)
        )
      }
    }
    Spacer(modifier = Modifier.size(16.dp))
    var jumpToPosition by remember { mutableStateOf("") }

    OutlinedTextField(
      modifier = Modifier
        .align(Alignment.CenterHorizontally)
        .widthIn(max = 256.dp, min = 96.dp)
        .focusRequester(focusRequester),
      textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
      maxLines = 1,
      keyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Number,
        imeAction = ImeAction.Go,
      ),
      label = {
        Text(text = "mm:ss")
      },
      keyboardActions = KeyboardActions(
        onGo = {
          if (jumpToPosition.isNotEmpty()) {
            onPositionSelected(jumpToPositionTime(jumpToPosition))
          } else {
            onDismiss()
          }
        },
      ),
      value = jumpToPosition,
      onValueChange = { value ->
        // take 7 because 999 hours = 999:00:00 = 7 digits
        jumpToPosition = value
          .filter { it.isDigit() }
          .take(7)
      },
      visualTransformation = TimeTransformingVisualTransformation,
    )

    LaunchedEffect(focusRequester) {
      focusRequester.requestFocus()
    }

    Spacer(modifier = Modifier.size(64.dp))
  }
}
