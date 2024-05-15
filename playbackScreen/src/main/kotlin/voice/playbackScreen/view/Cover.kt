package voice.playbackScreen.view

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import voice.common.R
import voice.common.compose.ImmutableFile
import kotlin.math.abs
import voice.strings.R as StringsR

@Composable
internal fun Cover(
  onDoubleClick: () -> Unit = {},
  onRightSwipe: () -> Unit = {},
  onLeftSwipe: () -> Unit = {},
  onDownSwipe: () -> Unit = {},
  onUpSwipe: () -> Unit = {},
  cover: ImmutableFile?,
  cornerRadius: Dp = 12.dp,
  elevation: Dp = 16.dp,
  useGestures: Boolean = false,
  useHapticFeedback: Boolean = false,
  ) {
  var direction by remember { mutableIntStateOf(-1) }
  val layoutDirection = LocalLayoutDirection.current
  val isRtl = layoutDirection == LayoutDirection.Rtl
  AsyncImage(
    modifier = Modifier
      .fillMaxSize()
      .shadow(
        elevation = elevation,
        shape = RoundedCornerShape(cornerRadius),
        clip = true,
        ambientColor = MaterialTheme.colorScheme.onBackground,
        spotColor = MaterialTheme.colorScheme.onBackground,
      )
      .clip(RoundedCornerShape(cornerRadius)),
    contentScale = ContentScale.Crop,
    model = cover?.file,
    placeholder = painterResource(id = R.drawable.album_art),
    error = painterResource(id = R.drawable.album_art),
    contentDescription = stringResource(id = StringsR.string.cover),
  )
  val haptic = LocalHapticFeedback.current
  if (useGestures) Box(
    modifier = Modifier
      .fillMaxSize()
      .pointerInput(Unit) {
        detectTapGestures(
          onDoubleTap = {
            if (useHapticFeedback) haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            onDoubleClick()
          },
        )
      }
      .pointerInput(Unit) {
        detectDragGestures(
          onDrag = { change, dragAmount ->
            change.consume()
            val (x, y) = dragAmount
            if (abs(x) > abs(y)) {
              when {
                x > 0 -> {
                  direction = 0
                } //right
                x < 0 -> {
                  direction = 1
                } // left
              }
            } else {
              when {
                y > 0 -> {
                  direction = 2
                } // down
                y < 0 -> {
                  direction = 3
                } // up
              }
            }
          },
          onDragEnd = {
            when (direction) {
              0 -> {
                if (useHapticFeedback) haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                if (isRtl) onLeftSwipe() else onRightSwipe()
              } //right swipe = next
              1 -> {
                if (useHapticFeedback) haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                if (isRtl) onRightSwipe() else onLeftSwipe()
              } // left swipe = previous
              2 -> {
                if (useHapticFeedback) haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onDownSwipe()
              } // down swipe = close
              3 -> {
                if (useHapticFeedback) haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onUpSwipe()
              } // up swipe = show list of chapters
            }
          }
        )
      }
  )
}
