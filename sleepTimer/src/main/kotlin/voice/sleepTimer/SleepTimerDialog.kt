package voice.sleepTimer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Circle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.RemoveCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import voice.common.compose.TimePickerDialog
import voice.strings.R
import java.time.LocalTime
import voice.strings.R as StringsR
import voice.common.R as CommonR

@Composable
fun SleepTimerDialog(
  viewState: SleepTimerViewState,
  onDismiss: () -> Unit,
  onIncrementSleepTime: () -> Unit,
  onDecrementSleepTime: () -> Unit,
  onAcceptSleepTime: (Int) -> Unit,
  onCheckAutoSleepTimer: () -> Unit,
  onSetAutoSleepTimerStart: (Int, Int) -> Unit,
  onSetAutoSleepTimerEnd: (Int, Int) -> Unit,
  onAcceptSleepEoc: () -> Unit,
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
    Column {
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
          text = stringResource(id = R.string.action_sleep),
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

      val scrollState = rememberScrollState()
      Column(modifier = Modifier.verticalScroll(scrollState)) {
        Card(
          modifier = Modifier.padding(start = 16.dp, end = 16.dp),
          shape = RoundedCornerShape(8.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHighest),
          elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(42.dp)
              .clickable {
                onAcceptSleepEoc()
              },
            contentAlignment = Alignment.CenterStart
          ) {
            Text(
              modifier = Modifier
                .padding(horizontal = 16.dp),
              text = stringResource(id = CommonR.string.end_of_current_chapter)
            )
          }
          HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            thickness = Dp.Hairline
          )
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(42.dp)
              .clickable {
                onAcceptSleepTime(viewState.customSleepTime)
              },
            contentAlignment = Alignment.CenterStart
          ) {
            Text(
              modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(horizontal = 16.dp),
              text = minutes(minutes = viewState.customSleepTime)
            )
            Row(
              modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 4.dp)
            ) {
              IconButton(onClick = onDecrementSleepTime) {
                Icon(
                  imageVector = Icons.Rounded.RemoveCircle,
                  stringResource(id = StringsR.string.sleep_timer_button_decrement),
                )
              }
              IconButton(onClick = onIncrementSleepTime) {
                Icon(
                  imageVector = Icons.Rounded.AddCircle,
                  stringResource(id = StringsR.string.sleep_timer_button_increment),
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.size(12.dp))
        Card(
          modifier = Modifier.padding(start = 16.dp, end = 16.dp),
          shape = RoundedCornerShape(8.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHighest),
          elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
          listOf(10, 20, 30, 60).forEach { time ->
            ListItem(
              modifier = Modifier
                .height(42.dp)
                .clickable {
                  onAcceptSleepTime(time)
                },
              colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainerHighest),
              headlineContent = {
                Text(text = minutes(minutes = time))
              },
            )
            if (time != 60) {
              HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                thickness = Dp.Hairline
              )
            }
          }
        }

        Spacer(modifier = Modifier.size(12.dp))
        Card(
          modifier = Modifier.padding(start = 16.dp, end = 16.dp),
          shape = RoundedCornerShape(8.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHighest),
          elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
          ListItem(
            modifier = Modifier
              .height(42.dp)
              .clickable {
                onCheckAutoSleepTimer()
              },
            colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainerHighest),
            headlineContent = {
              Text(text = stringResource(id = CommonR.string.auto_sleep_timer))
            },
            trailingContent = {
              Switch(
                checked = viewState.autoSleepTimer,
                onCheckedChange = { onCheckAutoSleepTimer() },
              )
            },
          )
          if (viewState.autoSleepTimer) {
            HorizontalDivider(
              modifier = Modifier.padding(horizontal = 16.dp),
              thickness = Dp.Hairline
            )
            val shouldShowStartTimePicker = remember { mutableStateOf(false) }
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .height(42.dp)
                .clickable {
                  shouldShowStartTimePicker.value = true
                },
              contentAlignment = Alignment.CenterStart
            ) {
              Text(
                modifier = Modifier
                  .align(Alignment.CenterStart)
                  .padding(horizontal = 16.dp),
                text = stringResource(id = CommonR.string.auto_sleep_timer_start)
              )
              TextButton(
                modifier = Modifier
                  .align(Alignment.CenterEnd)
                  .padding(horizontal = 8.dp),
                onClick = {
                  shouldShowStartTimePicker.value = true
                },
              ) {
                Text(viewState.autoSleepTimeStart)
              }
              if (shouldShowStartTimePicker.value) {
                val initialTime = LocalTime.parse(viewState.autoSleepTimeStart)
                TimePickerDialog(
                  stringResource(id = CommonR.string.auto_sleep_timer_start),
                  initialTime.hour,
                  initialTime.minute,
                  { timePickerState: TimePickerState ->
                    onSetAutoSleepTimerStart(timePickerState.hour, timePickerState.minute)
                    shouldShowStartTimePicker.value = false
                  },
                  { shouldShowStartTimePicker.value = false },
                )
              }
            }
            HorizontalDivider(
              modifier = Modifier.padding(horizontal = 16.dp),
              thickness = Dp.Hairline
            )
            val shouldShowEndTimePicker = remember { mutableStateOf(false) }
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .height(42.dp)
                .clickable {
                  shouldShowEndTimePicker.value = true
                },
              contentAlignment = Alignment.CenterStart
            ) {
              Text(
                modifier = Modifier
                  .align(Alignment.CenterStart)
                  .padding(horizontal = 16.dp),
                text = stringResource(id = CommonR.string.auto_sleep_timer_end)
              )
              TextButton(
                modifier = Modifier
                  .align(Alignment.CenterEnd)
                  .padding(horizontal = 8.dp),
                onClick = {
                  shouldShowEndTimePicker.value = true
                },
              ) {
                Text(viewState.autoSleepTimeEnd)
              }
              if (shouldShowEndTimePicker.value) {
                val initialTime = LocalTime.parse(viewState.autoSleepTimeEnd)
                TimePickerDialog(
                  stringResource(id = CommonR.string.auto_sleep_timer_end),
                  initialTime.hour,
                  initialTime.minute,
                  { timePickerState: TimePickerState ->
                    onSetAutoSleepTimerEnd(timePickerState.hour, timePickerState.minute)
                    shouldShowEndTimePicker.value = false
                  },
                  { shouldShowEndTimePicker.value = false },
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.size(4.dp))
        Text(
          modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
          text = stringResource(id = CommonR.string.sleep_timer_note),
          style = MaterialTheme.typography.bodySmall,
          textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.size(8.dp))
      }
    }
  }
}

@Composable
@ReadOnlyComposable
private fun minutes(minutes: Int): String {
  return pluralStringResource(StringsR.plurals.minutes, minutes, minutes)
}
