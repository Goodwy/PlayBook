package voice.folderPicker.folderPicker

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.squareup.anvil.annotations.ContributesTo
import voice.common.AppScope
import voice.common.R
import voice.common.compose.rememberScoped
import voice.common.rootComponentAs
import voice.data.folders.FolderType
import voice.folderPicker.FolderTypeIcon
import voice.strings.R as StringsR
import voice.common.R as CommonR

@ContributesTo(AppScope::class)
interface FolderPickerComponent {
  val folderPickerViewModel: FolderPickerViewModel
}

@Composable
fun FolderOverview(onCloseClick: () -> Unit) {
  val viewModel: FolderPickerViewModel = rememberScoped {
    rootComponentAs<FolderPickerComponent>()
      .folderPickerViewModel
  }
  val viewState = viewModel.viewState()

  FolderOverviewView(
    viewState = viewState,
    onAddClick = {
      viewModel.add()
    },
    onDeleteClick = {
      viewModel.removeFolder(it)
    },
    onCloseClick = onCloseClick,
  )
}

@Composable
private fun FolderOverviewView(
  viewState: FolderPickerViewState,
  onAddClick: () -> Unit,
  onDeleteClick: (FolderPickerViewState.Item) -> Unit,
  onCloseClick: () -> Unit,
) {
  val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()//TopAppBarDefaults.pinnedScrollBehavior()
  val top = viewState.paddings.substringBefore(';').toInt()
  val bottom = viewState.paddings.substringAfter(';').substringBefore(';').toInt()
  val start = viewState.paddings.substringBeforeLast(';').substringAfterLast(';').toInt()
  val end = viewState.paddings.substringAfterLast(';').toInt()
  Scaffold(
    modifier = Modifier
      .nestedScroll(scrollBehavior.nestedScrollConnection)
      .padding(start = start.dp, end = end.dp),
    topBar = {
      MediumTopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
          Text(text = stringResource(StringsR.string.audiobook_folders_title))
        },
        navigationIcon = {
          IconButton(onClick = onCloseClick) {
            Icon(
              imageVector = Icons.AutoMirrored.Rounded.ArrowBackIos,
              contentDescription = stringResource(StringsR.string.close),
            )
          }
        },
        windowInsets = WindowInsets(top = top.dp),
      )
    },
    floatingActionButton = {
      val text = stringResource(id = StringsR.string.add)
      ExtendedFloatingActionButton(
        modifier = Modifier.padding(bottom = bottom.dp),
        text = {
          Text(text)
        },
        onClick = {
          onAddClick()
        },
        icon = {
          Icon(
            imageVector = Icons.Rounded.Add,
            contentDescription = text,
          )
        },
      )
    },
  ) { contentPadding ->
    LazyColumn(contentPadding = contentPadding) {
      item { Spacer(modifier = Modifier.size(16.dp)) }
      items(viewState.items) { item ->
        ListItem(
          modifier = Modifier.padding(start = 6.dp),
          leadingContent = {
            FolderTypeIcon(folderType = item.folderType)
          },
          trailingContent = {
            IconButton(
              onClick = {
                onDeleteClick(item)
              },
              content = {
                Icon(
                  imageVector = ImageVector.vectorResource(R.drawable.ic_delete),
                  contentDescription = stringResource(StringsR.string.delete),
                )
              },
            )
          },
          headlineContent = {
            Column(
              modifier = Modifier.padding(start = 4.dp)
            ) {
              Text(text = item.name)
              val urlTrim = item.id.toString().trimMargin("content://com.android.external storage.documents/").trimMargin("tree/").trimMargin("document/")
              val urlText = urlTrim.replace("%3A", ": ").replace("%2F", "/").replace("primary", stringResource(CommonR.string.internal_storage))
              Text(text = urlText,
                lineHeight = 14.sp,
                fontSize = 12.sp,
                maxLines = 2,)
            }
          },
        )
      }
      item { Spacer(modifier = Modifier.size(124.dp)) }
    }
  }
}

@Suppress("ktlint:compose:preview-public-check")
@Composable
@Preview
fun FolderOverviewPreview() {
  FolderOverviewView(
    viewState = FolderPickerViewState(
      items = listOf(
        FolderPickerViewState.Item(
          name = "My Audiobooks",
          id = Uri.parse("content://com.android.externalstorage.documents/document/primary:My Audiobooks"),
          folderType = FolderType.Root,
        ),
        FolderPickerViewState.Item(
          name = "My Audiobooks",
          id = Uri.parse("content://com.android.externalstorage.documents/document/primary:My Audiobooks"),
          folderType = FolderType.Author,
        ),
        FolderPickerViewState.Item(
          name = "Bobiverse 1-4",
          id = Uri.parse("content://com.android.externalstorage.documents/document/SDCARD:Books/Bobiverse 1-4"),
          folderType = FolderType.SingleFolder,
        ),
        FolderPickerViewState.Item(
          name = "Harry Potter 1",
          id = Uri.parse("content://com.android.externalstorage.documents/document/primary:Harry Potter 1.mp3"),
          folderType = FolderType.SingleFile,
        ),
      ),
      paddings = "0;0;0;0"
    ),
    onAddClick = { },
    onDeleteClick = {},
  ) {
  }
}
