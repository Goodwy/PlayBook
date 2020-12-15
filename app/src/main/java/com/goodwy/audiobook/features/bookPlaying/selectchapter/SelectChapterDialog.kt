package com.goodwy.audiobook.features.bookPlaying.selectchapter

import android.app.Dialog
import android.os.Bundle
import android.view.View
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.list.customListAdapter
import com.afollestad.materialdialogs.list.getRecyclerView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.goodwy.audiobook.R
import com.goodwy.audiobook.data.ChapterMark
import com.goodwy.audiobook.databinding.SelectChapterRowBinding
import com.goodwy.audiobook.injection.appComponent
import com.goodwy.audiobook.misc.DialogController
import com.goodwy.audiobook.misc.getUUID
import com.goodwy.audiobook.misc.groupie.BindingItem
import com.goodwy.audiobook.misc.putUUID
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

private const val NI_BOOK_ID = "ni#bookId"

class SelectChapterDialog(bundle: Bundle) : DialogController(bundle) {

  @Inject
  lateinit var viewModel: SelectChapterViewModel

  constructor(bookId: UUID) : this(Bundle().apply {
    putUUID(NI_BOOK_ID, bookId)
  })

  init {
    appComponent.inject(this)
    viewModel.bookId = args.getUUID(NI_BOOK_ID)
  }

  override fun onCreateDialog(savedViewState: Bundle?): Dialog {
    val viewState = viewModel.viewState()
    val items = viewState.chapters.mapIndexed { index, mark ->
      val listener = View.OnClickListener {
        viewModel.chapterClicked(index)
      }
      BindingItem<SelectChapterRowBinding, ChapterMark>(
        mark,
        R.layout.select_chapter_row,
        SelectChapterRowBinding::bind
      ) { data, position ->
        root.setOnClickListener(listener)
        @Suppress("SetTextI18n")
        textView.text = "${position + 1} - ${data.name}"
        textView.setCompoundDrawablesWithIntrinsicBounds(
          0,
          0,
          if (position == viewState.selectedIndex) R.drawable.avd_play_to_pause else 0,
          0
        )
      }
    }

    val adapter = GroupAdapter<GroupieViewHolder>()
    adapter.addAll(items)
    return MaterialDialog(activity!!, BottomSheet(LayoutMode.WRAP_CONTENT)).apply {
      cornerRadius(4f)
      customListAdapter(adapter)
      icon(R.drawable.ic_contents)
      title(R.string.contents)
      if (viewState.selectedIndex != null) {
        getRecyclerView().layoutManager!!.scrollToPosition(viewState.selectedIndex)
      }
      positiveButton (R.string.dialog_ок)
    }
  }

  override fun onAttach(view: View) {
    super.onAttach(view)
    lifecycleScope.launch {
      viewModel.viewEffects.collect {
        when (it) {
          SelectChapterViewEffect.CloseScreen -> {
            dismissDialog()
          }
        }
      }
    }
  }
}
