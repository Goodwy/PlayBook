package voice.bookOverview.overview

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableMap
import voice.common.BookId
import voice.common.compose.ImmutableFile

sealed interface BookOverviewViewState {

  val playButtonState: PlayButtonState?
  val showAddBookHint: Boolean
  val showMigrateHint: Boolean
  val showMigrateIcon: Boolean
  val showSearchIcon: Boolean
  val paddings: String
  val title: String
  val chapterName: String
  val cover: ImmutableFile?
  val currentBook: BookId?
  val miniPlayerStyle: Int

  object Loading : BookOverviewViewState {
    override val playButtonState: PlayButtonState? = null
    override val showAddBookHint: Boolean = false
    override val showMigrateHint: Boolean = false
    override val showMigrateIcon: Boolean = false
    override val showSearchIcon: Boolean = false
    override val paddings: String = "0;0;0;0"
    override val title: String = ""
    override val chapterName: String = ""
    override val cover: ImmutableFile? = null
    override val currentBook: BookId? = null
    override val miniPlayerStyle: Int = 0
  }

  @Immutable
  data class Content(
    val books: ImmutableMap<BookOverviewCategory, List<BookOverviewItemViewState>>,
    val layoutMode: BookOverviewLayoutMode,
    override val playButtonState: PlayButtonState?,
    override val showAddBookHint: Boolean,
    override val showMigrateHint: Boolean,
    override val showMigrateIcon: Boolean,
    override val showSearchIcon: Boolean,
    override val paddings: String = "0;0;0;0",
    override val title: String = "",
    override val chapterName: String = "",
    override val cover: ImmutableFile? = null,
    override val currentBook: BookId? = null,
    override val miniPlayerStyle: Int = 0
  ) : BookOverviewViewState

  enum class PlayButtonState {
    Playing, Paused
  }
}
