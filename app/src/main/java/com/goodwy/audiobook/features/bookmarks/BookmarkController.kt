package com.goodwy.audiobook.features.bookmarks

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.goodwy.audiobook.R
import com.goodwy.audiobook.common.pref.PrefKeys
import com.goodwy.audiobook.data.Bookmark
import com.goodwy.audiobook.data.Chapter
import com.goodwy.audiobook.databinding.BookmarkBinding
import com.goodwy.audiobook.features.bookmarks.dialogs.AddBookmarkDialog
import com.goodwy.audiobook.features.bookmarks.dialogs.DeleteBookmarkDialog
import com.goodwy.audiobook.features.bookmarks.dialogs.EditBookmarkDialog
import com.goodwy.audiobook.features.bookmarks.list.BookMarkHolder
import com.goodwy.audiobook.features.bookmarks.list.BookmarkAdapter
import com.goodwy.audiobook.features.bookmarks.list.BookmarkClickListener
import com.goodwy.audiobook.injection.appComponent
import com.goodwy.audiobook.misc.conductor.context
import com.goodwy.audiobook.misc.getUUID
import com.goodwy.audiobook.misc.putUUID
import com.goodwy.audiobook.mvp.MvpController
import com.goodwy.audiobook.uitools.VerticalDividerItemDecoration
import de.paulwoitaschek.flowpref.Pref
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject
import javax.inject.Named

/**
 * Dialog for creating a bookmark
 */
private const val NI_BOOK_ID = "ni#bookId"

class BookmarkController(args: Bundle) :
  MvpController<BookmarkView, BookmarkPresenter, BookmarkBinding>(BookmarkBinding::inflate, args), BookmarkView,
    BookmarkClickListener, AddBookmarkDialog.Callback, DeleteBookmarkDialog.Callback,
    EditBookmarkDialog.Callback {

  constructor(bookId: UUID) : this(Bundle().apply {
    putUUID(NI_BOOK_ID, bookId)
  })

  @field:[Inject Named(PrefKeys.PADDING)]
  lateinit var paddingPref: Pref<String>

  private val bookId = args.getUUID(NI_BOOK_ID)
  private val adapter = BookmarkAdapter(this)

  init {
    appComponent.inject(this)
  }

  override fun createPresenter() = appComponent.bookmarkPresenter.apply {
    bookId = this@BookmarkController.bookId
  }

  override fun render(bookmarks: List<Bookmark>, chapters: List<Chapter>) {
    adapter.newData(bookmarks, chapters)
  }

  override fun showBookmarkAdded(bookmark: Bookmark) {
    val index = adapter.indexOf(bookmark)
    binding.recycler.smoothScrollToPosition(index)
    Snackbar.make(view!!, R.string.bookmark_added, Snackbar.LENGTH_SHORT)
        .show()
  }

  override fun onDeleteBookmarkConfirmed(id: UUID) {
    presenter.deleteBookmark(id)
  }

  override fun onBookmarkClicked(bookmark: Bookmark) {
    presenter.selectBookmark(bookmark.id)
    router.popController(this)
  }

  override fun onBookmarkLongClicked(bookmark: Bookmark) {
    showEditBookmarkDialog(bookmark)
  }

  override fun onEditBookmark(id: UUID, title: String) {
    presenter.editBookmark(id, title)
  }

  override fun onBookmarkNameChosen(name: String) {
    presenter.addBookmark(name)
  }

  override fun finish() {
    router.popController(this)
  }

  override fun BookmarkBinding.onBindingCreated() {
    setupToolbar()
    setupList()

    addBookmarkFab.setOnClickListener {
      showAddBookmarkDialog()
    }
  }

  override fun BookmarkBinding.onAttach() {
    //padding for Edge-to-edge
    lifecycleScope.launch {
      paddingPref.flow.collect {
        val top = paddingPref.value.substringBefore(';').toInt()
        val bottom = paddingPref.value.substringAfter(';').substringBefore(';').toInt()
        val left = paddingPref.value.substringBeforeLast(';').substringAfterLast(';').toInt()
        val right = paddingPref.value.substringAfterLast(';').toInt()
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
        target: RecyclerView.ViewHolder
      ): Boolean {
        return false
      }

      override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val boundBookmark = (viewHolder as BookMarkHolder).boundBookmark
        boundBookmark?.let { presenter.deleteBookmark(it.id) }
      }
    }
    ItemTouchHelper(swipeCallback).attachToRecyclerView(recycler)
  }

  override fun onOptionsMenuClicked(bookmark: Bookmark, v: View) {
    val popup = PopupMenu(context, v)
    popup.menuInflater.inflate(R.menu.bookmark_popup, popup.menu)
    popup.setOnMenuItemClickListener {
      when (it.itemId) {
        R.id.edit -> {
          showEditBookmarkDialog(bookmark)
          true
        }
        R.id.delete -> {
          showDeleteBookmarkDialog(bookmark)
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

  private fun showDeleteBookmarkDialog(bookmark: Bookmark) {
    DeleteBookmarkDialog(this, bookmark).showDialog(router)
  }
}
