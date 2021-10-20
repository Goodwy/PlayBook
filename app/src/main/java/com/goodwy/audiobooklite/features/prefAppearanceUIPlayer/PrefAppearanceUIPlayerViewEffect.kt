package com.goodwy.audiobooklite.features.prefAppearanceUIPlayer

import com.goodwy.audiobooklite.features.prefSkipInterval.PrefSkipIntervalViewEffect

sealed class PrefAppearanceUIPlayerViewEffect {

  data class ShowChangePlayButtonStyleDialog(val amountInSeconds: Int) : PrefAppearanceUIPlayerViewEffect()
  data class ShowChangeRewindButtonStyleDialog(val amountInSeconds: Int) : PrefAppearanceUIPlayerViewEffect()
}
