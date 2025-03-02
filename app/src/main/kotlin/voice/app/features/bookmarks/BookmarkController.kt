package voice.app.features.bookmarks

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.google.android.material.snackbar.Snackbar
import de.paulwoitaschek.flowpref.Pref
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import voice.app.R
import voice.app.databinding.BookmarkBinding
import voice.app.features.bookmarks.dialogs.AddBookmarkDialog
import voice.app.features.bookmarks.dialogs.EditBookmarkDialog
import voice.app.features.bookmarks.list.BookMarkHolder
import voice.app.features.bookmarks.list.BookmarkAdapter
import voice.app.features.bookmarks.list.BookmarkClickListener
import voice.app.injection.appComponent
import voice.app.misc.conductor.context
import voice.app.mvp.MvpController
import voice.app.uitools.VerticalDividerItemDecoration
import voice.common.BookId
import voice.common.constants.SORTING_BOOKMARK_NAME
import voice.common.constants.SORTING_BOOKMARK_TIME
import voice.common.copyToClipboard
import voice.common.dpToPx
import voice.common.pref.PrefKeys
import voice.data.Bookmark
import voice.data.Chapter
import voice.data.getBookId
import voice.data.putBookId
import javax.inject.Inject
import javax.inject.Named
import voice.strings.R as StringsR
import voice.common.R as CommonR

/**
 * Dialog for creating a bookmark
 */
private const val NI_BOOK_ID = "ni#bookId"

