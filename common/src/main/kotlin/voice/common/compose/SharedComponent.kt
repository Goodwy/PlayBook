package voice.common.compose

import com.squareup.anvil.annotations.ContributesTo
import voice.common.AppScope
import voice.common.pref.PrefKeys
import voice.pref.Pref
import javax.inject.Named

@ContributesTo(AppScope::class)
interface SharedComponent {

  @get:[
  Named(PrefKeys.DARK_THEME)
  ]
  val useDarkTheme: Pref<Boolean>

  @get:[
  Named(PrefKeys.THEME)
  ]
  val themePref: Pref<Int>

  @get:[
  Named(PrefKeys.COLOR_THEME)
  ]
  val colorThemePreference: Pref<Int>

  @get:[
  Named(PrefKeys.TRANSPARENT_NAVIGATION)
  ]
  val useTransparentNavigation: Pref<Boolean>

  @get:[
  Named(PrefKeys.PADDING)
  ]
  val paddingPref: Pref<String>
}
