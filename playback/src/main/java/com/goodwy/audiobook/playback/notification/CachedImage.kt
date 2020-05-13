package com.goodwy.audiobook.playback.notification

import android.graphics.Bitmap
import com.goodwy.audiobook.data.Book
import java.util.UUID

/**
 * A cache entry for a bitmap
 */
data class CachedImage(val bookId: UUID, val cover: Bitmap) {

  fun matches(book: Book) = book.id == bookId
}
