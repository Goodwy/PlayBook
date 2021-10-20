package com.goodwy.audiobooklite.features.bookPlaying.selectchapter

import com.goodwy.audiobooklite.data.ChapterMark

data class SelectChapterViewState(
  val chapters: List<ChapterMark>,
  val selectedIndex: Int?,
  val bookName: String?,
  val bookDuration: Long?,
  val showChapterNumbers: Boolean
)
