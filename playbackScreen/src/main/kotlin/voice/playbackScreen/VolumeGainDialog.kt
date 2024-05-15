package voice.playbackScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import voice.playback.misc.Decibel
import voice.strings.R as StringsR
import voice.common.R as CommonR

@Composable
internal fun VolumeGainDialog(
  dialogState: BookPlayDialogViewState.VolumeGainDialog,
  viewModel: BookPlayViewModel,
) {
  AlertDialog(
    onDismissRequest = { viewModel.dismissDialog() },
    confirmButton = {},
    text = {
      Column {
        Text(
          text = stringResource(id = StringsR.string.volume_boost) + ": " + dialogState.valueFormatted,
          style = MaterialTheme.typography.titleLarge,
          )
        val valueRange = 0F..dialogState.maxGain.value
        val rangeSize = valueRange.endInclusive - valueRange.start
        val steps = rangeSize.toInt() - 1
        Slider(
          steps = steps,
          valueRange = valueRange,
          value = dialogState.gain.value,
          onValueChange = {
            viewModel.onVolumeGainChanged(Decibel(it))
          },
        )
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically,
        ) {
          IconButton(
            //modifier = Modifier.weight(1f),
            onClick = { viewModel.onVolumeGainChanged(Decibel(if (dialogState.gain.value > valueRange.start) dialogState.gain.value - 1 else valueRange.start)) }
          ) {
            Icon(
              imageVector = Icons.Rounded.RemoveCircle,
              contentDescription = stringResource(id = StringsR.string.volume_boost),
            )
          }
          Spacer(modifier = Modifier.size(6.dp))
          Button(
            modifier = Modifier.weight(1f),
            onClick = { viewModel.onVolumeGainChanged(Decibel(0F)) },
            contentPadding = PaddingValues(0.dp)
          ){
            Text(
              text = stringResource(id = CommonR.string.pref_default_theme),
              overflow = TextOverflow.Ellipsis,
              maxLines = 1,
              )
          }
          Spacer(modifier = Modifier.size(6.dp))
          IconButton(
            //modifier = Modifier.weight(1f),
            onClick = { viewModel.onVolumeGainChanged(Decibel(if (dialogState.gain.value < valueRange.endInclusive) dialogState.gain.value + 1 else valueRange.endInclusive)) }
          ) {
            Icon(
              imageVector = Icons.Rounded.AddCircle,
              contentDescription = stringResource(id = StringsR.string.volume_boost),
            )
          }
        }
      }
    },
  )
}
