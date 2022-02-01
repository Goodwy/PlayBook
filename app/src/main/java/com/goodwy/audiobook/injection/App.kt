package com.goodwy.audiobook.injection

import android.annotation.SuppressLint
import android.app.Application
import android.content.res.Configuration
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
import com.goodwy.audiobook.features.BaseActivity
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
import java.util.Locale
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
  @field:[Inject Named(PrefKeys.USE_ENGLISH)]
  lateinit var useEnglishPref: Pref<Boolean>
  @field:[Inject Named(PrefKeys.PRO)]
  lateinit var isProPref: Pref<Boolean>

  @SuppressLint("ObsoleteSdkInt")
  override fun onCreate() {
    super.onCreate()

    Cyanea.init(this, resources) //initialize Cyanea

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

    //language ->
    var change = ""
    //val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
    //val language = sharedPreferences.getString("language", "bak")
    //if (language == "English") {
    if (useEnglishPref.value) {
      change="en"
    } else {
      change =""
    }
    BaseActivity.dLocale = Locale(change) //set any locale you want here
    //<- language

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
    if (!isProPref.value) {
      val grey: Int = android.graphics.Color.parseColor("#FFE0E0E0")
      val dark: Int = android.graphics.Color.parseColor("#FF272f35")
      val backgroundLight: Int = android.graphics.Color.parseColor("#FFFAFAFA")
      if (useDarkTheme.value) (
        cyanea.edit {
          background(backgroundLight)
          backgroundLight(backgroundLight)
          backgroundLightLighter(backgroundLight) /*dialogs*/
          backgroundLightDarker(backgroundLight)
          navigationBar(backgroundLight)
          baseTheme(Cyanea.BaseTheme.LIGHT)
          if (cyanea.primaryDark == dark) {
            primaryDark(grey)
          }
        }
        )
      if (useDarkTheme.value) (
        viewModel.toggleDarkTheme()
        )
    }
  }

  private fun isUsingSystemDarkTheme() = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_YES != 0

  fun setSystemTheme() {
    val grey: Int = android.graphics.Color.parseColor("#FFE0E0E0")
    val dark: Int = android.graphics.Color.parseColor("#FF272f35")
    val backgroundLight: Int = android.graphics.Color.parseColor("#FFFAFAFA")
    val backgroundDark: Int = android.graphics.Color.parseColor("#FF1E2225")
    if (isUsingSystemDarkTheme()) {
      AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
      useDarkTheme.value = true
      cyanea.edit {
        background(backgroundDark)
        backgroundDark(backgroundDark)
        backgroundDarkDarker(backgroundDark)
        backgroundDarkLighter(backgroundDark) /*dialogs*/
        navigationBar(backgroundDark)
        baseTheme(Cyanea.BaseTheme.DARK)
        if (cyanea.primaryDark == grey) {primaryDark(dark)}
      }
    } else {
      AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
      useDarkTheme.value = false
      cyanea.edit {
        background(backgroundLight)
        backgroundLight(backgroundLight)
        backgroundLightLighter(backgroundLight) /*dialogs*/
        backgroundLightDarker(backgroundLight)
        navigationBar(backgroundLight)
        baseTheme(Cyanea.BaseTheme.LIGHT)
        if (cyanea.primaryDark == dark) {primaryDark(grey)}
      }
    }
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
