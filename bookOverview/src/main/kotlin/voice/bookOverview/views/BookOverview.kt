package voice.bookOverview.views

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Circle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import coil.compose.AsyncImage
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.coroutines.launch
import voice.bookOverview.bottomSheet.BottomSheetContent
import voice.bookOverview.bottomSheet.BottomSheetItem
import voice.bookOverview.bottomSheet.EditBookBottomSheetState
import voice.bookOverview.deleteBook.DeleteBookDialog
import voice.bookOverview.di.BookOverviewComponent
import voice.bookOverview.editAuthor.EditBookAuthorDialog
import voice.bookOverview.editTitle.EditBookTitleDialog
import voice.bookOverview.overview.BookOverviewCategory
import voice.bookOverview.overview.BookOverviewItemViewState
import voice.bookOverview.overview.BookOverviewLayoutMode
import voice.bookOverview.overview.BookOverviewViewState
import voice.bookOverview.search.BookSearchViewState
import voice.bookOverview.views.topbar.BookOverviewTopBar
import voice.common.BookId
import voice.common.compose.PlayButton
import voice.common.compose.ImmutableFile
import voice.common.compose.VoiceTheme
import voice.common.compose.rememberScoped
import voice.common.constants.MINI_PLAYER_PLAYER
import voice.common.rootComponentAs
import java.util.UUID
import kotlin.math.abs
import kotlin.time.Duration.Companion.seconds
import voice.strings.R as StringsR
import voice.common.R as CommonR

