package voice.common

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.Toast
import kotlin.math.roundToInt

fun Context.layoutInflater(): LayoutInflater = LayoutInflater.from(this)

fun Context.dpToPx(dp: Float) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)

fun Context.dpToPxRounded(dp: Float): Int = dpToPx(dp).roundToInt()

fun checkMainThread() {
  check(Looper.getMainLooper() == Looper.myLooper()) {
    "Is not on ui thread!"
  }
}

fun Context.convertPixelsToDp(pixels: Int) = (pixels / resources.displayMetrics.density).toInt()

fun Context.copyToClipboard(text: String) {
  val clip = ClipData.newPlainText(getString(R.string.app_name), text)
  (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(clip)
  val toastText = String.format(getString(R.string.copied_to_clipboard_show), text)
  toast(toastText)
}

fun Context.toast(msg: String, length: Int = Toast.LENGTH_SHORT) {
  try {
    if (isOnMainThread()) {
      doToast(this, msg, length)
    } else {
      Handler(Looper.getMainLooper()).post {
        doToast(this, msg, length)
      }
    }
  } catch (_: Exception) {
  }
}

private fun doToast(context: Context, message: String, length: Int) {
  if (context is Activity) {
    if (!context.isFinishing && !context.isDestroyed) {
      Toast.makeText(context, message, length).show()
    }
  } else {
    Toast.makeText(context, message, length).show()
  }
}

fun isOnMainThread() = Looper.myLooper() == Looper.getMainLooper()

inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? {
  return if (Build.VERSION.SDK_INT >= 33) {
    getParcelable(key, T::class.java)
  } else {
    @Suppress("DEPRECATION")
    getParcelable(key)
  }
}
