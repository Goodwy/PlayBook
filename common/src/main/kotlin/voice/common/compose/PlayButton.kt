package voice.common.compose

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import voice.common.constants.PLAY_BUTTON_ROUND
import voice.common.constants.PLAY_BUTTON_ROUND_AND_SQUARE
import voice.common.constants.PLAY_BUTTON_SQUARE
import voice.common.R as CommonR
import voice.strings.R as StringsR

@Composable
fun PlayButton(
  modifier: Modifier = Modifier,
  playing: Boolean,
  fabSize: Dp = 68.dp,
  iconSize: Dp = 46.dp,
  onPlayClick: () -> Unit,
  style: Int,
) {
  //0-classic, 1-round, 2-square, 3-round/square
  when (style) {
    PLAY_BUTTON_ROUND, PLAY_BUTTON_SQUARE -> FloatingActionButton(
      modifier = modifier.size(fabSize),
      shape = if (style == PLAY_BUTTON_SQUARE) FloatingActionButtonDefaults.shape else MaterialTheme.shapes.small.copy(CornerSize(percent = 50)),
      onClick = onPlayClick
    ) {
      Icon(
        modifier = Modifier.size(iconSize),
        painter = rememberAnimatedVectorPainter(
          animatedImageVector = AnimatedImageVector.animatedVectorResource(
            id = CommonR.drawable.avd_pause_to_play,
          ),
          atEnd = !playing,
        ),
        contentDescription = stringResource(
          id = if (playing) {
            StringsR.string.pause
          } else {
            StringsR.string.play
          },
        ),
      )
    }
    PLAY_BUTTON_ROUND_AND_SQUARE -> {
      val cornerSize by animateDpAsState(
        targetValue = if (playing) 16.dp else fabSize / 2,
        label = "cornerSize",
      )
      FloatingActionButton(
        modifier = modifier.size(fabSize),
        onClick = onPlayClick,
        shape = RoundedCornerShape(cornerSize),
      ) {
        Icon(
          modifier = Modifier.size(iconSize),
          painter = rememberPlayIconPainter(playing = playing),
          contentDescription = stringResource(
            id = if (playing) {
              StringsR.string.pause
            } else {
              StringsR.string.play
            },
          ),
        )
      }
    }
    else -> Box(
      contentAlignment = Alignment.Center,
      modifier = Modifier
        .clickable(
          interactionSource = remember { MutableInteractionSource() },
          indication = rememberRipple(bounded = false),
          onClick = onPlayClick,
        ),
    ) {
      Icon(
        modifier = Modifier.size(82.dp),
        painter = rememberAnimatedVectorPainter(
          animatedImageVector = AnimatedImageVector.animatedVectorResource(
            id = CommonR.drawable.avd_pause_to_play,
          ),
          atEnd = !playing,
        ),
        contentDescription = stringResource(
          id = if (playing) {
            StringsR.string.pause
          } else {
            StringsR.string.play
          },
        ),
      )
    }
  }
}

@Composable
private fun rememberPlayIconPainter(playing: Boolean): Painter {
  return rememberAnimatedVectorPainter(
    animatedImageVector = AnimatedImageVector.animatedVectorResource(
      id = CommonR.drawable.avd_pause_to_play,
    ),
    atEnd = !playing,
  )
}
