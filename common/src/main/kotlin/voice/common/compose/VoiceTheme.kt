package voice.common.compose

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import material.util.color.scheme.Scheme
import voice.common.COLOR_SCHEME_SETTABLE
import voice.common.DARK_THEME_SETTABLE
import voice.common.compose.MyColorLightTokens.paletteTokens
import voice.common.constants.THEME_AUTO
import voice.common.constants.THEME_DARK
import voice.common.constants.THEME_LIGHT
import voice.common.rootComponentAs
import androidx.compose.material3.MaterialTheme as Material3Theme

@Composable
fun VoiceTheme(
  preview: Boolean = false, // preview render does not work without it
  content: @Composable () -> Unit,
) {
  Material3Theme(
    colorScheme = if (isDarkTheme(preview)) {
      if (isSystemScheme(preview)) { //Build.VERSION.SDK_INT >= 31
        dynamicDarkColorScheme(LocalContext.current)
      } else {
        rememberColorScheme(Color(colorTheme())) //myDarkColorScheme() //darkColorScheme()
      }
    } else {
      if (isSystemScheme(preview)) { //Build.VERSION.SDK_INT >= 31
        dynamicLightColorScheme(LocalContext.current)
      } else {
        rememberColorScheme(Color(colorTheme())) //myLightColorScheme() //lightColorScheme()
      }
    },
  ) {
    content()
  }
}

//@Composable
//fun isDarkTheme(): Boolean {
//  return if (DARK_THEME_SETTABLE) {
//    val darkThemeFlow = remember {
//      rootComponentAs<SharedComponent>().useDarkTheme.flow
//    }
//    darkThemeFlow.collectAsState(initial = false, context = Dispatchers.Unconfined).value
//  } else {
//    isSystemInDarkTheme()
//  }
//}

@Composable
fun isDarkTheme(preview: Boolean = false): Boolean {
  return if (preview) isSystemInDarkTheme()
  else if (DARK_THEME_SETTABLE) {
    theme() == THEME_DARK
  } else {
    when (theme()) {
      THEME_LIGHT -> false
      THEME_DARK -> true
      else -> isSystemInDarkTheme()
    }
  }
}

@Composable
fun isSystemScheme(preview: Boolean = false): Boolean {
  return if (preview) true
  else if (COLOR_SCHEME_SETTABLE) {
    when (theme()) {
      THEME_AUTO -> true
      else -> false
    }
  } else {
    false
  }
}

@Composable
fun theme(): Int {
  val themeFlow = remember {
    rootComponentAs<SharedComponent>().themePref.flow
  }
  return themeFlow.collectAsState(initial = THEME_DARK, context = Dispatchers.Unconfined).value
}

@Composable
fun colorTheme(): Int {
  val colorThemeFlow = remember {
    rootComponentAs<SharedComponent>().colorThemePreference.flow
  }
  return colorThemeFlow.collectAsState(initial = -0x1, context = Dispatchers.Unconfined).value
}

@Composable
fun useTransparentNavigation(): Boolean {
  val useTransparentNavigationFlow = remember {
    rootComponentAs<SharedComponent>().useTransparentNavigation.flow
  }
  return useTransparentNavigationFlow.collectAsState(initial = false, context = Dispatchers.Unconfined).value
}

@Composable
private fun rememberColorScheme(color: Color): ColorScheme {
  val isDarkTheme = isDarkTheme()
  val colorArgb = color.toArgb()
  return remember(color) {
    if (isDarkTheme) {
      Scheme.darkContent(colorArgb).toDarkThemeColorScheme()
    } else {
      Scheme.lightContent(colorArgb).toLightThemeColorScheme()
    }
  }
}

private fun Scheme.toDarkThemeColorScheme(): ColorScheme {
  return darkColorScheme(
    primary = Color(primary),
    onPrimary = Color(onPrimary),
    primaryContainer = Color(primaryContainer),
    onPrimaryContainer = Color(onPrimaryContainer),
    inversePrimary = Color(inversePrimary),
    secondary = Color(secondary),
    onSecondary = Color(onSecondary),
    secondaryContainer = Color(secondaryContainer),
    onSecondaryContainer = Color(onSecondaryContainer),
    tertiary = Color(tertiary),
    onTertiary = Color(onTertiary),
    tertiaryContainer = Color(tertiaryContainer),
    onTertiaryContainer = Color(onTertiaryContainer),
    background = Color(background),
    onBackground = Color(onBackground),
    surface = Color(surface),
    onSurface = Color(onSurface),
    surfaceVariant = Color(surfaceVariant),
    onSurfaceVariant = Color(onSurfaceVariant),
    surfaceTint = Color(surfaceVariant),
    inverseSurface = Color(inverseSurface),
    inverseOnSurface = Color(inverseOnSurface),
    error = Color(error),
    onError = Color(onError),
    errorContainer = Color(errorContainer),
    onErrorContainer = Color(onErrorContainer),
    outline = Color(outline),
    outlineVariant = Color(outlineVariant),
    scrim = Color(scrim),
  )
}

