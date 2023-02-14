package voice.data.repo.internals.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import voice.data.Chapter

@Dao
interface ChapterDao {

  @Query("SELECT * FROM chapters2 WHERE id = :id")
  suspend fun chapter(id: Chapter.Id): Chapter?

  @Query("SELECT * FROM chapters2 WHERE id IN (:ids)")
  suspend fun chapters(ids: List<Chapter.Id>): List<Chapter>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(chapter: Chapter)
}
