package voice.common

import android.content.Context
import java.util.concurrent.TimeUnit

fun formatTime(
  timeMs: Long,
  durationMs: Long = 0,
): String {
  val m = timeMs.minutes()
  val s = timeMs.seconds()
  val largerTime = maxOf(timeMs, durationMs)
  val hourDigits = largerTime.hours().digits()
  return if (hourDigits > 0) {
    val h = timeMs.hours()
    "%1\$0${hourDigits}d:%2\$02d:%3\$02d".format(h, m, s)
  } else {
    val minuteDigits = largerTime.minutes().digits()
    val pattern = if (minuteDigits == 0) {
      "%1\$d:%2\$02d"
    } else {
      "%1\$0${minuteDigits}d:%2\$02d"
    }
    pattern.format(m, s)
  }
}

private fun Long.digits(): Int {
  return if (this == 0L) {
    0
  } else {
    toString().length
  }
}

private fun Long.hours() = TimeUnit.MILLISECONDS.toHours(this)

private fun Long.minutes() = TimeUnit.MILLISECONDS.toMinutes(this) % 60

private fun Long.seconds() = TimeUnit.MILLISECONDS.toSeconds(this) % 60

fun formatTimeMinutes(context: Context, timeMs: Long, durationMs: Long = 0): String {
  val m = timeMs.minutes()
  val largerTime = maxOf(timeMs, durationMs)
  val hourDigits = largerTime.hours().digits()
  val hour = context.getString(R.string.hours_letter)
  val min = context.getString(R.string.minutes_letter)
  return if (hourDigits > 0) {
    val h = timeMs.hours()
    "%1\$0${hourDigits}d %2\$s, %3\$02d %4\$s".format(h, hour, m, min)
  } else {
    val minuteDigits = largerTime.minutes().digits()
    val pattern = if (minuteDigits == 0) {
      "%1\$d %2\$s"
    } else {
      "%1\$0${minuteDigits}d %2\$s"
    }
    pattern.format(m, min)
  }
}
