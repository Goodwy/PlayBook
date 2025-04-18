package voice.app.scanner

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import voice.common.BookId
import voice.common.toast
import voice.data.Chapter
import voice.data.repo.BookRepository
import voice.data.repo.ChapterRepo
import voice.logging.core.Logger
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.DecimalFormat
import java.util.UUID
import javax.inject.Inject
import kotlin.math.log10
import kotlin.math.max
import kotlin.math.pow
import voice.common.R as CommonR

class CoverSaver
@Inject constructor(
  private val repo: BookRepository,
  private val context: Context,
  private val repoChapter: ChapterRepo,
) {
  private val _sizeCoversDirectory = MutableStateFlow("0MB")
  val sizeCoversDirectory: Flow<String> = _sizeCoversDirectory

  suspend fun save(
    bookId: BookId,
    cover: Bitmap,
  ) {
    val newCover = newBookCoverFile()

    withContext(Dispatchers.IO) {
      // scale down if bitmap is too large
      val preferredSize = 1920
      val bitmapToSave = if (max(cover.width, cover.height) > preferredSize) {
        Bitmap.createScaledBitmap(cover, preferredSize, preferredSize, true)
      } else {
        cover
      }

      try {
        FileOutputStream(newCover).use {
          val compressFormat = when (newCover.extension) {
            "png" -> Bitmap.CompressFormat.PNG
            "webp" -> if (Build.VERSION.SDK_INT >= 30) {
              Bitmap.CompressFormat.WEBP_LOSSLESS
            } else {
              @Suppress("DEPRECATION")
              Bitmap.CompressFormat.WEBP
            }
            else -> error("Unhandled image extension for $newCover")
          }
          bitmapToSave.compress(compressFormat, 70, it)
          it.flush()
        }
      } catch (e: IOException) {
        Logger.w(e, "Error at saving image with destination=$newCover")
      }
    }

    setBookCover(newCover, bookId)
  }

  suspend fun newBookCoverFile(): File {
    val coversFolder = withContext(Dispatchers.IO) {
      File(context.filesDir, "bookCovers")
        .also { coverFolder -> coverFolder.mkdirs() }
    }
    return File(coversFolder, "${UUID.randomUUID()}.png")
  }

  suspend fun setBookCover(
    cover: File,
    bookId: BookId,
  ) {
    val oldCover = repo.get(bookId)?.content?.cover
    if (oldCover != null) {
      withContext(Dispatchers.IO) {
        oldCover.delete()
      }
    }

    repo.updateBook(bookId) {
      it.copy(cover = cover)
    }
  }

  suspend fun setChapterCover(
    chapterIdToCoverFile: Map<Chapter,
      File>
  ) {
    chapterIdToCoverFile.forEach { (providedChapter, cover) ->
      val chapterId = providedChapter.id
      val chapter = repoChapter.get(chapterId)
      if (chapter == null) {
        Logger.e("Missing chapter with id=$chapterId for $this")
        return@forEach
      }
      val oldCover = chapter.cover
      if (oldCover != null) {
        withContext(Dispatchers.IO) {
          oldCover.delete()
        }
      }
      repoChapter.updateChapter(chapterId) {
        it.copy(cover = cover)
      }
    }
  }

  suspend fun clearCoversDirectory() {
    try {
      val directory = withContext(Dispatchers.IO) {
        File(context.filesDir, "bookCovers")
          .also { coverFolder -> coverFolder.mkdirs() }
      }
      if (directory.exists() && directory.isDirectory) {
        directory.listFiles()?.forEach { file ->
          file.delete()
        }
        context.toast(context.getString(CommonR.string.cleared))
      }
      updateSizeCoversDirectory()
    } catch (e: IOException) {
      e.printStackTrace()
      context.toast(e.toString())
      updateSizeCoversDirectory()
    }
  }

  suspend fun updateSizeCoversDirectory() {
    val directory = withContext(Dispatchers.IO) {
      File(context.filesDir, "bookCovers")
        .also { coverFolder -> coverFolder.mkdirs() }
    }
    val size =
      directory.walkTopDown().filter { it.isFile }.map { it.length() }.sum()

    if (size <= 0) _sizeCoversDirectory.value = "0MB"
    else {
      val units = arrayOf("B", "KB", "MB", "GB", "TB")
      val digitGroups = (log10(size.toDouble()) / log10(1024.0)).toInt()
      _sizeCoversDirectory.value = DecimalFormat("#,##0.#").format(size / 1024.0.pow(digitGroups.toDouble())) + " " + units[digitGroups]
    }
  }
}
