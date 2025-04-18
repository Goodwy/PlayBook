package voice.settings

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.net.toUri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import voice.app.scanner.CoverSaver
import voice.common.AppInfoProvider
import voice.common.DARK_THEME_SETTABLE
import voice.common.DispatcherProvider
import voice.common.constants.*
import voice.common.grid.GridCount
import voice.common.grid.GridMode
import voice.common.navigation.Destination
import voice.common.navigation.Navigator
import voice.common.pref.PrefKeys
import voice.pref.Pref
import java.time.LocalTime
import javax.inject.Inject
import javax.inject.Named

class SettingsViewModel
@Inject constructor(
  @Named(PrefKeys.DARK_THEME)
  private val useDarkTheme: Pref<Boolean>,
  @Named(PrefKeys.AUTO_REWIND_AMOUNT)
  private val autoRewindAmountPref: Pref<Int>,
  @Named(PrefKeys.SEEK_TIME)
  private val seekTimePref: Pref<Int>,
  @Named(PrefKeys.SEEK_TIME_REWIND)
  private val seekTimeRewindPref: Pref<Int>,
  private val navigator: Navigator,
  private val appInfoProvider: AppInfoProvider,
  @Named(PrefKeys.GRID_MODE)
  private val gridModePref: Pref<GridMode>,
  private val gridCount: GridCount,
  @Named(PrefKeys.AUTO_SLEEP_TIMER)
  private val autoSleepTimerPref: Pref<Boolean>,
  @Named(PrefKeys.AUTO_SLEEP_TIMER_START)
  private val autoSleepTimeStartPref: Pref<String>,
  @Named(PrefKeys.AUTO_SLEEP_TIMER_END)
  private val autoSleepTimeEndPref: Pref<String>,
  @Named(PrefKeys.PADDING)
  private val paddingPref: Pref<String>,
  @Named(PrefKeys.TRANSPARENT_NAVIGATION)
  private val useTransparentNavigationPref: Pref<Boolean>,
  @Named(PrefKeys.PLAY_BUTTON_STYLE)
  private val playButtonStylePref: Pref<Int>,
  @Named(PrefKeys.SKIP_BUTTON_STYLE)
  private val skipButtonStylePref: Pref<Int>,
  @Named(PrefKeys.MINI_PLAYER_STYLE)
  private val miniPlayerStylePref: Pref<Int>,
  @Named(PrefKeys.PLAYER_BACKGROUND)
  private val playerBackgroundPref: Pref<Int>,
  @Named(PrefKeys.SHOW_SLIDER_VOLUME)
  private val showSliderVolumePref: Pref<Boolean>,
  @Named(PrefKeys.THEME)
  private val themePref: Pref<Int>,
  @Named(PrefKeys.COLOR_THEME)
  private val colorThemePreference: Pref<Int>,
  @Named(PrefKeys.THEME_WIDGET)
  private val themeWidgetPref: Pref<Int>,
  @Named(PrefKeys.PRO)
  private val isProPref: Pref<Boolean>,
  @Named(PrefKeys.PRO_SUBS)
  private val isProSubsPref: Pref<Boolean>,
  @Named(PrefKeys.PRO_RUSTORE)
  private val isProRuPref: Pref<Boolean>,
  @Named(PrefKeys.PRO_NO_GP)
  private val isProNoGpPref: Pref<Boolean>,
  @Named(PrefKeys.USE_GESTURES)
  private val useGestures: Pref<Boolean>,
  @Named(PrefKeys.USE_HAPTIC_FEEDBACK)
  private val useHapticFeedback: Pref<Boolean>,
  @Named(PrefKeys.SCAN_COVER_CHAPTER)
  private val scanCoverChapter: Pref<Boolean>,
  @Named(PrefKeys.USE_MENU_ICONS)
  private val useMenuIconsPref: Pref<Boolean>,
  @Named(PrefKeys.USE_ANIMATED_MARQUEE)
  private val useAnimatedMarqueePref: Pref<Boolean>,
  private val coverSaver: CoverSaver,
  dispatcherProvider: DispatcherProvider,
) : SettingsListener {

  private val scope = CoroutineScope(SupervisorJob() + dispatcherProvider.main)
  private val dialog = mutableStateOf<SettingsViewState.Dialog?>(null)

  fun attach() {
    scope.launch {
      coverSaver.updateSizeCoversDirectory()
    }
  }

  @Composable
  fun viewState(): SettingsViewState {
    val useDarkTheme by remember { useDarkTheme.flow }.collectAsState(initial = false)
    val autoRewindAmount by remember { autoRewindAmountPref.flow }.collectAsState(initial = 0)
    val seekTime by remember { seekTimePref.flow }.collectAsState(initial = 0)
    val seekTimeRewind by remember { seekTimeRewindPref.flow }.collectAsState(initial = 0)
    val gridMode by remember { gridModePref.flow }.collectAsState(initial = GridMode.GRID)
    val autoSleepTimer by remember { autoSleepTimerPref.flow }.collectAsState(initial = false)
    val autoSleepTimeStart by remember { autoSleepTimeStartPref.flow }.collectAsState(initial = "")
    val autoSleepTimeEnd by remember { autoSleepTimeEndPref.flow }.collectAsState(initial = "")
    val paddings by remember { paddingPref.flow }.collectAsState(initial = "0;0;0;0")
    val useTransparentNavigationPref by remember { useTransparentNavigationPref.flow }.collectAsState(initial = true)
    val playButtonStylePref by remember { playButtonStylePref.flow }.collectAsState(initial = 2)
    val skipButtonStylePref by remember { skipButtonStylePref.flow }.collectAsState(initial = SKIP_BUTTON_ROUND)
    val miniPlayerStylePref by remember { miniPlayerStylePref.flow }.collectAsState(initial = MINI_PLAYER_PLAYER)
    val playerBackgroundPref by remember { playerBackgroundPref.flow }.collectAsState(initial = PLAYER_BACKGROUND_THEME)
    val showSliderVolumePref by remember { showSliderVolumePref.flow }.collectAsState(initial = true)
    val themePref by remember { themePref.flow }.collectAsState(initial = THEME_LIGHT)
    val colorThemePref by remember { colorThemePreference.flow }.collectAsState(initial = -0x1)
    val themeWidgetPref by remember { themeWidgetPref.flow }.collectAsState(initial = THEME_LIGHT)
    val isProPref by remember { isProPref.flow }.collectAsState(initial = false)
    val isProSubsPref by remember { isProSubsPref.flow }.collectAsState(initial = false)
    val isProRuPref by remember { isProRuPref.flow }.collectAsState(initial = false)
    val isProNoGpPref by remember { isProNoGpPref.flow }.collectAsState(initial = false)
    val useGestures by remember { useGestures.flow }.collectAsState(initial = true)
    val useHapticFeedback by remember { useHapticFeedback.flow }.collectAsState(initial = true)
    val sizeCoversDirectory by remember { coverSaver.sizeCoversDirectory }.collectAsState(initial = "0")
    val scanCoverChapter by remember { scanCoverChapter.flow }.collectAsState(initial = false)
    val useMenuIconsPref by remember { useMenuIconsPref.flow }.collectAsState(initial = false)
    val useAnimatedMarquee by remember { useAnimatedMarqueePref.flow }.collectAsState(initial = true)
    return SettingsViewState(
      useDarkTheme = useDarkTheme,
      showDarkThemePref = DARK_THEME_SETTABLE,
      seekTimeInSeconds = seekTime,
      seekTimeRewindInSeconds = seekTimeRewind,
      autoRewindInSeconds = autoRewindAmount,
      dialog = dialog.value,
      appVersion = appInfoProvider.versionName,
      useGrid = when (gridMode) {
        GridMode.LIST -> false
        GridMode.GRID -> true
        GridMode.FOLLOW_DEVICE -> gridCount.useGridAsDefault()
      },
      autoSleepTimer = autoSleepTimer,
      autoSleepTimeStart = autoSleepTimeStart,
      autoSleepTimeEnd = autoSleepTimeEnd,
      gridMode = when (gridMode) {
        GridMode.LIST -> 0
        GridMode.GRID -> 1
        GridMode.FOLLOW_DEVICE -> 2
      },
      paddings = paddings,
      useTransparentNavigation = useTransparentNavigationPref,
      playButtonStyle = playButtonStylePref,
      skipButtonStyle = skipButtonStylePref,
      miniPlayerStyle = miniPlayerStylePref,
      playerBackground = playerBackgroundPref,
      showSliderVolume = showSliderVolumePref,
      theme = themePref,
      colorTheme = colorThemePref,
      themeWidget = themeWidgetPref,
      isPro = isProPref || isProSubsPref || isProRuPref || isProNoGpPref,
      useGestures = useGestures,
      useHapticFeedback = useHapticFeedback,
      sizeCoversDirectory = sizeCoversDirectory,
      scanCoverChapter = scanCoverChapter,
      useMenuIconsPref = useMenuIconsPref,
      useAnimatedMarquee = useAnimatedMarquee,
    )
  }

  override fun close() {
    navigator.goBack()
  }

  override fun toggleDarkTheme() {
    useDarkTheme.value = !useDarkTheme.value
  }

  override fun toggleTransparentNavigation() {
    useTransparentNavigationPref.value = !useTransparentNavigationPref.value
  }

  override fun toggleShowSliderVolume() {
    showSliderVolumePref.value = !showSliderVolumePref.value
  }

  override fun toggleGrid() {
    gridModePref.value = when (gridModePref.value) {
      GridMode.LIST -> GridMode.GRID
      GridMode.GRID -> GridMode.LIST
      GridMode.FOLLOW_DEVICE -> if (gridCount.useGridAsDefault()) {
        GridMode.LIST
      } else {
        GridMode.GRID
      }
    }
  }

  override fun gridModeDialog() {
    gridModePref.value = when (gridModePref.value) {
      GridMode.LIST -> GridMode.GRID
      GridMode.GRID -> GridMode.LIST
      GridMode.FOLLOW_DEVICE -> if (gridCount.useGridAsDefault()) {
        GridMode.LIST
      } else {
        GridMode.GRID
      }
    }
  }

  override fun gridModeDialogChanged(item: Int) {
    gridModePref.value = when (item) {
      0 -> GridMode.LIST
      1 -> GridMode.GRID
      else -> GridMode.FOLLOW_DEVICE
    }
  }

  override fun onGridModeDialogRowClick() {
    dialog.value = SettingsViewState.Dialog.GridModeDialog
  }

  override fun playButtonStyleDialogChanged(item: Int) {
    playButtonStylePref.value = item
  }

  override fun onPlayButtonStyleDialogRowClick() {
    dialog.value = SettingsViewState.Dialog.PlayButtonStyleDialog
  }

  override fun skipButtonStyleDialogChanged(item: Int) {
    skipButtonStylePref.value = item
  }

  override fun onSkipButtonStyleDialogRowClick() {
    dialog.value = SettingsViewState.Dialog.SkipButtonStyleDialog
  }

  override fun miniPlayerStyleDialogChanged(item: Int) {
    miniPlayerStylePref.value = item
  }

  override fun onMiniPlayerStyleDialogRowClick() {
    dialog.value = SettingsViewState.Dialog.MiniPlayerStyleDialog
  }

  override fun playerBackgroundDialogChanged(item: Int) {
    playerBackgroundPref.value = item
  }

  override fun onPlayerBackgroundDialogRowClick() {
    dialog.value = SettingsViewState.Dialog.PlayerBackgroundDialog
  }

  override fun seekAmountChanged(seconds: Int) {
    seekTimePref.value = seconds
  }

  override fun seekRewindAmountChanged(seconds: Int) {
    seekTimeRewindPref.value = seconds
  }

  override fun onSeekAmountRowClick() {
    dialog.value = SettingsViewState.Dialog.SeekTime
  }

  override fun onSeekRewindAmountRowClick() {
    dialog.value = SettingsViewState.Dialog.SeekTimeRewind
  }

  override fun autoRewindAmountChanged(seconds: Int) {
    autoRewindAmountPref.value = seconds
  }

  override fun onAutoRewindRowClick() {
    dialog.value = SettingsViewState.Dialog.AutoRewindAmount
  }

  override fun colorThemeChanged(color: Int) {
    colorThemePreference.value = color
  }

  override fun onColorThemeDialogRowClick() {
    dialog.value = SettingsViewState.Dialog.ColorThemeDialog
  }

  override fun themeChanged(theme: Int) {
    themePref.value = theme
  }

  override fun themeWidgetChanged(themeWidget: Int) {
    themeWidgetPref.value = themeWidget
  }

  override fun onThemeDialogRowClick(isWidget: Boolean) {
    if (isWidget) dialog.value = SettingsViewState.Dialog.ThemeWidgetDialog
    else dialog.value = SettingsViewState.Dialog.ThemeDialog
  }

  override fun dismissDialog() {
    dialog.value = null
  }

  override fun getSupport() {
    navigator.goTo(Destination.Website("https://github.com/Goodwy/PlayBook/discussions/new?category=q-a"))
  }

  override fun suggestIdea() {
    navigator.goTo(Destination.Website("https://github.com/Goodwy/PlayBook/discussions/new?category=ideas"))
  }

  override fun openBugReport() {
    val url = "https://github.com/Goodwy/PlayBooks/issues/new".toUri()
      .buildUpon()
      .appendQueryParameter("template", "bug.yml")
      .appendQueryParameter("version", appInfoProvider.versionName)
      .appendQueryParameter("androidversion", Build.VERSION.SDK_INT.toString())
      .appendQueryParameter("device", Build.MODEL)
      .toString()
    navigator.goTo(Destination.Website(url))
  }

  override fun openTranslations() {
    dismissDialog()
    navigator.goTo(Destination.Website("https://www.transifex.com/projects/p/voice"))
  }

  override fun toggleAutoSleepTimer() {
    autoSleepTimerPref.value = !autoSleepTimerPref.value
  }

  override fun setAutoSleepTimerStart(hour: Int, minute: Int) {
    val time = LocalTime.of(hour, minute).toString()
    autoSleepTimeStartPref.value = time
  }

  override fun setAutoSleepTimerEnd(hour: Int, minute: Int) {
    val time = LocalTime.of(hour, minute).toString()
    autoSleepTimeEndPref.value = time
  }

  override fun onAboutClick() {
    navigator.goTo(Destination.About)
  }

  override fun onPurchaseClick() {
    navigator.goTo(Destination.Purchase)
  }

  override fun toggleUseSwipe() {
    useGestures.value = !useGestures.value
  }

  override fun onUseSwipeFaqClick() {
    navigator.goTo(Destination.OnboardingCompletionFaq)
  }

  override fun toggleUseHapticFeedback() {
    useHapticFeedback.value = !useHapticFeedback.value
  }

  override fun clearCoversDirectory() {
    scope.launch {
      coverSaver.clearCoversDirectory()
    }
  }

  override fun onClearCoversDirectoryRowClick() {
    dialog.value = SettingsViewState.Dialog.ClearCoversDirectoryAlertDialog
  }

  override fun toggleScanCoverChapter() {
    scanCoverChapter.value = !scanCoverChapter.value
    clearCoversDirectory()
  }

  override fun onScanCoverChapterRowClick() {
    dialog.value = SettingsViewState.Dialog.ScanChapterCoverAlertDialog
  }

  override fun toggleUseMenuIcons() {
    useMenuIconsPref.value = !useMenuIconsPref.value
  }

  override fun toggleUseAnimatedMarquee() {
    useAnimatedMarqueePref.value = !useAnimatedMarqueePref.value
  }
}
