package com.goodwy.audiobook.features.bookPlaying.selectchapter

import com.goodwy.audiobook.data.ChapterMark

data class SelectChapterViewState(
  val chapters: List<ChapterMark>,
  val selectedIndex: Int?,
  val showChapterNumbers: Boolean
)
