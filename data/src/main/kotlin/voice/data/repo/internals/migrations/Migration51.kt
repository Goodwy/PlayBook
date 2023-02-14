package voice.data.repo.internals.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.squareup.anvil.annotations.ContributesMultibinding
import voice.common.AppScope
import javax.inject.Inject

@ContributesMultibinding(
  scope = AppScope::class,
  boundType = Migration::class,
)
class Migration51
@Inject constructor() : IncrementalMigration(51) {

  override fun migrate(db: SupportSQLiteDatabase) {
    db.execSQL("ALTER TABLE bookSettings ADD showChapterNumbers INTEGER NOT NULL DEFAULT 1")
  }
}
