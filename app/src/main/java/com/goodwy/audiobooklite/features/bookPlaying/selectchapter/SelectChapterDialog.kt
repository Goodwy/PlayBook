package com.goodwy.audiobooklite.features.bookPlaying.selectchapter

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
import com.goodwy.audiobooklite.R
import com.goodwy.audiobooklite.data.ChapterMark
import com.goodwy.audiobooklite.databinding.SelectChapterRowBinding
import com.goodwy.audiobooklite.injection.appComponent
import com.goodwy.audiobooklite.misc.DialogController
import com.goodwy.audiobooklite.misc.formatTime
import com.goodwy.audiobooklite.misc.getUUID
import com.goodwy.audiobooklite.misc.groupie.BindingItem
import com.goodwy.audiobooklite.misc.putUUID
import com.jaredrummler.cyanea.app.BaseCyaneaActivity
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

private const val NI_BOOK_ID = "ni#bookId"

class SelectChapterDialog(bundle: Bundle) : DialogController(bundle),
  BaseCyaneaActivity {

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
        if (viewState.showChapterNumbers) {
          textView.text = "${position + 1} - ${data.name}"
        } else {
          textView.text = data.name
        }
        textView.setCompoundDrawablesWithIntrinsicBounds(
          0,
          0,
          if (position == viewState.selectedIndex) R.drawable.ic_headphones else 0,
          0
        )
        textTime.text = formatTime(data.endMs, data.endMs)
        current.visibility = if (position == viewState.selectedIndex) View.VISIBLE else View.INVISIBLE
        current.setColorFilter(cyanea.primary)
      }
    }

    val adapter = GroupAdapter<GroupieViewHolder>()
    adapter.addAll(items)
    return MaterialDialog(activity!!, BottomSheet(LayoutMode.WRAP_CONTENT)).apply {
      cornerRadius(4f)
      customListAdapter(adapter)
      icon(R.drawable.ic_contents)
      val duration = formatTime(viewState.bookDuration!!)
      title(null, "${viewState.bookName}")//(R.string.contents)
      if (viewState.selectedIndex != null) {
        getRecyclerView().layoutManager!!.scrollToPosition(viewState.selectedIndex)
      }
      positiveButton (R.string.dialog_cancel)
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