private fun Scheme.toLightThemeColorScheme(): ColorScheme {
  return lightColorScheme(
    primary = Color(primary),
    onPrimary = Color(onPrimary),
    primaryContainer = Color(primaryContainer),
    onPrimaryContainer = Color(onPrimaryContainer),
    inversePrimary = Color(inversePrimary),
    secondary = Color(secondary),
    onSecondary = Color(onSecondary),
    secondaryContainer = Color(secondaryContainer),
    onSecondaryContainer = Color(onSecondaryContainer),
    tertiary = Color(tertiary),
    onTertiary = Color(onTertiary),
    tertiaryContainer = Color(tertiaryContainer),
    onTertiaryContainer = Color(onTertiaryContainer),
    background = Color(background),
    onBackground = Color(onBackground),
    surface = Color(surface),
    onSurface = Color(onSurface),
    surfaceVariant = Color(surfaceVariant),
    onSurfaceVariant = Color(onSurfaceVariant),
    surfaceTint = Color(surfaceVariant),
    inverseSurface = Color(inverseSurface),
    inverseOnSurface = Color(inverseOnSurface),
    error = Color(error),
    onError = Color(onError),
    errorContainer = Color(errorContainer),
    onErrorContainer = Color(onErrorContainer),
    outline = Color(outline),
    outlineVariant = Color(outlineVariant),
    scrim = Color(scrim),
  )
}

@Composable
fun myLightColorScheme(
  primary: Color = MyColorLightTokens.Primary,
  onPrimary: Color = MyColorLightTokens.OnPrimary,
  primaryContainer: Color = MyColorLightTokens.PrimaryContainer,
  onPrimaryContainer: Color = MyColorLightTokens.OnPrimaryContainer,
  inversePrimary: Color = MyColorLightTokens.InversePrimary,
  secondary: Color = MyColorLightTokens.Secondary,
  onSecondary: Color = MyColorLightTokens.OnSecondary,
  secondaryContainer: Color = MyColorLightTokens.SecondaryContainer,
  onSecondaryContainer: Color = MyColorLightTokens.OnSecondaryContainer,
  tertiary: Color = MyColorLightTokens.Tertiary,
  onTertiary: Color = MyColorLightTokens.OnTertiary,
  tertiaryContainer: Color = MyColorLightTokens.TertiaryContainer,
  onTertiaryContainer: Color = MyColorLightTokens.OnTertiaryContainer,
  background: Color = MyColorLightTokens.Background,
  onBackground: Color = MyColorLightTokens.OnBackground,
  surface: Color = MyColorLightTokens.Surface,
  onSurface: Color = MyColorLightTokens.OnSurface,
  surfaceVariant: Color = MyColorLightTokens.SurfaceVariant,
  onSurfaceVariant: Color = MyColorLightTokens.OnSurfaceVariant,
  surfaceTint: Color = primary,
  inverseSurface: Color = MyColorLightTokens.InverseSurface,
  inverseOnSurface: Color = MyColorLightTokens.InverseOnSurface,
  error: Color = MyColorLightTokens.Error,
  onError: Color = MyColorLightTokens.OnError,
  errorContainer: Color = MyColorLightTokens.ErrorContainer,
  onErrorContainer: Color = MyColorLightTokens.OnErrorContainer,
  outline: Color = MyColorLightTokens.Outline,
  outlineVariant: Color = MyColorLightTokens.OutlineVariant,
  scrim: Color = MyColorLightTokens.Scrim,
  surfaceBright: Color = MyColorLightTokens.SurfaceBright,
  surfaceDim: Color = MyColorLightTokens.SurfaceDim,
  surfaceContainer: Color = MyColorLightTokens.SurfaceContainer,
  surfaceContainerHigh: Color = MyColorLightTokens.SurfaceContainerHigh,
  surfaceContainerHighest: Color = MyColorLightTokens.SurfaceContainerHighest,
  surfaceContainerLow: Color = MyColorLightTokens.SurfaceContainerLow,
  surfaceContainerLowest: Color = MyColorLightTokens.SurfaceContainerLowest,
): ColorScheme =
  ColorScheme(
    primary = primary,
    onPrimary = onPrimary,
    primaryContainer = primaryContainer,
    onPrimaryContainer = onPrimaryContainer,
    inversePrimary = inversePrimary,
    secondary = secondary,
    onSecondary = onSecondary,
    secondaryContainer = secondaryContainer,
    onSecondaryContainer = onSecondaryContainer,
    tertiary = tertiary,
    onTertiary = onTertiary,
    tertiaryContainer = tertiaryContainer,
    onTertiaryContainer = onTertiaryContainer,
    background = background,
    onBackground = onBackground,
    surface = surface,
    onSurface = onSurface,
    surfaceVariant = surfaceVariant,
    onSurfaceVariant = onSurfaceVariant,
    surfaceTint = surfaceTint,
    inverseSurface = inverseSurface,
    inverseOnSurface = inverseOnSurface,
    error = error,
    onError = onError,
    errorContainer = errorContainer,
    onErrorContainer = onErrorContainer,
    outline = outline,
    outlineVariant = outlineVariant,
    scrim = scrim,
    surfaceBright = surfaceBright,
    surfaceDim = surfaceDim,
    surfaceContainer = surfaceContainer,
    surfaceContainerHigh = surfaceContainerHigh,
    surfaceContainerHighest = surfaceContainerHighest,
    surfaceContainerLow = surfaceContainerLow,
    surfaceContainerLowest = surfaceContainerLowest,
  )

