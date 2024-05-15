package voice.settings.views

import androidx.annotation.PluralsRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.RemoveCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import voice.common.R
import voice.playback.misc.Decibel
import kotlin.math.roundToInt
import voice.strings.R as StringsR

@Composable
fun TimeSettingDialog(
  title: String,
  currentSeconds: Int,
  @PluralsRes textPluralRes: Int,
  minSeconds: Int,
  maxSeconds: Int,
  defaultSeconds: Int,
  onSecondsConfirmed: (Int) -> Unit,
  onDismiss: () -> Unit,
) {
  var sliderValue by remember { mutableFloatStateOf(currentSeconds.toFloat()) }
  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(text = title)
    },
    text = {
      Column {
        Text(
          LocalContext.current.resources.getQuantityString(
            textPluralRes,
            sliderValue.roundToInt(),
            sliderValue.roundToInt(),
          ),
        )
        val valueRange = minSeconds.toFloat()..maxSeconds.toFloat()
        Slider(
          valueRange = valueRange,
          value = sliderValue,
          onValueChange = {
            sliderValue = it
          },
        )
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically,
        ) {
          IconButton(
            //modifier = Modifier.weight(1f),
            onClick = { sliderValue = if (sliderValue > valueRange.start) sliderValue - 1 else valueRange.start }
          ) {
            Icon(
              imageVector = Icons.Rounded.RemoveCircle,
              contentDescription = stringResource(id = StringsR.string.volume_boost),
            )
          }
          Spacer(modifier = Modifier.size(6.dp))
          Button(
            modifier = Modifier.weight(1f),
            onClick = { sliderValue = defaultSeconds.toFloat() },
            contentPadding = PaddingValues(0.dp)
          ){
            Text(
              text = stringResource(id = R.string.pref_default_theme),
              overflow = TextOverflow.Ellipsis,
              maxLines = 1,
            )
          }
          Spacer(modifier = Modifier.size(6.dp))
          IconButton(
            //modifier = Modifier.weight(1f),
            onClick = { sliderValue = if (sliderValue < valueRange.endInclusive) sliderValue + 1 else valueRange.endInclusive }
          ) {
            Icon(
              imageVector = Icons.Rounded.AddCircle,
              contentDescription = stringResource(id = StringsR.string.volume_boost),
            )
          }
        }
      }
    },
    confirmButton = {
      TextButton(
        onClick = {
          onSecondsConfirmed(sliderValue.roundToInt())
          onDismiss()
        },
      ) {
        Text(stringResource(StringsR.string.dialog_confirm))
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
