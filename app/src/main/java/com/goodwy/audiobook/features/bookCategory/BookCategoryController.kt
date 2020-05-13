package com.goodwy.audiobook.features.bookCategory

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.goodwy.audiobook.R
import com.goodwy.audiobook.data.Book
import com.goodwy.audiobook.data.BookComparator
import com.goodwy.audiobook.databinding.BookCategoryBinding
import com.goodwy.audiobook.features.GalleryPicker
import com.goodwy.audiobook.features.ViewBindingController
import com.goodwy.audiobook.features.bookOverview.EditBookBottomSheetController
import com.goodwy.audiobook.features.bookOverview.EditCoverDialogController
import com.goodwy.audiobook.features.bookOverview.list.BookOverviewClick
import com.goodwy.audiobook.features.bookOverview.list.header.BookOverviewCategory
import com.goodwy.audiobook.features.bookPlaying.BookPlayController
import com.goodwy.audiobook.features.imagepicker.CoverFromInternetController
import com.goodwy.audiobook.injection.appComponent
import com.goodwy.audiobook.misc.conductor.asTransaction
import com.goodwy.audiobook.misc.conductor.clearAfterDestroyView
import com.goodwy.audiobook.misc.conductor.popOrBack
import com.goodwy.audiobook.uitools.BookChangeHandler
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

private const val NI_CATEGORY = "ni#category"

class BookCategoryController(bundle: Bundle) : ViewBindingController<BookCategoryBinding>(bundle, BookCategoryBinding::inflate),
  EditBookBottomSheetController.Callback,
  CoverFromInternetController.Callback, EditCoverDialogController.Callback {

  @Inject
  lateinit var viewModel: BookCategoryViewModel
  @Inject
  lateinit var galleryPicker: GalleryPicker

  constructor(category: BookOverviewCategory) : this(Bundle().apply {
    putSerializable(NI_CATEGORY, category)
  })

  private val category = bundle.getSerializable(NI_CATEGORY) as BookOverviewCategory
  private var adapter by clearAfterDestroyView<BookCategoryAdapter>()

  init {
    appComponent.inject(this)
    viewModel.category = category
  }

  override fun BookCategoryBinding.onBindingCreated() {
    toolbar.setTitle(category.nameRes)
    toolbar.inflateMenu(R.menu.book_category)
    toolbar.setOnMenuItemClickListener {
      when (it.itemId) {
        R.id.sort -> {
          showSortingPopup()
          true
        }
        else -> false
      }
    }
    toolbar.setNavigationOnClickListener { popOrBack() }

    val adapter = BookCategoryAdapter { book, clickType ->
      when (clickType) {
        BookOverviewClick.REGULAR -> {
          val changeHandler = BookChangeHandler().apply {
            transitionName = book.coverTransitionName
          }
          router.replaceTopController(BookPlayController(book.id).asTransaction(changeHandler, changeHandler))
        }
        BookOverviewClick.MENU -> {
          EditBookBottomSheetController(this@BookCategoryController, book).showDialog(router)
        }
      }
    }.also { adapter = it }
    recyclerView.adapter = adapter
    val layoutManager = GridLayoutManager(activity, 1)
    recyclerView.layoutManager = layoutManager
    recyclerView.addItemDecoration(BookCategoryItemDecoration(activity!!, layoutManager))
    (recyclerView.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false

    lifecycleScope.launch {
      viewModel.get()
        .collect {
          layoutManager.spanCount = it.gridColumnCount
          adapter.submitList(it.models)
        }
    }
  }

  private fun BookCategoryBinding.showSortingPopup() {
    val anchor = toolbar.findViewById<View>(R.id.sort)
    PopupMenu(activity!!, anchor).apply {
      inflate(R.menu.sort_menu)
      val bookSorting = viewModel.bookSorting()
      menu.findItem(bookSorting.menuId).isChecked = true
      setOnMenuItemClickListener { menuItem ->
        val itemId = menuItem.itemId
        val comparator = BookComparator.values().find { it.menuId == itemId }
        if (comparator != null) {
          viewModel.sort(comparator)
          true
        } else {
          false
        }
      }
      show()
    }
  }

  override fun onInternetCoverRequested(book: Book) {
    router.pushController(CoverFromInternetController(book.id, this).asTransaction())
  }

  override fun onBookCoverChanged(bookId: UUID) {
    adapter.notifyCoverChanged(bookId)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    val arguments = galleryPicker.parse(requestCode, resultCode, data)
    if (arguments != null) {
      EditCoverDialogController(this, arguments).showDialog(router)
    }
  }

  override fun onFileCoverRequested(book: Book) {
    galleryPicker.pick(book.id, this)
  }
}

private val BookComparator.menuId: Int
  get() = when (this) {
    BookComparator.BY_LAST_PLAYED -> R.id.byLastPlayed
    BookComparator.BY_NAME -> R.id.byName
    BookComparator.BY_DATE_ADDED -> R.id.byAdded
  }
