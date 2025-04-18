package voice.playbackScreen.view

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import voice.common.R
import voice.common.compose.MyThumb
import voice.common.formatTime
import voice.common.formatTimeMinutes
import voice.playbackScreen.BookPlayViewState
import java.text.DecimalFormat
import kotlin.time.Duration

@Composable
internal fun SliderRow(
  viewState: BookPlayViewState,
  onCurrentTimeClick: () -> Unit,
  onSeek: (Duration) -> Unit,
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .height(40.dp)
      .padding(horizontal = 16.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    var localValue by remember { mutableFloatStateOf(0F) }
    val interactionSource = remember { MutableInteractionSource() }
    val dragging by interactionSource.collectIsDraggedAsState()
    Slider(
      track = { sliderState ->
        SliderDefaults.Track(
          modifier = Modifier.height(4.dp),
          sliderState = sliderState,
          thumbTrackGapSize = 0.dp,
          drawStopIndicator = null,
        )
      },
      thumb = { MyThumb(interactionSource = interactionSource) },
      modifier = Modifier
        .weight(1F)
        .height(16.dp)
        .padding(horizontal = 8.dp),
      interactionSource = interactionSource,
      value = if (dragging) {
        localValue
      } else {
        (viewState.playedTime / viewState.duration).toFloat()
          .coerceIn(0F, 1F)
      },
      onValueChange = {
        localValue = it
      },
      onValueChangeFinished = {
        onSeek(viewState.duration * localValue.toDouble())
      },
    )
    Spacer(modifier = Modifier.size(2.dp))
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp, vertical = 4.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Text(
        text = formatTime(
          timeMs = if (dragging) {
            (viewState.duration * localValue.toDouble()).inWholeMilliseconds
          } else {
            viewState.playedTime.inWholeMilliseconds
          },
          durationMs = viewState.duration.inWholeMilliseconds,
        ),
        fontSize = 12.sp,
        modifier = Modifier
          //.clickable(onClick = onCurrentTimeClick)
          .padding(horizontal = 8.dp)
          .combinedClickable(
            onClick = onCurrentTimeClick,
            indication = ripple(bounded = false),
            interactionSource = remember { MutableInteractionSource() },
          ),
      )
      Spacer(modifier = Modifier.weight(1f))
      val playbackSpeed = viewState.playbackSpeed
      val remainingTime = (viewState.remainingTimeInMs / playbackSpeed).toLong()
      var textSpeed = DecimalFormat("0.00").format(playbackSpeed)
      if (textSpeed[3].toString() == "0") textSpeed = textSpeed.removeRange(3..3)
      val remainingText = if (playbackSpeed == 1.0.toFloat()) stringResource(id = R.string.left, formatTimeMinutes(LocalContext.current, remainingTime))
                  else stringResource(id = R.string.left, formatTimeMinutes(LocalContext.current, remainingTime)) + " (x$textSpeed)"
      Text(
        //text = viewState.playedTimeInPer.toString() + '%',
        text = remainingText,
        fontSize = 12.sp,
      )
      Spacer(modifier = Modifier.weight(1f))
      Text(
        text = formatTime(
          timeMs = viewState.duration.inWholeMilliseconds,
          durationMs = viewState.duration.inWholeMilliseconds,
        ),
        fontSize = 12.sp,
        modifier = Modifier
          .padding(horizontal = 8.dp),
      )
    }
  }
}