@Composable
fun myDarkColorScheme(
  primary: Color = MyColorDarkTokens.Primary,
  onPrimary: Color = MyColorDarkTokens.OnPrimary,
  primaryContainer: Color = MyColorDarkTokens.PrimaryContainer,
  onPrimaryContainer: Color = MyColorDarkTokens.OnPrimaryContainer,
  inversePrimary: Color = MyColorDarkTokens.InversePrimary,
  secondary: Color = MyColorDarkTokens.Secondary,
  onSecondary: Color = MyColorDarkTokens.OnSecondary,
  secondaryContainer: Color = MyColorDarkTokens.SecondaryContainer,
  onSecondaryContainer: Color = MyColorDarkTokens.OnSecondaryContainer,
  tertiary: Color = MyColorDarkTokens.Tertiary,
  onTertiary: Color = MyColorDarkTokens.OnTertiary,
  tertiaryContainer: Color = MyColorDarkTokens.TertiaryContainer,
  onTertiaryContainer: Color = MyColorDarkTokens.OnTertiaryContainer,
  background: Color = MyColorDarkTokens.Background,
  onBackground: Color = MyColorDarkTokens.OnBackground,
  surface: Color = MyColorDarkTokens.Surface,
  onSurface: Color = MyColorDarkTokens.OnSurface,
  surfaceVariant: Color = MyColorDarkTokens.SurfaceVariant,
  onSurfaceVariant: Color = MyColorDarkTokens.OnSurfaceVariant,
  surfaceTint: Color = primary,
  inverseSurface: Color = MyColorDarkTokens.InverseSurface,
  inverseOnSurface: Color = MyColorDarkTokens.InverseOnSurface,
  error: Color = MyColorDarkTokens.Error,
  onError: Color = MyColorDarkTokens.OnError,
  errorContainer: Color = MyColorDarkTokens.ErrorContainer,
  onErrorContainer: Color = MyColorDarkTokens.OnErrorContainer,
  outline: Color = MyColorDarkTokens.Outline,
  outlineVariant: Color = MyColorDarkTokens.OutlineVariant,
  scrim: Color = MyColorDarkTokens.Scrim,
  surfaceBright: Color = MyColorDarkTokens.SurfaceBright,
  surfaceDim: Color = MyColorDarkTokens.SurfaceDim,
  surfaceContainer: Color = MyColorDarkTokens.SurfaceContainer,
  surfaceContainerHigh: Color = MyColorDarkTokens.SurfaceContainerHigh,
  surfaceContainerHighest: Color = MyColorDarkTokens.SurfaceContainerHighest,
  surfaceContainerLow: Color = MyColorDarkTokens.SurfaceContainerLow,
  surfaceContainerLowest: Color = MyColorDarkTokens.SurfaceContainerLowest,
): ColorScheme =
  ColorScheme(
    primary = primary,
    onPrimary = onPrimary,
    primaryContainer = primaryContainer,
    onPrimaryContainer = onPrimaryContainer,
    inversePrimary = inversePrimary,
    secondary = secondary,
    onSecondary = onSecondary,
    secondaryContainer = secondaryContainer,
    onSecondaryContainer = onSecondaryContainer,
    tertiary = tertiary,
    onTertiary = onTertiary,
    tertiaryContainer = tertiaryContainer,
    onTertiaryContainer = onTertiaryContainer,
    background = background,
    onBackground = onBackground,
    surface = surface,
    onSurface = onSurface,
    surfaceVariant = surfaceVariant,
    onSurfaceVariant = onSurfaceVariant,
    surfaceTint = surfaceTint,
    inverseSurface = inverseSurface,
    inverseOnSurface = inverseOnSurface,
    error = error,
    onError = onError,
    errorContainer = errorContainer,
    onErrorContainer = onErrorContainer,
    outline = outline,
    outlineVariant = outlineVariant,
    scrim = scrim,
    surfaceBright = surfaceBright,
    surfaceDim = surfaceDim,
    surfaceContainer = surfaceContainer,
    surfaceContainerHigh = surfaceContainerHigh,
    surfaceContainerHighest = surfaceContainerHighest,
    surfaceContainerLow = surfaceContainerLow,
    surfaceContainerLowest = surfaceContainerLowest,
  )


