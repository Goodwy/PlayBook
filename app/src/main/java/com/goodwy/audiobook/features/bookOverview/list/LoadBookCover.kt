package com.goodwy.audiobook.features.bookOverview.list

import androidx.core.view.doOnPreDraw
import com.squareup.picasso.Picasso
import com.goodwy.audiobook.R
import com.goodwy.audiobook.common.CoverReplacement
import com.goodwy.audiobook.common.MAX_IMAGE_SIZE
import com.goodwy.audiobook.covercolorextractor.CoverColorExtractor
import com.goodwy.audiobook.data.Book
import com.goodwy.audiobook.injection.appComponent
import com.goodwy.audiobook.misc.coverFile
import kotlinx.android.synthetic.main.book_overview_row_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoadBookCover(holder: BookOverviewHolder) {

  @Inject
  lateinit var coverColorExtractor: CoverColorExtractor

  init {
    appComponent.inject(this)
  }

  private val context = holder.itemView.context
  private val progress = holder.progress
  private val cover = holder.cover
  private val defaultProgressColor = context.getColor(R.color.progressColor)

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
      val shouldLoadImage = coverFileLength in 1 until MAX_IMAGE_SIZE
      withContext(Dispatchers.Main) {
        progress.color = extractedColor ?: defaultProgressColor
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
