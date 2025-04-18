package voice.bookmark

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Sort
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.MoreTime
import androidx.compose.material.icons.rounded.SortByAlpha
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.squareup.anvil.annotations.ContributesTo
import voice.bookmark.dialogs.AddBookmarkDialog
import voice.bookmark.dialogs.EditBookmarkDialog
import voice.common.AppScope
import voice.common.BookId
import voice.common.compose.rememberScoped
import voice.common.constants.MINI_PLAYER_ROUND_BUTTON
import voice.common.constants.SORTING_BOOKMARK_LAST
import voice.common.constants.SORTING_BOOKMARK_NAME
import voice.common.constants.SORTING_BOOKMARK_TIME
import voice.common.rootComponentAs
import voice.data.Bookmark
import voice.strings.R
import java.util.UUID
import voice.strings.R as StringsR
import voice.common.R as CommonR

@ContributesTo(AppScope::class)
interface Component {
  val bookmarkViewModelFactory: BookmarkViewModel.Factory
}

@Composable
fun BookmarkScreen(bookId: BookId) {
  val viewModel = rememberScoped(bookId.value) {
    rootComponentAs<Component>().bookmarkViewModelFactory.create(bookId)
  }
  val viewState = viewModel.viewState()
  BookmarkScreen(
    viewState = viewState,
    onClose = viewModel::closeScreen,
    onAdd = viewModel::onAddClick,
    onDelete = viewModel::deleteBookmark,
    onEdit = viewModel::onEditClick,
    onScrollConfirm = viewModel::onScrollConfirm,
    onClick = viewModel::selectBookmark,
    onNewBookmarkNameChoose = viewModel::addBookmark,
    onCloseDialog = viewModel::closeDialog,
    onEditBookmark = viewModel::editBookmark,
    onSortBookmarks = viewModel::sortBookmarks,
  )
}

