package voice.common

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast

val DARK_THEME_SETTABLE = Build.VERSION.SDK_INT < 29

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
val COLOR_SCHEME_SETTABLE = Build.VERSION.SDK_INT >= 31


val PADDING = "Build.VERSION.SDK_INT < 29"
