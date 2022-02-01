package com.goodwy.audiobook.features.prefAppearanceUI

sealed class PrefAppearanceUIViewEffect {

  data class ShowChangeMiniPlayerStyleDialog(val amountInSeconds: Int) : PrefAppearanceUIViewEffect()
}
