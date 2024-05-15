package voice.folderPicker.folderPicker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import de.paulwoitaschek.flowpref.Pref
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import voice.common.navigation.Destination
import voice.common.navigation.Navigator
import voice.common.pref.PrefKeys
import voice.data.folders.AudiobookFolders
import voice.documentfile.nameWithoutExtension
import javax.inject.Inject
import javax.inject.Named

class FolderPickerViewModel
@Inject constructor(
  private val audiobookFolders: AudiobookFolders,
  private val navigator: Navigator,
  @Named(PrefKeys.PADDING)
  private val paddingPref: Pref<String>,
) {

  @Composable
  fun viewState(): FolderPickerViewState {
    val folders: List<FolderPickerViewState.Item> by remember {
      items()
    }.collectAsState(initial = emptyList())
    return FolderPickerViewState(
      items = folders,
      paddings = paddingPref.value,
    )
  }

  private fun items(): Flow<List<FolderPickerViewState.Item>> {
    return audiobookFolders.all().map { folders ->
      withContext(Dispatchers.IO) {
        folders.flatMap { (folderType, folders) ->
          folders.map { (documentFile, uri) ->
            FolderPickerViewState.Item(
              name = documentFile.nameWithoutExtension(),
              id = uri,
              folderType = folderType,
            )
          }
        }.sortedDescending()
      }
    }
  }

  internal fun add() {
    navigator.goTo(
      Destination.AddContent(
        Destination.AddContent.Mode.Default,
      ),
    )
  }

  fun removeFolder(item: FolderPickerViewState.Item) {
    audiobookFolders.remove(item.id, item.folderType)
  }
}
