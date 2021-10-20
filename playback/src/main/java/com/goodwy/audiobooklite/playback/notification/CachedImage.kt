package com.goodwy.audiobooklite.playback.notification

import android.graphics.Bitmap
import com.goodwy.audiobooklite.data.Book
import java.util.UUID

/**
 * A cache entry for a bitmap
 */
data class CachedImage(val bookId: UUID, val cover: Bitmap) {

  fun matches(book: Book) = book.id == bookId
}
