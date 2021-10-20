package com.goodwy.audiobooklite.features.bookPlaying

sealed class BookPlayViewEffect {
  object BookmarkAdded : BookPlayViewEffect()
  object ShowSleepTimeDialog : BookPlayViewEffect()
}
