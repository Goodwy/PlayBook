package com.goodwy.audiobook.features.prefBeta

sealed class PrefBetaViewEffect {

  data class ShowChangeSkipAmountDialog(val amountInSeconds: Int) : PrefBetaViewEffect()
  data class ShowChangeAutoRewindAmountDialog(val amountInSeconds: Int) : PrefBetaViewEffect()
}
