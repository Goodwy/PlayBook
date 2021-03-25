package com.goodwy.audiobook.features.prefAppearanceUI

sealed class PrefAppearanceUIViewEffect {

  data class ShowChangeMiniPlayerStyleDialog(val amountInSeconds: Int) : PrefAppearanceUIViewEffect()
  data class ShowChangeRewindButtonStyleDialog(val amountInSeconds: Int) : PrefAppearanceUIViewEffect()
}
