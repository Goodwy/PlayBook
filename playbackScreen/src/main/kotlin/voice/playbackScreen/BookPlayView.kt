package voice.playbackScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
//import androidx.compose.foundation.MarqueeAnimationMode.Companion.WhileFocused
import androidx.compose.foundation.background
//import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Circle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.FastForward
import androidx.compose.material.icons.rounded.FiberManualRecord
import androidx.compose.material.icons.rounded.FormatListBulleted
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.VolumeMute
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import voice.common.AudioWave
import voice.common.BlurTransformation
import voice.common.compose.ImmutableFile
import voice.common.compose.VoiceTheme
import voice.common.formatTime
import voice.common.formatTimeMinutes
import voice.common.recomposeHighlighter
import voice.data.ChapterMark
import java.text.DecimalFormat
import kotlin.math.abs
import kotlin.time.Duration
import kotlin.time.Duration.Companion.ZERO
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes

@Composable
internal fun BookPlayView(
  viewState: BookPlayViewState,
  prefViewState: PrefViewState,
  useLandscapeLayout: Boolean,
  onPlayClick: () -> Unit,
  onRewindClick: () -> Unit,
  onFastForwardClick: () -> Unit,
  onSeek: (Duration) -> Unit,
  onSeekVolume: (Int) -> Unit,
  onSleepTimerClick: () -> Unit,
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
  snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
  onChapterClick: (Int) -> Unit,
) {
  Box(Modifier.background(MaterialTheme.colorScheme.background)) {
    if (viewState.playerBackground == 1) {
      val request = ImageRequest.Builder(LocalContext.current)
        .data(viewState.cover?.file)
        .transformations(
          listOf(
            BlurTransformation(
              radius = 25,
              scale = 0.5f,
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
    }
    val top = viewState.paddings.substringBefore(';').toInt()
    val bottom = viewState.paddings.substringAfter(';').substringBefore(';').toInt()
    val start = viewState.paddings.substringBeforeLast(';').substringAfterLast(';').toInt()
    val end = viewState.paddings.substringAfterLast(';').toInt()
    val bottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    val dialogState = BookPlayDialogViewState.SelectChapterDialog(
      chapters = viewState.chapterMarks,
      selectedIndex = viewState.selectedIndex.takeUnless { it == -1 },
    )
    ModalBottomSheetLayout(
      modifier = Modifier.padding(start = start.dp, end = end.dp),
      sheetState = bottomSheetState,
      sheetShape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
      //sheetBackgroundColor = MaterialTheme.colorScheme.background,
      scrimColor = Color.Black.copy(alpha = 0.5f),
      sheetContent = { //Chapter bottom dialog
        Column(
          modifier = Modifier
            .fillMaxHeight(0.9F)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background),
          content = {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.inverseOnSurface)
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
                  onDoubleClick = onPlayClick,
                  cover = viewState.cover,
                  cornerRadius = 4.dp,
                  elevation = 4.dp
                )
              }
              Column(
                modifier = Modifier.height(58.dp)
                  .fillMaxWidth()
                  .weight(1f)
                  .padding(horizontal = 12.dp)
                  .padding(top = 2.dp),
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
                  overflow = TextOverflow.Ellipsis,
                  maxLines = 1,
                )
                Text(
                  modifier = Modifier
                    .weight(1F)
                    .alpha(0.6f)
                    .padding(horizontal = 2.dp),
                  text = viewState.playedTimeInPer.toString()+"%",
                  //text = stringResource(id = R.string.left, formatTimeMinutes(LocalContext.current, viewState.remainingTimeInMs)),
                  color = contentColorFor(MaterialTheme.colorScheme.background),
                  style = MaterialTheme.typography.bodyLarge,
                  textAlign = TextAlign.Start,
                  fontSize = 12.sp,
                  overflow = TextOverflow.Ellipsis,
                  maxLines = 1,
                )
              }
              Box(
                modifier = Modifier
                  .size(32.dp)
                  .combinedClickable(
                    onClick = {
                      scope.launch {
                        delay(300)
                        bottomSheetState.hide()
                      }
                    },
                    indication = rememberRipple(bounded = false, radius = 20.dp),
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
                initialFirstVisibleItemScrollOffset = dialogState.selectedIndex ?: 0),
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
                          .padding(1.dp, 5.dp, 4.dp, 0.dp),
                      ) {
                        Cover(
                          onDoubleClick = onPlayClick,
                          cover = chapter.cover?.let(::ImmutableFile),
                          cornerRadius = 4.dp,
                          elevation = 4.dp
                        )
                      }
                    },
                    headlineText = {
                      val chapterName = chapter.name ?: ""
                      Text(
                        text = if (viewState.showChapterNumbers) "${index + 1} - $chapterName" else chapterName,
                        lineHeight = 16.sp,
                      )
                    },
                    supportingText = {
                      Text(
                        text = formatTime(
                          timeMs = chapter.endMs.milliseconds.inWholeMilliseconds,
                          durationMs = chapter.endMs.milliseconds.inWholeMilliseconds,
                        ),
                        fontSize = 12.sp,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 2,
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
                    Divider(
                      modifier = Modifier
                        .padding(start = 88.dp, end = 24.dp),
                      thickness = 0.2.dp
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
    ) {
      Scaffold(
        contentWindowInsets = WindowInsets(bottom = bottom.dp),
        containerColor = Color.Transparent,
        contentColor = contentColorFor(MaterialTheme.colorScheme.background),
        snackbarHost = {
          SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
          Column {
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
                delay(300)
                bottomSheetState.show()
              }
            },
            useLandscapeLayout = useLandscapeLayout,
            onSleepTimerClick = onSleepTimerClick,
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
        }
      )
    }
    /*Scaffold(
      modifier = Modifier.padding(WindowInsets(left,top,right,bottom).asPaddingValues()),
      containerColor = Color.Transparent,
      contentColor = contentColorFor(MaterialTheme.colorScheme.background),
      snackbarHost = {
        SnackbarHost(hostState = snackbarHostState)
      },
      topBar = {
        BookPlayAppBar(
          viewState = viewState,
          //onSleepTimerClick = onSleepTimerClick,
          //onBookmarkClick = onBookmarkClick,
          //onBookmarkLongClick = onBookmarkLongClick,
          //onSpeedChangeClick = onSpeedChangeClick,
          //onSkipSilenceClick = onSkipSilenceClick,
          //onVolumeBoostClick = onVolumeBoostClick,
          onCloseClick = onCloseClick,
          useLandscapeLayout = useLandscapeLayout,
        )
      },
      content = {
        BookPlayContent(
          contentPadding = it,
          viewState = viewState,
          onPlayClick = onPlayClick,
          onRewindClick = onRewindClick,
          onFastForwardClick = onFastForwardClick,
          onSeek = onSeek,
          onSeekVolume = onSeekVolume,
          onSkipToNext = onSkipToNext,
          onSkipToPrevious = onSkipToPrevious,
          onCurrentChapterClick = onCurrentChapterClick,
          useLandscapeLayout = useLandscapeLayout,
        )
      },
      bottomBar = {
        BookPlayBottomBar(
          viewState = viewState,
          onSleepTimerClick = onSleepTimerClick,
          onBookmarkClick = onBookmarkClick,
          onBookmarkLongClick = onBookmarkLongClick,
          onSpeedChangeClick = onSpeedChangeClick,
          onSkipSilenceClick = onSkipSilenceClick,
          onVolumeBoostClick = onVolumeBoostClick,
        )
      },
    )*/
  }
}

@Composable
private fun BookPlayContent(
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
  useLandscapeLayout: Boolean,
  onSleepTimerClick: () -> Unit,
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
  if (useLandscapeLayout) {
    Row(Modifier.padding(contentPadding)) {
      CoverRow(
        cover = viewState.cover,
        onPlayClick = onPlayClick,
        onSkipToNext = onSkipToNext,
        onSkipToPrevious = onSkipToPrevious,
        onCloseClick = onCloseClick,
        sleepTime = viewState.sleepTime,
        sleepEoc = viewState.sleepEoc,
        modifier = Modifier
          .fillMaxHeight()
          .weight(1F)
          .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 24.dp)
          .aspectRatio(1f, true),
      )
      Column(
        modifier = Modifier
          .fillMaxHeight()
          .weight(1F),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          modifier = Modifier
            .fillMaxWidth()
            .recomposeHighlighter()
            .padding(horizontal = 32.dp),
            /*.graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
            .drawWithContent {
              drawContent()
              //drawFadedEdge(leftEdge = true)
              drawFadedEdge(leftEdge = false)
            }
            /*.clickable { focusRequester.requestFocus() }
            .basicMarquee(animationMode = WhileFocused)
            .focusRequester(focusRequester)
            .focusable()*/
            .basicMarquee(initialDelayMillis = 5000),*/
          text = viewState.title,
          fontSize = 16.sp,
          style = MaterialTheme.typography.titleLarge,
          overflow = TextOverflow.Ellipsis,
          maxLines = 1,
        )
        viewState.chapterName?.let { chapterName ->
          ChapterRow(
            chapterName = chapterName,
            nextPreviousVisible = false, // viewState.showPreviousNextButtons,
            onSkipToNext = onSkipToNext,
            onSkipToPrevious = onSkipToPrevious,
            //onCurrentChapterClick = onCurrentChapterClick,
          )
        }
        Spacer(modifier = Modifier.size(20.dp))
        SliderRow(viewState, onSeek = onSeek)
        Spacer(modifier = Modifier.size(16.dp))
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
  } else {
    Column(
      modifier = Modifier.padding(contentPadding),
      horizontalAlignment = Alignment.CenterHorizontally) {
      Row(
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
          cover = viewState.cover,
          sleepTime = viewState.sleepTime,
          sleepEoc = viewState.sleepEoc,
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
            .padding(horizontal = 32.dp),
            /*.graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
            .drawWithContent {
              drawContent()
              //drawFadedEdge(leftEdge = true)
              drawFadedEdge(leftEdge = false)
            }
            /*.clickable { focusRequester.requestFocus() }
            .basicMarquee(animationMode = WhileFocused)
            .focusRequester(focusRequester)
            .focusable()*/
            .basicMarquee(initialDelayMillis = 5000),*/
          text = viewState.title,
          fontSize = 18.sp,
          style = MaterialTheme.typography.titleLarge,
          overflow = TextOverflow.Ellipsis,
          maxLines = 1,
        )
        viewState.chapterName?.let { chapterName ->
          ChapterRow(
            chapterName = chapterName,
            nextPreviousVisible = false, //viewState.showPreviousNextButtons,
            onSkipToNext = onSkipToNext,
            onSkipToPrevious = onSkipToPrevious,
            //onCurrentChapterClick = onCurrentChapterClick,
          )
        }
        Spacer(modifier = Modifier.size(20.dp))
        SliderRow(viewState, onSeek = onSeek)
        Spacer(modifier = Modifier.size(14.dp))
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

/*val edgeWidth = 32.dp
fun ContentDrawScope.drawFadedEdge(leftEdge: Boolean) {
  val edgeWidthPx = edgeWidth.toPx()
  drawRect(
    topLeft = Offset(if (leftEdge) 0f else size.width - edgeWidthPx, 0f),
    size = Size(edgeWidthPx, size.height),
    brush = Brush.horizontalGradient(
      colors = listOf(Color.Transparent, Color.Black),
      startX = if (leftEdge) 0f else size.width,
      endX = if (leftEdge) edgeWidthPx else size.width - edgeWidthPx
    ),
    blendMode = BlendMode.DstIn
  )
}*/

@Composable
private fun BookPlayAppBar(
  viewState: BookPlayViewState,
  //onSleepTimerClick: () -> Unit,
  //onBookmarkClick: () -> Unit,
  //onBookmarkLongClick: () -> Unit,
  //onSpeedChangeClick: () -> Unit,
  onSkipSilenceClick: () -> Unit,
  onShowChapterNumbersClick: () -> Unit,
  onUseChapterCoverClick: () -> Unit,
  onVolumeBoostClick: () -> Unit,
  onCloseClick: () -> Unit,
  useLandscapeLayout: Boolean,
) {
  val appBarActions: @Composable RowScope.() -> Unit = {
    /*IconButton(onClick = onSpeedChangeClick) {
      var textSpeed = DecimalFormat("0.00x").format(viewState.playbackSpeed)
      if (textSpeed[3].toString() == "0") textSpeed = textSpeed.removeRange(3..3)
      else if (textSpeed[3].toString() != "0") textSpeed = textSpeed.removeSuffix("x")
      Text(
        text = textSpeed,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.sp,
        textAlign = TextAlign.Center,
        maxLines = 1,)
      /*Icon(
        imageVector = Icons.Rounded.Speed,
        contentDescription = stringResource(id = R.string.playback_speed),
      )*/
    }
    IconButton(onClick = onSleepTimerClick) {
      Icon(
        painter = if (viewState.sleepTime == ZERO) {
          painterResource(id = R.drawable.alarm)
        } else {
          painterResource(id = R.drawable.alarm_off)
        },
        /*imageVector = if (viewState.sleepTime == ZERO) {
          Icons.Rounded.Alarm
        } else {
          Icons.Rounded.AlarmOff
        },*/
        contentDescription = stringResource(id = R.string.action_sleep),
      )
    }
    Box(
      modifier = Modifier
        .size(40.dp)
        .combinedClickable(
          onClick = onBookmarkClick,
          onLongClick = onBookmarkLongClick,
          indication = rememberRipple(bounded = false, radius = 20.dp),
          interactionSource = remember { MutableInteractionSource() },
        ),
      contentAlignment = Alignment.Center,
    ) {
      Icon(
        imageVector = Icons.Rounded.BookmarkBorder,
        contentDescription = stringResource(id = R.string.bookmark),
      )
    }*/
    OverflowMenu(
      skipSilence = viewState.skipSilence,
      onSkipSilenceClick = onSkipSilenceClick,
      showChapterNumbers = viewState.showChapterNumbers,
      onShowChapterNumbersClick = onShowChapterNumbersClick,
      useChapterCover = viewState.useChapterCover,
      onUseChapterCoverClick= onUseChapterCoverClick,
      onVolumeBoostClick = onVolumeBoostClick,
    )
  }
  if (useLandscapeLayout) {
    TopAppBar(
      navigationIcon = {
        CloseIcon(onCloseClick)
      },
      actions = appBarActions,
      title = {
        AppBarTitle(viewState)
      },
      colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Transparent),
    )
  } else {
    TopAppBar(
      navigationIcon = {
        CloseIcon(onCloseClick)
      },
      actions = appBarActions,
      title = {
        AppBarTitle(viewState)
      },
      colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Transparent),
    )
  }
}

@Composable
private fun BookPlayBottomBar(
  viewState: BookPlayViewState,
  prefViewState: PrefViewState,
  onSleepTimerClick: () -> Unit,
  onBookmarkClick: () -> Unit,
  onBookmarkLongClick: () -> Unit,
  onSpeedChangeClick: () -> Unit,
  onSkipSilenceClick: () -> Unit,
  onRepeatClick: () -> Unit,
  onShowChapterNumbersClick: () -> Unit,
  onUseChapterCoverClick: () -> Unit,
  onVolumeBoostClick: () -> Unit,
  onCurrentChapterClick: () -> Unit,
  showOverflowMenu: Boolean = false,
) {
  Column(
    modifier = Modifier
      .recomposeHighlighter()
      .fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Row(
      modifier = Modifier
        .padding(start = 8.dp)
        .fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween,
    ) {
      IconButton(
        onClick = onSpeedChangeClick,
        modifier = Modifier
          .weight(1F)
      ) {
        var textSpeed = DecimalFormat("0.00x").format(viewState.playbackSpeed)
        if (textSpeed[3].toString() == "0") textSpeed = textSpeed.removeRange(3..3)
        //else if (textSpeed[3].toString() != "0") textSpeed = textSpeed.removeSuffix("x")
        Text(
          text = textSpeed,
          fontSize = 18.sp,
          //fontWeight = FontWeight.Bold,
          letterSpacing = 0.sp,
          textAlign = TextAlign.Center,
          maxLines = 1,
        )
        /*Icon(
      imageVector = Icons.Rounded.Speed,
      contentDescription = stringResource(id = R.string.playback_speed),
    )*/
      }
      IconButton(
        onClick = onRepeatClick,
        modifier = Modifier
          .weight(1F)
      ) {
        val repeatMode = if (true) viewState.repeatModeBook else prefViewState.repeatMode
        Icon(
          painter = when (repeatMode) {
            1 -> painterResource(id = R.drawable.ic_repeat_one)
            2 -> painterResource(id = R.drawable.ic_repeat_all)
            else -> painterResource(id = R.drawable.ic_repeat_off)
          },
          contentDescription = stringResource(id = R.string.repeat),
        )
      }
      IconButton(
        onClick = onSleepTimerClick,
        modifier = Modifier
          .weight(1F)
      ) {
        Icon(
          painter = if (!viewState.sleepEoc && viewState.sleepTime == ZERO) {
            painterResource(id = R.drawable.alarm)
          } else {
            painterResource(id = R.drawable.alarm_off)
          },
          contentDescription = stringResource(id = R.string.action_sleep),
        )
      }
      Box(
        modifier = Modifier
          .weight(1F)
          .size(40.dp)
          .combinedClickable(
            onClick = onBookmarkClick,
            onLongClick = onBookmarkLongClick,
            indication = rememberRipple(bounded = false, radius = 20.dp),
            interactionSource = remember { MutableInteractionSource() },
          ),
        contentAlignment = Alignment.Center,
      ) {
        Icon(
          imageVector = Icons.Rounded.BookmarkBorder,
          contentDescription = stringResource(id = R.string.bookmark),
        )
      }
      IconButton(
        onClick = onCurrentChapterClick,
        modifier = Modifier
          .weight(1F)
      ) {
        Icon(
          imageVector = Icons.Rounded.FormatListBulleted,
          contentDescription = stringResource(id = R.string.migration_detail_content_position_current_chapter_title),
        )
      }
      if (showOverflowMenu) {
        Box(
          modifier = Modifier
            .weight(1F)
            .size(40.dp),
          contentAlignment = Alignment.Center,
        ) {
          OverflowMenu(
            skipSilence = viewState.skipSilence,
            onSkipSilenceClick = onSkipSilenceClick,
            showChapterNumbers = viewState.showChapterNumbers,
            onShowChapterNumbersClick = onShowChapterNumbersClick,
            useChapterCover = viewState.useChapterCover,
            onUseChapterCoverClick = onUseChapterCoverClick,
            onVolumeBoostClick = onVolumeBoostClick,
          )
        }
      }
    }
    Spacer(modifier = Modifier.size(8.dp))
  }
}

@Composable
private fun AppBarTitle(viewState: BookPlayViewState) {
  viewState.author?.let { author ->
    Text(
      modifier = Modifier.fillMaxWidth().alpha(0.8f).padding(horizontal = 16.dp),
      text = author,
      fontSize = 14.sp,
      textAlign = TextAlign.Center,
      overflow = TextOverflow.Ellipsis,
      maxLines = 1,
    )
  }
}

@Composable
private fun CloseIcon(onCloseClick: () -> Unit) {
  IconButton(onClick = onCloseClick) {
    Icon(
      modifier = Modifier.rotate(degrees = -90F),
      imageVector = Icons.Rounded.ArrowBackIosNew,
      contentDescription = stringResource(id = R.string.close),
    )
  }
}

@Composable
private fun OverflowMenu(
  skipSilence: Boolean,
  onSkipSilenceClick: () -> Unit,
  showChapterNumbers: Boolean,
  onShowChapterNumbersClick: () -> Unit,
  useChapterCover: Boolean,
  onUseChapterCoverClick: () -> Unit,
  onVolumeBoostClick: () -> Unit,
) {
  Box(Modifier.recomposeHighlighter()) {
    var expanded by remember { mutableStateOf(false) }
    IconButton(
      onClick = {
        expanded = !expanded
      },
    ) {
      Icon(
        imageVector = Icons.Rounded.MoreHoriz,
        contentDescription = stringResource(id = R.string.more),
      )
    }
    DropdownMenu(
      expanded = expanded,
      onDismissRequest = { expanded = false },
    ) {
      ListItem(
        modifier = Modifier.clickable(
          onClick = {
            expanded = false
            onSkipSilenceClick()
          },
        ),
        headlineText = {
          Text(text = stringResource(id = R.string.skip_silence))
        },
        trailingContent = {
          Checkbox(
            checked = skipSilence,
            onCheckedChange = {
              expanded = false
              onSkipSilenceClick()
            },
          )
        },
      )
      ListItem(
        modifier = Modifier.clickable(
          onClick = {
            expanded = false
            onShowChapterNumbersClick()
          },
        ),
        headlineText = {
          Text(text = stringResource(id = R.string.show_chapter_numbers))
        },
        trailingContent = {
          Checkbox(
            checked = showChapterNumbers,
            onCheckedChange = {
              expanded = false
              onShowChapterNumbersClick()
            },
          )
        },
      )
      ListItem(
        modifier = Modifier.clickable(
          onClick = {
            expanded = false
            onUseChapterCoverClick()
          },
        ),
        headlineText = {
          Text(text = stringResource(id = R.string.pref_use_chapter_cover))
        },
        trailingContent = {
          Checkbox(
            checked = useChapterCover,
            onCheckedChange = {
              expanded = false
              onUseChapterCoverClick()
            },
          )
        },
      )
      ListItem(
        modifier = Modifier.clickable(
          onClick = {
            expanded = false
            onVolumeBoostClick()
          },
        ),
        headlineText = {
          Text(text = stringResource(id = R.string.volume_boost))
        },
      )
    }
  }
}

@Composable
private fun CoverRow(
  cover: ImmutableFile?,
  sleepTime: Duration,
  sleepEoc: Boolean,
  onPlayClick: () -> Unit,
  onSkipToNext: () -> Unit,
  onSkipToPrevious: () -> Unit,
  onCloseClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Box(contentAlignment = Alignment.Center, modifier = modifier) {
    Cover(
      onDoubleClick = onPlayClick,
      onRightSwipe = onSkipToPrevious,
      onLeftSwipe = onSkipToNext,
      onDownSwipe = onCloseClick,
      cover = cover)
    if (sleepTime != ZERO || sleepEoc) {
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
  }
}

@Composable
private fun Cover(
  onDoubleClick: () -> Unit,
  onRightSwipe: () -> Unit = {},
  onLeftSwipe: () -> Unit = {},
  onDownSwipe: () -> Unit = {},
  onUpSwipe: () -> Unit = {},
  cover: ImmutableFile?,
  cornerRadius: Dp = 12.dp,
  elevation: Dp = 16.dp) {
  var direction by remember { mutableStateOf(-1)}
  AsyncImage(
    modifier = Modifier
      .recomposeHighlighter()
      .fillMaxSize()
      //.size(300.dp, 300.dp)
      .pointerInput(Unit) {
        detectTapGestures(
          onDoubleTap = {
            onDoubleClick()
          },
        )
      }
      .pointerInput(Unit) {
        detectDragGestures(
          onDrag = {change, dragAmount ->
            change.consume()
            val (x, y) = dragAmount
            if(abs(x) > abs(y)){
              when {
                x > 0 -> { direction = 0 } //right
                x < 0 -> { direction = 1 } // left
              }
            }else{
              when {
                y > 0 -> { direction = 2 } // down
                y < 0 -> { direction = 3 } // up
              }
            }
          },
          onDragEnd = {
            when (direction) {
              0 -> { onRightSwipe() } //right swipe = next
              1 -> { onLeftSwipe()} // left swipe = previous
              2 -> { onDownSwipe() } // down swipe = close
              3 -> { onUpSwipe() } // up swipe = show list of chapters
            }
          }
        )
      }
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
    contentDescription = stringResource(id = R.string.cover),
  )
}

@Composable
private fun ChapterRow(
  chapterName: String,
  nextPreviousVisible: Boolean,
  onSkipToNext: () -> Unit,
  onSkipToPrevious: () -> Unit,
  //onCurrentChapterClick: () -> Unit,
) {
  Row(
    modifier = Modifier
      .recomposeHighlighter()
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
        .alpha(0.8f),
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
          contentDescription = stringResource(id = R.string.pref_rewind),
        )
      }
    }
  }
}

