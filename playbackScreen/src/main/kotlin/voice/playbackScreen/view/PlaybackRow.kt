package voice.playbackScreen.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import voice.common.compose.PlayButton
import voice.common.constants.PLAY_BUTTON_ROUND
import voice.common.constants.SKIP_BUTTON_CLASSIC

@Composable
internal fun PlaybackRow(
  playing: Boolean,
  onPlayClick: () -> Unit,
  onRewindClick: () -> Unit,
  onFastForwardClick: () -> Unit,
  seekTime: Int = 30,
  seekTimeRewind: Int = 20,
  skipButtonStyle: Int = SKIP_BUTTON_CLASSIC,
  playButtonStyle: Int = PLAY_BUTTON_ROUND
) {
  Row(
    modifier = Modifier
      .fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.Center,
  ) {
    SkipButton(forward = false, style = skipButtonStyle, onClick = onRewindClick, text = seekTimeRewind.toString())
    Spacer(modifier = Modifier.size(36.dp))
    PlayButton(
      playing = playing,
      style = playButtonStyle,
      onPlayClick = onPlayClick,
    )
    Spacer(modifier = Modifier.size(36.dp))
    SkipButton(forward = true, style = skipButtonStyle, onClick = onFastForwardClick, text = seekTime.toString())
  }
}
