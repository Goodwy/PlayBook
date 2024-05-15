package voice.playbackScreen.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import voice.common.R
import voice.common.compose.ImmutableFile
import voice.common.formatTime
import kotlin.time.Duration

@Composable
internal fun CoverRow(
  cover: ImmutableFile?,
  sleepTime: Duration,
  sleepEoc: Boolean,
  useGestures: Boolean,
  useHapticFeedback: Boolean,
  showCloseButton: Boolean = false,
  onPlayClick: () -> Unit,
  onSkipToNext: () -> Unit,
  onSkipToPrevious: () -> Unit,
  onCloseClick: () -> Unit,
  onCurrentChapterClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Box(contentAlignment = Alignment.Center, modifier = modifier) {
    Cover(
      onDoubleClick = onPlayClick,
      onRightSwipe = onSkipToPrevious,
      onLeftSwipe = onSkipToNext,
      onDownSwipe = onCloseClick,
      onUpSwipe = onCurrentChapterClick,
      cover = cover,
      useGestures = useGestures,
      useHapticFeedback = useHapticFeedback,
    )
    if (sleepTime != Duration.ZERO || sleepEoc) {
      Text(
        modifier = Modifier
          .align(Alignment.TopEnd)
          .padding(top = 8.dp, end = 8.dp)
          .background(
            color = Color(0x7E000000),
            shape = RoundedCornerShape(8.dp),
          )
          .padding(horizontal = 12.dp, vertical = 6.dp),
        text = when (sleepEoc) {
          true -> stringResource(R.string.end_of_current_chapter)
          false -> formatTime(
            timeMs = sleepTime.inWholeMilliseconds,
            durationMs = sleepTime.inWholeMilliseconds,
          )
        },
        color = Color.White,
      )
    }
    if (showCloseButton) {
      IconButton(
        modifier = Modifier
          .align(Alignment.TopStart)
          //.padding(top = 8.dp, start = 8.dp)
          .background(
            color = Color(0x7E000000),
            shape = RoundedCornerShape(topStart = 12.dp, bottomEnd = 24.dp),
          ),
        onClick = onCloseClick
      ) {
        Icon(
          modifier = Modifier.rotate(degrees = -90F),
          imageVector = Icons.Rounded.ArrowBackIosNew,
          contentDescription = stringResource(id = voice.strings.R.string.close),
          tint = Color.White,
        )
      }
    }
  }
}
