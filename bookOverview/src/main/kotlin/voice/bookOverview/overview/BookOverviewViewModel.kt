package voice.bookOverview.overview

import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.datastore.core.DataStore
import de.paulwoitaschek.flowpref.Pref
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import voice.app.scanner.DeviceHasStoragePermissionBug
import voice.app.scanner.MediaScanTrigger
import voice.bookOverview.BookMigrationExplanationQualifier
import voice.bookOverview.BookMigrationExplanationShown
import voice.bookOverview.di.BookOverviewScope
import voice.bookOverview.search.BookSearchViewState
import voice.common.BookId
import voice.common.comparator.sortedNaturally
import voice.common.compose.ImmutableFile
import voice.common.constants.MINI_PLAYER_PLAYER
import voice.common.constants.SORTING_AUTHOR
import voice.common.constants.SORTING_CLASSIC
import voice.common.constants.SORTING_LAST
import voice.common.constants.SORTING_NAME
import voice.common.grid.GridCount
import voice.common.grid.GridMode
import voice.common.navigation.Destination
import voice.common.navigation.Navigator
import voice.common.pref.CurrentBook
import voice.common.pref.PrefKeys
import voice.data.repo.BookContentRepo
import voice.data.markForPosition
import voice.data.repo.BookRepository
import voice.data.repo.internals.dao.LegacyBookDao
import voice.data.repo.internals.dao.RecentBookSearchDao
import voice.playback.PlayerController
import voice.playback.playstate.PlayStateManager
import voice.search.BookSearch
import javax.inject.Inject
import javax.inject.Named

