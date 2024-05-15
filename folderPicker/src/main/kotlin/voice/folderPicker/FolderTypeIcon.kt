package voice.folderPicker

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import voice.common.R
import voice.data.folders.FolderType
import voice.strings.R as StringsR

@Composable
internal fun FolderTypeIcon(folderType: FolderType) {
  Icon(
    modifier = Modifier.size(28.dp),
    imageVector = folderType.icon(),
    contentDescription = folderType.contentDescription(),
  )
}

@Composable
private fun FolderType.icon(): ImageVector = when (this) {
  FolderType.SingleFile -> ImageVector.vectorResource(R.drawable.ic_audio_file)
  FolderType.SingleFolder -> ImageVector.vectorResource(R.drawable.ic_book)
  FolderType.Root -> ImageVector.vectorResource(R.drawable.ic_folder_tree_book)
  FolderType.Author -> ImageVector.vectorResource(R.drawable.ic_folder_tree_autor)
}

@Composable
private fun FolderType.contentDescription(): String {
  val res = when (this) {
    FolderType.SingleFile,
    FolderType.SingleFolder,
    -> StringsR.string.folder_mode_single
    FolderType.Root -> StringsR.string.folder_mode_root
    FolderType.Author -> StringsR.string.folder_mode_author
  }
  return stringResource(res)
}
