package com.goodwy.audiobook.features.bookPlaying.selectchapter

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.bottomsheets.setPeekHeight
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.list.customListAdapter
import com.afollestad.materialdialogs.list.getRecyclerView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.goodwy.audiobook.R
import com.goodwy.audiobook.common.pref.PrefKeys
import com.goodwy.audiobook.data.Book
import com.goodwy.audiobook.data.ChapterMark
import com.goodwy.audiobook.databinding.DialogSelectChapterTitleBinding
import com.goodwy.audiobook.databinding.SelectChapterRowBinding
import com.goodwy.audiobook.features.bookOverview.list.LoadBookCover
import com.goodwy.audiobook.features.bookOverview.list.remainingTimeInMs
import com.goodwy.audiobook.injection.appComponent
import com.goodwy.audiobook.misc.DialogController
import com.goodwy.audiobook.misc.conductor.context
import com.goodwy.audiobook.misc.digits
import com.goodwy.audiobook.misc.formatTime
import com.goodwy.audiobook.misc.getUUID
import com.goodwy.audiobook.misc.groupie.BindingItem
import com.goodwy.audiobook.misc.hours
import com.goodwy.audiobook.misc.minutes
import com.goodwy.audiobook.misc.putUUID
import com.jaredrummler.cyanea.app.BaseCyaneaActivity
import com.squareup.picasso.Picasso
import de.paulwoitaschek.flowpref.Pref
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject
import javax.inject.Named

private const val NI_BOOK_ID = "ni#bookId"

class SelectChapterDialog(bundle: Bundle) : DialogController(bundle),
  BaseCyaneaActivity {

  @Inject
  lateinit var viewModel: SelectChapterViewModel

  @field:[Inject Named(PrefKeys.COVER_RADIUS)]
  lateinit var coverRadiusPref: Pref<Int>

  @field:[Inject Named(PrefKeys.COVER_ELEVATION)]
  lateinit var coverElevationPref: Pref<Int>

  private var coverLoaded = false

  constructor(bookId: UUID) : this(Bundle().apply {
    putUUID(NI_BOOK_ID, bookId)
  })

  init {
    appComponent.inject(this)
    viewModel.bookId = args.getUUID(NI_BOOK_ID)
  }

  override fun onCreateDialog(savedViewState: Bundle?): Dialog {
    coverLoaded = false
    val viewState = viewModel.viewState()

    //Title
    val binding = DialogSelectChapterTitleBinding.inflate(activity!!.layoutInflater)


    if (!coverLoaded) {
      coverLoaded = true
      val coverFile = viewState.cover.file()
      val placeholder = viewState.cover.placeholder(activity!!)
      if (coverFile == null) {
        Picasso.get().cancelRequest(binding.cover)
        binding.cover.setImageDrawable(placeholder)
      } else {
        Picasso.get()
          .load(coverFile)
          .placeholder(placeholder)
          .into(binding.cover)
      }
      binding.coverCard.radius = coverRadiusPref.value.toFloat() //16F
      binding.coverCard.cardElevation = coverElevationPref.value.toFloat()/3 //128F
      binding.coverCard.maxCardElevation = 128F
      binding.holder.setBackgroundResource(R.drawable.dialog_title_backgroung)
      //shadow color
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        binding.coverCard.outlineAmbientShadowColor = ContextCompat.getColor(context, R.color.shadow_elevation)
        binding.coverCard.outlineSpotShadowColor = ContextCompat.getColor(context, R.color.shadow_elevation)
      }
    }

    binding.author.text = viewState.bookAuthor
    binding.title.text = viewState.bookName
    binding.remainingTime.text = context.getString(R.string.left_in_audiobook, formatTimeMinutes(viewState.remainingTimeInMs!!))

    //Chapters
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
      cornerRadius(res = R.dimen.md_corner_radius)
      customView(view = binding.root, scrollable = true, noVerticalPadding = true)
      customListAdapter(adapter)
      //icon(R.drawable.ic_contents)
      //title(null, "${viewState.bookName}")//(R.string.contents)
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

  private fun formatTimeMinutes(timeMs: Long, durationMs: Long = 0): String {
    val m = timeMs.minutes()
    val largerTime = maxOf(timeMs, durationMs)
    val hourDigits = largerTime.hours().digits()
    val hour = context.getString(R.string.hours_letter)
    val min = context.getString(R.string.minutes_letter)
    return if (hourDigits > 0) {
      val h = timeMs.hours()
      "%1\$0${hourDigits}d %2\$s %3\$02d %4\$s".format(h, hour, m, min)
    } else {
      val minuteDigits = largerTime.minutes().digits()
      val pattern = if (minuteDigits == 0) {
        "%1\$d %2\$s"
      } else {
        "%1\$0${minuteDigits}d %2\$s"
      }
      pattern.format(m, min)
    }
  }
}
