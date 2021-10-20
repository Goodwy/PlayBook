package com.goodwy.audiobooklite.injection

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import android.webkit.WebView
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.jakewharton.threetenabp.AndroidThreeTen
import com.squareup.picasso.Picasso
import de.paulwoitaschek.flowpref.Pref
import com.goodwy.audiobooklite.BuildConfig
import com.goodwy.audiobooklite.common.pref.PrefKeys
import com.goodwy.audiobooklite.crashreporting.CrashLoggingTree
import com.goodwy.audiobooklite.crashreporting.CrashReporter
import com.goodwy.audiobooklite.features.BaseActivity
import com.goodwy.audiobooklite.features.BookAdder
import com.goodwy.audiobooklite.features.settings.SettingsViewModel
import com.goodwy.audiobooklite.features.widget.TriggerWidgetOnChange
import com.goodwy.audiobooklite.misc.DARK_THEME_SETTABLE
import com.goodwy.audiobooklite.misc.StrictModeInit
import com.goodwy.audiobooklite.playback.androidauto.AndroidAutoConnectedReceiver
import com.goodwy.audiobooklite.playback.di.PlaybackComponent
import com.goodwy.audiobooklite.playback.di.PlaybackComponentFactoryProvider
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

  private val grey: Int = android.graphics.Color.parseColor("#FFE0E0E0")
  private val dark: Int = android.graphics.Color.parseColor("#FF272f35")

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
    val backgroundLight: Int = android.graphics.Color.parseColor("#FFFAFAFA")
    if (useDarkTheme.value) (
      cyanea.edit {
        background(backgroundLight)
        backgroundLight(backgroundLight)
        backgroundLightLighter(backgroundLight) /*dialogs*/
        backgroundLightDarker(backgroundLight)
        navigationBar(backgroundLight)
        baseTheme(Cyanea.BaseTheme.LIGHT)
        if (cyanea.primaryDark == dark) {primaryDark(grey)}
      }
      )
    if (useDarkTheme.value) (
      viewModel.toggleDarkTheme()
      )
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
