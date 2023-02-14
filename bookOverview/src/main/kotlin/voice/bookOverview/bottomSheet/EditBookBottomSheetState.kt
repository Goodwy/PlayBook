package voice.bookOverview.bottomSheet

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.HourglassEmpty
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.material.icons.rounded.Title
import androidx.compose.ui.graphics.vector.ImageVector
import voice.bookOverview.R

internal data class EditBookBottomSheetState(
  val items: List<BottomSheetItem>,
)

enum class BottomSheetItem(
  @StringRes val titleRes: Int,
  val icon: ImageVector,
  val divider: Boolean = false,
) {
  Title(R.string.change_book_name, Icons.Rounded.Title),
  InternetCover(R.string.download_book_cover, Icons.Rounded.Download),
  FileCover(R.string.pick_book_cover, Icons.Rounded.Image),
  DeleteBook(R.string.delete_book_bottom_sheet_title, Icons.Filled.DeleteOutline, true),
  BookCategoryMarkAsNotStarted(R.string.mark_as_not_started, Icons.Rounded.HourglassEmpty),
  BookCategoryMarkAsCurrent(R.string.mark_as_current, Icons.Rounded.PlayCircle),
  BookCategoryMarkAsCompleted(R.string.mark_as_completed, Icons.Rounded.CheckCircle),
}