class BookmarkController(args: Bundle) :
  MvpController<BookmarkView, BookmarkPresenter, BookmarkBinding>(BookmarkBinding::inflate, args),
  BookmarkView,
  BookmarkClickListener,
  AddBookmarkDialog.Callback,
  EditBookmarkDialog.Callback {

  constructor(bookId: BookId) : this(
    Bundle().apply {
      putBookId(NI_BOOK_ID, bookId)
    },
  )

  @field:[Inject Named(PrefKeys.PADDING)]
  lateinit var paddingPref: Pref<String>

  @field:[Inject Named(PrefKeys.COLOR_THEME)]
  lateinit var colorThemePreference: Pref<Int>

  @field:[Inject Named(PrefKeys.SORTING_BOOKMARK)]
  lateinit var sortingBookmarkPref: Pref<Int>

  private val bookId = args.getBookId(NI_BOOK_ID)!!
  private val adapter = BookmarkAdapter(this)

  init {
    appComponent.inject(this)
  }

  private var onCreateViewScope: CoroutineScope? = null

  override fun createPresenter() = appComponent.bookmarkPresenter.apply {
    bookId = this@BookmarkController.bookId
    sortingBy = sortingBookmarkPref.value
  }

  override fun render(
    bookmarks: List<Bookmark>,
    chapters: List<Chapter>,
  ) {
    adapter.newData(bookmarks, chapters)
  }

  override fun showBookmarkAdded(bookmark: Bookmark) {
    val index = adapter.indexOf(bookmark)
    binding.recycler.smoothScrollToPosition(index)
    Snackbar.make(view!!, StringsR.string.bookmark_added, Snackbar.LENGTH_SHORT)
      .show()
  }

  override fun onBookmarkClicked(bookmark: Bookmark) {
    presenter.selectBookmark(bookmark.id)
    router.popController(this)
  }

  override fun onBookmarkLongClicked(bookmark: Bookmark) {
    if (bookmark.title != null) context.copyToClipboard(bookmark.title!!)
  }

  override fun onEditBookmark(
    id: Bookmark.Id,
    title: String,
  ) {
    presenter.editBookmark(id, title, sortingBookmarkPref.value)
  }

  override fun onBookmarkNameChosen(name: String) {
    presenter.addBookmark(name)
  }

  override fun finish() {
    router.popController(this)
  }

  override fun BookmarkBinding.onBindingCreated() {
    onCreateViewScope = MainScope()
    setupToolbar()
    setupList()

    addBookmarkFab.backgroundTintList = ColorStateList.valueOf(colorThemePreference.value)
    addBookmarkFab.setOnClickListener {
      showAddBookmarkDialog()
    }
  }

  override fun BookmarkBinding.onAttach() {
    //padding for Edge-to-edge
    onCreateViewScope?.launch {
      paddingPref.flow.collect {
        val top = context.dpToPx(paddingPref.value.substringBefore(';').toFloat()).toInt()
        val bottom = context.dpToPx(paddingPref.value.substringAfter(';').substringBefore(';').toFloat()).toInt()
        val left = context.dpToPx(paddingPref.value.substringBeforeLast(';').substringAfterLast(';').toFloat()).toInt()
        val right = context.dpToPx(paddingPref.value.substringAfterLast(';').toFloat()).toInt()
        root.setPadding(left, top, right, bottom)
      }
    }
  }

  override fun onDestroyView() {
    binding.recycler.adapter = null
  }

  private fun BookmarkBinding.setupToolbar() {
    toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
    toolbar.setNavigationOnClickListener {
      router.popController(this@BookmarkController)
    }

    toolbar.setOnMenuItemClickListener {
      when (it.itemId) {
        R.id.sorting -> {
          sortingBookmarkDialog()
          true
        }
        else -> false
      }
    }
  }

  private fun BookmarkBinding.setupList() {
    val layoutManager = LinearLayoutManager(context)
    recycler.addItemDecoration(VerticalDividerItemDecoration(context))
    recycler.layoutManager = layoutManager
    recycler.adapter = adapter
    val itemAnimator = recycler.itemAnimator as DefaultItemAnimator
    itemAnimator.supportsChangeAnimations = false

    val swipeCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
      override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder,
      ): Boolean {
        return false
      }

      override fun onSwiped(
        viewHolder: RecyclerView.ViewHolder,
        direction: Int,
      ) {
        val boundBookmark = (viewHolder as BookMarkHolder).boundBookmark
        boundBookmark?.let { presenter.deleteBookmark(it.id) }
      }
    }
    ItemTouchHelper(swipeCallback).attachToRecyclerView(recycler)


    val swipeCallbackLeft = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
      override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder,
      ): Boolean {
        return false
      }

      override fun onSwiped(
        viewHolder: RecyclerView.ViewHolder,
        direction: Int,
      ) {
        val boundBookmark = (viewHolder as BookMarkHolder).boundBookmark
        boundBookmark?.let { presenter.deleteBookmark(it.id) }
      }
    }
    ItemTouchHelper(swipeCallbackLeft).attachToRecyclerView(recycler)
  }

  override fun onOptionsMenuClicked(
    bookmark: Bookmark,
    v: View,
  ) {
    val popup = PopupMenu(context, v)
    popup.menuInflater.inflate(R.menu.bookmark_popup, popup.menu)
    popup.setOnMenuItemClickListener {
      when (it.itemId) {
        R.id.edit -> {
          showEditBookmarkDialog(bookmark)
          true
        }
        R.id.delete -> {
          presenter.deleteBookmark(bookmark.id)
          true
        }
        else -> false
      }
    }
    popup.show()
  }

  private fun showEditBookmarkDialog(bookmark: Bookmark) {
    EditBookmarkDialog(this, bookmark).showDialog(router)
  }

  private fun showAddBookmarkDialog() {
    AddBookmarkDialog(this).showDialog(router)
  }

  private fun sortingBookmarkDialog() {
    MaterialDialog(activity!!)
      .title(CommonR.string.pref_sorting)
      .listItemsSingleChoice(R.array.pref_sorting_bookmark, initialSelection = sortingBookmarkPref.value) { _, index, _ ->
        changeSortingBookmark(index)
      }
      .show()
  }

  private fun changeSortingBookmark(sortingBy: Int = sortingBookmarkPref.value) {
    sortingBookmarkPref.value = sortingBy
    presenter.sortingBookmark(sortingBy)
  }
}