@BookOverviewScope
class BookOverviewViewModel
@Inject
constructor(
  private val repo: BookRepository,
  private val mediaScanner: MediaScanTrigger,
  private val playStateManager: PlayStateManager,
  private val playerController: PlayerController,
  @CurrentBook
  private val currentBookDataStore: DataStore<BookId?>,
  @Named(PrefKeys.GRID_MODE)
  private val gridModePref: Pref<GridMode>,
  @Named(PrefKeys.PADDING)
  private val paddingPref: Pref<String>,
  @Named(PrefKeys.MINI_PLAYER_STYLE)
  private val miniPlayerStylePref: Pref<Int>,
  @Named(PrefKeys.SORTING)
  private val sortingPref: Pref<Int>,
  @Named(PrefKeys.USE_GESTURES)
  private val useGestures: Pref<Boolean>,
  @Named(PrefKeys.USE_HAPTIC_FEEDBACK)
  private val useHapticFeedback: Pref<Boolean>,
  @Named(PrefKeys.SCAN_COVER_CHAPTER)
  private val scanCoverChapter: Pref<Boolean>,
  @Named(PrefKeys.USE_MENU_ICONS)
  private val useMenuIconsPref: Pref<Boolean>,
  private val gridCount: GridCount,
  @BookMigrationExplanationQualifier
  private val bookMigrationExplanationShown: BookMigrationExplanationShown,
  private val legacyBookDao: LegacyBookDao,
  private val navigator: Navigator,
  private val recentBookSearchDao: RecentBookSearchDao,
  private val search: BookSearch,
  private val contentRepo: BookContentRepo,
  private val deviceHasStoragePermissionBug: DeviceHasStoragePermissionBug,
) {

  private val scope = MainScope()
  private var searchActive by mutableStateOf(false)
  private var query by mutableStateOf("")

  fun attach() {
    mediaScanner.scan()
  }

  @Composable
  internal fun state(): BookOverviewViewState {
    val playState = remember { playStateManager.flow }
      .collectAsState(initial = PlayStateManager.PlayState.Paused).value
    val hasStoragePermissionBug = remember { deviceHasStoragePermissionBug.hasBug }
      .collectAsState().value
    val books = remember { repo.flow() }
      .collectAsState(initial = emptyList()).value
    val currentBookId = remember { currentBookDataStore.data }
      .collectAsState(initial = null).value
    val scannerActive = remember { mediaScanner.scannerActive }
      .collectAsState(initial = false).value
    val gridMode = remember { gridModePref.flow }
      .collectAsState(initial = null).value
      ?: return BookOverviewViewState.Loading
    val bookMigrationExplanationShown = remember { bookMigrationExplanationShown.data }
      .collectAsState(initial = null).value
      ?: return BookOverviewViewState.Loading

    val currentBook = books.firstOrNull { it.id.value == currentBookId?.value }
    val useChapterCover = currentBook?.content?.useChapterCover ?: false
    val chapterName = currentBook?.currentChapter?.markForPosition(currentBook.content.positionInChapter)?.name.takeIf { true }
    val paddings = paddingPref.flow.collectAsState(initial = null).value ?: return BookOverviewViewState.Loading
    val miniPlayerStyle = miniPlayerStylePref.flow.collectAsState(initial = MINI_PLAYER_PLAYER).value
    val sortingPref = sortingPref.flow.collectAsState(initial = SORTING_CLASSIC).value
    val useGestures = useGestures.flow.collectAsState(initial = true).value
    val useHapticFeedback = useHapticFeedback.flow.collectAsState(initial = true).value
    val scanCoverChapter = scanCoverChapter.flow.collectAsState(initial = true).value
    val useMenuIconsPref = useMenuIconsPref.flow.collectAsState(initial = false).value

    val hasLegacyBooks = produceState<Boolean?>(initialValue = null) {
      value = legacyBookDao.bookMetaDataCount() != 0
    }.value ?: return BookOverviewViewState.Loading

    val noBooks = !scannerActive && books.isEmpty()
    val showMigrateHint = hasLegacyBooks && !bookMigrationExplanationShown

    val layoutMode = when (gridMode) {
      GridMode.LIST -> BookOverviewLayoutMode.List
      GridMode.GRID -> BookOverviewLayoutMode.Grid
      GridMode.FOLLOW_DEVICE -> if (gridCount.useGridAsDefault()) {
        BookOverviewLayoutMode.Grid
      } else {
        BookOverviewLayoutMode.List
      }
    }

    val bookSearchViewState = bookSearchViewState(layoutMode)

    return BookOverviewViewState(
      layoutMode = layoutMode,
      books = books
        .groupBy {
          when (sortingPref) {
            SORTING_NAME -> it.categoryByName
            SORTING_LAST -> it.categoryByLast
            SORTING_AUTHOR -> it.categoryByAuthor
            else -> it.category
          }
        }
        .mapValues { (category, books) ->
          books
            .sortedWith(category.comparator)
            .map { book ->
              book.toItemViewState(scanCoverChapter)
            }
        }
        .toSortedMap()
        .toImmutableMap(),
      playButtonState = if (playState == PlayStateManager.PlayState.Playing) {
        BookOverviewViewState.PlayButtonState.Playing
      } else {
        BookOverviewViewState.PlayButtonState.Paused
      }.takeIf { currentBookId != null },
      showAddBookHint = if (showMigrateHint || hasStoragePermissionBug) {
        false
      } else {
        noBooks
      },
      showMigrateIcon = hasLegacyBooks,
      showMigrateHint = showMigrateHint,
      showSearchIcon = books.isNotEmpty(),
      isLoading = scannerActive,
      searchActive = searchActive,
      searchViewState = bookSearchViewState,
      showStoragePermissionBugCard = hasStoragePermissionBug,
      paddings = paddings,
      title = currentBook?.content?.name ?: "",
      chapterName = chapterName ?: currentBook?.content?.author ?: "",
      cover = if (useChapterCover && scanCoverChapter) currentBook?.currentChapter?.cover?.let(::ImmutableFile) else currentBook?.content?.cover?.let(::ImmutableFile),
      currentBook = currentBookId,
      miniPlayerStyle = miniPlayerStyle,
      useGestures = useGestures,
      useHapticFeedback = useHapticFeedback,
      useMenuIconsPref = useMenuIconsPref,
    )
  }

  @Composable
  private fun bookSearchViewState(layoutMode: BookOverviewLayoutMode): BookSearchViewState {
    return if (searchActive) {
      val recentBookSearch = remember {
        recentBookSearchDao.recentBookSearches()
      }.collectAsState(initial = emptyList()).value.reversed()
      recentBookSearchDao.recentBookSearches()
      var searchBooks by remember {
        mutableStateOf(emptyList<BookOverviewItemViewState>())
      }
      val scanCoverChapter = scanCoverChapter.flow.collectAsState(initial = true).value
      LaunchedEffect(query) {
        searchBooks = search.search(query).map { it.toItemViewState(scanCoverChapter) }
      }
      val suggestedAuthors: List<String> by produceState(initialValue = emptyList()) {
        value = contentRepo.all()
          .filter { it.isActive }
          .mapNotNull { it.author }
          .toSet()
          .sortedNaturally()
      }

      val bookSearchViewState = if (query.isNotBlank()) {
        BookSearchViewState.SearchResults(
          query = query,
          books = searchBooks,
          layoutMode = layoutMode,
        )
      } else {
        BookSearchViewState.EmptySearch(
          recentQueries = recentBookSearch,
          suggestedAuthors = suggestedAuthors,
          query = query,
        )
      }
      bookSearchViewState
    } else {
      BookSearchViewState.EmptySearch(
        recentQueries = emptyList(),
        suggestedAuthors = emptyList(),
        query = query,
      )
    }
  }

  fun onSettingsClick() {
    navigator.goTo(Destination.Settings)
  }

  fun onBookClick(id: BookId) {
    navigator.goTo(Destination.Playback(id))
  }

  fun onBookFolderClick() {
    navigator.goTo(Destination.FolderPicker)
  }

  fun onBookMigrationClick() {
    navigator.goTo(Destination.Migration)
  }

  fun onSearchActiveChange(active: Boolean) {
    if (active && !searchActive) {
      query = ""
    }
    this.searchActive = active
  }

  fun onSearchQueryChange(query: String) {
    this.query = query
  }

  fun onSearchBookClick(id: BookId) {
    val query = query.trim()
    if (query.isNotBlank()) {
      scope.launch {
        recentBookSearchDao.add(query)
      }
    }
    searchActive = false
    navigator.goTo(Destination.Playback(id))
  }

  fun onBoomMigrationHelperConfirmClick() {
    scope.launch {
      bookMigrationExplanationShown.updateData { true }
    }
  }

  fun playPause() {
    playerController.playPause()
  }

  fun next() {
    playerController.next()
  }

  fun previous() {
    playerController.previous()
  }

  fun onPermissionBugCardClicked() {
    if (Build.VERSION.SDK_INT >= 30) {
      navigator.goTo(
        Destination.Activity(
          Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            .setData("package:com.android.externalstorage".toUri()),
        ),
      )
    }
  }
}