@Composable
private fun PlaybackRow(
  playing: Boolean,
  onPlayClick: () -> Unit,
  onRewindClick: () -> Unit,
  onFastForwardClick: () -> Unit,
  seekTime: Int = 30,
  seekTimeRewind: Int = 20,
  skipButtonStyle: Int = 2,
  playButtonStyle: Int = 2
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .recomposeHighlighter(),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.Center,
  ) {
    SkipButton(forward = false, style = skipButtonStyle, onClick = onRewindClick, text = seekTimeRewind.toString())
    Spacer(modifier = Modifier.size(36.dp))
    /*FloatingActionButton(
      modifier = Modifier.size(68.dp),
      onClick = onPlayClick,
    ) {
      Icon(
        modifier = Modifier.size(46.dp),
        imageVector = if (playing) {
          Icons.Rounded.Pause
        } else {
          Icons.Rounded.PlayArrow
        },
        contentDescription = stringResource(id = R.string.play_pause),
      )
    }*/
    PlayButton(
      playing = playing,
      style = playButtonStyle,
      onClick = onPlayClick,
    )
    Spacer(modifier = Modifier.size(36.dp))
    SkipButton(forward = true, style = skipButtonStyle, onClick = onFastForwardClick, text = seekTime.toString())
  }
}

@Composable
private fun PlayButton(playing: Boolean, style: Int, onClick: () -> Unit) {
  if (style != 0)
    FloatingActionButton(
      modifier = Modifier.size(68.dp),
      shape = if (style == 2) FloatingActionButtonDefaults.shape else MaterialTheme.shapes.small.copy(CornerSize(percent = 50)),
      onClick = onClick
    ) {
      Icon(
        modifier = Modifier.size(46.dp),
        painter = rememberAnimatedVectorPainter(
          animatedImageVector = AnimatedImageVector.animatedVectorResource(
            id = R.drawable.avd_pause_to_play,
          ),
          atEnd = !playing,
        ),
        contentDescription = stringResource(R.string.play_pause),
      )
    }
  else
    Box(
      contentAlignment = Alignment.Center,
      modifier = Modifier
        .recomposeHighlighter()
        .clickable(
          interactionSource = remember { MutableInteractionSource() },
          indication = rememberRipple(bounded = false),
          onClick = onClick,
        ),
    ) {
      Icon(
        modifier = Modifier.size(82.dp),
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

@Composable
private fun SkipButton(
  forward: Boolean,
  style: Int,
  onClick: () -> Unit,
  text: String,
) {
  Box(
    contentAlignment = Alignment.Center,
    modifier = Modifier
      .recomposeHighlighter()
      .clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = rememberRipple(bounded = false),
        onClick = onClick,
      ),
  ) {
    if (style == 1) {
      Icon(
        modifier = Modifier
          .size(50.dp)
          .scale(scaleX = if (!forward) -1f else 1F, scaleY = 1f)
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
          .scale(scaleX = if (!forward) -1f else 1F, scaleY = 1f),
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

@Composable
private fun SliderRow(
  viewState: BookPlayViewState,
  onSeek: (Duration) -> Unit,
) {
  Column(
    modifier = Modifier
      .recomposeHighlighter()
      .fillMaxWidth()
      .height(32.dp)
      .padding(horizontal = 16.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    var localValue by remember { mutableStateOf(0F) }
    val interactionSource = remember { MutableInteractionSource() }
    val dragging by interactionSource.collectIsDraggedAsState()
    Slider(
      modifier = Modifier
        .weight(1F)
        .padding(horizontal = 8.dp),
      interactionSource = interactionSource,
      value = if (dragging) {
        localValue
      } else {
        (viewState.playedTime / viewState.duration).toFloat()
          .coerceIn(0F, 1F)
      },
      onValueChange = {
        localValue = it
      },
      onValueChangeFinished = {
        onSeek(viewState.duration * localValue.toDouble())
      },
    )
    Spacer(modifier = Modifier.size(2.dp))
    Row(
      modifier = Modifier
        .recomposeHighlighter()
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Text(
        text = formatTime(
          timeMs = if (dragging) {
            (viewState.duration * localValue.toDouble()).inWholeMilliseconds
          } else {
            viewState.playedTime.inWholeMilliseconds
          },
          durationMs = viewState.duration.inWholeMilliseconds,
        ),
        fontSize = 12.sp,
      )
      Spacer(modifier = Modifier.weight(1f))
      Text(
        //text = viewState.playedTimeInPer.toString() + '%',
        text = stringResource(id = R.string.left, formatTimeMinutes(LocalContext.current, viewState.remainingTimeInMs)),
        fontSize = 12.sp,
      )
      Spacer(modifier = Modifier.weight(1f))
      Text(
        text = formatTime(
          timeMs = viewState.duration.inWholeMilliseconds,
          durationMs = viewState.duration.inWholeMilliseconds,
        ),
        fontSize = 12.sp,
      )
    }
  }
}

@Composable
private fun SliderVolumeRow(
  viewState: BookPlayViewState,
  onSeekVolume: (Int) -> Unit,
) {
  Row(
    modifier = Modifier
      .recomposeHighlighter()
      .fillMaxWidth()
      .padding(horizontal = 28.dp),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Icon(
      modifier = Modifier
        .size(16.dp),
      imageVector = Icons.Rounded.VolumeMute,
      contentDescription = stringResource(
        id = R.string.volume_boost
      ),
    )
    Slider(
      modifier = Modifier
        .weight(1F)
        .padding(start = 2.dp, end = 3.dp),
      valueRange = 0f..15f,
      value = viewState.currentVolume.toFloat(),
      onValueChange = {
        onSeekVolume(it.toInt())
      },
      onValueChangeFinished = {
      },
    )
    Icon(
      modifier = Modifier
        .padding(end = 3.dp)
        .size(16.dp),
      imageVector = Icons.Rounded.VolumeUp,
      contentDescription = stringResource(
        id = R.string.volume_boost
      ),
    )
  }
}

@Composable
@Preview
private fun BookPlayPreview(
  @PreviewParameter(BookPlayViewStatePreviewProvider::class)
  viewState: BookPlayViewState,
  prefViewState: PrefViewState = PrefViewState(repeatMode = 0)
) {
  VoiceTheme(preview = true) {
    BookPlayView(
      viewState = viewState,
      prefViewState = prefViewState,
      onPlayClick = {},
      onRewindClick = {},
      onFastForwardClick = {},
      onSeek = {},
      onSeekVolume = {},
      onSleepTimerClick = {},
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
      onChapterClick = {},
    )
  }
}

private class BookPlayViewStatePreviewProvider : PreviewParameterProvider<BookPlayViewState> {
  override val values = sequence {
    val initial = BookPlayViewState(
      chapterName = "My chapter",
      showPreviousNextButtons = false,
      cover = null,
      duration = 10.minutes,
      playedTime = 3.minutes,
      playing = true,
      skipSilence = true,
      showChapterNumbers = true,
      useChapterCover = true,
      sleepTime = 1.minutes,
      sleepEoc = true,
      playedTimeInPer = 30,
      remainingTimeInMs = 7,
      title = "My Book",
      seekTime = 30,
      seekTimeRewind = 20,
      currentVolume = 8,
      showSliderVolume = true,
      playbackSpeed = 1.00f,
      skipButtonStyle = 1,
      playButtonStyle = 2,
      paddings = "0;0;0;0",
      chapterMarks = listOf(ChapterMark("name", 0L, 100, null)),
      selectedIndex = 1,
      author = "Author",
      playerBackground = 0,
      repeatModeBook = 0,
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
