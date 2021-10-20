package com.goodwy.audiobooklite.data.repo.internals.migrations

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

class Migration42to43 : IncrementalMigration(42) {
  override fun migrate(db: SupportSQLiteDatabase) {
    // invalidate modification time stamps so the chapters will be re-scanned
    val lastModifiedCv = ContentValues().apply {
      put("lastModified", 0)
    }
    db.update("tableChapters", SQLiteDatabase.CONFLICT_FAIL, lastModifiedCv, null, null)
  }
}
