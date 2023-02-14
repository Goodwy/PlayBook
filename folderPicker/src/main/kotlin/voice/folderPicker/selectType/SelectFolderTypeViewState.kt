package voice.folderPicker.selectType

internal data class SelectFolderTypeViewState(
  val books: List<Book>,
  val selectedFolderMode: FolderMode,
  val loading: Boolean,
  val noBooksDetected: Boolean,
  val addButtonVisible: Boolean,
  val paddings: String,
) {
  data class Book(
    val name: String,
    val fileCount: Int,
  )
}
