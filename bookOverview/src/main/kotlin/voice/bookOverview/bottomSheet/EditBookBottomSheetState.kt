package voice.bookOverview.bottomSheet

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.HourglassEmpty
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.material.icons.rounded.Title
import androidx.compose.ui.graphics.vector.ImageVector
import voice.strings.R as StringsR
import voice.common.R as CommonR

internal data class EditBookBottomSheetState(val items: List<BottomSheetItem>)

enum class BottomSheetItem(
  @StringRes val titleRes: Int,
  val icon: ImageVector,
  val red: Boolean = false,
  val showBookMark: Boolean = false,
) {
  Title(titleRes = StringsR.string.change_book_name, icon = Icons.Rounded.Title),
  Author(titleRes = CommonR.string.change_book_author, icon = Icons.Rounded.Person),
  InternetCover(titleRes = StringsR.string.download_book_cover, icon = Icons.Rounded.Download),
  FileCover(titleRes = StringsR.string.pick_book_cover, icon = Icons.Rounded.Image),
  DeleteBook(titleRes = StringsR.string.delete_book_bottom_sheet_title, icon = Icons.Filled.DeleteOutline, red = true),
  BookCategoryMarkAsNotStarted(titleRes = StringsR.string.mark_as_not_started, icon = Icons.Rounded.HourglassEmpty, showBookMark = true),
  BookCategoryMarkAsCurrent(titleRes = StringsR.string.mark_as_current, icon = Icons.Rounded.PlayCircle, showBookMark = true),
  BookCategoryMarkAsCompleted(titleRes = StringsR.string.mark_as_completed, icon = Icons.Rounded.CheckCircle, showBookMark = true),
}
