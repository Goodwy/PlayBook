package com.goodwy.audiobook.features.prefSkipInterval

sealed class PrefSkipIntervalViewEffect {

  data class ShowChangeSkipAmountDialog(val amountInSeconds: Int) : PrefSkipIntervalViewEffect()
  data class ShowChangeSkipRewindAmountDialog(val amountInSeconds: Int) : PrefSkipIntervalViewEffect()
  data class ShowChangeAutoRewindAmountDialog(val amountInSeconds: Int) : PrefSkipIntervalViewEffect()
}
