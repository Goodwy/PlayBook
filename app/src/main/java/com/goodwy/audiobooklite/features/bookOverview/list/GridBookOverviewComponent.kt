package com.goodwy.audiobooklite.features.bookOverview.list

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.goodwy.audiobooklite.R
import com.goodwy.audiobooklite.common.pref.PrefKeys
import com.goodwy.audiobooklite.data.Book
import com.goodwy.audiobooklite.injection.appComponent
import com.goodwy.audiobooklite.misc.RoundRectOutlineProvider
import com.goodwy.audiobooklite.misc.dpToPx
import com.goodwy.audiobooklite.misc.formatTime
import com.goodwy.audiobooklite.misc.recyclerComponent.AdapterComponent
import com.goodwy.audiobooklite.uitools.ExtensionsHolder
import de.paulwoitaschek.flowpref.Pref
import kotlinx.android.synthetic.main.book_overview_row_grid.*
import kotlinx.android.synthetic.main.book_overview_row_list.*
import kotlinx.android.synthetic.main.book_overview_row_list.cover
import kotlinx.android.synthetic.main.book_overview_row_list.more
import kotlinx.android.synthetic.main.book_overview_row_list.playedTime
import kotlinx.android.synthetic.main.book_overview_row_list.playingIndicator
import kotlinx.android.synthetic.main.book_overview_row_list.progress
import kotlinx.android.synthetic.main.book_overview_row_list.remainingTime
import kotlinx.android.synthetic.main.book_overview_row_list.roundProgress
import kotlinx.android.synthetic.main.book_overview_row_list.title
import javax.inject.Inject
import javax.inject.Named

class GridBookOverviewComponent(private val listener: BookClickListener) :
  AdapterComponent<BookOverviewModel, BookOverviewHolder>(BookOverviewModel::class) {

  override val viewType = 42

  override fun onCreateViewHolder(parent: ViewGroup): BookOverviewHolder {
    return BookOverviewHolder(
      layoutRes = R.layout.book_overview_row_grid,
      parent = parent,
      listener = listener
    )
  }

  override fun onBindViewHolder(model: BookOverviewModel, holder: BookOverviewHolder) {
    holder.bind(model)
  }

  override fun isForViewType(model: Any): Boolean {
    return model is BookOverviewModel && model.useGridView
  }
}

class ListBookOverviewComponent(private val listener: BookClickListener) :
  AdapterComponent<BookOverviewModel, BookOverviewHolder>(BookOverviewModel::class) {

  override val viewType = 43

  override fun onCreateViewHolder(parent: ViewGroup): BookOverviewHolder {
    return BookOverviewHolder(
      layoutRes = R.layout.book_overview_row_list,
      parent = parent,
      listener = listener
    )
  }

  override fun onBindViewHolder(model: BookOverviewModel, holder: BookOverviewHolder) {
    holder.bind(model)
  }

  override fun isForViewType(model: Any): Boolean {
    return model is BookOverviewModel && !model.useGridView
  }
}

class BookOverviewHolder(
  layoutRes: Int,
  parent: ViewGroup,
  private val listener: BookClickListener
) : ExtensionsHolder(parent, layoutRes) {

  @field:[Inject Named(PrefKeys.COVER_RADIUS)]
  lateinit var coverRadiusPref: Pref<Int>

  @field:[Inject Named(PrefKeys.COVER_ELEVATION)]
  lateinit var coverElevationPref: Pref<Int>

  private var boundBook: Book? = null
  private val loadBookCover = LoadBookCover(this)

  init {
    appComponent.inject(this)
    itemView.setOnClickListener {
      boundBook?.let { book ->
        listener(book, BookOverviewClick.REGULAR)
      }
    }
    itemView.setOnLongClickListener {
      boundBook?.let { book ->
        listener(book, BookOverviewClick.MENU)
        true
      } ?: false
    }
    more.setOnClickListener {
      boundBook?.let { book ->
        listener(book, BookOverviewClick.MENU)
        true
      } ?: false
    }
  }

  fun bind(model: BookOverviewModel) {
    boundBook = model.book
    val name = model.name
    title.text = name
    if (model.useGridView) {
      title.maxLines = 2
    } else {
      author.text = model.author
      author.isVisible = model.author != null
      title.maxLines = if (model.author == null) 2 else 1
    }

    cover.clipToOutline = true
    cover.outlineProvider = RoundRectOutlineProvider(itemView.context.dpToPx(coverRadiusPref.value.toFloat()/3)) // TODO RADIUS COVER
    cover.transitionName = model.transitionName
    remainingTime.text = formatTime(model.remainingTimeInMs)
    val playedTimeVal = (model.playedTimeInPer.toString() + '%')
    //val playedTimeVal = (formatTime(model.positionTimeInMs) + '/' + formatTime(model.durationTimeInMs))
    playedTime.text = playedTimeVal
    if (model.showProgressBar) {this.progress.visibility = View.VISIBLE} else {this.progress.visibility = View.GONE}
    //this.progress.visibility = View.GONE
    if (model.showDivider) {this.divider?.visibility = View.VISIBLE} else {this.divider?.visibility = View.GONE}
    this.progress.progress = model.progress
    this.roundProgress.progress = model.progress
    loadBookCover.load(model.book)
    cover.elevation = coverElevationPref.value.toFloat()/4
    if (model.useGridView) {
      percentLayout.elevation = coverElevationPref.value.toFloat()/4
      more.elevation = coverElevationPref.value.toFloat()/4
    }

    playingIndicator.isVisible = model.isCurrentBook
  }
}
