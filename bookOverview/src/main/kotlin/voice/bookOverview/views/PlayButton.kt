package voice.bookOverview.views

import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import voice.bookOverview.R

@Composable
internal fun PlayButton(modifier: Modifier, playing: Boolean, style: Int = 1, onClick: () -> Unit) {
  FloatingActionButton(
    modifier = modifier,
    shape = if (style == 2) FloatingActionButtonDefaults.shape else MaterialTheme.shapes.small.copy(CornerSize(percent = 50)),
    onClick = onClick
  ) {
    Icon(
      painter = rememberAnimatedVectorPainter(
        animatedImageVector = AnimatedImageVector.animatedVectorResource(
          id = R.drawable.avd_pause_to_play,
        ),
        atEnd = !playing,
      ),
      contentDescription = stringResource(R.string.play_pause),
    )
  }
}
