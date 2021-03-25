package com.goodwy.audiobook.features.prefAppearanceUI

import de.paulwoitaschek.flowpref.Pref
import com.goodwy.audiobook.common.pref.PrefKeys
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import javax.inject.Named

class PrefAppearanceUIViewModel
@Inject constructor(
  @Named(PrefKeys.MINI_PLAYER_STYLE)
  private val miniPlayerStylePref: Pref<Int>,
  @Named(PrefKeys.REWIND_BUTTON_STYLE)
  private val rewindButtonStylePref: Pref<Int>
) {

  private val _viewEffects = BroadcastChannel<PrefAppearanceUIViewEffect>(1)
  val viewEffects: Flow<PrefAppearanceUIViewEffect> get() = _viewEffects.asFlow()

  fun viewState(): Flow<PrefAppearanceUIViewState> {
    return combine(
      miniPlayerStylePref.flow,
      rewindButtonStylePref.flow
    ) { miniPlayerStylePref, rewindButtonStylePref ->
      PrefAppearanceUIViewState(
        miniPlayerStylePref = miniPlayerStylePref,
        rewindButtonStylePref = rewindButtonStylePref
      )
    }
  }

  fun changeMiniPlayerStyle() {
    _viewEffects.offer(PrefAppearanceUIViewEffect.ShowChangeMiniPlayerStyleDialog(miniPlayerStylePref.value))
  }

  fun changeRewindButtonStyle() {
    _viewEffects.offer(PrefAppearanceUIViewEffect.ShowChangeRewindButtonStyleDialog(miniPlayerStylePref.value))
  }
}
