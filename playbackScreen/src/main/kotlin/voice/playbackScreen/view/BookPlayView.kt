package voice.playbackScreen.view

import android.text.format.DateUtils
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Circle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.launch
import voice.common.AudioWave
import voice.common.compose.ImmutableFile
import voice.common.BlurTransformation
import voice.common.compose.VoiceTheme
import voice.common.compose.isDarkTheme
import voice.common.constants.PLAYER_BACKGROUND_BLUR_COVER
import voice.common.constants.PLAYER_BACKGROUND_BLUR_COVER_2
import voice.common.constants.PLAY_BUTTON_ROUND
import voice.common.constants.REPEAT_OFF
import voice.common.constants.SKIP_BUTTON_CLASSIC
import voice.common.formatTime
import voice.data.ChapterMark
import voice.playbackScreen.BookPlayDialogViewState
import voice.playbackScreen.BookPlayViewState
import voice.playbackScreen.PrefViewState
import voice.strings.R
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes

@Composable
internal fun BookPlayView(
  viewState: BookPlayViewState,
  prefViewState: PrefViewState,
  useLandscapeLayout: Boolean,
  isNotLong: Boolean,
  isSmallScreen: Boolean,
  onPlayClick: () -> Unit,
  onRewindClick: () -> Unit,
  onFastForwardClick: () -> Unit,
  onSeek: (Duration) -> Unit,
  onSeekVolume: (Int) -> Unit,
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
  onSkipToNext: () -> Unit,
  onSkipToPrevious: () -> Unit,
  onCloseClick: () -> Unit,
  //onCurrentChapterClick: () -> Unit,
  onCurrentTimeClick: () -> Unit,
  snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
  onChapterClick: (Int) -> Unit,
) {
  Box(Modifier.background(MaterialTheme.colorScheme.background)) {
    if (viewState.playerBackground == PLAYER_BACKGROUND_BLUR_COVER) {
      val scale = if (isDarkTheme()) 0.2f else 0.5f
      val request = ImageRequest.Builder(LocalContext.current)
        .data(viewState.cover?.file)
        .transformations(
          listOf(
            BlurTransformation(
              radius = 25,
              scale = scale,
            )
          )
        )
        .build()
      AsyncImage(
        modifier = Modifier
          .alpha(0.2f)
          .fillMaxSize(),
        contentScale = ContentScale.FillBounds,
        model = request,
        placeholder = ColorPainter(MaterialTheme.colorScheme.background),
        error = ColorPainter(MaterialTheme.colorScheme.background),
        contentDescription = stringResource(id = R.string.cover),
      )
    } else if (viewState.playerBackground == PLAYER_BACKGROUND_BLUR_COVER_2) {
      val scale = if (isDarkTheme()) 0.2f else 0.5f
      val request = ImageRequest.Builder(LocalContext.current)
        .data(viewState.cover?.file)
        .transformations(
          listOf(
            BlurTransformation(
              radius = 100,
              scale = scale,
            )
          )
        )
        .build()
      AsyncImage(
        modifier = Modifier
          .alpha(0.6f)
          .fillMaxSize(),
        contentScale = ContentScale.FillBounds,
        model = request,
        placeholder = ColorPainter(MaterialTheme.colorScheme.background),
        error = ColorPainter(MaterialTheme.colorScheme.background),
        contentDescription = stringResource(id = R.string.cover),
      )
    }
    val top = viewState.paddings.substringBefore(';').toInt()
    val bottom = viewState.paddings.substringAfter(';').substringBefore(';').toInt()
//    val start = viewState.paddings.substringBeforeLast(';').substringAfterLast(';').toInt()
//    val end = viewState.paddings.substringAfterLast(';').toInt()
    val scope = rememberCoroutineScope()
    val dialogState = BookPlayDialogViewState.SelectChapterDialog(
      chapters = viewState.chapterMarks,
      selectedIndex = viewState.selectedIndex.takeUnless { it == -1 },
    )
    var showBottomSheet by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    if (showBottomSheet) {
      // Chapter List
      ModalBottomSheet(
        onDismissRequest = {
          showBottomSheet = false
        },
        dragHandle = null,
        //modifier = Modifier.padding(start = start.dp, end = end.dp),
        sheetState = bottomSheetState,
        shape = RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp),
        //sheetBackgroundColor = MaterialTheme.colorScheme.background,
        scrimColor = Color.Black.copy(alpha = 0.5f),
        containerColor = MaterialTheme.colorScheme.background,
        content = { //Chapter bottom dialog
          Column(
            modifier = Modifier
              .fillMaxHeight(0.9F)
              .fillMaxWidth()
              .background(MaterialTheme.colorScheme.background),
            content = {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                  .padding(12.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Center,
              ) {
                Box(
                  contentAlignment = Alignment.Center,
                  modifier = Modifier
                    .size(56.dp)
                    .padding(5.dp, 5.dp, 0.dp, 0.dp),
                ) {
                  Cover(
                    cover = viewState.cover,
                    cornerRadius = 8.dp,
                    elevation = 4.dp
                  )
                }
                Column(
                  modifier = Modifier.height(58.dp)
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 12.dp)
                    .padding(top = 5.dp),
                ) {
                  Text(
                    modifier = Modifier
                      .weight(1F)
                      .padding(horizontal = 2.dp),
                    text = viewState.author ?: viewState.title,
                    color = contentColorFor(MaterialTheme.colorScheme.background),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Start,
                    fontSize = 14.sp,
                    lineHeight = 14.sp,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                  )
                  Text(
                    modifier = Modifier
                      .weight(1F)
                      .alpha(0.6f)
                      .padding(horizontal = 2.dp),
                    text = viewState.title,
                    color = contentColorFor(MaterialTheme.colorScheme.background),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Start,
                    fontSize = 14.sp,
                    lineHeight = 14.sp,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                  )
                  Text(
                    modifier = Modifier
                      .weight(1F)
                      .alpha(0.6f)
                      .padding(horizontal = 1.dp),
                    text = viewState.playedTimeInPer.toString() + "%     "
                      + DateUtils.formatElapsedTime(viewState.remainingTimeInMs / 1000)
                      + " / " + DateUtils.formatElapsedTime(viewState.bookDuration / 1000),
                    //text = stringResource(id = R.string.left, formatTimeMinutes(LocalContext.current, viewState.remainingTimeInMs)),
                    color = contentColorFor(MaterialTheme.colorScheme.background),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Start,
                    fontSize = 12.sp,
                    lineHeight = 12.sp,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                  )
                }
                Box(
                  modifier = Modifier
                    .size(32.dp)
                    .combinedClickable(
                      onClick = {
                        scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                          if (!bottomSheetState.isVisible) {
                            showBottomSheet = false
                          }
                        }
                      },
                      indication = ripple(bounded = false, radius = 20.dp),
                      interactionSource = remember { MutableInteractionSource() },
                    ),
                  contentAlignment = Alignment.Center,
                ) {
                  Icon(
                    modifier = Modifier
                      .size(32.dp)
                      .alpha(0.2f),
                    imageVector = Icons.Rounded.Circle,
                    contentDescription = stringResource(id = R.string.close),
                    tint = contentColorFor(MaterialTheme.colorScheme.background)
                  )
                  Icon(
                    modifier = Modifier
                      .size(32.dp)
                      .padding(5.dp),
                    imageVector = Icons.Rounded.Close,
                    contentDescription = stringResource(id = R.string.close),
                    tint = contentColorFor(MaterialTheme.colorScheme.background)
                  )
                }
              }
              LazyColumn(
                state = rememberLazyListState(
                  initialFirstVisibleItemIndex = dialogState.selectedIndex ?: 0,
                  initialFirstVisibleItemScrollOffset = dialogState.selectedIndex ?: 0
                ),
                content = {
                  itemsIndexed(dialogState.chapters) { index, chapter ->
                    ListItem(
                      modifier = Modifier.clickable {
                        onChapterClick(index)
                      },
                      leadingContent = {
                        /*Box(
                        modifier = Modifier
                          .size(8.dp),
                        contentAlignment = Alignment.Center,
                      ) {
                        if (dialogState.selectedIndex == index) {
                          Icon(
                            modifier = Modifier
                              .size(8.dp),
                            imageVector = Icons.Rounded.FiberManualRecord,
                            contentDescription = stringResource(id = R.string.migration_detail_content_position_current_chapter_title),
                          )
                        }
                      }*/
                        Box(
                          contentAlignment = Alignment.Center,
                          modifier = Modifier
                            .size(56.dp)
                            .padding(1.dp, 2.5.dp, 4.dp, 2.5.dp),
                        ) {
                          Cover(
                            cover = chapter.cover?.let(::ImmutableFile),
                            cornerRadius = 8.dp,
                            elevation = 3.dp
                          )
                        }
                      },
                      headlineContent = {
                        val chapterName = chapter.name ?: ""
                        Text(
                          text = if (viewState.showChapterNumbers) "${index + 1} - $chapterName" else chapterName,
                          lineHeight = 16.sp,
                          maxLines = 2,
                          color = if (dialogState.selectedIndex == index) MaterialTheme.colorScheme.primary else Color.Unspecified,
                        )
                      },
                      supportingContent = {
                        Text(
                          text = formatTime(
                            timeMs = chapter.endMs.milliseconds.inWholeMilliseconds,
                            durationMs = chapter.endMs.milliseconds.inWholeMilliseconds,
                          ),
                          fontSize = 12.sp,
                          overflow = TextOverflow.Ellipsis,
                          maxLines = 1,
                          color = if (dialogState.selectedIndex == index) MaterialTheme.colorScheme.primary else Color.Unspecified,
                        )
                      },
                      trailingContent = {
                        if (dialogState.selectedIndex == index) {
                          AnimatedVisibility(
                            visible = true,
                            enter = scaleIn(),
                            exit = scaleOut()
                          ) {
                            AudioWave(isPlaying = viewState.playing)
                          }
                        }
                      },
                    )
                    if (index < dialogState.chapters.lastIndex)
                      HorizontalDivider(
                        modifier = Modifier
                          .padding(start = 88.dp, end = 24.dp),
                        thickness = Dp.Hairline
                      )
                    if (index == dialogState.chapters.lastIndex)
                      Spacer(
                        modifier = Modifier
                          .height(bottom.dp)
                          .fillMaxWidth()
                          .background(MaterialTheme.colorScheme.background)
                      )
                  }
                },
              )
            },
          )
        },
      )
    }

    Scaffold(
      contentWindowInsets = WindowInsets(bottom = bottom.dp),
      containerColor = Color.Transparent,
      contentColor = contentColorFor(MaterialTheme.colorScheme.background),
      snackbarHost = {
        SnackbarHost(hostState = snackbarHostState)
      },
      topBar = {
        if (!useLandscapeLayout) Column {
          Spacer(modifier = Modifier.size(top.dp))
          BookPlayAppBar(
            viewState = viewState,
            //onSleepTimerClick = onSleepTimerClick,
            //onBookmarkClick = onBookmarkClick,
            //onBookmarkLongClick = onBookmarkLongClick,
            //onSpeedChangeClick = onSpeedChangeClick,
            onSkipSilenceClick = onSkipSilenceClick,
            onShowChapterNumbersClick = onShowChapterNumbersClick,
            onUseChapterCoverClick = onUseChapterCoverClick,
            onVolumeBoostClick = onVolumeBoostClick,
            onCloseClick = onCloseClick,
            useLandscapeLayout = useLandscapeLayout,
          )
        }
      },
      content = {
        BookPlayContent(
          contentPadding = it,
          viewState = viewState,
          prefViewState = prefViewState,
          onPlayClick = onPlayClick,
          onRewindClick = onRewindClick,
          onFastForwardClick = onFastForwardClick,
          onSeek = onSeek,
          onSeekVolume = onSeekVolume,
          onSkipToNext = onSkipToNext,
          onSkipToPrevious = onSkipToPrevious,
          onCurrentChapterClick = {
            scope.launch {
              showBottomSheet = !showBottomSheet
            }
          },
          useLandscapeLayout = useLandscapeLayout,
          isNotLong = isNotLong,
          isSmallScreen = isSmallScreen,
          onCurrentTimeClick = onCurrentTimeClick,
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
          onCloseClick = onCloseClick,
        )
      },
      bottomBar = {
        if (!useLandscapeLayout) {
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
            onCurrentChapterClick = {
              scope.launch {
                showBottomSheet = !showBottomSheet
              }
            },
            useLandscapeLayout = false
          )
        }
      }
    )
  }
}

