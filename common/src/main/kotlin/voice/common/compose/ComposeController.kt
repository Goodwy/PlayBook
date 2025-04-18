package voice.common.compose

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.ComposeView
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.bluelinelabs.conductor.Controller

abstract class ComposeController(args: Bundle = Bundle()) : Controller(args) {

  final override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup,
    savedViewState: Bundle?,
  ): View {
    return ComposeView(container.context).also {
      it.setContent {
        VoiceTheme {
          Content()
          //TODO statusBar and navigationBar Color
//          if (!useTransparentNavigation()) {
//            val window = (view!!.context as Activity).window
//            window.statusBarColor = MaterialTheme.colorScheme.background.toArgb()
//            window.navigationBarColor = MaterialTheme.colorScheme.background.toArgb()
//          }

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

  @Composable
  abstract fun Content()
}
