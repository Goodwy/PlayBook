package com.goodwy.audiobooklite.features.prefAppearanceUIPlayer

import de.paulwoitaschek.flowpref.Pref
import com.goodwy.audiobooklite.common.pref.PrefKeys
import com.goodwy.audiobooklite.features.prefSkipInterval.PrefSkipIntervalViewEffect
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import javax.inject.Named

class PrefAppearanceUIPlayerViewModel
@Inject constructor(
  @Named(PrefKeys.PLAY_BUTTON_STYLE)
  private val playButtonStylePref: Pref<Int>,
  @Named(PrefKeys.REWIND_BUTTON_STYLE)
  private val rewindButtonStylePref: Pref<Int>,
  @Named(PrefKeys.SHOW_SLIDER_VOLUME)
  private val showSliderVolumePref: Pref<Boolean>
) {

  private val _viewEffects = BroadcastChannel<PrefAppearanceUIPlayerViewEffect>(1)
  val viewEffects: Flow<PrefAppearanceUIPlayerViewEffect> get() = _viewEffects.asFlow()

  fun viewState(): Flow<PrefAppearanceUIPlayerViewState> {
    return combine(
      playButtonStylePref.flow,
      rewindButtonStylePref.flow,
      showSliderVolumePref.flow
    ) { playButtonStylePref, rewindButtonStylePref, showSliderVolumePref ->
      PrefAppearanceUIPlayerViewState(
        playButtonStylePref = playButtonStylePref,
        rewindButtonStylePref = rewindButtonStylePref,
        showSliderVolumePref = showSliderVolumePref
      )
    }
  }

  fun changePlayButtonStyle() {
    _viewEffects.offer(PrefAppearanceUIPlayerViewEffect.ShowChangePlayButtonStyleDialog(playButtonStylePref.value))
  }

  fun changeRewindButtonStyle() {
    _viewEffects.offer(PrefAppearanceUIPlayerViewEffect.ShowChangeRewindButtonStyleDialog(rewindButtonStylePref.value))
  }

  fun toggleShowSliderVolume() {
    showSliderVolumePref.value = !showSliderVolumePref.value
  }
}
