package com.goodwy.audiobook.injection

import android.app.Application
import android.os.Build
import android.webkit.WebView
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatDelegate
import com.jakewharton.threetenabp.AndroidThreeTen
import com.squareup.picasso.Picasso
import de.paulwoitaschek.flowpref.Pref
import com.goodwy.audiobook.BuildConfig
import com.goodwy.audiobook.common.pref.PrefKeys
import com.goodwy.audiobook.crashreporting.CrashLoggingTree
import com.goodwy.audiobook.crashreporting.CrashReporter
import com.goodwy.audiobook.features.BookAdder
import com.goodwy.audiobook.features.settings.SettingsViewModel
import com.goodwy.audiobook.features.widget.TriggerWidgetOnChange
import com.goodwy.audiobook.misc.DARK_THEME_SETTABLE
import com.goodwy.audiobook.misc.StrictModeInit
import com.goodwy.audiobook.playback.androidauto.AndroidAutoConnectedReceiver
import com.goodwy.audiobook.playback.di.PlaybackComponent
import com.goodwy.audiobook.playback.di.PlaybackComponentFactoryProvider
import com.jaredrummler.cyanea.Cyanea
import com.jaredrummler.cyanea.app.BaseCyaneaActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class App : Application(), PlaybackComponentFactoryProvider,
  BaseCyaneaActivity {

  @Inject
  lateinit var viewModel: SettingsViewModel
  @Inject
  lateinit var bookAdder: BookAdder
  @Inject
  lateinit var triggerWidgetOnChange: TriggerWidgetOnChange
  @Inject
  lateinit var autoConnectedReceiver: AndroidAutoConnectedReceiver
  @field:[Inject Named(PrefKeys.DARK_THEME)]
  lateinit var useDarkTheme: Pref<Boolean>
  @field:[Inject Named(PrefKeys.DARK_THEME)]
  lateinit var darkThemePref: Pref<Boolean>

  override fun onCreate() {
    super.onCreate()

    Cyanea.init(this, resources) /*initialize Cyanea*/

    if (BuildConfig.DEBUG) StrictModeInit.init()

    if (!alreadyCreated) {
      // robolectric creates multiple instances of the Application so we need to prevent
      // additional initializations
      alreadyCreated = true
      Picasso.setSingletonInstance(Picasso.Builder(this).build())
    }

    CrashReporter.init(this)

    GlobalScope.launch {
      if (BuildConfig.DEBUG) {
        Timber.plant(Timber.DebugTree())
      } else {
        Timber.plant(CrashLoggingTree())
      }
    }

    AndroidThreeTen.init(this)

    appComponent = AppComponent.factory()
      .create(this)
    appComponent.inject(this)

    if (DARK_THEME_SETTABLE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      // instantiating a web-view for the first time changes the day night theme.
      // therefore we work around by creating a webview first.
      // https://issuetracker.google.com/issues/37124582
      WebView(this)
    }

    if (DARK_THEME_SETTABLE) {
      @Suppress("CheckResult")
      GlobalScope.launch(Dispatchers.Main) {
        useDarkTheme.flow
          .distinctUntilChanged()
          .collect { useDarkTheme ->
            val nightMode = if (useDarkTheme) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            AppCompatDelegate.setDefaultNightMode(nightMode)
          }
      }
    }

    bookAdder.scanForFiles()

    autoConnectedReceiver.register(this)

    triggerWidgetOnChange.init()

    // todo Lite
   /* val backgroundLight: Int = android.graphics.Color.parseColor("#FFFAFAFA")
    if (darkThemePref.value) (
      cyanea.edit {
        background(backgroundLight)
        backgroundLight(backgroundLight)
        backgroundLightLighter(backgroundLight) /*dialogs*/
        backgroundLightDarker(backgroundLight)
        navigationBar(backgroundLight)
        baseTheme(Cyanea.BaseTheme.LIGHT)
      }
      )
    if (darkThemePref.value) (
      viewModel.toggleDarkTheme()
      )*/
  }

  override fun factory(): PlaybackComponent.Factory {
    return appComponent.playbackComponentFactory()
  }

  companion object {

    private var alreadyCreated = false
  }
}

lateinit var appComponent: AppComponent
  @VisibleForTesting set
