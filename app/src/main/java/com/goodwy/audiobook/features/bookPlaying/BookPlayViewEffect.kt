package com.goodwy.audiobook.features.bookPlaying

sealed class BookPlayViewEffect {
  object BookmarkAdded : BookPlayViewEffect()
  object ShowSleepTimeDialog : BookPlayViewEffect()
}