@Composable
fun BookOverviewScreen(modifier: Modifier = Modifier) {
  val bookComponent = rememberScoped {
    rootComponentAs<BookOverviewComponent.Factory.Provider>()
      .bookOverviewComponentProviderFactory.create()
  }
  val bookOverviewViewModel = bookComponent.bookOverviewViewModel
  val editBookTitleViewModel = bookComponent.editBookTitleViewModel
  val editBookAuthorViewModel = bookComponent.editBookAuthorViewModel
  val bottomSheetViewModel = bookComponent.bottomSheetViewModel
  val deleteBookViewModel = bookComponent.deleteBookViewModel
  val fileCoverViewModel = bookComponent.fileCoverViewModel

  val lifecycleOwner = LocalLifecycleOwner.current
  DisposableEffect(lifecycleOwner) {
    val observer = LifecycleEventObserver { _, event ->
      if (event == Lifecycle.Event.ON_START) {
        bookOverviewViewModel.attach()
      }
    }
    lifecycleOwner.lifecycle.addObserver(observer)
    onDispose {
      lifecycleOwner.lifecycle.removeObserver(observer)
    }
  }
  val viewState = bookOverviewViewModel.state()

  val scope = rememberCoroutineScope()

  val getContentLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.GetContent(),
    onResult = { uri ->
      if (uri != null) {
        fileCoverViewModel.onImagePicked(uri)
      }
    },
  )

  var showBottomSheet by remember { mutableStateOf(false) }
  val start = viewState.paddings.substringBeforeLast(';').substringAfterLast(';').toInt()
  val end = viewState.paddings.substringAfterLast(';').toInt()
  var selectBookTitle by remember { mutableStateOf("") }
  var selectBookCover by remember { mutableStateOf(null as ImmutableFile?) }
  var selectBookDuration : Long by remember { mutableLongStateOf(0) }
  var selectBookChaptersSize by remember { mutableIntStateOf(1) }
  BookOverview(
    viewState = viewState,
    onSettingsClick = bookOverviewViewModel::onSettingsClick,
    onBookClick = bookOverviewViewModel::onBookClick,
    onBookLongClick = { bookId ->
      scope.launch {
        selectBookTitle = editBookTitleViewModel.selectBookTitle(bookId)
        selectBookCover = editBookTitleViewModel.selectBookCover(bookId)
        selectBookDuration = editBookTitleViewModel.selectBookDuration(bookId)
        selectBookChaptersSize = editBookTitleViewModel.selectBookChaptersSize(bookId)
        bottomSheetViewModel.bookSelected(bookId)
        showBottomSheet = !showBottomSheet
      }
    },
    onBookFolderClick = bookOverviewViewModel::onBookFolderClick,
    onPlayButtonClick = bookOverviewViewModel::playPause,
    onBookMigrationClick = {
      bookOverviewViewModel.onBoomMigrationHelperConfirmClick()
      bookOverviewViewModel.onBookMigrationClick()
    },
    onBoomMigrationHelperConfirmClick = bookOverviewViewModel::onBoomMigrationHelperConfirmClick,
    onSearchActiveChange = bookOverviewViewModel::onSearchActiveChange,
    onSearchQueryChange = bookOverviewViewModel::onSearchQueryChange,
    onSearchBookClick = bookOverviewViewModel::onSearchBookClick,
    onPermissionBugCardClicked = bookOverviewViewModel::onPermissionBugCardClicked,
  )
  val deleteBookViewState = deleteBookViewModel.state.value
  if (deleteBookViewState != null) {
    DeleteBookDialog(
      viewState = deleteBookViewState,
      onDismiss = deleteBookViewModel::onDismiss,
      onConfirmDeletion = deleteBookViewModel::onConfirmDeletion,
      onDeleteCheckBoxChecked = deleteBookViewModel::onDeleteCheckBoxChecked,
    )
  }
  val editBookTitleState = editBookTitleViewModel.state.value
  if (editBookTitleState != null) {
    EditBookTitleDialog(
      onDismissEditTitleClick = editBookTitleViewModel::onDismissEditTitle,
      onConfirmEditTitle = editBookTitleViewModel::onConfirmEditTitle,
      viewState = editBookTitleState,
      onUpdateEditTitle = editBookTitleViewModel::onUpdateEditTitle,
    )
  }
  val editBookAuthorState = editBookAuthorViewModel.state.value
  if (editBookAuthorState != null) {
    EditBookAuthorDialog(
      onDismissEditAuthorClick = editBookAuthorViewModel::onDismissEditAuthor,
      onConfirmEditAuthor = editBookAuthorViewModel::onConfirmEditAuthor,
      viewState = editBookAuthorState,
      onUpdateEditAuthor = editBookAuthorViewModel::onUpdateEditAuthor,
    )
  }
  if (showBottomSheet) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
      modifier = modifier.padding(start = start.dp, end = end.dp),
      dragHandle = null,
      sheetState = sheetState,
      shape = RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp),
      scrimColor = Color.Black.copy(alpha = 0.5f),
      content = {
        Surface {
          Column {
            Row(
              modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(start = 16.dp, top = 12.dp, end = 12.dp),
              verticalAlignment = Alignment.Top,
            ) {
              Row(
                modifier = Modifier
                  .wrapContentHeight()
                  .padding(top = 5.dp, bottom = 8.dp)
                  .weight(1f),
                verticalAlignment = Alignment.CenterVertically,
              ) {
                Cover(cover = selectBookCover, size = 56.dp, cornerRadius = 8.dp)
                Column(
                  modifier = Modifier.height(58.dp)
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp)
                    .padding(top = 3.dp),
                  verticalArrangement = Arrangement.Center
                ) {
                  Text(
                    modifier = Modifier.heightIn(min = 20.dp, max = 38.dp),
                    text = selectBookTitle,
                    fontSize = 16.sp,
                    lineHeight = 16.sp,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                  )
                  val chaptersSize = pluralStringResource(
                    id = CommonR.plurals.chapters_count,
                    count = selectBookChaptersSize,
                    selectBookChaptersSize
                  )
                  Text(
                    modifier = Modifier
                      .heightIn(min = 18.dp, max = 24.dp)
                      .alpha(0.6f),
                    text = chaptersSize + "     " + selectBookDuration.seconds.toString(),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Start,
                    fontSize = 12.sp,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                  )
                }
              }
              Box(
                modifier = Modifier
                  .size(32.dp)
                  .combinedClickable(
                    onClick = {
                      scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                          showBottomSheet = false
                        }
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
                  contentDescription = stringResource(id = StringsR.string.close),
                  tint = contentColorFor(MaterialTheme.colorScheme.background)
                )
                Icon(
                  modifier = Modifier
                    .size(32.dp)
                    .padding(5.dp),
                  imageVector = Icons.Rounded.Close,
                  contentDescription = stringResource(id = StringsR.string.close),
                  tint = contentColorFor(MaterialTheme.colorScheme.background)
                )
              }
            }
            Spacer(modifier = Modifier.size(6.dp))
            Card(
              modifier = Modifier.padding(start = 16.dp, end = 16.dp),
              shape = RoundedCornerShape(8.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHighest),
              elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
              BottomSheetContent(
                EditBookBottomSheetState(
                  items = listOf(
                    BottomSheetItem.Title,
                    BottomSheetItem.Author,
                    BottomSheetItem.InternetCover,
                    BottomSheetItem.FileCover
                  )
                )
              ) { item ->
                if (item == BottomSheetItem.FileCover) {
                  getContentLauncher.launch("image/*")
                }
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                  if (!sheetState.isVisible) {
                    showBottomSheet = false
                    bottomSheetViewModel.onItemClick(item)
                  }
                }
              }
            }
            Spacer(modifier = Modifier.size(12.dp))
            Card(
              modifier = Modifier.padding(start = 16.dp, end = 16.dp),
              shape = RoundedCornerShape(8.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHighest),
              elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
              BottomSheetContent(EditBookBottomSheetState(items = listOf(BottomSheetItem.DeleteBook))) { item ->
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                  if (!sheetState.isVisible) {
                    showBottomSheet = false
                    bottomSheetViewModel.onItemClick(item)
                  }
                }
              }
            }
            Spacer(modifier = Modifier.size(12.dp))
            Card(
              modifier = Modifier.padding(start = 16.dp, end = 16.dp),
              shape = RoundedCornerShape(8.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHighest),
              elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
              BottomSheetContent(bottomSheetViewModel.state.value) { item ->
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                  if (!sheetState.isVisible) {
                    showBottomSheet = false
                    bottomSheetViewModel.onItemClick(item)
                  }
                }
              }
            }
            val bottom = viewState.paddings.substringAfter(';').substringBefore(';').toInt()
            Spacer(modifier = Modifier.size(bottom.dp + 4.dp))
          }
        }
      },
      onDismissRequest = {
        showBottomSheet = false
      },
    )
  }
}