internal object MyColorDarkTokens {
  val Background = MyPaletteTokens.Neutral10
  val Error = MyPaletteTokens.Error80
  val ErrorContainer = MyPaletteTokens.Error30
  val InverseOnSurface = MyPaletteTokens.Neutral20
  val InversePrimary = MyPaletteTokens.Primary40
  val InverseSurface = MyPaletteTokens.Neutral90
  val OnBackground = MyPaletteTokens.Neutral90
  val OnError = MyPaletteTokens.Error20
  val OnErrorContainer = MyPaletteTokens.Error90
  val OnPrimary = MyPaletteTokens.Primary20
  val OnPrimaryContainer = MyPaletteTokens.Primary90
  val OnSecondary = MyPaletteTokens.Secondary20
  val OnSecondaryContainer = MyPaletteTokens.Secondary90
  val OnSurface = MyPaletteTokens.Neutral90
  val OnSurfaceVariant = MyPaletteTokens.NeutralVariant80
  val OnTertiary = MyPaletteTokens.Tertiary20
  val OnTertiaryContainer = MyPaletteTokens.Tertiary90
  val Outline = MyPaletteTokens.NeutralVariant60
  val OutlineVariant = MyPaletteTokens.NeutralVariant30
  val Primary = MyPaletteTokens.Primary80
  val PrimaryContainer = MyPaletteTokens.Primary30
  val Scrim = MyPaletteTokens.Neutral0
  val Secondary = MyPaletteTokens.Secondary80
  val SecondaryContainer = MyPaletteTokens.Secondary30
  val Surface = MyPaletteTokens.Neutral10
  val SurfaceTint = Primary
  val SurfaceVariant = MyPaletteTokens.NeutralVariant30
  val Tertiary = MyPaletteTokens.Tertiary80
  val TertiaryContainer = MyPaletteTokens.Tertiary30
  val SurfaceBright = MyPaletteTokens.Neutral95
  val SurfaceDim = MyPaletteTokens.Neutral80
  val SurfaceContainer = MyPaletteTokens.Neutral95
  val SurfaceContainerHigh = MyPaletteTokens.Neutral90
  val SurfaceContainerHighest = MyPaletteTokens.Neutral80
  val SurfaceContainerLow = MyPaletteTokens.Neutral95
  val SurfaceContainerLowest = MyPaletteTokens.Neutral100
}

internal object MyColorLightTokens {
  private val paletteTokens = BluePaletteTokens
  val Background = paletteTokens.Neutral99
  val Error = paletteTokens.Error40
  val ErrorContainer = paletteTokens.Error90
  val InverseOnSurface = paletteTokens.Neutral95
  val InversePrimary = paletteTokens.Primary80
  val InverseSurface = paletteTokens.Neutral20
  val OnBackground = paletteTokens.Neutral10
  val OnError = paletteTokens.Error100
  val OnErrorContainer = paletteTokens.Error10
  val OnPrimary = paletteTokens.Primary100
  val OnPrimaryContainer = paletteTokens.Primary10
  val OnSecondary = paletteTokens.Secondary100
  val OnSecondaryContainer = paletteTokens.Secondary10
  val OnSurface = paletteTokens.Neutral10
  val OnSurfaceVariant = paletteTokens.NeutralVariant30
  val OnTertiary = paletteTokens.Tertiary100
  val OnTertiaryContainer = paletteTokens.Tertiary10
  val Outline = paletteTokens.NeutralVariant50
  val OutlineVariant = paletteTokens.NeutralVariant80
  val Primary = paletteTokens.Primary40
  val PrimaryContainer = paletteTokens.Primary90
  val Scrim = paletteTokens.Neutral0
  val Secondary = paletteTokens.Secondary40
  val SecondaryContainer = paletteTokens.Secondary90
  val Surface = paletteTokens.Neutral99
  val SurfaceTint = Primary
  val SurfaceVariant = paletteTokens.NeutralVariant90
  val Tertiary = paletteTokens.Tertiary40
  val TertiaryContainer = paletteTokens.Tertiary90
  val SurfaceBright = paletteTokens.Neutral95
  val SurfaceDim = paletteTokens.Neutral80
  val SurfaceContainer = paletteTokens.Neutral95
  val SurfaceContainerHigh = paletteTokens.Neutral90
  val SurfaceContainerHighest = paletteTokens.Neutral80
  val SurfaceContainerLow = paletteTokens.Neutral95
  val SurfaceContainerLowest = paletteTokens.Neutral100
}

