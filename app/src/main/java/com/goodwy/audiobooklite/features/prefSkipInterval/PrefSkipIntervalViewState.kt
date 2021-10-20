package com.goodwy.audiobooklite.features.prefSkipInterval

data class PrefSkipIntervalViewState(
  val seekTimeInSeconds: Int,
  val seekTimeRewindInSeconds: Int,
  val autoRewindInSeconds: Int
)
