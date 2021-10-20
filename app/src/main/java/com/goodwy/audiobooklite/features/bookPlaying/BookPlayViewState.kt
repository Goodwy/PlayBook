package com.goodwy.audiobooklite.features.bookPlaying

import android.content.Context
import com.goodwy.audiobooklite.common.CoverReplacement
import com.goodwy.audiobooklite.data.Book
import com.goodwy.audiobooklite.misc.coverFile
import java.io.File
import kotlin.time.Duration

data class BookPlayViewState(
  val bookName: String?,
  val chapterName: String?,
  val showPreviousNextButtons: Boolean,
  val title: String,
  val sleepTime: Duration,
  val playedTime: Duration,
  val duration: Duration,
  val playing: Boolean,
  val cover: BookPlayCover,
  val skipSilence: Boolean,
  val showChapterNumbers: Boolean,
  val rewindButtonStylePref: Int,
  val currentVolumePref: Int,
  val repeatModePref: Int
)

data class BookPlayCover(private val book: Book) {

  fun file(): File? {
    return book.coverFile()
  }

  fun placeholder(context: Context): CoverReplacement = CoverReplacement(book.name, context)

  fun coverTransitionName(): String = book.coverTransitionName
}
