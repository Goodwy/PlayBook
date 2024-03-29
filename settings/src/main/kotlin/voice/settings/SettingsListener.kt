package voice.settings

interface SettingsListener {
  fun close()
  fun toggleResumeOnReplug()
  fun toggleDarkTheme()
  fun toggleGrid()
  fun seekAmountChanged(seconds: Int)
  fun seekRewindAmountChanged(seconds: Int)
  fun onSeekAmountRowClicked()
  fun onSeekRewindAmountRowClicked()
  fun autoRewindAmountChanged(seconds: Int)
  fun onAutoRewindRowClicked()
  fun dismissDialog()
  fun getSupport()
  fun suggestIdea()
  fun openBugReport()
  fun openTranslations()
  fun gridModeDialog()
  fun gridModeDialogChanged(item: Int)
  fun onGridModeDialogRowClicked()
  fun toggleTransparentNavigation()
  fun playButtonStyleDialogChanged(item: Int)
  fun onPlayButtonStyleDialogRowClicked()
  fun skipButtonStyleDialogChanged(item: Int)
  fun onSkipButtonStyleDialogRowClicked()
  fun miniPlayerStyleDialogChanged(item: Int)
  fun onMiniPlayerStyleDialogRowClicked()
  fun playerBackgroundDialogChanged(item: Int)
  fun onPlayerBackgroundDialogRowClicked()
  fun toggleShowSliderVolume()
  fun themeChanged(theme: Int)
  fun onThemeDialogRowClicked()
  fun colorThemeChanged(color: Int)
  fun onColorThemeDialogRowClicked()
  fun onAboutClick()
  fun onPurchaseClick()
}