@Composable
internal fun BookmarkScreen(
  viewState: BookmarkViewState,
  onClose: () -> Unit,
  onAdd: () -> Unit,
  onDelete: (Bookmark.Id) -> Unit,
  onEdit: (Bookmark.Id) -> Unit,
  onScrollConfirm: () -> Unit,
  onClick: (Bookmark.Id) -> Unit,
  onCloseDialog: () -> Unit,
  onNewBookmarkNameChoose: (String) -> Unit,
  onEditBookmark: (Bookmark.Id, String) -> Unit,
  onSortBookmarks: (Int) -> Unit,
  modifier: Modifier = Modifier,
) {
  val snackbarHostState = remember { SnackbarHostState() }
  val top = viewState.paddings.substringBefore(';').toInt()
  val bottom = viewState.paddings.substringAfter(';').substringBefore(';').toInt()
  val start = viewState.paddings.substringBeforeLast(';').substringAfterLast(';').toInt()
  val end = viewState.paddings.substringAfterLast(';').toInt()

  Scaffold(
    modifier = modifier
      .padding(start = start.dp, end = end.dp),
    snackbarHost = {
      SnackbarHost(hostState = snackbarHostState)
    },
    topBar = {
      TopAppBar(
        windowInsets = WindowInsets(top = top.dp),
        title = { Text(text = stringResource(id = StringsR.string.bookmark)) },
        navigationIcon = {
          IconButton(onClick = onClose) {
            Icon(
              imageVector = Icons.Rounded.ArrowBackIosNew,
              contentDescription = stringResource(id = StringsR.string.close),
            )
          }
        },
        actions = {
          var expanded by remember { mutableStateOf(false) }
          IconButton(
            onClick = {
              expanded = !expanded
            },
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Rounded.Sort,
              contentDescription = stringResource(id = CommonR.string.pref_sorting),
            )
          }
          DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
          ) {
            DropdownMenuItem(
              onClick = {
                expanded = false
                onSortBookmarks(SORTING_BOOKMARK_LAST)
              },
              text = {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                ) {
                  RadioButton(
                    onClick = {
                      expanded = false
                      onSortBookmarks(SORTING_BOOKMARK_LAST)
                    },
                    selected = viewState.sorted == SORTING_BOOKMARK_LAST,
                  )
                  Text(text = stringResource(id = CommonR.string.pref_sorting_last_added))
                }
              },
              trailingIcon = {
                Icon(
                  modifier = Modifier.padding(horizontal = 8.dp),
                  imageVector = Icons.Rounded.MoreTime,
                  contentDescription = stringResource(id = CommonR.string.pref_sorting_last_added),
                )
              },
            )
            DropdownMenuItem(
              onClick = {
                expanded = false
                onSortBookmarks(SORTING_BOOKMARK_TIME)
              },
              text = {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                ) {
                  RadioButton(
                    onClick = {
                      expanded = false
                      onSortBookmarks(SORTING_BOOKMARK_TIME)
                    },
                    selected = viewState.sorted == SORTING_BOOKMARK_TIME,
                  )
                  Text(text = stringResource(id = CommonR.string.pref_sorting_time))
                }
              },
              trailingIcon = {
                Icon(
                  modifier = Modifier.padding(horizontal = 8.dp),
                  painter = painterResource(id = CommonR.drawable.ic_time_left),
                  contentDescription = stringResource(id = CommonR.string.pref_sorting_time),
                )
              },
            )
            DropdownMenuItem(
              onClick = {
                expanded = false
                onSortBookmarks(SORTING_BOOKMARK_NAME)
              },
              text = {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                ) {
                  RadioButton(
                    onClick = {
                      expanded = false
                      onSortBookmarks(SORTING_BOOKMARK_NAME)
                    },
                    selected = viewState.sorted == SORTING_BOOKMARK_NAME,
                  )
                  Text(text = stringResource(id = R.string.migration_detail_content_name))
                }
              },
              trailingIcon = {
                Icon(
                  modifier = Modifier.padding(horizontal = 8.dp),
                  imageVector = Icons.Rounded.SortByAlpha,
                  contentDescription = stringResource(id = R.string.migration_detail_content_name),
                )
              },
            )
          }
        },
      )
    },
    floatingActionButton = {
      FloatingActionButton(
        modifier = Modifier.padding(bottom = bottom.dp),
        shape = if (viewState.miniPlayerStyle == MINI_PLAYER_ROUND_BUTTON) MaterialTheme.shapes.small.copy(CornerSize(percent = 50)) else FloatingActionButtonDefaults.shape,
        onClick = onAdd,
        content = {
          Icon(Icons.Default.Add, contentDescription = stringResource(id = StringsR.string.add))
        },
      )
    },
  ) { paddingValues ->
    val lazyListState = rememberLazyListState()
    LaunchedEffect(viewState.shouldScrollTo, onScrollConfirm) {
      val index = viewState.bookmarks.indexOfFirst { it.id == viewState.shouldScrollTo }
      if (index != -1) {
        lazyListState.animateScrollToItem(index)
        onScrollConfirm()
      }
    }
    LazyColumn(
      state = lazyListState,
      contentPadding = paddingValues,
    ) {
      items(
        items = viewState.bookmarks,
        key = { it.id.value.toString() },
      ) { bookmark ->
        BookmarkItem(
          modifier = Modifier.animateItem(),
          bookmark = bookmark,
          onDelete = onDelete,
          onEdit = onEdit,
          onClick = onClick,
        )
        HorizontalDivider(
          modifier = Modifier
            .padding(horizontal = 16.dp),
          thickness = Dp.Hairline
        )
      }
      item {
        Spacer(Modifier.size(98.dp))
      }
    }
  }

  when (viewState.dialogViewState) {
    BookmarkDialogViewState.AddBookmark -> {
      AddBookmarkDialog(
        onDismissRequest = onCloseDialog,
        onBookmarkNameChoose = onNewBookmarkNameChoose,
      )
    }
    BookmarkDialogViewState.None -> {
    }
    is BookmarkDialogViewState.EditBookmark -> {
      EditBookmarkDialog(
        onDismissRequest = onCloseDialog,
        onEditBookmark = onEditBookmark,
        bookmarkId = viewState.dialogViewState.id,
        initialTitle = viewState.dialogViewState.title ?: "",
      )
    }
  }
}

