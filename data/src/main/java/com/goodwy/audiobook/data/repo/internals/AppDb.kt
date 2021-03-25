package com.goodwy.audiobook.data.repo.internals

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.goodwy.audiobook.data.BookMetaData
import com.goodwy.audiobook.data.BookSettings
import com.goodwy.audiobook.data.Bookmark
import com.goodwy.audiobook.data.Chapter

@Database(
  entities = [Bookmark::class, Chapter::class, BookMetaData::class, BookSettings::class],
  version = AppDb.VERSION
)
@TypeConverters(Converters::class)
abstract class AppDb : RoomDatabase() {

  abstract fun bookmarkDao(): BookmarkDao
  abstract fun chapterDao(): ChapterDao
  abstract fun bookMetadataDao(): BookMetaDataDao
  abstract fun bookSettingsDao(): BookSettingsDao

  companion object {
    const val VERSION = 52
    const val DATABASE_NAME = "autoBookDB"
  }
}
