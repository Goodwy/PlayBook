package voice.settings

import android.net.Uri
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import de.paulwoitaschek.flowpref.Pref
import voice.common.AppInfoProvider
import voice.common.DARK_THEME_SETTABLE
import voice.common.grid.GridCount
import voice.common.grid.GridMode
import voice.common.navigation.Destination
import voice.common.navigation.Navigator
import voice.common.pref.PrefKeys
import javax.inject.Inject
import javax.inject.Named

class SettingsViewModel
@Inject constructor(
  @Named(PrefKeys.DARK_THEME)
  private val useDarkTheme: Pref<Boolean>,
  @Named(PrefKeys.RESUME_ON_REPLUG)
  private val resumeOnReplugPref: Pref<Boolean>,
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
  @Named(PrefKeys.PRO)
  private val isProPref: Pref<Boolean>,
) : SettingsListener {

  private val dialog = mutableStateOf<SettingsViewState.Dialog?>(null)

  @Composable
  fun viewState(): SettingsViewState {
    val useDarkTheme by remember { useDarkTheme.flow }.collectAsState(initial = false)
    val resumeOnReplug by remember { resumeOnReplugPref.flow }.collectAsState(initial = false)
    val autoRewindAmount by remember { autoRewindAmountPref.flow }.collectAsState(initial = 0)
    val seekTime by remember { seekTimePref.flow }.collectAsState(initial = 0)
    val seekTimeRewind by remember { seekTimeRewindPref.flow }.collectAsState(initial = 0)
    val gridMode by remember { gridModePref.flow }.collectAsState(initial = GridMode.GRID)
    val paddings by remember { paddingPref.flow }.collectAsState(initial = "0;0;0;0")
    val useTransparentNavigationPref by remember { useTransparentNavigationPref.flow }.collectAsState(initial = true)
    val playButtonStylePref by remember { playButtonStylePref.flow }.collectAsState(initial = 2)
    val skipButtonStylePref by remember { skipButtonStylePref.flow }.collectAsState(initial = 2)
    val miniPlayerStylePref by remember { miniPlayerStylePref.flow }.collectAsState(initial = 0)
    val playerBackgroundPref by remember { playerBackgroundPref.flow }.collectAsState(initial = 0)
    val showSliderVolumePref by remember { showSliderVolumePref.flow }.collectAsState(initial = true)
    val themePref by remember { themePref.flow }.collectAsState(initial = 0)
    val colorThemePref by remember { colorThemePreference.flow }.collectAsState(initial = -0x1)
    val isProPref by remember { isProPref.flow }.collectAsState(initial = false)
    return SettingsViewState(
      useDarkTheme = useDarkTheme,
      showDarkThemePref = DARK_THEME_SETTABLE,
      resumeOnReplug = resumeOnReplug,
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
      isPro = isProPref,
    )
  }

  override fun close() {
    navigator.goBack()
  }

  override fun toggleResumeOnReplug() {
    resumeOnReplugPref.value = !resumeOnReplugPref.value
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

  override fun onGridModeDialogRowClicked() {
    dialog.value = SettingsViewState.Dialog.GridModeDialog
  }

  override fun playButtonStyleDialogChanged(item: Int) {
    playButtonStylePref.value = item
  }

  override fun onPlayButtonStyleDialogRowClicked() {
    dialog.value = SettingsViewState.Dialog.PlayButtonStyleDialog
  }

  override fun skipButtonStyleDialogChanged(item: Int) {
    skipButtonStylePref.value = item
  }

  override fun onSkipButtonStyleDialogRowClicked() {
    dialog.value = SettingsViewState.Dialog.SkipButtonStyleDialog
  }

  override fun miniPlayerStyleDialogChanged(item: Int) {
    miniPlayerStylePref.value = item
  }

  override fun onMiniPlayerStyleDialogRowClicked() {
    dialog.value = SettingsViewState.Dialog.MiniPlayerStyleDialog
  }

  override fun playerBackgroundDialogChanged(item: Int) {
    playerBackgroundPref.value = item
  }

  override fun onPlayerBackgroundDialogRowClicked() {
    dialog.value = SettingsViewState.Dialog.PlayerBackgroundDialog
  }

  override fun seekAmountChanged(seconds: Int) {
    seekTimePref.value = seconds
  }

  override fun seekRewindAmountChanged(seconds: Int) {
    seekTimeRewindPref.value = seconds
  }

  override fun onSeekAmountRowClicked() {
    dialog.value = SettingsViewState.Dialog.SeekTime
  }

  override fun onSeekRewindAmountRowClicked() {
    dialog.value = SettingsViewState.Dialog.SeekTimeRewind
  }

  override fun autoRewindAmountChanged(seconds: Int) {
    autoRewindAmountPref.value = seconds
  }

  override fun onAutoRewindRowClicked() {
    dialog.value = SettingsViewState.Dialog.AutoRewindAmount
  }

  override fun colorThemeChanged(color: Int) {
    colorThemePreference.value = color
  }

  override fun onColorThemeDialogRowClicked() {
    dialog.value = SettingsViewState.Dialog.ColorThemeDialog
  }

  override fun themeChanged(theme: Int) {
    themePref.value = theme
  }

  override fun onThemeDialogRowClicked() {
    dialog.value = SettingsViewState.Dialog.ThemeDialog
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
    val url = Uri.parse("https://github.com/Goodwy/PlayBooks/issues/new")
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

  override fun onAboutClick() {
    navigator.goTo(Destination.About)
  }

  override fun onPurchaseClick() {
    navigator.goTo(Destination.Purchase)
  }
}