internal object BluePaletteTokens {
  val Black = Color(red = 0, green = 0, blue = 0)
  val Error0 = Color(red = 0, green = 0, blue = 0)
  val Error10 = Color(red = 65, green = 14, blue = 11)
  val Error100 = Color(red = 255, green = 255, blue = 255)
  val Error20 = Color(red = 96, green = 20, blue = 16)
  val Error30 = Color(red = 140, green = 29, blue = 24)
  val Error40 = Color(red = 179, green = 38, blue = 30)
  val Error50 = Color(red = 220, green = 54, blue = 46)
  val Error60 = Color(red = 228, green = 105, blue = 98)
  val Error70 = Color(red = 236, green = 146, blue = 142)
  val Error80 = Color(red = 242, green = 184, blue = 181)
  val Error90 = Color(red = 249, green = 222, blue = 220)
  val Error95 = Color(red = 252, green = 238, blue = 238)
  val Error99 = Color(red = 255, green = 251, blue = 249)
  val Neutral0 = Color(red = 0, green = 0, blue = 0)
  val Neutral10 = Color(red = 28, green = 27, blue = 31)
  val Neutral100 = Color(red = 255, green = 255, blue = 255)
  val Neutral20 = Color(red = 49, green = 48, blue = 51)
  val Neutral30 = Color(red = 72, green = 70, blue = 73)
  val Neutral40 = Color(red = 96, green = 93, blue = 98)
  val Neutral50 = Color(red = 120, green = 117, blue = 121)
  val Neutral60 = Color(red = 147, green = 144, blue = 148)
  val Neutral70 = Color(red = 174, green = 170, blue = 174)
  val Neutral80 = Color(red = 201, green = 197, blue = 202)
  val Neutral90 = Color(red = 230, green = 225, blue = 229)
  val Neutral95 = Color(red = 240, green = 242, blue = 245, alpha = 255)
  val Neutral99 = Color(red = 250, green = 251, blue = 253, alpha = 255)
  val NeutralVariant0 = Color(red = 0, green = 0, blue = 0)
  val NeutralVariant10 = Color(red = 26, green = 30, blue = 34, alpha = 255)
  val NeutralVariant100 = Color(red = 255, green = 255, blue = 255)
  val NeutralVariant20 = Color(red = 47, green = 50, blue = 54, alpha = 255)
  val NeutralVariant30 = Color(red = 69, green = 73, blue = 78, alpha = 255)
  val NeutralVariant40 = Color(red = 92, green = 96, blue = 100, alpha = 255)
  val NeutralVariant50 = Color(red = 114, green = 119, blue = 124, alpha = 255)
  val NeutralVariant60 = Color(red = 143, green = 148, blue = 153, alpha = 255)
  val NeutralVariant70 = Color(red = 166, green = 171, blue = 177, alpha = 255)
  val NeutralVariant80 = Color(red = 193, green = 199, blue = 206, alpha = 255)
  val NeutralVariant90 = Color(red = 225, green = 230, blue = 236, alpha = 255)
  val NeutralVariant95 = Color(red = 238, green = 244, blue = 250, alpha = 255)
  val NeutralVariant99 = Color(red = 249, green = 252, blue = 255, alpha = 255)
  val Primary0 = Color(red = 0, green = 0, blue = 0)
  val Primary10 = Color(red = 1, green = 43, blue = 94, alpha = 255)
  val Primary100 = Color(red = 255, green = 255, blue = 255)
  val Primary20 = Color(red = 29, green = 66, blue = 111, alpha = 255)
  val Primary30 = Color(red = 53, green = 91, blue = 136, alpha = 255)
  val Primary40 = Color(red = 76, green = 134, blue = 203, alpha = 255)
  val Primary50 = Color(red = 101, green = 139, blue = 185, alpha = 255)
  val Primary60 = Color(red = 128, green = 167, blue = 214, alpha = 255)
  val Primary70 = Color(red = 158, green = 200, blue = 250, alpha = 255)
  val Primary80 = Color(red = 191, green = 220, blue = 255, alpha = 255)
  val Primary90 = Color(red = 223, green = 237, blue = 255, alpha = 255)
  val Primary95 = Color(red = 246, green = 237, blue = 255)
  val Primary99 = Color(red = 255, green = 251, blue = 254)
  val Secondary0 = Color(red = 0, green = 0, blue = 0)
  val Secondary10 = Color(red = 25, green = 33, blue = 43, alpha = 255)
  val Secondary100 = Color(red = 255, green = 255, blue = 255)
  val Secondary20 = Color(red = 46, green = 55, blue = 66, alpha = 255)
  val Secondary30 = Color(red = 70, green = 79, blue = 90, alpha = 255)
  val Secondary40 = Color(red = 92, green = 102, blue = 114, alpha = 255)
  val Secondary50 = Color(red = 113, green = 124, blue = 136, alpha = 255)
  val Secondary60 = Color(red = 144, green = 155, blue = 168, alpha = 255)
  val Secondary70 = Color(red = 167, green = 179, blue = 192, alpha = 255)
  val Secondary80 = Color(red = 195, green = 207, blue = 221, alpha = 255)
  val Secondary90 = Color(red = 223, green = 234, blue = 248, alpha = 255)
  val Secondary95 = Color(red = 236, green = 244, blue = 253, alpha = 255)
  val Secondary99 = Color(red = 248, green = 250, blue = 252, alpha = 255)
  val Tertiary0 = Color(red = 0, green = 0, blue = 0)
  val Tertiary10 = Color(red = 49, green = 17, blue = 29)
  val Tertiary100 = Color(red = 255, green = 255, blue = 255)
  val Tertiary20 = Color(red = 73, green = 37, blue = 50)
  val Tertiary30 = Color(red = 99, green = 59, blue = 72)
  val Tertiary40 = Color(red = 81, green = 101, blue = 124, alpha = 255)
  val Tertiary50 = Color(red = 152, green = 105, blue = 119)
  val Tertiary60 = Color(red = 181, green = 131, blue = 146)
  val Tertiary70 = Color(red = 210, green = 157, blue = 172)
  val Tertiary80 = Color(red = 239, green = 184, blue = 200)
  val Tertiary90 = Color(red = 217, green = 234, blue = 255, alpha = 255)
  val Tertiary95 = Color(red = 255, green = 236, blue = 241)
  val Tertiary99 = Color(red = 255, green = 251, blue = 250)
  val White = Color(red = 255, green = 255, blue = 255)
}

