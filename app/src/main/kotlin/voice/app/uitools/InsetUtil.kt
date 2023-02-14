@file:Suppress("DEPRECATION")

package voice.app.uitools

import android.app.Activity
import android.graphics.Color
import android.view.View
import androidx.core.view.ViewCompat

object InsetUtil {

  fun removeSystemInsets(view: View, listener: OnSystemInsetsChangedListener, statusBar: Boolean = false) {
    ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->

      /*val desiredBottomInset = calculateDesiredBottomInset(
          view,
          insets.systemWindowInsetTop,
          insets.systemWindowInsetBottom,
          listener
      )*/

      listener.invoke(
        insets.systemWindowInsetTop,
        insets.systemWindowInsetBottom,
        insets.systemWindowInsetLeft,
        insets.systemWindowInsetRight
      )
      //Удаляем системные отступы
      val top = if (statusBar) insets.systemWindowInsetTop else 0
      ViewCompat.onApplyWindowInsets(view, insets.replaceSystemWindowInsets(0, top, 0, 0))
    }
  }

  fun calculateDesiredBottomInset(
    view: View,
    topInset: Int,
    bottomInset: Int,
    listener: OnSystemInsetsChangedListener
  ): Int {
    val hasKeyboard = view.isKeyboardAppeared(bottomInset)
    val desiredBottomInset = if (hasKeyboard) bottomInset else 0
    listener(topInset, if (hasKeyboard) 0 else bottomInset, 0, 0)
    return desiredBottomInset
  }
}

private fun View.isKeyboardAppeared(bottomInset: Int) =
  bottomInset / resources.displayMetrics.heightPixels.toDouble() > .25

typealias OnSystemBarsSizeChangedListener =
    (statusBarSize: Int, navigationBarSize: Int) -> Unit

typealias OnSystemInsetsChangedListener = (
  statusBarSize: Int,
  bottomNavigationBarSize: Int,
  leftNavigationBarSize: Int,
  rightNavigationBarSize: Int
) -> Unit

fun Activity.setWindowTransparency(
  statusBar: Boolean = false,
  listener: OnSystemInsetsChangedListener = { _, _, _, _ -> }
) {
  InsetUtil.removeSystemInsets(window.decorView, listener, statusBar)
  window.navigationBarColor = Color.TRANSPARENT
  if (!statusBar) window.statusBarColor = Color.TRANSPARENT
}
