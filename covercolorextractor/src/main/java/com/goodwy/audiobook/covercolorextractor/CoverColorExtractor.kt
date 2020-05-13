package com.goodwy.audiobook.covercolorextractor

import android.graphics.Bitmap
import androidx.palette.graphics.Palette
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CoverColorExtractor {

  private val tasks = HashMap<Long, Deferred<Int?>>()
  private val extractedColors = HashMap<Long, Int?>()

  suspend fun extract(file: File): Int? = withContext(Dispatchers.IO) {
    if (!file.canRead()) {
      return@withContext null
    }
    val fileHash = fileHash(file)
    val extracted = extractedColors.getOrElse(fileHash) { 0 }
    if (extracted == 0) {
      val task = tasks.getOrPut(fileHash) { extractionAsync(file) }
      task.await()
    } else extracted
  }

  private suspend fun extractionAsync(file: File): Deferred<Int?> = coroutineScope {
    async {
      val bitmap = bitmapByFile(file)
      if (bitmap != null) {
        val extracted = extractColor(bitmap)
        bitmap.recycle()
        val hash = fileHash(file)
        extractedColors[hash] = extracted
        extracted
      } else null
    }
  }

  private suspend fun bitmapByFile(file: File): Bitmap? = withContext(Dispatchers.IO) {
    Timber.i("load cover for $file")
    try {
      Picasso.get()
        .load(file)
        .memoryPolicy(MemoryPolicy.NO_STORE)
        .resize(500, 500)
        .get()
    } catch (e: IOException) {
      Timber.e(e, "Error loading coverFile $file")
      null
    }
  }

  private suspend fun extractColor(bitmap: Bitmap): Int? =
    suspendCoroutine { cont ->
      Palette.from(bitmap)
        .generate { palette ->
          val invalidColor = -1
          val color = palette?.getVibrantColor(invalidColor)
          cont.resume(color.takeUnless { it == invalidColor })
        }
    }

  private fun fileHash(file: File) =
    file.absolutePath.hashCode() + file.lastModified() + file.length()
}
