package voice.bookOverview.views

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Circle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import voice.bookOverview.R
import voice.bookOverview.bottomSheet.BottomSheetContent
import voice.bookOverview.bottomSheet.BottomSheetItem
import voice.bookOverview.deleteBook.DeleteBookDialog
import voice.bookOverview.di.BookOverviewComponent
import voice.bookOverview.editTitle.EditBookTitleDialog
import voice.bookOverview.overview.BookOverviewCategory
import voice.bookOverview.overview.BookOverviewItemViewState
import voice.bookOverview.overview.BookOverviewLayoutMode
import voice.bookOverview.overview.BookOverviewViewState
import voice.common.BookId
import voice.common.compose.ImmutableFile
import voice.common.compose.VoiceTheme
import voice.common.compose.rememberScoped
import voice.common.recomposeHighlighter
import voice.common.rootComponentAs
import java.util.UUID

@Composable
fun BookOverviewScreen(modifier: Modifier = Modifier) {
  val bookComponent = rememberScoped {
    rootComponentAs<BookOverviewComponent.Factory.Provider>()
      .bookOverviewComponentProviderFactory.create()
  }
  val bookOverviewViewModel = bookComponent.bookOverviewViewModel
  val editBookTitleViewModel = bookComponent.editBookTitleViewModel
  val bottomSheetViewModel = bookComponent.bottomSheetViewModel
  val deleteBookViewModel = bookComponent.deleteBookViewModel
  val fileCoverViewModel = bookComponent.fileCoverViewModel
  LaunchedEffect(Unit) {
    bookOverviewViewModel.attach()
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

  val bottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
  val start = viewState.paddings.substringBeforeLast(';').substringAfterLast(';').toInt()
  val end = viewState.paddings.substringAfterLast(';').toInt()
  ModalBottomSheetLayout(
    modifier = modifier.padding(start = start.dp, end = end.dp),
    sheetState = bottomSheetState,
    sheetShape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
    scrimColor = Color.Black.copy(alpha = 0.5f),
    sheetContent = {
      Surface {
        Column {
          Box(
            modifier = Modifier
              .wrapContentHeight()
              .fillMaxWidth()
              .padding(top = 12.dp, end = 12.dp),
            contentAlignment = Alignment.TopEnd,
          ) {
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
          BottomSheetContent(bottomSheetViewModel.state.value) { item ->
            if (item == BottomSheetItem.FileCover) {
              getContentLauncher.launch("image/*")
            }
            scope.launch {
              delay(300)
              bottomSheetState.hide()
              bottomSheetViewModel.onItemClick(item)
            }
          }
          val bottom = viewState.paddings.substringAfter(';').substringBefore(';').toInt()
          Spacer(modifier = Modifier.size(bottom.dp))
        }
      }
    },
  ) {
    BookOverview(
      viewState = viewState,
      onSettingsClick = bookOverviewViewModel::onSettingsClick,
      onBookClick = bookOverviewViewModel::onBookClick,
      onBookLongClick = { bookId ->
        scope.launch {
          bottomSheetViewModel.bookSelected(bookId)
          bottomSheetState.show()
        }
      },
      onBookFolderClick = bookOverviewViewModel::onBookFolderClick,
      onPlayButtonClick = bookOverviewViewModel::playPause,
      onBookMigrationClick = {
        bookOverviewViewModel.onBoomMigrationHelperConfirmClick()
        bookOverviewViewModel.onBookMigrationClick()
      },
      onBoomMigrationHelperConfirmClick = bookOverviewViewModel::onBoomMigrationHelperConfirmClick,
      onSearchClick = bookOverviewViewModel::onSearchClick,
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
  onSearchClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
  val top = viewState.paddings.substringBefore(';').toInt()
  val bottom = viewState.paddings.substringAfter(';').substringBefore(';').toInt()
  Scaffold(
    modifier = modifier
      .nestedScroll(scrollBehavior.nestedScrollConnection),
    topBar = {
      TopAppBar(
        title = {
          //Text(text = stringResource(id = R.string.app_name))
          Row(modifier = Modifier.padding(end = 16.dp)) {
            SettingsIcon(onSettingsClick = onSettingsClick, icon = false)
            BookFolderIcon(withHint = viewState.showAddBookHint, onClick = onBookFolderClick, icon = false)
            Spacer(modifier = Modifier.weight(1f))
            if (viewState.showMigrateIcon) {
              MigrateIcon(
                onClick = onBookMigrationClick,
                withHint = viewState.showMigrateHint,
                onHintClick = onBoomMigrationHelperConfirmClick,
              )
            }
            if (viewState.showSearchIcon) {
              IconButton(onClick = onSearchClick) {
                Icon(
                  imageVector = Icons.Rounded.Search,
                  contentDescription = stringResource(R.string.search_hint),
                  tint = MaterialTheme.colorScheme.primary
                )
              }
            }
          }
        },
        scrollBehavior = scrollBehavior,
        windowInsets = WindowInsets(top = top.dp),
        /*actions = {
          if (viewState.showSearchIcon) {
            IconButton(onClick = onSearchClick) {
              Icon(imageVector = Icons.Rounded.Search, contentDescription = null)
            }
          }
          if (viewState.showMigrateIcon) {
            MigrateIcon(
              onClick = onBookMigrationClick,
              withHint = viewState.showMigrateHint,
              onHintClick = onBoomMigrationHelperConfirmClick,
            )
          }
          BookFolderIcon(withHint = viewState.showAddBookHint, onClick = onBookFolderClick)

          SettingsIcon(onSettingsClick)
        },*/
      )
    },
    floatingActionButton = {
      if (viewState.playButtonState != null) {
        val playing = viewState.playButtonState == BookOverviewViewState.PlayButtonState.Playing
        if (viewState.miniPlayerStyle != 0)
          PlayButton(
            modifier = modifier.padding(bottom = bottom.dp),
            playing = playing,
            style = viewState.miniPlayerStyle,
            onClick = onPlayButtonClick,
          )
        else
          FloatingActionButton(
            modifier = modifier
              .padding(start = 32.dp, bottom = bottom.dp)
              .height(68.dp)
              .fillMaxWidth(),
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
                      id = R.drawable.avd_pause_to_play,
                    ),
                    atEnd = !playing,
                  ),
                  contentDescription = stringResource(R.string.play_pause),
                )
              }
            }
          }
      }
    },
  ) { contentPadding ->
    when (viewState) {
      is BookOverviewViewState.Content -> {
        when (viewState.layoutMode) {
          BookOverviewLayoutMode.List -> {
            ListBooks(
              books = viewState.books,
              onBookClick = onBookClick,
              onBookLongClick = onBookLongClick,
              contentPadding = contentPadding,
            )
          }
          BookOverviewLayoutMode.Grid -> {
            GridBooks(
              books = viewState.books,
              onBookClick = onBookClick,
              onBookLongClick = onBookLongClick,
              contentPadding = contentPadding,
            )
          }
        }
      }
      BookOverviewViewState.Loading -> {
        Box(
          Modifier
            .fillMaxSize()
            .padding(contentPadding),
        ) {
          CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
      }
    }
  }
}

@Composable
private fun Cover(
  cover: ImmutableFile?,
  cornerRadius: Dp = 8.dp,
  elevation: Dp = 2.dp) {
  AsyncImage(
    modifier = Modifier
      .recomposeHighlighter()
      .size(52.dp, 52.dp)
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

@Preview // ktlint-disable twitter-compose:preview-public-check
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
      onSearchClick = {},
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
    BookOverviewViewState.Loading,
    BookOverviewViewState.Content(
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
      title = "Book",
      chapterName = "Chapter",
      cover = null
    ),
  )
}
