package voice.playbackScreen.view

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import voice.playbackScreen.BookPlayViewState
import voice.playbackScreen.PrefViewState
import kotlin.time.Duration

@Composable
internal fun BookPlayContent(
  contentPadding: PaddingValues,
  viewState: BookPlayViewState,
  prefViewState: PrefViewState,
  onPlayClick: () -> Unit,
  onRewindClick: () -> Unit,
  onFastForwardClick: () -> Unit,
  onSeek: (Duration) -> Unit,
  onSeekVolume: (Int) -> Unit,
  onSkipToNext: () -> Unit,
  onSkipToPrevious: () -> Unit,
  onCurrentChapterClick: () -> Unit,
  onCurrentTimeClick: () -> Unit,
  useLandscapeLayout: Boolean,
  isNotLong: Boolean,
  isSmallScreen: Boolean,
  onSleepTimerClick: () -> Unit,
  onAcceptSleepTime: (Int) -> Unit,
  onBookmarkClick: () -> Unit,
  onBookmarkLongClick: () -> Unit,
  onSpeedChangeClick: () -> Unit,
  onSkipSilenceClick: () -> Unit,
  onRepeatClick: () -> Unit,
  onShowChapterNumbersClick: () -> Unit,
  onUseChapterCoverClick: () -> Unit,
  onVolumeBoostClick: () -> Unit,
  onCloseClick: () -> Unit,
) {
  //val focusRequester = remember { FocusRequester() }
  val top = viewState.paddings.substringBefore(';').toInt()
  val bottom = viewState.paddings.substringAfter(';').substringBefore(';').toInt()
  if (useLandscapeLayout) {
    if (isSmallScreen) { //trying to determine window mode or split-screen mode
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(top = 8.dp + top.dp, bottom = bottom.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween,
        ) {
          CloseIcon(onCloseClick)
          Text(
            modifier = Modifier
              .weight(1f)
              .padding(start = 8.dp, end = 8.dp),
            text = viewState.title,
            fontSize = 16.sp,
            style = MaterialTheme.typography.titleLarge,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
          )
          Box(
            contentAlignment = Alignment.Center,
          ) {
            OverflowMenu(
              skipSilence = viewState.skipSilence,
              onSkipSilenceClick = onSkipSilenceClick,
              showChapterNumbers = viewState.showChapterNumbers,
              onShowChapterNumbersClick = onShowChapterNumbersClick,
              useChapterCover = viewState.useChapterCover,
              scanCoverChapter = viewState.scanCoverChapter,
              onUseChapterCoverClick = onUseChapterCoverClick,
              onVolumeBoostClick = onVolumeBoostClick,
            )
          }
        }
        viewState.chapterName?.let { chapterName ->
          val index = viewState.selectedIndex?.plus(1)
          Text(
            modifier = Modifier
              .padding(horizontal = 2.dp)
              .alpha(0.8f),
            text = if (viewState.showChapterNumbers && index != null) "$index - $chapterName" else chapterName,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            lineHeight = 18.sp,
            maxLines = 2,
          )
        }
        Spacer(modifier = Modifier.size(20.dp))
        SliderRow(
          viewState = viewState,
          onCurrentTimeClick = onCurrentTimeClick,
          onSeek = onSeek,
        )
        Spacer(modifier = Modifier.size(8.dp))
        PlaybackRow(
          playing = viewState.playing,
          onPlayClick = onPlayClick,
          onRewindClick = onRewindClick,
          onFastForwardClick = onFastForwardClick,
          seekTime = viewState.seekTime,
          seekTimeRewind = viewState.seekTimeRewind,
          skipButtonStyle = viewState.skipButtonStyle,
          playButtonStyle = viewState.playButtonStyle,
        )
        Spacer(modifier = Modifier.size(16.dp))
        if (viewState.showSliderVolume) {
          SliderVolumeRow(viewState, onSeekVolume = onSeekVolume)
        }
        BookPlayBottomBar(
          viewState = viewState,
          prefViewState = prefViewState,
          onSleepTimerClick = onSleepTimerClick,
          onAcceptSleepTime = onAcceptSleepTime,
          onBookmarkClick = onBookmarkClick,
          onBookmarkLongClick = onBookmarkLongClick,
          onSpeedChangeClick = onSpeedChangeClick,
          onSkipSilenceClick = onSkipSilenceClick,
          onRepeatClick = onRepeatClick,
          onShowChapterNumbersClick = onShowChapterNumbersClick,
          onUseChapterCoverClick = onUseChapterCoverClick,
          onVolumeBoostClick = onVolumeBoostClick,
          onCurrentChapterClick = onCurrentChapterClick,
        )
      }
    } else {
      Row(Modifier.padding(contentPadding)) {
        val padding = if (isNotLong) 24.dp else 0.dp
        CoverRow(
          cover = viewState.cover,
          onPlayClick = onPlayClick,
          onSkipToNext = onSkipToNext,
          onSkipToPrevious = onSkipToPrevious,
          onCloseClick = onCloseClick,
          onCurrentChapterClick = onCurrentChapterClick,
          sleepTime = viewState.sleepTime,
          sleepEoc = viewState.sleepEoc,
          useGestures = viewState.useGestures,
          useHapticFeedback = viewState.useHapticFeedback,
          showCloseButton = true,
          modifier = Modifier
            .fillMaxHeight()
            .weight(1F)
            .padding(start = 32.dp + padding, end = 12.dp, top = 16.dp + top.dp, bottom = 16.dp)
            .aspectRatio(1f, !isNotLong),
        )
        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp + top.dp, end = 16.dp)
            .weight(1F),
          verticalArrangement = Arrangement.Center,
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(start = 32.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
          ) {
            Text(
              modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
                .basicMarquee(iterations = 3, initialDelayMillis = 2000),
              text = viewState.title,
              fontSize = 16.sp,
              style = MaterialTheme.typography.titleLarge,
              overflow = TextOverflow.Ellipsis,
              maxLines = 1,
            )
            Box(
              contentAlignment = Alignment.Center,
            ) {
              OverflowMenu(
                skipSilence = viewState.skipSilence,
                onSkipSilenceClick = onSkipSilenceClick,
                showChapterNumbers = viewState.showChapterNumbers,
                onShowChapterNumbersClick = onShowChapterNumbersClick,
                useChapterCover = viewState.useChapterCover,
                scanCoverChapter = viewState.scanCoverChapter,
                onUseChapterCoverClick = onUseChapterCoverClick,
                onVolumeBoostClick = onVolumeBoostClick,
              )
            }
          }
          viewState.chapterName?.let { chapterName ->
            val index = viewState.selectedIndex?.plus(1)
            ChapterRow(
              chapterName = if (viewState.showChapterNumbers && index != null) "$index - $chapterName" else chapterName,
              nextPreviousVisible = false, // viewState.showPreviousNextButtons,
              onSkipToNext = onSkipToNext,
              onSkipToPrevious = onSkipToPrevious,
              //onCurrentChapterClick = onCurrentChapterClick,
            )
          }
          Spacer(modifier = Modifier.size(20.dp))
          SliderRow(
            viewState = viewState,
            onCurrentTimeClick = onCurrentTimeClick,
            onSeek = onSeek,
          )
          Spacer(modifier = Modifier.size(8.dp))
          PlaybackRow(
            playing = viewState.playing,
            onPlayClick = onPlayClick,
            onRewindClick = onRewindClick,
            onFastForwardClick = onFastForwardClick,
            seekTime = viewState.seekTime,
            seekTimeRewind = viewState.seekTimeRewind,
            skipButtonStyle = viewState.skipButtonStyle,
            playButtonStyle = viewState.playButtonStyle,
          )
          Spacer(modifier = Modifier.size(16.dp))
          if (viewState.showSliderVolume) {
            SliderVolumeRow(viewState, onSeekVolume = onSeekVolume)
          }
          BookPlayBottomBar(
            viewState = viewState,
            prefViewState = prefViewState,
            onSleepTimerClick = onSleepTimerClick,
            onAcceptSleepTime = onAcceptSleepTime,
            onBookmarkClick = onBookmarkClick,
            onBookmarkLongClick = onBookmarkLongClick,
            onSpeedChangeClick = onSpeedChangeClick,
            onSkipSilenceClick = onSkipSilenceClick,
            onRepeatClick = onRepeatClick,
            onShowChapterNumbersClick = onShowChapterNumbersClick,
            onUseChapterCoverClick = onUseChapterCoverClick,
            onVolumeBoostClick = onVolumeBoostClick,
            onCurrentChapterClick = onCurrentChapterClick,
          )
        }
      }
    }
  } else {
    Column(
      modifier = Modifier.padding(contentPadding),
      horizontalAlignment = Alignment.CenterHorizontally) {
      if (!isSmallScreen) Row(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1F)
          .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
      ) {
        CoverRow(
          onPlayClick = onPlayClick,
          onSkipToNext = onSkipToNext,
          onSkipToPrevious = onSkipToPrevious,
          onCloseClick = onCloseClick,
          onCurrentChapterClick = onCurrentChapterClick,
          cover = viewState.cover,
          sleepTime = viewState.sleepTime,
          sleepEoc = viewState.sleepEoc,
          useGestures = viewState.useGestures,
          useHapticFeedback = viewState.useHapticFeedback,
          modifier = Modifier
            .wrapContentSize()
            .aspectRatio(1f, true),
        )
      }
      Column(
        modifier = Modifier
          .widthIn(max = 500.dp)
          .weight(1F),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        Spacer(modifier = Modifier.size(16.dp))
        Text(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
            .basicMarquee(iterations = 3, initialDelayMillis = 2000),
          text = viewState.title,
          fontSize = 18.sp,
          style = MaterialTheme.typography.titleLarge,
          overflow = TextOverflow.Ellipsis,
          maxLines = 1,
        )
        viewState.chapterName?.let { chapterName ->
          val index = viewState.selectedIndex?.plus(1)
          ChapterRow(
            chapterName = if (viewState.showChapterNumbers && index != null) "$index - $chapterName" else chapterName,
            nextPreviousVisible = false, //viewState.showPreviousNextButtons,
            onSkipToNext = onSkipToNext,
            onSkipToPrevious = onSkipToPrevious,
            //onCurrentChapterClick = onCurrentChapterClick,
          )
        }
        Spacer(modifier = Modifier.size(20.dp))
        SliderRow(
          viewState = viewState,
          onCurrentTimeClick = onCurrentTimeClick,
          onSeek = onSeek
        )
        Spacer(modifier = Modifier.size(6.dp))
        PlaybackRow(
          playing = viewState.playing,
          onPlayClick = onPlayClick,
          onRewindClick = onRewindClick,
          onFastForwardClick = onFastForwardClick,
          seekTime = viewState.seekTime,
          seekTimeRewind = viewState.seekTimeRewind,
          skipButtonStyle = viewState.skipButtonStyle,
          playButtonStyle = viewState.playButtonStyle,
        )
        Spacer(modifier = Modifier.size(16.dp))
        if (viewState.showSliderVolume) {
          SliderVolumeRow(viewState, onSeekVolume = onSeekVolume)
        }
        BookPlayBottomBar(
          viewState = viewState,
          prefViewState = prefViewState,
          onSleepTimerClick = onSleepTimerClick,
          onAcceptSleepTime = onAcceptSleepTime,
          onBookmarkClick = onBookmarkClick,
          onBookmarkLongClick = onBookmarkLongClick,
          onSpeedChangeClick = onSpeedChangeClick,
          onSkipSilenceClick = onSkipSilenceClick,
          onRepeatClick = onRepeatClick,
          onShowChapterNumbersClick = onShowChapterNumbersClick,
          onUseChapterCoverClick = onUseChapterCoverClick,
          onVolumeBoostClick = onVolumeBoostClick,
          onCurrentChapterClick = onCurrentChapterClick,
        )
      }
    }
  }
}
