package voice.settings

data class SettingsViewState(
  val useDarkTheme: Boolean,
  val showDarkThemePref: Boolean,
  val seekTimeInSeconds: Int,
  val seekTimeRewindInSeconds: Int,
  val autoRewindInSeconds: Int,
  val appVersion: String,
  val dialog: Dialog?,
  val useGrid: Boolean,
  val autoSleepTimer: Boolean,
  val autoSleepTimeStart: String,
  val autoSleepTimeEnd: String,
  val gridMode: Int,
  val paddings: String,
  val useTransparentNavigation: Boolean,
  val playButtonStyle: Int,
  val skipButtonStyle: Int,
  val miniPlayerStyle: Int,
  val playerBackground: Int,
  val showSliderVolume: Boolean,
  val theme: Int,
  val colorTheme: Int,
  val themeWidget: Int,
  val isPro: Boolean,
  val useGestures: Boolean,
  val useHapticFeedback: Boolean,
  val sizeCoversDirectory: String,
  val scanCoverChapter: Boolean,
  val useMenuIconsPref: Boolean,
  val useAnimatedMarquee: Boolean,
) {

  enum class Dialog {
    AutoRewindAmount,
    SeekTime,
    SeekTimeRewind,
    GridModeDialog,
    PlayButtonStyleDialog,
    SkipButtonStyleDialog,
    MiniPlayerStyleDialog,
    PlayerBackgroundDialog,
    ColorThemeDialog,
    ThemeDialog,
    ThemeWidgetDialog,
    ScanChapterCoverAlertDialog,
    ClearCoversDirectoryAlertDialog
  }
}
