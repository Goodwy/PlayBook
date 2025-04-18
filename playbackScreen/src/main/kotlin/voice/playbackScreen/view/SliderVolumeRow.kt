package voice.playbackScreen.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.VolumeMute
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import voice.common.compose.MyThumb
import voice.playbackScreen.BookPlayViewState

@Composable
internal fun SliderVolumeRow(
  viewState: BookPlayViewState,
  onSeekVolume: (Int) -> Unit,
) {
  Row(
    modifier = Modifier
      .height(40.dp)
      .fillMaxWidth()
      .padding(horizontal = 28.dp),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Icon(
      modifier = Modifier
        .size(16.dp)
        .combinedClickable(
          onClick = { onSeekVolume(viewState.currentVolume - 1) },
          onLongClick = { onSeekVolume(0) },
          indication = ripple(bounded = false, radius = 20.dp),
          interactionSource = remember { MutableInteractionSource() },
        ),
      imageVector = Icons.AutoMirrored.Rounded.VolumeMute,
      contentDescription = stringResource(
        id = voice.strings.R.string.volume_boost
      ),
    )
    val maxValue = if (viewState.maxVolume > 0) viewState.maxVolume.toFloat() else 15f
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
        .weight(1F)
        .height(16.dp)
        .padding(start = 4.dp, end = 6.dp),
      valueRange = 0f..maxValue,
      value = viewState.currentVolume.toFloat(),
      onValueChange = {
        onSeekVolume(it.toInt())
      },
      onValueChangeFinished = {
      },
    )
    Icon(
      modifier = Modifier
        .padding(end = 3.dp)
        .size(16.dp).clickable(
          interactionSource = remember { MutableInteractionSource() },
          indication = ripple(bounded = false, radius = 20.dp),
          onClick = { onSeekVolume(viewState.currentVolume + 1) },
        ),
      imageVector = Icons.AutoMirrored.Rounded.VolumeUp,
      contentDescription = stringResource(
        id = voice.strings.R.string.volume_boost
      ),
    )
  }
}
