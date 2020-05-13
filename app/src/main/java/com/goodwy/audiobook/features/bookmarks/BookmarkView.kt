package com.goodwy.audiobook.features.bookmarks

import com.goodwy.audiobook.data.Bookmark
import com.goodwy.audiobook.data.Chapter

/**
 * View of the bookmarks
 */
interface BookmarkView {

  fun render(bookmarks: List<Bookmark>, chapters: List<Chapter>)
  fun showBookmarkAdded(bookmark: Bookmark)
  fun finish()
}
