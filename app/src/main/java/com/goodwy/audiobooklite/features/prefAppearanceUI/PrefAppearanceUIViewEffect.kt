package com.goodwy.audiobooklite.features.prefAppearanceUI

sealed class PrefAppearanceUIViewEffect {

  data class ShowChangeMiniPlayerStyleDialog(val amountInSeconds: Int) : PrefAppearanceUIViewEffect()
}
