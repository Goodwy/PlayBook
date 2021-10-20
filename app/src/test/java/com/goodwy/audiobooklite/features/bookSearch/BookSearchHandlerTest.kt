package com.goodwy.audiobooklite.features.bookSearch

import android.annotation.SuppressLint
import android.provider.MediaStore
import com.google.common.truth.Truth.assertThat
import de.paulwoitaschek.flowpref.Pref
import de.paulwoitaschek.flowpref.inmemory.InMemoryPref
import com.goodwy.audiobooklite.data.Book
import com.goodwy.audiobooklite.data.BookContent
import com.goodwy.audiobooklite.data.BookMetaData
import com.goodwy.audiobooklite.data.BookSettings
import com.goodwy.audiobooklite.data.Chapter
import com.goodwy.audiobooklite.data.repo.BookRepository
import com.goodwy.audiobooklite.playback.PlayerController
import com.goodwy.audiobooklite.playback.session.search.BookSearch
import com.goodwy.audiobooklite.playback.session.search.BookSearchHandler
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import java.io.File
import java.util.UUID

/**
 * A test case to easily test the voice search functionality for Android auto (and OK google commands)
 */
@SuppressLint("SdCardPath")
class BookSearchHandlerTest {

  private val searchHandler: BookSearchHandler

  private val repo = mockk<BookRepository>()
  private val player = mockk<PlayerController>(relaxUnitFun = true)

  private val currentBookIdPref: Pref<UUID>

  private val anotherBookChapter1 = Chapter(
    File("/sdcard/AnotherBook/chapter1.mp3"),
    "anotherBookChapter1",
    5000,
    0,
    bookId = UUID.randomUUID(),
    markData = emptyList()
  )
  private val anotherBookChapter2 = Chapter(
    File("/sdcard/AnotherBook/chapter2.mp3"),
    "anotherBookChapter2",
    10000,
    0,
    bookId = UUID.randomUUID(),
    markData = emptyList()
  )

  private val anotherBook = UUID.randomUUID().let { id ->
    Book(
      id = id,
      metaData = BookMetaData(
        id = id,
        author = "AnotherBookAuthor",
        type = Book.Type.SINGLE_FOLDER,
        name = "AnotherBook",
        root = "/sdcard/AnotherBook",
        addedAtMillis = 0
      ),
      content = BookContent(
        id = id,
        settings = BookSettings(
          id = id,
          currentFile = anotherBookChapter1.file,
          positionInChapter = 3000,
          playbackSpeed = 1F,
          loudnessGain = 0,
          active = true,
          lastPlayedAtMillis = System.currentTimeMillis()
        ),
        chapters = listOf(
          anotherBookChapter1.copy(bookId = id),
          anotherBookChapter2.copy(bookId = id)
        )
      )
    )
  }

  private val bookToFindChapter1 =
    Chapter(
      File("/sdcard/Book1/chapter1.mp3"),
      "bookToFindChapter1",
      5000,
      0,
      emptyList(),
      UUID.randomUUID()
    )
  private val bookToFindChapter2 =
    Chapter(
      File("/sdcard/Book1/chapter2.mp3"),
      "bookToFindChapter2",
      10000,
      0,
      emptyList(),
      bookId = UUID.randomUUID()
    )
  private val bookToFind = UUID.randomUUID().let { id ->
    Book(
      metaData = BookMetaData(
        id = id,
        type = Book.Type.SINGLE_FOLDER,
        author = "Book1Author",
        name = "Book1",
        root = "/sdcard/Book1",
        addedAtMillis = 0
      ),
      id = id,
      content = BookContent(
        settings = BookSettings(
          id = id,
          currentFile = bookToFindChapter2.file,
          positionInChapter = 3000,
          playbackSpeed = 1F,
          loudnessGain = 0,
          active = true,
          lastPlayedAtMillis = System.currentTimeMillis()
        ),
        id = id,
        chapters = listOf(
          bookToFindChapter1.copy(bookId = id),
          bookToFindChapter2.copy(bookId = id)
        )
      )
    )
  }

  init {
    coEvery { repo.activeBooks() } returns listOf(anotherBook, bookToFind)
    currentBookIdPref = InMemoryPref(UUID.randomUUID())

    searchHandler = BookSearchHandler(repo, player, currentBookIdPref)
  }

  @Test
  fun unstructuredSearchByBook() {
    val bookSearch = BookSearch(query = bookToFind.name)
    searchHandler.handle(bookSearch)

    assertThat(currentBookIdPref.value).isEqualTo(bookToFind.id)
    verify(exactly = 1) { player.play() }
  }

  @Test
  fun unstructuredSearchByArtist() {
    val bookSearch = BookSearch(query = bookToFind.author)
    searchHandler.handle(bookSearch)

    assertThat(currentBookIdPref.value).isEqualTo(bookToFind.id)
    verify(exactly = 1) { player.play() }
  }

  @Test
  fun unstructuredSearchByChapter() {
    val bookSearch = BookSearch(query = bookToFindChapter1.name)
    searchHandler.handle(bookSearch)

    assertThat(currentBookIdPref.value).isEqualTo(bookToFind.id)
    verify(exactly = 1) { player.play() }
  }

  @Test
  fun mediaFocusAnyNoneFoundButPlayed() {
    val bookSearch = BookSearch(mediaFocus = "vnd.android.cursor.item/*")
    searchHandler.handle(bookSearch)

    assertThat(currentBookIdPref.value).isEqualTo(anotherBook.id)
    verify(exactly = 1) { player.play() }
  }

  @Test
  fun mediaFocusArtist() {
    val bookSearch = BookSearch(
      mediaFocus = MediaStore.Audio.Artists.ENTRY_CONTENT_TYPE,
      artist = bookToFind.author
    )
    searchHandler.handle(bookSearch)

    assertThat(currentBookIdPref.value).isEqualTo(bookToFind.id)
    verify(exactly = 1) { player.play() }
  }

  @Test
  fun mediaFocusArtistInTitleNoArtistInBook() {
    val bookToFind = bookToFind.updateMetaData { copy(author = null, name = "The book of Tim") }
    coEvery { repo.activeBooks() } returns listOf(bookToFind)

    val bookSearch = BookSearch(
      mediaFocus = MediaStore.Audio.Artists.ENTRY_CONTENT_TYPE,
      query = "Tim",
      artist = "Tim"
    )
    searchHandler.handle(bookSearch)

    assertThat(currentBookIdPref.value).isEqualTo(bookToFind.id)
    verify(exactly = 1) { player.play() }
  }

  @Test
  fun mediaFocusAlbum() {
    val bookSearch = BookSearch(
      mediaFocus = MediaStore.Audio.Albums.ENTRY_CONTENT_TYPE,
      artist = bookToFind.author,
      album = bookToFind.name
    )
    searchHandler.handle(bookSearch)

    assertThat(currentBookIdPref.value).isEqualTo(bookToFind.id)
    verify(exactly = 1) { player.play() }
  }

  @Test
  fun mediaFocusPlaylist() {
    val bookSearch = BookSearch(
      mediaFocus = MediaStore.Audio.Playlists.ENTRY_CONTENT_TYPE,
      artist = bookToFind.author,
      playList = bookToFind.name,
      album = bookToFind.name
    )
    searchHandler.handle(bookSearch)

    assertThat(currentBookIdPref.value).isEqualTo(bookToFind.id)
    verify(exactly = 1) { player.play() }
  }
}
