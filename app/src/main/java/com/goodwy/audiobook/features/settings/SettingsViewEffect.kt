package com.goodwy.audiobook.features.settings

import com.goodwy.audiobook.features.prefAppearanceUIPlayer.PrefAppearanceUIPlayerViewEffect

sealed class SettingsViewEffect {

  data class ShowChangeSkipAmountDialog(val amountInSeconds: Int) : SettingsViewEffect()
  data class ShowChangeAutoRewindAmountDialog(val amountInSeconds: Int) : SettingsViewEffect()
  data class ShowChangeCoverSettingsDialog(val amountInSeconds: Int) : SettingsViewEffect()
}
