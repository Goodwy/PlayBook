package voice.common.compose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.Dispatchers
import voice.common.DARK_THEME_SETTABLE
import voice.common.conductor.BaseController
import voice.common.rootComponentAs

abstract class ComposeController(args: Bundle = Bundle()) : BaseController(args) {

  final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedViewState: Bundle?): View {
    onCreateView()
    return ComposeView(container.context).also {
      it.setContent {
        VoiceTheme {
          Content()
          //TODO statusBar and navigationBar Color
          //val window = (view!!.context as Activity).window
          //window.statusBarColor = colors.primary.toArgb()
          //window.navigationBarColor = colors.primary.toArgb()

          val systemUiController = rememberSystemUiController()
          val useDarkIcons = !isDarkTheme() //!isSystemInDarkTheme()
          DisposableEffect(systemUiController, useDarkIcons) {
            // Update all of the system bar colors to be transparent, and use
            // dark icons if we're in light theme
            systemUiController.setSystemBarsColor(
              color = Color.Transparent,
              darkIcons = useDarkIcons
            )
            // setStatusBarColor() and setNavigationBarColor() also exist
            onDispose {}
          }
        }
      }
    }
  }

  open fun onCreateView() {}

  @Composable
  abstract fun Content()
}
