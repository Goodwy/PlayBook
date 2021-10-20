package com.goodwy.audiobooklite.features.bookOverview.list

import androidx.core.view.doOnPreDraw
import com.squareup.picasso.Picasso
import com.goodwy.audiobooklite.R
import com.goodwy.audiobooklite.common.CoverReplacement
import com.goodwy.audiobooklite.common.MAX_IMAGE_SIZE
import com.goodwy.audiobooklite.common.pref.PrefKeys
import com.goodwy.audiobooklite.covercolorextractor.CoverColorExtractor
import com.goodwy.audiobooklite.data.Book
import com.goodwy.audiobooklite.features.bookOverview.GridMode
import com.goodwy.audiobooklite.features.gridCount.GridCount
import com.goodwy.audiobooklite.injection.appComponent
import com.goodwy.audiobooklite.misc.coverFile
import de.paulwoitaschek.flowpref.Pref
import kotlinx.android.synthetic.main.book_overview_row_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class LoadBookCover(holder: BookOverviewHolder) {

  @field:[Inject Named(PrefKeys.GRID_MODE)]
  lateinit var gridModePref: Pref<GridMode>

  @Inject
  lateinit var coverColorExtractor: CoverColorExtractor
  @Inject
  lateinit var gridCount: GridCount

  init {
    appComponent.inject(this)
  }

  private val context = holder.itemView.context
  private val progress = holder.progress
  private val roundProgress = holder.roundProgress
  private val cover = holder.cover
  private val defaultProgressColor = context.getColor(R.color.progressColor)
  private val defaultRoundProgressColor = context.getColor(R.color.colorIcon)
  private val white = context.getColor(R.color.white)

  private var boundFileLength: Long = Long.MIN_VALUE
  private var boundName: String? = null

  private var currentCoverBindingJob: Job? = null

  fun load(book: Book) {
    currentCoverBindingJob?.cancel()
    currentCoverBindingJob = GlobalScope.launch(Dispatchers.IO) {
      val coverFile = book.coverFile()
      val bookName = book.name

      val coverFileLength = coverFile.length()
      if (boundName == book.name && boundFileLength == coverFileLength) {
        return@launch
      }

      withContext(Dispatchers.Main) {
        progress.color = defaultProgressColor
      }
      val extractedColor = coverColorExtractor.extract(coverFile)
      val amountOfColumns = gridCount.gridColumnCount(gridModePref.value)
      val extractedColorRound = if (amountOfColumns > 1) white else defaultRoundProgressColor
      val shouldLoadImage = coverFileLength in 1 until MAX_IMAGE_SIZE
      withContext(Dispatchers.Main) {
        progress.color = extractedColor ?: defaultProgressColor
        roundProgress.colorForeground = extractedColorRound
        val coverReplacement = CoverReplacement(bookName, context)
        if (!isActive) return@withContext
        if (shouldLoadImage) {
          Picasso.get()
            .load(coverFile)
            .placeholder(coverReplacement)
            .into(cover)
        } else {
          Picasso.get().cancelRequest(cover)
          // we have to set the replacement in onPreDraw, else the transition will fail.
          cover.doOnPreDraw { cover.setImageDrawable(coverReplacement) }
        }

        boundFileLength = coverFileLength
        boundName = bookName
      }
    }
  }
}
