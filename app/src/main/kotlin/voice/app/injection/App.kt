package voice.app.injection

import android.app.Application
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatDelegate
import coil.Coil
import coil.ImageLoader
import com.google.android.material.color.DynamicColors
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import voice.app.features.widget.TriggerWidgetOnChange
import voice.app.scanner.MediaScanTrigger
import voice.common.DARK_THEME_SETTABLE
import voice.common.constants.THEME_DARK
import voice.common.constants.THEME_LIGHT
import voice.common.pref.PrefKeys
import voice.common.rootComponent
import voice.common.R as CommonR
import voice.pref.Pref
import javax.inject.Inject
import javax.inject.Named

class App : Application() {

  @Inject
  lateinit var mediaScanner: MediaScanTrigger

  @Inject
  lateinit var triggerWidgetOnChange: TriggerWidgetOnChange

  @field:[Inject Named(PrefKeys.DARK_THEME)]
  lateinit var useDarkTheme: Pref<Boolean>

  @field:[Inject Named(PrefKeys.THEME)]
  lateinit var themePref: Pref<Int>

  @field:[Inject Named(PrefKeys.PRO)]
  lateinit var isProPref: Pref<Boolean>

  @field:[Inject Named(PrefKeys.PRO_SUBS)]
  lateinit var isProSubsPref: Pref<Boolean>

  @field:[Inject Named(PrefKeys.PRO_RUSTORE)]
  lateinit var isProRuPref: Pref<Boolean>

  @field:[Inject Named(PrefKeys.PRO_NO_GP)]
  lateinit var isProNoGpPref: Pref<Boolean>

  override fun onCreate() {
    super.onCreate()

    Coil.setImageLoader(
      ImageLoader.Builder(this)
        .addLastModifiedToFileCacheKey(false)
        .build(),
    )

    DynamicColors.applyToActivitiesIfAvailable(this)

    appComponent = AppComponent.factory()
      .create(this)
    rootComponent = appComponent
    appComponent.inject(this)

    if (!isPro()) themePref.value = THEME_LIGHT

    RuStoreModule.install(this, themePref.value)

    if (DARK_THEME_SETTABLE) {
      MainScope().launch {
        themePref.flow
          .distinctUntilChanged()
          .collect { themePref ->
            val nightMode = if (themePref == THEME_DARK) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            AppCompatDelegate.setDefaultNightMode(nightMode)
          }
      }
    }

    mediaScanner.scan()

    triggerWidgetOnChange.init()
  }

  private fun isPro(): Boolean {
    val isProApp = resources.getBoolean(CommonR.bool.is_pro_app)
    return isProPref.value || isProSubsPref.value || isProRuPref.value || isProNoGpPref.value || isProApp
  }
}

lateinit var appComponent: AppComponent
  @VisibleForTesting set
