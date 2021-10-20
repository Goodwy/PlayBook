package com.goodwy.audiobooklite.data.repo.internals

import com.google.common.truth.Truth.assertThat
import com.goodwy.audiobooklite.data.Book
import com.goodwy.audiobooklite.data.MarkData
import org.junit.Test
import org.threeten.bp.Instant
import java.io.File
import java.util.UUID

class ConvertersTest {

  @Test
  fun instant() {
    test(Instant.now(), Converters::fromInstant, Converters::toInstant)
  }

  @Test
  fun uuid() {
    test(UUID.randomUUID(), Converters::fromUUID, Converters::toUUID)
  }

  @Test
  fun file() {
    test(File("/sdcard/audiobooks/potter.mp3"), Converters::fromFile, Converters::toFile)
  }

  @Test
  fun bookType() {
    Book.Type.values().forEach { bookType ->
      test(bookType, Converters::fromBookType, Converters::toBookType)
    }
  }

  @Test
  fun marksEmpty() {
    test(emptyList(), Converters::fromMarks, Converters::toMarks)
  }

  @Test
  fun marks() {
    test(
      value = listOf(
        MarkData(0L, "Hello"),
        MarkData(5L, "World"),
        MarkData(Long.MIN_VALUE, "")
      ),
      serialize = Converters::fromMarks,
      deSerialize = Converters::toMarks
    )
  }

  private fun <S, D> test(
    value: D,
    serialize: (Converters.(D) -> S),
    deSerialize: (Converters.(S) -> D)
  ) {
    val converters = Converters()
    val serialized: S = converters.serialize(value)
    val deSerialized: D = converters.deSerialize(serialized)
    assertThat(deSerialized).isEqualTo(value)
  }
}
