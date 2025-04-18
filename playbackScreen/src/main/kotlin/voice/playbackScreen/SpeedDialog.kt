package voice.playbackScreen

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.RemoveCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import voice.common.compose.MyThumb
import java.text.DecimalFormat
import voice.strings.R as StringsR

@Composable
internal fun SpeedDialog(
  dialogState: BookPlayDialogViewState.SpeedDialog,
  viewModel: BookPlayViewModel,
) {
  val speedFormatter = remember { DecimalFormat("0.00 x") }

  AlertDialog(
    onDismissRequest = { viewModel.dismissDialog() },
    confirmButton = {},
//    title = {
//      Text(stringResource(id = StringsR.string.playback_speed))
//    },
    text = {
      Column {
        Text(
          text = stringResource(id = StringsR.string.playback_speed) + ": " + speedFormatter.format(dialogState.speed),
          style = MaterialTheme.typography.titleLarge,
          )
        Spacer(modifier = Modifier.size(24.dp))
        val valueRange = 0.5F..dialogState.maxSpeed
        val rangeSize = valueRange.endInclusive - valueRange.start
        val stepSize = 0.05
        val steps = (rangeSize / stepSize).toInt() - 1
        Slider(
          track = { sliderState ->
            SliderDefaults.Track(
              modifier = Modifier.height(4.dp),
              sliderState = sliderState,
              thumbTrackGapSize = 0.dp,
              drawStopIndicator = null,
            )
          },
          thumb = { MyThumb(interactionSource = remember { MutableInteractionSource() }) },
          modifier = Modifier
            .height(16.dp),
          steps = steps,
          valueRange = valueRange,
          value = dialogState.speed,
          onValueChange = {
            viewModel.onPlaybackSpeedChanged(it)
          },
        )
        Spacer(modifier = Modifier.size(20.dp))
        Row(
          modifier = Modifier
            .fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically,
        ) {
          IconButton(
            modifier = Modifier.weight(1f),
            onClick = { viewModel.onPlaybackSpeedChanged(if (dialogState.speed > valueRange.start) dialogState.speed - 0.05F else 0.5F) }
          ) {
            Icon(
              imageVector = Icons.Rounded.RemoveCircle,
              contentDescription = stringResource(id = StringsR.string.playback_speed),
            )
          }
          Button(
            modifier = Modifier.weight(1f),
            onClick = { viewModel.onPlaybackSpeedChanged(1.0F) },
            contentPadding = PaddingValues(0.dp)
          ){
            Text("1,0x")
          }
          Spacer(modifier = Modifier.size(3.dp))
          Button(
            modifier = Modifier.weight(1f),
            onClick = { viewModel.onPlaybackSpeedChanged(1.5F) },
            contentPadding = PaddingValues(0.dp)
          ){
            Text("1,5x")
          }
          Spacer(modifier = Modifier.size(3.dp))
          Button(
            modifier = Modifier.weight(1f),
            onClick = { viewModel.onPlaybackSpeedChanged(2.0F) },
            contentPadding = PaddingValues(0.dp)
          ){
            Text("2,0x")
          }
          IconButton(
            modifier = Modifier.weight(1f),
            onClick = { viewModel.onPlaybackSpeedChanged(if (dialogState.speed < valueRange.endInclusive) dialogState.speed + 0.05F else valueRange.endInclusive) }
          ) {
            Icon(
              imageVector = Icons.Rounded.AddCircle,
              contentDescription = stringResource(id = StringsR.string.playback_speed),
            )
          }
        }
      }
    },
  )
}
