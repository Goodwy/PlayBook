package com.goodwy.audiobook.features.bookPlaying.selectchapter

import com.goodwy.audiobook.data.ChapterMark
import com.goodwy.audiobook.features.bookPlaying.BookPlayCover

data class SelectChapterViewState(
  val chapters: List<ChapterMark>,
  val selectedIndex: Int?,
  val bookName: String?,
  val bookDuration: Long?,
  val showChapterNumbers: Boolean,
  val cover: BookPlayCover,
  val bookAuthor: String?,
  val remainingTimeInMs: Long?
)
