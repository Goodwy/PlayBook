package com.goodwy.audiobook.features.prefBeta

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import de.paulwoitaschek.flowpref.Pref
import com.goodwy.audiobook.common.pref.PrefKeys
import com.goodwy.audiobook.features.bookOverview.GridMode
import com.goodwy.audiobook.misc.DARK_THEME_SETTABLE
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import javax.inject.Named

class PrefBetaViewModel
@Inject constructor(
  private val context: Context,
  @Named(PrefKeys.DARK_THEME)
  private val useDarkTheme: Pref<Boolean>,
  @Named(PrefKeys.RESUME_ON_REPLUG)
  private val resumeOnReplugPref: Pref<Boolean>,
  @Named(PrefKeys.AUTO_REWIND_AMOUNT)
  private val autoRewindAmountPref: Pref<Int>,
  @Named(PrefKeys.SHOW_MINI_PLAYER)
  private val showMiniPlayerPref: Pref<Boolean>,
  @Named(PrefKeys.SHOW_SLIDER_VOLUME)
  private val showSliderVolumePref: Pref<Boolean>,
  @Named(PrefKeys.ICON_MODE)
  private val iconModePref: Pref<Boolean>,
  @Named(PrefKeys.SHOW_RATING)
  private val showRatingPref: Pref<Boolean>,
  @Named(PrefKeys.SCREEN_ORIENTATION)
  private val screenOrientationPref: Pref<Boolean>,
  @Named(PrefKeys.GRID_AUTO)
  private val gridViewAutoPref: Pref<Boolean>,
  @Named(PrefKeys.GRID_MODE)
  private val gridModePref: Pref<GridMode>
) {

  /*private val _viewEffects = BroadcastChannel<PrefBetaViewEffect>(1)
  val viewEffects: Flow<PrefBetaViewEffect> get() = _viewEffects.asFlow()*/

  fun viewState(): Flow<PrefBetaViewState> {
    return combine(
      screenOrientationPref.flow,
      iconModePref.flow,
      showSliderVolumePref.flow,
      showMiniPlayerPref.flow,
      gridViewAutoPref.flow
    ) { screenOrientation, iconMode, showSliderVolumePref, showMiniPlayerPref, gridViewAutoPref ->
      PrefBetaViewState(
        iconModePref = iconMode,
        screenOrientationPref = screenOrientation,
        showSliderVolumePref = showSliderVolumePref,
        showMiniPlayerPref = showMiniPlayerPref,
        gridViewAutoPref = gridViewAutoPref
      )
    }
  }

  /*fun changeSkipAmount() {
    _viewEffects.offer(PrefBetaViewEffect.ShowChangeSkipAmountDialog(seekTimePref.value))
  }

  fun changeAutoRewindAmount() {
    _viewEffects.offer(PrefBetaViewEffect.ShowChangeAutoRewindAmountDialog(seekTimePref.value))
  }*/

  fun toggleShowSliderVolume() {
    showSliderVolumePref.value = !showSliderVolumePref.value
  }

  fun toggleIconMode() {
    iconModePref.value = !iconModePref.value
  }

  fun toggleScreenOrientation() {
    screenOrientationPref.value = !screenOrientationPref.value
  }

  fun toggleShowMiniPlayer() {
    showMiniPlayerPref.value = !showMiniPlayerPref.value
  }

  fun toggleGridViewAuto() {
    gridViewAutoPref.value = !gridViewAutoPref.value
    gridModePref.value = GridMode.FOLLOW_DEVICE
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

  private fun startActivityExternal(intent: Intent) {
    if (intent.resolveActivity(context.packageManager) != null) {
      startActivity(intent)
    } else {
      startActivity(Intent.createChooser(intent, null))
    }
  }
  // <- Rate
}
