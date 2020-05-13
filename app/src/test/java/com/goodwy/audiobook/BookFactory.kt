package com.goodwy.audiobook

import com.goodwy.audiobook.data.Book
import com.goodwy.audiobook.data.BookContent
import com.goodwy.audiobook.data.BookMetaData
import com.goodwy.audiobook.data.BookSettings
import com.goodwy.audiobook.data.Chapter
import java.util.UUID

internal object BookFactory {

  fun create(
    id: UUID = UUID.randomUUID(),
    type: Book.Type = Book.Type.SINGLE_FOLDER,
    author: String = "TestAuthor",
    currentFileIndex: Int = 0,
    time: Long = 0,
    name: String = "TestBook",
    playbackSpeed: Float = 1F,
    loudnessGain: Int = 500,
    skipSilence: Boolean = false,
    chapters: List<Chapter> = listOf(
      ChapterFactory.create(bookId = id, file = "1.mp3"),
      ChapterFactory.create(bookId = id, file = "2.mp3")
    ),
    lastPlayedAtMillis: Long = 0L,
    addedAtMillis: Long = 0L
  ): Book {

    val currentFile = chapters[currentFileIndex].file
    val root = if (currentFile.parent != null) currentFile.parent else "fakeRoot"

    return Book(
      id = id,
      metaData = BookMetaData(
        id = id,
        type = type,
        author = author,
        name = name,
        root = root,
        addedAtMillis = addedAtMillis
      ),
      content = BookContent(
        id = id,
        settings = BookSettings(
          id = id,
          currentFile = currentFile,
          positionInChapter = time,
          playbackSpeed = playbackSpeed,
          loudnessGain = loudnessGain,
          skipSilence = skipSilence,
          active = true,
          lastPlayedAtMillis = lastPlayedAtMillis
        ),
        chapters = chapters
      )
    )
  }
}