@Composable
internal fun BookmarkItem(
  bookmark: BookmarkItemViewState,
  onDelete: (Bookmark.Id) -> Unit,
  onEdit: (Bookmark.Id) -> Unit,
  onClick: (Bookmark.Id) -> Unit,
  modifier: Modifier = Modifier,
) {
  var expanded by remember { mutableStateOf(false) }
  val haptic = LocalHapticFeedback.current
  val swipeToDismissState = rememberSwipeToDismissBoxState(
    confirmValueChange = {
      when (it) {
        SwipeToDismissBoxValue.StartToEnd -> {
          onEdit(bookmark.id)
          if (bookmark.useHapticFeedback) haptic.performHapticFeedback(HapticFeedbackType.LongPress)
          false
        }
        SwipeToDismissBoxValue.EndToStart -> {
          onDelete(bookmark.id)
          if (bookmark.useHapticFeedback) haptic.performHapticFeedback(HapticFeedbackType.LongPress)
          true
        }
        SwipeToDismissBoxValue.Settled,
          -> false
      }
    },
  )

  SwipeToDismissBox(
    modifier = modifier,
    state = swipeToDismissState,
    backgroundContent = { SwipeToDismissBackground(swipeToDismissState)},
    enableDismissFromStartToEnd = bookmark.useGestures,
    enableDismissFromEndToStart = bookmark.useGestures,
    content = {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .background(color = MaterialTheme.colorScheme.surface)
          .clickable {
            onClick(bookmark.id)
          },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
      ) {
        Column(
          modifier = Modifier
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .weight(9f),
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            if (bookmark.showSleepIcon) {
              Icon(
                modifier = Modifier
                  .padding(end = 4.dp)
                  .size(20.dp),
                painter = painterResource(id = voice.common.R.drawable.alarm),
                contentDescription = stringResource(StringsR.string.action_sleep),
              )
            }
            Text(
              modifier = Modifier
                .padding(start = 2.dp),
              text = bookmark.title,
              fontSize = 14.sp,
              lineHeight = 14.sp,
            )
          }
          Spacer(modifier = Modifier.size(4.dp))
          Row(
            modifier = Modifier
              .padding(start = 2.dp)
              .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row {
              Text(
                text = bookmark.chapterNumber,
                fontSize = 12.sp,
                )
              Spacer(modifier = Modifier.size(16.dp))
              Text(
                text = bookmark.subtitle,
                fontSize = 12.sp,
                )
            }
            Spacer(modifier = Modifier.size(16.dp))
            Text(
              text = bookmark.date,
              fontSize = 12.sp,
              )
          }
        }

        Box(
          modifier = Modifier
            .padding(end = 6.dp)
            .weight(1f),
        ) {
          IconButton(
            onClick = {
              expanded = !expanded
            },
            content = {
              Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = stringResource(id = StringsR.string.popup_edit),
              )
            },
          )
          DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
          ) {
            DropdownMenuItem(
              text = { Text(stringResource(id = StringsR.string.popup_edit)) },
              onClick = {
                expanded = false
                onEdit(bookmark.id)
              },
            )
            DropdownMenuItem(
              text = { Text(stringResource(id = StringsR.string.remove)) },
              onClick = {
                expanded = false
                onDelete(bookmark.id)
              },
            )
          }
        }
      }
    },
  )
}

@Composable
fun SwipeToDismissBackground(dismissState: SwipeToDismissBoxState) {
  val color = when (dismissState.dismissDirection) {
    SwipeToDismissBoxValue.StartToEnd -> Color(0xFF0D81DE)
    SwipeToDismissBoxValue.EndToStart -> Color(0xFFEB5545)
    SwipeToDismissBoxValue.Settled -> Color.Transparent
  }
  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(color),
  ) {
    Icon(
      modifier = Modifier
        .padding(start = 16.dp)
        .align(Alignment.CenterStart),
      imageVector = Icons.Rounded.Edit,
      contentDescription = stringResource(id = StringsR.string.popup_edit),
      tint = Color.White,
    )
    Icon(
      modifier = Modifier
        .padding(end = 16.dp)
        .align(Alignment.CenterEnd),
      imageVector = Icons.Outlined.Delete,
      contentDescription = stringResource(id = StringsR.string.delete),
      tint = Color.White,
    )
  }
}

@Composable
@Preview
private fun BookmarkItemPreview() {
  BookmarkItem(
    bookmark = BookmarkItemViewState(
      title = "Bookmark 1",
      subtitle = "10:10:10 / 12:12:12",
      id = Bookmark.Id(UUID.randomUUID()),
      showSleepIcon = true,
      chapterNumber = "2/5",
      date = "Just now",
      useGestures = true,
      useHapticFeedback = true,
    ),
    onDelete = {},
    onEdit = { },
    onClick = {},
  )
}
