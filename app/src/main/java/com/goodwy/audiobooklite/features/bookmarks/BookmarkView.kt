package com.goodwy.audiobooklite.features.bookmarks

import com.goodwy.audiobooklite.data.Bookmark
import com.goodwy.audiobooklite.data.Chapter

/**
 * View of the bookmarks
 */
interface BookmarkView {

  fun render(bookmarks: List<Bookmark>, chapters: List<Chapter>)
  fun showBookmarkAdded(bookmark: Bookmark)
  fun finish()
}
