package voice.playbackScreen.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FastForward
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import voice.common.constants.SKIP_BUTTON_ROUND
import voice.strings.R

@Composable
internal fun SkipButton(
  forward: Boolean,
  style: Int,
  onClick: () -> Unit,
  text: String,
) {
  Box(
    contentAlignment = Alignment.Center,
    modifier = Modifier
      .clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = ripple(bounded = false),
        onClick = onClick,
      ),
  ) {
    val layoutDirection = LocalLayoutDirection.current
    val isRtl = layoutDirection == LayoutDirection.Rtl
    val scaleX = if (isRtl) {
      if (!forward) 1f else -1F
    } else {
      if (!forward) -1f else 1F
    }
    if (style == SKIP_BUTTON_ROUND) {
      Icon(
        modifier = Modifier
          .size(50.dp)
          .scale(scaleX = scaleX, scaleY = 1f)
          .rotate(degrees = -45F),
        imageVector = Icons.Rounded.Refresh,
        contentDescription = stringResource(
          id = if (forward) {
            R.string.fast_forward
          } else {
            R.string.rewind
          },
        ),
      )
      Text(
        modifier = Modifier.offset(y = (1).dp),
        text = text,
        style = MaterialTheme.typography.bodySmall,
      )
    } else {
      Icon(
        modifier = Modifier
          .size(50.dp)
          .scale(scaleX = scaleX, scaleY = 1f),
        imageVector = Icons.Rounded.FastForward,
        contentDescription = stringResource(
          id = if (forward) {
            R.string.fast_forward
          } else {
            R.string.rewind
          },
        ),
      )
    }
  }
}
