package com.goodwy.audiobook.features.bookPlaying

import android.content.Context
import com.goodwy.audiobook.common.CoverReplacement
import com.goodwy.audiobook.data.Book
import com.goodwy.audiobook.misc.coverFile
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
  val rewindButtonStylePref: Int
)

data class BookPlayCover(private val book: Book) {

  fun file(): File? {
    return book.coverFile()
  }

  fun placeholder(context: Context): CoverReplacement = CoverReplacement(book.name, context)

  fun coverTransitionName(): String = book.coverTransitionName
}
