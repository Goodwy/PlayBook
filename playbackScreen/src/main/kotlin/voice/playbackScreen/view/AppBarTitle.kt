package voice.playbackScreen.view

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import voice.playbackScreen.BookPlayViewState

@Composable
internal fun AppBarTitle(viewState: BookPlayViewState) {
  viewState.author?.let { author ->
    val iterations = if (viewState.useAnimatedMarquee) 3 else 0
    Text(
      modifier = Modifier
        .fillMaxWidth()
        .alpha(0.8f)
        .padding(horizontal = 16.dp)
        .basicMarquee(iterations = iterations, initialDelayMillis = 2000),
      text = author,
      fontSize = 14.sp,
      textAlign = TextAlign.Center,
      overflow = TextOverflow.Ellipsis,
      maxLines = 1,
    )
  }
}