internal object MyPaletteTokens {
  val Black = Color(red = 0, green = 0, blue = 0)
  val Error0 = Color(red = 0, green = 0, blue = 0)
  val Error10 = Color(red = 65, green = 14, blue = 11)
  val Error100 = Color(red = 255, green = 255, blue = 255)
  val Error20 = Color(red = 96, green = 20, blue = 16)
  val Error30 = Color(red = 140, green = 29, blue = 24)
  val Error40 = Color(red = 179, green = 38, blue = 30)
  val Error50 = Color(red = 220, green = 54, blue = 46)
  val Error60 = Color(red = 228, green = 105, blue = 98)
  val Error70 = Color(red = 236, green = 146, blue = 142)
  val Error80 = Color(red = 242, green = 184, blue = 181)
  val Error90 = Color(red = 249, green = 222, blue = 220)
  val Error95 = Color(red = 252, green = 238, blue = 238)
  val Error99 = Color(red = 255, green = 251, blue = 249)
  val Neutral0 = Color(red = 0, green = 0, blue = 0)
  val Neutral10 = Color(red = 28, green = 27, blue = 31)
  val Neutral100 = Color(red = 255, green = 255, blue = 255)
  val Neutral20 = Color(red = 49, green = 48, blue = 51)
  val Neutral30 = Color(red = 72, green = 70, blue = 73)
  val Neutral40 = Color(red = 96, green = 93, blue = 98)
  val Neutral50 = Color(red = 120, green = 117, blue = 121)
  val Neutral60 = Color(red = 147, green = 144, blue = 148)
  val Neutral70 = Color(red = 174, green = 170, blue = 174)
  val Neutral80 = Color(red = 201, green = 197, blue = 202)
  val Neutral90 = Color(red = 230, green = 225, blue = 229)
  val Neutral95 = Color(red = 244, green = 239, blue = 244)
  val Neutral99 = Color(red = 255, green = 251, blue = 254)
  val NeutralVariant0 = Color(red = 0, green = 0, blue = 0)
  val NeutralVariant10 = Color(red = 29, green = 26, blue = 34)
  val NeutralVariant100 = Color(red = 255, green = 255, blue = 255)
  val NeutralVariant20 = Color(red = 50, green = 47, blue = 55)
  val NeutralVariant30 = Color(red = 73, green = 69, blue = 79)
  val NeutralVariant40 = Color(red = 96, green = 93, blue = 102)
  val NeutralVariant50 = Color(red = 121, green = 116, blue = 126)
  val NeutralVariant60 = Color(red = 147, green = 143, blue = 153)
  val NeutralVariant70 = Color(red = 174, green = 169, blue = 180)
  val NeutralVariant80 = Color(red = 202, green = 196, blue = 208)
  val NeutralVariant90 = Color(red = 231, green = 224, blue = 236)
  val NeutralVariant95 = Color(red = 245, green = 238, blue = 250)
  val NeutralVariant99 = Color(red = 255, green = 251, blue = 254)
  val Primary0 = Color(red = 0, green = 0, blue = 0)
  val Primary10 = Color(red = 33, green = 0, blue = 93)
  val Primary100 = Color(red = 255, green = 255, blue = 255)
  val Primary20 = Color(red = 56, green = 30, blue = 114)
  val Primary30 = Color(red = 79, green = 55, blue = 139)
  val Primary40 = Color(red = 103, green = 80, blue = 164)
  val Primary50 = Color(red = 127, green = 103, blue = 190)
  val Primary60 = Color(red = 154, green = 130, blue = 219)
  val Primary70 = Color(red = 182, green = 157, blue = 248)
  val Primary80 = Color(red = 208, green = 188, blue = 255)
  val Primary90 = Color(red = 234, green = 221, blue = 255)
  val Primary95 = Color(red = 246, green = 237, blue = 255)
  val Primary99 = Color(red = 255, green = 251, blue = 254)
  val Secondary0 = Color(red = 0, green = 0, blue = 0)
  val Secondary10 = Color(red = 29, green = 25, blue = 43)
  val Secondary100 = Color(red = 255, green = 255, blue = 255)
  val Secondary20 = Color(red = 51, green = 45, blue = 65)
  val Secondary30 = Color(red = 74, green = 68, blue = 88)
  val Secondary40 = Color(red = 98, green = 91, blue = 113)
  val Secondary50 = Color(red = 122, green = 114, blue = 137)
  val Secondary60 = Color(red = 149, green = 141, blue = 165)
  val Secondary70 = Color(red = 176, green = 167, blue = 192)
  val Secondary80 = Color(red = 204, green = 194, blue = 220)
  val Secondary90 = Color(red = 232, green = 222, blue = 248)
  val Secondary95 = Color(red = 246, green = 237, blue = 255)
  val Secondary99 = Color(red = 255, green = 251, blue = 254)
  val Tertiary0 = Color(red = 0, green = 0, blue = 0)
  val Tertiary10 = Color(red = 49, green = 17, blue = 29)
  val Tertiary100 = Color(red = 255, green = 255, blue = 255)
  val Tertiary20 = Color(red = 73, green = 37, blue = 50)
  val Tertiary30 = Color(red = 99, green = 59, blue = 72)
  val Tertiary40 = Color(red = 125, green = 82, blue = 96)
  val Tertiary50 = Color(red = 152, green = 105, blue = 119)
  val Tertiary60 = Color(red = 181, green = 131, blue = 146)
  val Tertiary70 = Color(red = 210, green = 157, blue = 172)
  val Tertiary80 = Color(red = 239, green = 184, blue = 200)
  val Tertiary90 = Color(red = 255, green = 216, blue = 228)
  val Tertiary95 = Color(red = 255, green = 236, blue = 241)
  val Tertiary99 = Color(red = 255, green = 251, blue = 250)
  val White = Color(red = 255, green = 255, blue = 255)
}