@Composable
@Preview
private fun BookPlayPreview(
  @PreviewParameter(BookPlayViewStatePreviewProvider::class)
  viewState: BookPlayViewState,
  prefViewState: PrefViewState = PrefViewState(repeatMode = REPEAT_OFF)
) {
  VoiceTheme(true) {
    BookPlayView(
      viewState = viewState,
      prefViewState = prefViewState,
      onPlayClick = {},
      onRewindClick = {},
      onFastForwardClick = {},
      onSeek = {},
      onSeekVolume = {},
      onSleepTimerClick = {},
      onAcceptSleepTime = {},
      onBookmarkClick = {},
      onBookmarkLongClick = {},
      onSpeedChangeClick = {},
      onSkipSilenceClick = {},
      onRepeatClick = {},
      onShowChapterNumbersClick = {},
      onUseChapterCoverClick = {},
      onVolumeBoostClick = {},
      onSkipToNext = {},
      onSkipToPrevious = {},
      onCloseClick = {},
      //onCurrentChapterClick = {},
      useLandscapeLayout = false,
      isNotLong = false,
      isSmallScreen = false,
      onChapterClick = {},
      onCurrentTimeClick = {},
    )
  }
}

private class BookPlayViewStatePreviewProvider : PreviewParameterProvider<BookPlayViewState> {
  override val values = sequence {
    val initial = BookPlayViewState(
      chapterName = "My Chapter",
      showPreviousNextButtons = false,
      cover = null,
      duration = 10.minutes,
      playedTime = 3.minutes,
      playing = true,
      skipSilence = true,
      showChapterNumbers = true,
      useChapterCover = true,
      scanCoverChapter = true,
      sleepTime = 4.minutes,
      customSleepTime = 15,
      sleepEoc = true,
      playedTimeInPer = 30,
      remainingTimeInMs = 7,
      bookDuration = 60,
      title = "My Book",
      seekTime = 30,
      seekTimeRewind = 20,
      currentVolume = 8,
      maxVolume = 15,
      showSliderVolume = true,
      playbackSpeed = 1.00f,
      skipButtonStyle = SKIP_BUTTON_CLASSIC,
      playButtonStyle = PLAY_BUTTON_ROUND,
      paddings = "0;0;0;0",
      chapterMarks = listOf(ChapterMark("name", 0L, 100, null)),
      selectedIndex = 1,
      author = "Author",
      playerBackground = PLAYER_BACKGROUND_BLUR_COVER,
      repeatModeBook = REPEAT_OFF,
      useGestures = true,
      useHapticFeedback = true,
      useAnimatedMarquee = true,
    )
    yield(initial)
    yield(
      initial.copy(
        showPreviousNextButtons = !initial.showPreviousNextButtons,
        playing = !initial.playing,
        skipSilence = !initial.skipSilence,
      ),
    )
    yield(initial.copy(chapterName = null))
  }
}
