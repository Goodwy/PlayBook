package voice.folderPicker.folderPicker

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.squareup.anvil.annotations.ContributesTo
import voice.common.AppScope
import voice.common.compose.rememberScoped
import voice.common.rootComponentAs
import voice.data.folders.FolderType
import voice.folderPicker.FolderTypeIcon
import voice.folderPicker.R

@ContributesTo(AppScope::class)
interface FolderPickerComponent {
  val folderPickerViewModel: FolderPickerViewModel
}

@Composable
fun FolderPicker(
  onCloseClick: () -> Unit,
) {
  val viewModel: FolderPickerViewModel = rememberScoped {
    rootComponentAs<FolderPickerComponent>()
      .folderPickerViewModel
  }
  val viewState = viewModel.viewState()

  var showSelectFileDialog by remember {
    mutableStateOf(false)
  }
  val openDocumentLauncher = rememberLauncherForActivityResult(
    ActivityResultContracts.OpenDocument(),
  ) { uri ->
    if (uri != null) {
      viewModel.add(uri, FileTypeSelection.File)
    }
  }
  val documentTreeLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri ->
    if (uri != null) {
      viewModel.add(uri, FileTypeSelection.Folder)
    }
  }

  if (showSelectFileDialog) {
    FileTypeSelectionDialog(
      onDismiss = {
        showSelectFileDialog = false
      },
      onSelected = { selection ->
        when (selection) {
          FileTypeSelection.File -> {
            openDocumentLauncher.launch(arrayOf("*/*"))
          }
          FileTypeSelection.Folder -> {
            documentTreeLauncher.launch(null)
          }
        }
      },
    )
  }

  FolderPickerView(
    viewState = viewState,
    onAddClick = {
      showSelectFileDialog = true
    },
    onDeleteClick = {
      viewModel.removeFolder(it)
    },
    onCloseClick = onCloseClick,
  )
}

@Composable
private fun FolderPickerView(
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
          Text(text = stringResource(R.string.audiobook_folders_title))
        },
        navigationIcon = {
          IconButton(onClick = onCloseClick) {
            Icon(
              imageVector = Icons.Rounded.ArrowBackIosNew,
              contentDescription = stringResource(R.string.close),
            )
          }
        },
        windowInsets = WindowInsets(top = top.dp),
      )
    },
    floatingActionButton = {
      val text = stringResource(id = R.string.add)
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
          modifier = Modifier.padding(start = 8.dp),
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
                  imageVector = Icons.Outlined.Delete,
                  contentDescription = stringResource(R.string.delete),
                )
              },
            )
          },
          headlineText = {
            Column() {
              Text(text = item.name)
              val urlTrim = item.id.toString().trimMargin("content://com.android.externalstorage.documents/").trimMargin("tree/").trimMargin("document/")
              val urlText = urlTrim.replace("%3A", ": ").replace("%2F", "/").replace("primary", stringResource(R.string.internal_storage))
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

@Composable
@Preview
fun FolderPickerPreview() {
  FolderPickerView(
    viewState = FolderPickerViewState(
      items = listOf(
        FolderPickerViewState.Item(
          name = "My Audiobooks",
          id = Uri.parse("content://com.android.externalstorage.documents/document/primary:My Audiobooks"),
          folderType = FolderType.Root,
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