internal object VioletColorLightTokens {
  val Background = Color(red = 255, green = 251, blue = 254)
  val Error = Color(red = 179, green = 38, blue = 30)
  val ErrorContainer = Color(red = 249, green = 222, blue = 220)
  val InverseOnSurface = Color(red = 244, green = 239, blue = 244)
  val InversePrimary = Color(red = 208, green = 188, blue = 255)
  val InverseSurface = Color(red = 49, green = 48, blue = 51)
  val OnBackground = Color(red = 28, green = 27, blue = 31)
  val OnError = Color(red = 255, green = 255, blue = 255)
  val OnErrorContainer = Color(red = 65, green = 14, blue = 11)
  val OnPrimary = Color(red = 255, green = 255, blue = 255)
  val OnPrimaryContainer = Color(red = 33, green = 0, blue = 93)
  val OnSecondary = Color(red = 255, green = 255, blue = 255)
  val OnSecondaryContainer = Color(red = 29, green = 25, blue = 43)
  val OnSurface = Color(red = 28, green = 27, blue = 31)
  val OnSurfaceVariant = Color(red = 73, green = 69, blue = 79)
  val OnTertiary = Color(red = 255, green = 255, blue = 255)
  val OnTertiaryContainer = Color(red = 49, green = 17, blue = 29)
  val Outline = Color(red = 121, green = 116, blue = 126)
  val OutlineVariant = Color(red = 202, green = 196, blue = 208)
  val Primary = Color(red = 103, green = 80, blue = 164)
  val PrimaryContainer = Color(red = 234, green = 221, blue = 255)
  val Scrim = Color(red = 0, green = 0, blue = 0)
  val Secondary = Color(red = 98, green = 91, blue = 113)
  val SecondaryContainer = Color(red = 232, green = 222, blue = 248)
  val Surface = Color(red = 255, green = 251, blue = 254)
  val SurfaceTint = Primary
  val SurfaceVariant = Color(red = 231, green = 224, blue = 236)
  val Tertiary = Color(red = 125, green = 82, blue = 96)
  val TertiaryContainer = Color(red = 255, green = 216, blue = 228)
}

internal object VioletColorDarkTokens {
  val Background = Color(red = 28, green = 27, blue = 31)
  val Error = Color(red = 242, green = 184, blue = 181)
  val ErrorContainer = Color(red = 140, green = 29, blue = 24)
  val InverseOnSurface = Color(red = 49, green = 48, blue = 51)
  val InversePrimary = Color(red = 103, green = 80, blue = 164)
  val InverseSurface = Color(red = 230, green = 225, blue = 229)
  val OnBackground = Color(red = 230, green = 225, blue = 229)
  val OnError = Color(red = 96, green = 20, blue = 16)
  val OnErrorContainer = Color(red = 249, green = 222, blue = 220)
  val OnPrimary = Color(red = 56, green = 30, blue = 114)
  val OnPrimaryContainer = Color(red = 234, green = 221, blue = 255)
  val OnSecondary = Color(red = 51, green = 45, blue = 65)
  val OnSecondaryContainer = Color(red = 232, green = 222, blue = 248)
  val OnSurface = Color(red = 230, green = 225, blue = 229)
  val OnSurfaceVariant = Color(red = 202, green = 196, blue = 208)
  val OnTertiary = Color(red = 73, green = 37, blue = 50)
  val OnTertiaryContainer = Color(red = 255, green = 216, blue = 228)
  val Outline = Color(red = 147, green = 143, blue = 153)
  val OutlineVariant = Color(red = 73, green = 69, blue = 79)
  val Primary = Color(red = 208, green = 188, blue = 255)
  val PrimaryContainer = Color(red = 79, green = 55, blue = 139)
  val Scrim = Color(red = 0, green = 0, blue = 0)
  val Secondary = Color(red = 204, green = 194, blue = 220)
  val SecondaryContainer = Color(red = 74, green = 68, blue = 88)
  val Surface = Color(red = 28, green = 27, blue = 31)
  val SurfaceTint = Primary
  val SurfaceVariant = Color(red = 73, green = 69, blue = 79)
  val Tertiary = Color(red = 239, green = 184, blue = 200)
  val TertiaryContainer = Color(red = 99, green = 59, blue = 72)
}

