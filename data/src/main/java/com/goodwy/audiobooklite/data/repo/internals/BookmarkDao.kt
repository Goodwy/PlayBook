package com.goodwy.audiobooklite.data.repo.internals

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.goodwy.audiobooklite.data.Bookmark
import java.io.File
import java.util.UUID

@Dao
interface BookmarkDao {

  @Query("DELETE FROM bookmark WHERE id = :id")
  suspend fun deleteBookmark(id: UUID)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun addBookmark(bookmark: Bookmark)

  @Query("SELECT * FROM bookmark WHERE file IN(:files)")
  suspend fun allForFiles(files: List<@JvmSuppressWildcards File>): List<Bookmark>
}
