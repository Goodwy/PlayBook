package com.goodwy.audiobook.features.prefSkipInterval

data class PrefSkipIntervalViewState(
  val seekTimeInSeconds: Int,
  val seekTimeRewindInSeconds: Int,
  val autoRewindInSeconds: Int
)