internal object BlueColorLightTokens {
  val Background = Color(red = 255, green = 251, blue = 254)
  val Error = Color(red = 179, green = 38, blue = 30)
  val ErrorContainer = Color(red = 249, green = 222, blue = 220)
  val InverseOnSurface = Color(red = 244, green = 239, blue = 244)
  val InversePrimary = Color(red = 208, green = 188, blue = 255)
  val InverseSurface = Color(red = 49, green = 48, blue = 51)
  val OnBackground = Color(red = 28, green = 27, blue = 31)
  val OnError = Color(red = 255, green = 255, blue = 255)
  val OnErrorContainer = Color(red = 65, green = 14, blue = 11)
  val OnPrimary = Color(red = 255, green = 255, blue = 255)
  val OnPrimaryContainer = Color(red = 33, green = 0, blue = 93)
  val OnSecondary = Color(red = 255, green = 255, blue = 255)
  val OnSecondaryContainer = Color(red = 29, green = 25, blue = 43)
  val OnSurface = Color(red = 28, green = 27, blue = 31)
  val OnSurfaceVariant = Color(red = 73, green = 69, blue = 79)
  val OnTertiary = Color(red = 255, green = 255, blue = 255)
  val OnTertiaryContainer = Color(red = 49, green = 17, blue = 29)
  val Outline = Color(red = 121, green = 116, blue = 126)
  val OutlineVariant = Color(red = 202, green = 196, blue = 208)
  val Primary = Color(red = 76, green = 134, blue = 203, alpha = 255)
  val PrimaryContainer = Color(red = 234, green = 221, blue = 255)
  val Scrim = Color(red = 0, green = 0, blue = 0)
  val Secondary = Color(red = 98, green = 91, blue = 113)
  val SecondaryContainer = Color(red = 232, green = 222, blue = 248)
  val Surface = Color(red = 255, green = 251, blue = 254)
  val SurfaceTint = Primary
  val SurfaceVariant = Color(red = 231, green = 224, blue = 236)
  val Tertiary = Color(red = 125, green = 82, blue = 96)
  val TertiaryContainer = Color(red = 255, green = 216, blue = 228)
}

internal object BlueColorDarkTokens {
  val Background = Color(red = 28, green = 27, blue = 31)
  val Error = Color(red = 242, green = 184, blue = 181)
  val ErrorContainer = Color(red = 140, green = 29, blue = 24)
  val InverseOnSurface = Color(red = 49, green = 48, blue = 51)
  val InversePrimary = Color(red = 103, green = 80, blue = 164)
  val InverseSurface = Color(red = 230, green = 225, blue = 229)
  val OnBackground = Color(red = 230, green = 225, blue = 229)
  val OnError = Color(red = 96, green = 20, blue = 16)
  val OnErrorContainer = Color(red = 249, green = 222, blue = 220)
  val OnPrimary = Color(red = 56, green = 30, blue = 114)
  val OnPrimaryContainer = Color(red = 234, green = 221, blue = 255)
  val OnSecondary = Color(red = 51, green = 45, blue = 65)
  val OnSecondaryContainer = Color(red = 231, green = 238, blue = 245, alpha = 255)
  val OnSurface = Color(red = 230, green = 225, blue = 229)
  val OnSurfaceVariant = Color(red = 202, green = 196, blue = 208)
  val OnTertiary = Color(red = 73, green = 37, blue = 50)
  val OnTertiaryContainer = Color(red = 255, green = 216, blue = 228)
  val Outline = Color(red = 147, green = 143, blue = 153)
  val OutlineVariant = Color(red = 73, green = 69, blue = 79)
  val Primary = Color(red = 76, green = 134, blue = 203, alpha = 255)
  val PrimaryContainer = Color(red = 79, green = 55, blue = 139)
  val Scrim = Color(red = 0, green = 0, blue = 0)
  val Secondary = Color(red = 204, green = 194, blue = 220)
  val SecondaryContainer = Color(red = 74, green = 68, blue = 88)
  val Surface = Color(red = 28, green = 27, blue = 31)
  val SurfaceTint = Primary
  val SurfaceVariant = Color(red = 73, green = 69, blue = 79)
  val Tertiary = Color(red = 239, green = 184, blue = 200)
  val TertiaryContainer = Color(red = 99, green = 59, blue = 72)
}
