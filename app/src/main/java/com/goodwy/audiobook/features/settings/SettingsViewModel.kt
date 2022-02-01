package com.goodwy.audiobook.features.settings

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import de.paulwoitaschek.flowpref.Pref
import com.goodwy.audiobook.common.pref.PrefKeys
import com.goodwy.audiobook.features.bookOverview.GridMode
import com.goodwy.audiobook.features.prefAppearanceUIPlayer.PrefAppearanceUIPlayerViewEffect
import com.goodwy.audiobook.misc.DARK_THEME_SETTABLE
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import javax.inject.Named

class SettingsViewModel
@Inject constructor(
  private val context: Context,
  @Named(PrefKeys.DARK_THEME)
  private val useDarkTheme: Pref<Boolean>,
  @Named(PrefKeys.RESUME_ON_REPLUG)
  private val resumeOnReplugPref: Pref<Boolean>,
  @Named(PrefKeys.AUTO_REWIND_AMOUNT)
  private val autoRewindAmountPref: Pref<Int>,
  @Named(PrefKeys.SEEK_TIME)
  private val seekTimePref: Pref<Int>,
  @Named(PrefKeys.CONTENTS_BUTTON_MODE)
  private val tintNavBar: Pref<Boolean>,
  @Named(PrefKeys.STATUS_BAR_MODE)
  private val statusBarModePref: Pref<Int>,
  @Named(PrefKeys.SHOW_RATING)
  private val showRatingPref: Pref<Boolean>,
  @Named(PrefKeys.SCREEN_ORIENTATION)
  private val screenOrientationPref: Pref<Boolean>,
  @Named(PrefKeys.GRID_AUTO)
  private val gridViewAutoPref: Pref<Boolean>,
  @Named(PrefKeys.GRID_MODE)
  private val gridModePref: Pref<GridMode>,
  @Named(PrefKeys.USE_ENGLISH)
  private val useEnglishPref: Pref<Boolean>,
  @Named(PrefKeys.COVER_RADIUS)
  private val coverRadiusPref: Pref<Int>
) {

  private val _viewEffects = BroadcastChannel<SettingsViewEffect>(1)
  val viewEffects: Flow<SettingsViewEffect> get() = _viewEffects.asFlow()

  fun viewState(): Flow<SettingsViewState> {
    return combine(
      useDarkTheme.flow,
      resumeOnReplugPref.flow,
      //autoRewindAmountPref.flow,
      //seekTimePref.flow,
      //tintNavBar.flow,
      statusBarModePref.flow,
      screenOrientationPref.flow,
      gridViewAutoPref.flow
    ) { useDarkTheme, resumeOnreplug, /*autoRewindAmount, seekTime, tintNavBar,*/ statusBarModePref, screenOrientationPref, gridViewAutoPref ->
      SettingsViewState(
        useDarkTheme = useDarkTheme,
        showDarkThemePref = DARK_THEME_SETTABLE,
        resumeOnReplug = resumeOnreplug,
        //seekTimeInSeconds = seekTime,
        //autoRewindInSeconds = autoRewindAmount,
        //tintNavBar = tintNavBar,
        statusBarModePref = statusBarModePref,
        screenOrientationPref = screenOrientationPref,
        gridViewAutoPref = gridViewAutoPref
      )
    }
  }

  fun toggleResumeOnReplug() {
    resumeOnReplugPref.value = !resumeOnReplugPref.value
  }

  fun toggleDarkTheme() {
    useDarkTheme.value = !useDarkTheme.value
  }

  /*fun changeSkipAmount() {
    _viewEffects.offer(SettingsViewEffect.ShowChangeSkipAmountDialog(seekTimePref.value))
  }

  fun changeAutoRewindAmount() {
    _viewEffects.offer(SettingsViewEffect.ShowChangeAutoRewindAmountDialog(seekTimePref.value))
  }*/

  fun toggleTintNavBar() {
    tintNavBar.value = !tintNavBar.value
  }

  fun changeCoverSettings() {
    _viewEffects.offer(SettingsViewEffect.ShowChangeCoverSettingsDialog(coverRadiusPref.value))
  }

  fun toggleGridViewAuto() {
    gridViewAutoPref.value = !gridViewAutoPref.value
    gridModePref.value = GridMode.FOLLOW_DEVICE
  }

  fun toggleUseEnglish() {
    useEnglishPref.value = !useEnglishPref.value
  }

  // Rate - >
  fun rateIntent() {
    showRatingPref.value = !showRatingPref.value
    showRating()
  }

  fun showRating() {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.goodwy.audiobook"))
      .addFlags(
        Intent.FLAG_ACTIVITY_NO_HISTORY
          or Intent.FLAG_ACTIVITY_NEW_DOCUMENT
          or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)

    try {
      startActivityExternal(intent)
    } catch (e: ActivityNotFoundException) {
      val url = "http://play.google.com/store/apps/details?id=com.goodwy.audiobook"
      startActivityExternal(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
  }

  private fun startActivity(intent: Intent) {
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
  }

  @SuppressLint("QueryPermissionsNeeded")
  private fun startActivityExternal(intent: Intent) {
    if (intent.resolveActivity(context.packageManager) != null) {
      startActivity(intent)
    } else {
      startActivity(Intent.createChooser(intent, null))
    }
  }
  // <- Rate
}
