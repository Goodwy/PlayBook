package voice.playbackScreen.view

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import voice.strings.R

@Composable
internal fun ChapterRow(
  chapterName: String,
  nextPreviousVisible: Boolean,
  iterations: Int,
  onSkipToNext: () -> Unit,
  onSkipToPrevious: () -> Unit,
  //onCurrentChapterClick: () -> Unit,
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 32.dp),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    if (nextPreviousVisible) {
      IconButton(onClick = onSkipToPrevious) {
        Icon(
          modifier = Modifier.size(36.dp),
          imageVector = Icons.Rounded.ChevronLeft,
          contentDescription = stringResource(id = R.string.previous_track),
        )
      }
    }
    Text(
      modifier = Modifier
        .weight(1F)
        //.clickable(onClick = onCurrentChapterClick)
        .padding(horizontal = 2.dp)
        .alpha(0.8f)
        .basicMarquee(iterations = iterations, initialDelayMillis = 2000),
      text = chapterName,
      style = MaterialTheme.typography.bodyLarge,
      //textAlign = TextAlign.Center,
      overflow = TextOverflow.Ellipsis,
      lineHeight = 18.sp,
      maxLines = 2,
    )
    if (nextPreviousVisible) {
      IconButton(onClick = onSkipToNext) {
        Icon(
          modifier = Modifier.size(36.dp),
          imageVector = Icons.Rounded.ChevronRight,
          contentDescription = stringResource(id = voice.common.R.string.pref_rewind),
        )
      }
    }
  }
}
