package voice.bookOverview.editAuthor

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.squareup.anvil.annotations.ContributesMultibinding
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import voice.bookOverview.bottomSheet.BottomSheetItem
import voice.bookOverview.bottomSheet.BottomSheetItemViewModel
import voice.bookOverview.di.BookOverviewScope
import voice.common.BookId
import voice.data.repo.BookRepository
import javax.inject.Inject

@BookOverviewScope
@ContributesMultibinding(
  scope = BookOverviewScope::class,
  boundType = BottomSheetItemViewModel::class,
)
class EditBookAuthorViewModel
@Inject
constructor(
  private val repo: BookRepository,
) : BottomSheetItemViewModel {

  private val scope = MainScope()

  private val _state = mutableStateOf<EditBookAuthorState?>(null)
  internal val state: State<EditBookAuthorState?> get() = _state

  override suspend fun items(bookId: BookId): List<BottomSheetItem> {
    return listOf(BottomSheetItem.Author)
  }

  override suspend fun onItemClicked(bookId: BookId, item: BottomSheetItem) {
    if (item != BottomSheetItem.Author) return
    val book = repo.get(bookId) ?: return
    _state.value = EditBookAuthorState(
      author = book.content.author ?: "",
      bookId = bookId,
    )
  }

  internal fun onDismissEditAuthor() {
    _state.value = null
  }

  internal fun onUpdateEditAuthor(author: String) {
    _state.value = _state.value?.copy(author = author)
  }

  internal fun onConfirmEditAuthor() {
    val state = _state.value
    if (state != null) {
      check(state.confirmButtonEnabled)
      scope.launch {
        repo.updateBook(state.bookId) {
          it.copy(author = state.author.trim())
        }
      }
    }
    _state.value = null
  }
}
