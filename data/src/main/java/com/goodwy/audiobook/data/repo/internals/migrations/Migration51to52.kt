package com.goodwy.audiobook.data.repo.internals.migrations

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.goodwy.audiobook.data.repo.internals.transaction

class Migration51to52 : IncrementalMigration(51) {

  override fun migrate(db: SupportSQLiteDatabase) {
    with(db) {
      execSQL("ALTER TABLE bookSettings ADD COLUMN showChapterNumbers INTEGER NOT NULL DEFAULT 1")
    }
  }
}
