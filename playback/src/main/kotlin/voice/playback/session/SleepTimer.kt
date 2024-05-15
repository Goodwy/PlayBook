package voice.playback.session

import kotlinx.coroutines.flow.Flow
import voice.common.BookId
import kotlin.time.Duration

interface SleepTimer {
  val leftSleepTimeFlow: Flow<Duration>
  val sleepAtEocFlow: Flow<Boolean>
  fun sleepTimerActive(): Boolean
  fun setActive(enable: Boolean)
  fun setEoc(enable: Boolean, bookId: BookId)
}