@Composable
internal fun BookOverview(
  viewState: BookOverviewViewState,
  onSettingsClick: () -> Unit,
  onBookClick: (BookId) -> Unit,
  onBookLongClick: (BookId) -> Unit,
  onBookFolderClick: () -> Unit,
  onPlayButtonClick: () -> Unit,
  onBookMigrationClick: () -> Unit,
  onBoomMigrationHelperConfirmClick: () -> Unit,
  onSearchActiveChange: (Boolean) -> Unit,
  onSearchQueryChange: (String) -> Unit,
  onSearchBookClick: (BookId) -> Unit,
  onPermissionBugCardClicked: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
  val bottom = viewState.paddings.substringAfter(';').substringBefore(';').toInt()
  val start = viewState.paddings.substringBeforeLast(';').substringAfterLast(';').toInt()
  val end = viewState.paddings.substringAfterLast(';').toInt()
  Scaffold(
    modifier = modifier.padding(start = start.dp, end = end.dp)
      .nestedScroll(scrollBehavior.nestedScrollConnection),
    topBar = {
      BookOverviewTopBar(
        viewState = viewState,
        onBookMigrationClick = onBookMigrationClick,
        onBoomMigrationHelperConfirmClick = onBoomMigrationHelperConfirmClick,
        onBookFolderClick = onBookFolderClick,
        onSettingsClick = onSettingsClick,
        onActiveChange = onSearchActiveChange,
        onQueryChange = onSearchQueryChange,
        onSearchBookClick = onSearchBookClick,
        )
    },
    floatingActionButton = {
      if (viewState.playButtonState != null) {
        val playing = viewState.playButtonState == BookOverviewViewState.PlayButtonState.Playing
        if (viewState.miniPlayerStyle == MINI_PLAYER_PLAYER) {
          var direction by remember { mutableIntStateOf(-1) }
          val layoutDirection = LocalLayoutDirection.current
          val isRtl = layoutDirection == LayoutDirection.Rtl
          val bookComponent = rememberScoped {
            rootComponentAs<BookOverviewComponent.Factory.Provider>()
              .bookOverviewComponentProviderFactory.create()
          }
          val bookOverviewViewModel = bookComponent.bookOverviewViewModel
          val haptic = LocalHapticFeedback.current
          val useHapticFeedback = viewState.useHapticFeedback
          FloatingActionButton(
            modifier = modifier
              .padding(start = 32.dp, bottom = bottom.dp)
              .height(68.dp)
              .fillMaxWidth()
              .pointerInput(Unit) {
                if (viewState.useGestures) {
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
                        0 -> {//right swipe = next
                          if (useHapticFeedback) haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                          if (isRtl) bookOverviewViewModel.next() else bookOverviewViewModel.previous()
                        }
                        1 -> {// left swipe = previous
                          if (useHapticFeedback) haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                          if (isRtl) bookOverviewViewModel.previous() else bookOverviewViewModel.next()
                        }
                        2 -> {} // down swipe = not use
                        3 -> {// up swipe = show list of chapters
                          if (useHapticFeedback) haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                          onBookClick(viewState.currentBook!!)
                        }
                      }
                    }
                  )
                }
              },
            containerColor = MaterialTheme.colorScheme.inverseOnSurface,//MaterialTheme.colorScheme.surface,
            onClick = { onBookClick(viewState.currentBook!!) }
          ) {
            Row(
              modifier = Modifier
                .padding(start = 8.dp, end = 8.dp)
                .fillMaxSize(),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.Center,
            ) {
              Cover(cover = viewState.cover)
              Column(
                modifier = Modifier
                  .weight(1f)
                  .padding(start = 12.dp, end = 4.dp),
              ) {
                Text(
                  text = viewState.title,
                  style = MaterialTheme.typography.bodyMedium,
                  overflow = TextOverflow.Ellipsis,
                  maxLines = 1,
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                  modifier = Modifier.alpha(0.6f),
                  text = viewState.chapterName,
                  style = MaterialTheme.typography.bodyMedium,
                  overflow = TextOverflow.Ellipsis,
                  maxLines = 1,
                )
              }
              IconButton(onClick = onPlayButtonClick) {
                Icon(
                  modifier = Modifier.size(52.dp),
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
        }
        else
          PlayButton(
            modifier = modifier.padding(bottom = bottom.dp),
            playing = playing,
            fabSize = 56.dp,
            iconSize = 24.dp,
            onPlayClick = onPlayButtonClick,
            style = viewState.miniPlayerStyle,)
      }
    },
  ) { contentPadding ->
    Box(Modifier.padding(contentPadding)) {
      when (viewState.layoutMode) {
        BookOverviewLayoutMode.List -> {
          ListBooks(
            books = viewState.books,
            onBookClick = onBookClick,
            onBookLongClick = onBookLongClick,
            showPermissionBugCard = viewState.showStoragePermissionBugCard,
            onPermissionBugCardClicked = onPermissionBugCardClicked,
            currentBook = viewState.currentBook,
          )
        }
        BookOverviewLayoutMode.Grid -> {
          GridBooks(
            books = viewState.books,
            onBookClick = onBookClick,
            onBookLongClick = onBookLongClick,
            showPermissionBugCard = viewState.showStoragePermissionBugCard,
            onPermissionBugCardClicked = onPermissionBugCardClicked,
            currentBook = viewState.currentBook,
          )
        }
      }
    }
  }
}

@Composable
private fun Cover(
  cover: ImmutableFile?,
  size: Dp = 52.dp,
  cornerRadius: Dp = 8.dp,
  elevation: Dp = 2.dp) {
  AsyncImage(
    modifier = Modifier
      .size(size, size)
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
    placeholder = painterResource(id = CommonR.drawable.album_art),
    error = painterResource(id = CommonR.drawable.album_art),
    contentDescription = stringResource(id = StringsR.string.cover),
  )
}

@Suppress("ktlint:compose:preview-public-check")
@Preview
@Composable
fun BookOverviewPreview(
  @PreviewParameter(BookOverviewPreviewParameterProvider::class)
  viewState: BookOverviewViewState,
) {
  VoiceTheme(preview = true) {
    BookOverview(
      viewState = viewState,
      onSettingsClick = {},
      onBookClick = {},
      onBookLongClick = {},
      onBookFolderClick = {},
      onPlayButtonClick = {},
      onBookMigrationClick = {},
      onBoomMigrationHelperConfirmClick = {},
      onSearchActiveChange = {},
      onSearchQueryChange = {},
      onSearchBookClick = {},
      onPermissionBugCardClicked = {},
    )
  }
}

internal class BookOverviewPreviewParameterProvider : PreviewParameterProvider<BookOverviewViewState> {

  fun book(): BookOverviewItemViewState {
    return BookOverviewItemViewState(
      name = "Book",
      author = "Author",
      cover = null,
      progress = 0.8F,
      id = BookId(UUID.randomUUID().toString()),
      remainingTime = "01:04",
    )
  }

  override val values = sequenceOf(
    BookOverviewViewState(
      books = persistentMapOf(
        BookOverviewCategory.CURRENT to buildList { repeat(10) { add(book()) } },
        BookOverviewCategory.FINISHED to listOf(book(), book()),
      ),
      layoutMode = BookOverviewLayoutMode.List,
      playButtonState = BookOverviewViewState.PlayButtonState.Paused,
      showAddBookHint = false,
      showMigrateHint = false,
      showMigrateIcon = true,
      showSearchIcon = true,
      isLoading = true,
      searchActive = false,
      searchViewState = BookSearchViewState.EmptySearch(
        suggestedAuthors = emptyList(),
        recentQueries = emptyList(),
        query = "",
      ),
      showStoragePermissionBugCard = false,
      paddings = "0;0;0;0",
      title = "Book",
      chapterName = "Chapter",
      cover = null,
      currentBook = null,
      miniPlayerStyle = MINI_PLAYER_PLAYER,
      useGestures = true,
      useHapticFeedback = true,
      useMenuIconsPref = false,
    ),
  )
}
