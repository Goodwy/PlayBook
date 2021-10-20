package com.goodwy.audiobooklite.features.bookmarks.list

import com.goodwy.audiobooklite.data.Bookmark

interface BookmarkClickListener {

  fun onOptionsMenuClicked(bookmark: Bookmark, v: android.view.View)
  fun onBookmarkClicked(bookmark: Bookmark)
  fun onBookmarkLongClicked(bookmark: Bookmark)
}
