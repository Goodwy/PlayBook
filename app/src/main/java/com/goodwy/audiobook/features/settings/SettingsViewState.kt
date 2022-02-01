package com.goodwy.audiobook.features.settings

data class SettingsViewState(
  val useDarkTheme: Boolean,
  val showDarkThemePref: Boolean,
  val resumeOnReplug: Boolean,
  //val seekTimeInSeconds: Int,
  //val autoRewindInSeconds: Int,
  //val tintNavBar: Boolean,
  val statusBarModePref: Int,
  val screenOrientationPref: Boolean,
  val gridViewAutoPref: Boolean
)
