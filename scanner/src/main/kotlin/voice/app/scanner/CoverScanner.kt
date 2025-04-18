package voice.app.scanner

import android.content.Context
import androidx.documentfile.provider.DocumentFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import voice.common.pref.PrefKeys
import voice.data.Book
import voice.data.toUri
import voice.ffmpeg.ffmpeg
import voice.logging.core.Logger
import voice.pref.Pref
import java.io.IOException
import javax.inject.Inject
import javax.inject.Named

class CoverScanner
@Inject constructor(
  private val context: Context,
  private val coverSaver: CoverSaver,
  @Named(PrefKeys.SCAN_COVER_CHAPTER)
  private val scanCoverChapter: Pref<Boolean>,
) {

  suspend fun scan(books: List<Book>) {
    books.forEach { findCoverForBook(it) }
  }

  private suspend fun findCoverForBook(book: Book) {
    val coverFile = book.content.cover
    if (coverFile != null && coverFile.exists()) {
      return
    }

    val foundOnDisc = findAndSaveCoverFromDisc(book)
    if (foundOnDisc) {
      return
    }

    scanForEmbeddedCover(book)
    if (scanCoverChapter.value) scanForEmbeddedCoverChapter(book)
  }

  private suspend fun findAndSaveCoverFromDisc(book: Book): Boolean = withContext(Dispatchers.IO) {
    val documentFile = try {
      DocumentFile.fromTreeUri(context, book.id.toUri())
    } catch (e: IllegalArgumentException) {
      null
    } ?: return@withContext false

    if (!documentFile.isDirectory) {
      return@withContext false
    }

    documentFile.listFiles().forEach { child ->
      if (child.isFile && child.canRead() && child.type?.startsWith("image/") == true) {
        val coverFile = coverSaver.newBookCoverFile()
        val worked = try {
          context.contentResolver.openInputStream(child.uri)?.use { input ->
            coverFile.outputStream().use { output ->
              input.copyTo(output)
            }
          }
          true
        } catch (e: IOException) {
          Logger.w(e, "Error while copying the cover from ${child.uri}")
          false
        } catch (e: IllegalStateException) {
          // On some Samsung Devices, openInputStream throws this though it should not.
          Logger.w(e, "Error while copying the cover from ${child.uri}")
          false
        }
        if (worked) {
          coverSaver.setBookCover(coverFile, book.id)
          return@withContext true
        }
      }
    }

    false
  }

  private suspend fun scanForEmbeddedCover(book: Book) {
    val coverFile = coverSaver.newBookCoverFile()
    book.chapters
      .take(5).forEach { chapter ->
        ffmpeg(
          input = chapter.id.toUri(),
          context = context,
          command = listOf("-an", coverFile.absolutePath),
        )
        if (coverFile.exists() && coverFile.length() > 0) {
          coverSaver.setBookCover(coverFile, bookId = book.id)
          return
        }
      }
  }

  private suspend fun scanForEmbeddedCoverChapter(book: Book) {
    //book.currentChapter.cover?.let { coverSaver.setBookCover(it, bookId = book.id) }
    book.chapters
      .mapNotNull { chapter ->
        val coverFile = coverSaver.newBookCoverFile()
        ffmpeg(
          input = chapter.id.toUri(),
          context = context,
          command = listOf("-an", coverFile.absolutePath),
        )
        if (coverFile.exists() && coverFile.length() > 0) {
          chapter to coverFile
        } else null
      }.toMap().also {
        coverSaver.setChapterCover(it)
      }
  }
}
