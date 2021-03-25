package com.goodwy.audiobook.features.prefSkipInterval

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import de.paulwoitaschek.flowpref.Pref
import com.goodwy.audiobook.common.pref.PrefKeys
import com.goodwy.audiobook.features.bookOverview.GridMode
import com.goodwy.audiobook.features.settings.SettingsViewEffect
import com.goodwy.audiobook.misc.DARK_THEME_SETTABLE
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import javax.inject.Named

class PrefSkipIntervalViewModel
@Inject constructor(
  private val context: Context,
  @Named(PrefKeys.AUTO_REWIND_AMOUNT)
  private val autoRewindAmountPref: Pref<Int>,
  @Named(PrefKeys.SEEK_TIME)
  private val seekTimePref: Pref<Int>,
  @Named(PrefKeys.SEEK_TIME_REWIND)
  private val seekTimeRewindPref: Pref<Int>
) {

  private val _viewEffects = BroadcastChannel<PrefSkipIntervalViewEffect>(1)
  val viewEffects: Flow<PrefSkipIntervalViewEffect> get() = _viewEffects.asFlow()

  fun viewState(): Flow<PrefSkipIntervalViewState> {
    return combine(
      autoRewindAmountPref.flow,
      seekTimePref.flow,
      seekTimeRewindPref.flow
    ) { autoRewindAmount, seekTime, seekTimeRewind ->
      PrefSkipIntervalViewState(
        seekTimeInSeconds = seekTime,
        seekTimeRewindInSeconds = seekTimeRewind,
        autoRewindInSeconds = autoRewindAmount
      )
    }
  }

  /*fun changeSkipAmount() {
    _viewEffects.offer(PrefSkipIntervalViewEffect.ShowChangeSkipAmountDialog(seekTimePref.value))
  }

  fun changeAutoRewindAmount() {
    _viewEffects.offer(PrefSkipIntervalViewEffect.ShowChangeAutoRewindAmountDialog(seekTimePref.value))
  }*/

  fun changeSkipAmountForward() {
    _viewEffects.offer(PrefSkipIntervalViewEffect.ShowChangeSkipAmountDialog(seekTimePref.value))
  }

  fun changeskipAmountRewind() {
    _viewEffects.offer(PrefSkipIntervalViewEffect.ShowChangeSkipRewindAmountDialog(seekTimeRewindPref.value))
  }

  fun changeAutoRewindAmount() {
    _viewEffects.offer(PrefSkipIntervalViewEffect.ShowChangeAutoRewindAmountDialog(seekTimePref.value))
  }
}
