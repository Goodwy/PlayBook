package com.goodwy.audiobook.features.prefAppearanceUIPlayer

import com.goodwy.audiobook.features.prefSkipInterval.PrefSkipIntervalViewEffect

sealed class PrefAppearanceUIPlayerViewEffect {

  data class ShowChangePlayerBackgroundDialog(val amountInSeconds: Int) : PrefAppearanceUIPlayerViewEffect()
  data class ShowChangePlayButtonStyleDialog(val amountInSeconds: Int) : PrefAppearanceUIPlayerViewEffect()
  data class ShowChangeRewindButtonStyleDialog(val amountInSeconds: Int) : PrefAppearanceUIPlayerViewEffect()
}
