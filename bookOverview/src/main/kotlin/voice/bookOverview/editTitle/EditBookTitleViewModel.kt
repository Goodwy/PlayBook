package voice.bookOverview.editTitle

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.squareup.anvil.annotations.ContributesMultibinding
import de.paulwoitaschek.flowpref.Pref
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import voice.bookOverview.bottomSheet.BottomSheetItem
import voice.bookOverview.bottomSheet.BottomSheetItemViewModel
import voice.bookOverview.di.BookOverviewScope
import voice.common.BookId
import voice.common.compose.ImmutableFile
import voice.common.pref.PrefKeys
import voice.data.repo.BookRepository
import javax.inject.Inject
import javax.inject.Named

@BookOverviewScope
@ContributesMultibinding(
  scope = BookOverviewScope::class,
  boundType = BottomSheetItemViewModel::class,
)
class EditBookTitleViewModel
@Inject
constructor(
  private val repo: BookRepository,
  @Named(PrefKeys.SCAN_COVER_CHAPTER)
  private val scanCoverChapter: Pref<Boolean>,
) : BottomSheetItemViewModel {

  private val scope = MainScope()

  private val _state = mutableStateOf<EditBookTitleState?>(null)
  internal val state: State<EditBookTitleState?> get() = _state

  override suspend fun items(bookId: BookId): List<BottomSheetItem> {
    return listOf(BottomSheetItem.Title)
  }

  override suspend fun onItemClicked(
    bookId: BookId,
    item: BottomSheetItem,
  ) {
    if (item != BottomSheetItem.Title) return
    val book = repo.get(bookId) ?: return
    _state.value = EditBookTitleState(
      title = book.content.name,
      bookId = bookId,
    )
  }

  internal fun onDismissEditTitle() {
    _state.value = null
  }

  internal fun onUpdateEditTitle(title: String) {
    _state.value = _state.value?.copy(title = title)
  }

  internal fun onConfirmEditTitle() {
    val state = _state.value
    if (state != null) {
      check(state.confirmButtonEnabled)
      scope.launch {
        repo.updateBook(state.bookId) {
          it.copy(name = state.title.trim())
        }
      }
    }
    _state.value = null
  }

  //PlayBook
  internal suspend fun selectBookTitle(bookId: BookId): String {
    val book = repo.get(bookId) ?: return ""
    return book.content.name
  }

  internal suspend fun selectBookCover(bookId: BookId): ImmutableFile? {
    val book = repo.get(bookId) ?: return null
    return if (book.content.useChapterCover && scanCoverChapter.value) book.currentChapter.cover?.let(::ImmutableFile) else book.content.cover?.let(::ImmutableFile)
  }

  internal suspend fun selectBookDuration(bookId: BookId): Long {
    val book = repo.get(bookId) ?: return 0
    return book.duration / 1000
  }
  internal suspend fun selectBookChaptersSize(bookId: BookId): Int {
    val book = repo.get(bookId) ?: return 1
    return book.content.chapters.size
  }
}
