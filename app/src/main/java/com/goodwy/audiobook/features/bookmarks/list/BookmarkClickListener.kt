package com.goodwy.audiobook.features.bookmarks.list

import com.goodwy.audiobook.data.Bookmark

interface BookmarkClickListener {

  fun onOptionsMenuClicked(bookmark: Bookmark, v: android.view.View)
  fun onBookmarkClicked(bookmark: Bookmark)
  fun onBookmarkLongClicked(bookmark: Bookmark)
}
