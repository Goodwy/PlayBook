package voice.settings

interface SettingsListener {
  fun close()
  fun toggleDarkTheme()
  fun toggleGrid()
  fun seekAmountChanged(seconds: Int)
  fun seekRewindAmountChanged(seconds: Int)
  fun onSeekAmountRowClick()
  fun onSeekRewindAmountRowClick()
  fun autoRewindAmountChanged(seconds: Int)
  fun onAutoRewindRowClick()
  fun dismissDialog()
  fun getSupport()
  fun suggestIdea()
  fun openBugReport()
  fun openTranslations()
  fun toggleAutoSleepTimer()
  fun setAutoSleepTimerStart(hour: Int, minute: Int)
  fun setAutoSleepTimerEnd(hour: Int, minute: Int)
  fun gridModeDialog()
  fun gridModeDialogChanged(item: Int)
  fun onGridModeDialogRowClick()
  fun toggleTransparentNavigation()
  fun playButtonStyleDialogChanged(item: Int)
  fun onPlayButtonStyleDialogRowClick()
  fun skipButtonStyleDialogChanged(item: Int)
  fun onSkipButtonStyleDialogRowClick()
  fun miniPlayerStyleDialogChanged(item: Int)
  fun onMiniPlayerStyleDialogRowClick()
  fun playerBackgroundDialogChanged(item: Int)
  fun onPlayerBackgroundDialogRowClick()
  fun toggleShowSliderVolume()
  fun themeChanged(theme: Int)
  fun onThemeDialogRowClick(isWidget: Boolean)
  fun themeWidgetChanged(themeWidget: Int)
  fun colorThemeChanged(color: Int)
  fun onColorThemeDialogRowClick()
  fun onAboutClick()
  fun onPurchaseClick()
  fun toggleUseSwipe()
  fun onUseSwipeFaqClick()
  fun toggleUseHapticFeedback()
  fun clearCoversDirectory()
  fun onClearCoversDirectoryRowClick()
  fun toggleScanCoverChapter()
  fun onScanCoverChapterRowClick()
  fun toggleUseMenuIcons()
  fun toggleUseAnimatedMarquee()
}
