package com.goodwy.audiobooklite.data.repo.internals

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import dagger.Module
import dagger.Provides
import com.goodwy.audiobooklite.data.repo.internals.migrations.Migration23to24
import com.goodwy.audiobooklite.data.repo.internals.migrations.Migration24to25
import com.goodwy.audiobooklite.data.repo.internals.migrations.Migration25to26
import com.goodwy.audiobooklite.data.repo.internals.migrations.Migration26to27
import com.goodwy.audiobooklite.data.repo.internals.migrations.Migration27to28
import com.goodwy.audiobooklite.data.repo.internals.migrations.Migration28to29
import com.goodwy.audiobooklite.data.repo.internals.migrations.Migration29to30
import com.goodwy.audiobooklite.data.repo.internals.migrations.Migration30to31
import com.goodwy.audiobooklite.data.repo.internals.migrations.Migration31to32
import com.goodwy.audiobooklite.data.repo.internals.migrations.Migration32to34
import com.goodwy.audiobooklite.data.repo.internals.migrations.Migration34to35
import com.goodwy.audiobooklite.data.repo.internals.migrations.Migration35to36
import com.goodwy.audiobooklite.data.repo.internals.migrations.Migration36to37
import com.goodwy.audiobooklite.data.repo.internals.migrations.Migration37to38
import com.goodwy.audiobooklite.data.repo.internals.migrations.Migration38to39
import com.goodwy.audiobooklite.data.repo.internals.migrations.Migration39to40
import com.goodwy.audiobooklite.data.repo.internals.migrations.Migration40to41
import com.goodwy.audiobooklite.data.repo.internals.migrations.Migration41to42
import com.goodwy.audiobooklite.data.repo.internals.migrations.Migration42to43
import com.goodwy.audiobooklite.data.repo.internals.migrations.Migration43to44
import com.goodwy.audiobooklite.data.repo.internals.migrations.Migration44
import com.goodwy.audiobooklite.data.repo.internals.migrations.Migration45
import com.goodwy.audiobooklite.data.repo.internals.migrations.Migration46
import com.goodwy.audiobooklite.data.repo.internals.migrations.Migration47
import com.goodwy.audiobooklite.data.repo.internals.migrations.Migration48
import com.goodwy.audiobooklite.data.repo.internals.migrations.Migration49
import com.goodwy.audiobooklite.data.repo.internals.migrations.Migration50
import com.goodwy.audiobooklite.data.repo.internals.migrations.Migration51to52
import javax.inject.Singleton

@Module
object PersistenceModule {

  @Provides
  @JvmStatic
  fun bookmarkDao(appDb: AppDb) = appDb.bookmarkDao()

  @Provides
  @JvmStatic
  fun chapterDao(appDb: AppDb) = appDb.chapterDao()

  @Provides
  @JvmStatic
  fun metaDataDao(appDb: AppDb) = appDb.bookMetadataDao()

  @Provides
  @JvmStatic
  fun bookSettingsDao(appDb: AppDb) = appDb.bookSettingsDao()

  @Provides
  @JvmStatic
  fun roomDatabaseBuilder(context: Context): RoomDatabase.Builder<AppDb> {
    return Room.databaseBuilder(context, AppDb::class.java, AppDb.DATABASE_NAME)
  }

  @Provides
  @Singleton
  @JvmStatic
  fun appDb(
    builder: RoomDatabase.Builder<AppDb>,
    migrations: Array<Migration>
  ): AppDb {
    return builder
      .addMigrations(*migrations)
      .build()
  }

  @Provides
  @JvmStatic
  fun migrations(context: Context): Array<Migration> {
    return arrayOf(
      Migration23to24(),
      Migration24to25(context),
      Migration25to26(),
      Migration26to27(),
      Migration27to28(),
      Migration28to29(),
      Migration29to30(),
      Migration30to31(),
      Migration31to32(),
      Migration32to34(),
      Migration34to35(),
      Migration35to36(),
      Migration36to37(),
      Migration37to38(),
      Migration38to39(),
      Migration39to40(),
      Migration40to41(),
      Migration41to42(),
      Migration42to43(),
      Migration43to44(),
      Migration44(),
      Migration45(),
      Migration46(),
      Migration47(),
      Migration48(),
      Migration49(),
      Migration50(),
      Migration51to52()
    )
  }
}
