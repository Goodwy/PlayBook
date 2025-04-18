package voice.settings.views

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.squareup.anvil.annotations.ContributesTo
import voice.common.AppScope
import voice.common.COLOR_SCHEME_SETTABLE
import voice.common.compose.TimePickerDialog
import voice.common.compose.VoiceTheme
import voice.common.compose.rememberScoped
import voice.common.constants.*
import voice.common.rootComponentAs
import voice.settings.SettingsListener
import voice.settings.SettingsViewModel
import voice.settings.SettingsViewState
import java.time.LocalTime
import voice.strings.R as StringsR
import voice.common.R as CommonR

@Composable
@Preview
private fun SettingsPreview() {
  val viewState = SettingsViewState(
    useDarkTheme = false,
    showDarkThemePref = true,
    seekTimeInSeconds = 30,
    seekTimeRewindInSeconds = 20,
    autoRewindInSeconds = 12,
    dialog = null,
    appVersion = "1.2.3",
    useGrid = true,
    autoSleepTimer = false,
    autoSleepTimeEnd = "",
    autoSleepTimeStart = "",
    gridMode = 2,
    paddings = "0;0;0;0",
    useTransparentNavigation = true,
    playButtonStyle = 2,
    skipButtonStyle = SKIP_BUTTON_ROUND,
    miniPlayerStyle = MINI_PLAYER_PLAYER,
    playerBackground = PLAYER_BACKGROUND_THEME,
    showSliderVolume = true,
    theme = THEME_LIGHT,
    colorTheme = -0x1,
    themeWidget = THEME_LIGHT,
    isPro = false,
    useGestures = true,
    useHapticFeedback = true,
    sizeCoversDirectory = "0MB",
    scanCoverChapter = false,
    useMenuIconsPref = false,
    useAnimatedMarquee = false,
  )
  VoiceTheme(preview = true) {
    Settings(
      viewState,
      object : SettingsListener {
        override fun close() {}
        override fun toggleDarkTheme() {}
        override fun seekAmountChanged(seconds: Int) {}
        override fun seekRewindAmountChanged(seconds: Int) {}
        override fun onSeekAmountRowClick() {}
        override fun onSeekRewindAmountRowClick() {}
        override fun autoRewindAmountChanged(seconds: Int) {}
        override fun onAutoRewindRowClick() {}
        override fun dismissDialog() {}
        override fun openTranslations() {}
        override fun getSupport() {}
        override fun suggestIdea() {}
        override fun openBugReport() {}
        override fun toggleGrid() {}
        override fun toggleAutoSleepTimer() {}
        override fun setAutoSleepTimerStart(hour: Int, minute: Int) {}
        override fun setAutoSleepTimerEnd(hour: Int, minute: Int) {}
        override fun gridModeDialog() {}
        override fun gridModeDialogChanged(item: Int) {}
        override fun onGridModeDialogRowClick() {}
        override fun toggleTransparentNavigation() {}
        override fun onPlayButtonStyleDialogRowClick() {}
        override fun playButtonStyleDialogChanged(item: Int) {}
        override fun onSkipButtonStyleDialogRowClick() {}
        override fun skipButtonStyleDialogChanged(item: Int) {}
        override fun onMiniPlayerStyleDialogRowClick() {}
        override fun miniPlayerStyleDialogChanged(item: Int) {}
        override fun onPlayerBackgroundDialogRowClick() {}
        override fun playerBackgroundDialogChanged(item: Int) {}
        override fun toggleShowSliderVolume() {}
        override fun themeChanged(theme: Int) {}
        override fun onThemeDialogRowClick(isWidget: Boolean) {}
        override fun themeWidgetChanged(themeWidget: Int) {}
        override fun colorThemeChanged(color: Int) {}
        override fun onColorThemeDialogRowClick() {}
        override fun onAboutClick() {}
        override fun onPurchaseClick() {}
        override fun toggleUseSwipe() {}
        override fun onUseSwipeFaqClick() {}
        override fun toggleUseHapticFeedback() {}
        override fun clearCoversDirectory() {}
        override fun onClearCoversDirectoryRowClick() {}
        override fun toggleScanCoverChapter() {}
        override fun onScanCoverChapterRowClick() {}
        override fun toggleUseMenuIcons() {}
        override fun toggleUseAnimatedMarquee() {}
      },
    )
  }
}

@Composable
private fun Settings(
  viewState: SettingsViewState,
  listener: SettingsListener,
) {
  val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
  val top = viewState.paddings.substringBefore(';').toInt()
  val bottom = viewState.paddings.substringAfter(';').substringBefore(';').toInt()
  val start = viewState.paddings.substringBeforeLast(';').substringAfterLast(';').toInt()
  val end = viewState.paddings.substringAfterLast(';').toInt()
  Scaffold(
    modifier = Modifier
      .nestedScroll(scrollBehavior.nestedScrollConnection)
      .padding(start = start.dp, end = end.dp),
    topBar = {
      TopAppBar(
        title = {
          Text(stringResource(StringsR.string.action_settings))
        },
        scrollBehavior = scrollBehavior,
        navigationIcon = {
          IconButton(
            onClick = {
              listener.close()
            },
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Rounded.ArrowBackIos,
              contentDescription = stringResource(StringsR.string.close),
            )
          }
        },
        windowInsets = WindowInsets(top = top.dp),
      )
    },
  ) { contentPadding ->
    Box(
      Modifier
        .padding(contentPadding)
        .verticalScroll(rememberScrollState())
        .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) {
      Column(Modifier.padding(vertical = 8.dp, horizontal = 16.dp)) {
        // Support project
        var enabledShake by remember { mutableStateOf(false) }
        if (!viewState.isPro) {
          Row(
            modifier = Modifier
              .shake(enabledShake) { enabledShake = false }
              .clickable { listener.onPurchaseClick() }
              .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Card(
              shape = RoundedCornerShape(24.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
              elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
              Icon(
                modifier = Modifier
                  .size(86.dp)
                  .padding(2.dp),
                painter = painterResource(id = CommonR.drawable.ic_plus_support),
                contentDescription = stringResource(id = CommonR.string.action_support_project),
                tint = MaterialTheme.colorScheme.primary,
              )
            }
            Column(
              modifier = Modifier
                .heightIn(86.dp)
                .padding(bottom = 4.dp),
              verticalArrangement = Arrangement.SpaceBetween,
              horizontalAlignment = Alignment.Start,
            ) {
              Text(
                modifier = Modifier.padding(start = 16.dp),
                text = stringResource(CommonR.string.action_support_project),
                fontSize = 18.sp,
              )
              Text(
                modifier = Modifier.padding(start = 16.dp),
                text = stringResource(CommonR.string.action_support_project_summary),
                fontSize = 13.sp,
                lineHeight = 16.sp,
                color = LocalContentColor.current.copy(alpha = 0.5F),
              )
              Button(
                modifier = Modifier.padding(start = 16.dp, top = 3.dp).wrapContentWidth().height(24.dp).alpha(0.6f),
                onClick = { listener.onPurchaseClick() },
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
              ) {
                Text(
                  text = stringResource(CommonR.string.learn_more).toUpperCase(LocaleList.current),
                  fontSize = 12.sp,
                )
              }
            }
          }
          Spacer(modifier = Modifier.size(8.dp))
        }

        // Appearance
        HeaderRow(stringResource(CommonR.string.pref_category_appearance))
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.inverseOnSurface)
        ) {
          Column {
            val haptic = LocalHapticFeedback.current
            ThemeRow(viewState.theme, viewState.isPro) {
              if (viewState.isPro) listener.onThemeDialogRowClick(false)
              else {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                enabledShake = true
              }
            }
            if (viewState.theme != THEME_AUTO || !COLOR_SCHEME_SETTABLE) {
              DividerRow()
              ColorThemeRow(viewState.isPro) {
                if (viewState.isPro) listener.onColorThemeDialogRowClick()
                else {
                  haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                  enabledShake = true
                }
              }
            }
            DividerRow()
            ThemeRow(viewState.themeWidget, viewState.isPro, isWidget = true) {
              if (viewState.isPro) listener.onThemeDialogRowClick(true)
              else {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                enabledShake = true
              }
            }
//            if (viewState.showDarkThemePref) {
//              DarkThemeRow(viewState.useDarkTheme, listener::toggleDarkTheme)
//            }
//            TransparentNavigationRow(viewState.useTransparentNavigation, listener::toggleTransparentNavigation)
          }
        }
        Spacer(modifier = Modifier.size(16.dp))

        // Appearance library
        HeaderRow(stringResource(CommonR.string.pref_appearance_ui_library_title))
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.inverseOnSurface)
        ) {
          Column {
            GridModeRow(viewState.gridMode) {
              listener.onGridModeDialogRowClick()
            }
            DividerRow()
            MiniPlayerStyleRow(viewState.miniPlayerStyle) {
              listener.onMiniPlayerStyleDialogRowClick()
            }
            DividerRow()
            SettingsSwitchRow(
              title = stringResource(CommonR.string.pref_use_menu_icons),
              initSwitch = viewState.useMenuIconsPref,
              toggle = listener::toggleUseMenuIcons,
              paddingBottom = 5.dp,
            )
          }
        }
        Spacer(modifier = Modifier.size(16.dp))

        // Appearance player
        HeaderRow(stringResource(CommonR.string.pref_appearance_ui_player_title))
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.inverseOnSurface)
        ) {
          Column {
            PlayerBackgroundRow(viewState.playerBackground) {
              listener.onPlayerBackgroundDialogRowClick()
            }
            DividerRow()
            PlayButtonStyleRow(viewState.playButtonStyle) {
              listener.onPlayButtonStyleDialogRowClick()
            }
            DividerRow()
            SkipButtonStyleRow(viewState.skipButtonStyle) {
              listener.onSkipButtonStyleDialogRowClick()
            }
            DividerRow()
            SettingsSwitchRow(
              title = stringResource(CommonR.string.pref_show_slider_volume_title),
              initSwitch = viewState.showSliderVolume,
              toggle = listener::toggleShowSliderVolume,
            )
            DividerRow()
            SettingsSwitchRow(
              title = stringResource(CommonR.string.pref_animated_marquee),
              subtitle = stringResource(CommonR.string.pref_animated_marquee_summary),
              initSwitch = viewState.useAnimatedMarquee,
              toggle = listener::toggleUseAnimatedMarquee,
              paddingBottom = 6.dp,
            )
          }
        }
        Spacer(modifier = Modifier.size(16.dp))

        // Playback
        HeaderRow(stringResource(CommonR.string.pref_playback))
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.inverseOnSurface)
        ) {
          Column {
            SeekTimeRow(viewState.seekTimeInSeconds) {
              listener.onSeekAmountRowClick()
            }
            DividerRow()
            SeekTimeRow(viewState.seekTimeRewindInSeconds, false) {
              listener.onSeekRewindAmountRowClick()
            }
            DividerRow()
            AutoRewindRow(viewState.autoRewindInSeconds) {
              listener.onAutoRewindRowClick()
            }
            DividerRow()
            SettingsSwitchRow(
              title = stringResource(CommonR.string.auto_sleep_timer),
              initSwitch = viewState.autoSleepTimer,
              toggle = listener::toggleAutoSleepTimer,
              paddingBottom = 5.dp,
            )
            if (viewState.autoSleepTimer) {
              DividerRow()
              val shouldShowStartTimePicker = remember { mutableStateOf(false) }
              SettingsRow(
                title = stringResource(CommonR.string.auto_sleep_timer_start),
                value = viewState.autoSleepTimeStart,
                paddingBottom = 8.dp,
                click = {
                  shouldShowStartTimePicker.value = true
                }
              )
              if (shouldShowStartTimePicker.value) {
                val initialTime = LocalTime.parse(viewState.autoSleepTimeStart)
                TimePickerDialog(
                  stringResource(CommonR.string.auto_sleep_timer_start),
                  initialTime.hour,
                  initialTime.minute,
                  { timePickerState: TimePickerState ->
                    listener.setAutoSleepTimerStart(timePickerState.hour, timePickerState.minute)
                    shouldShowStartTimePicker.value = false
                  },
                  { shouldShowStartTimePicker.value = false },
                )
              }
              DividerRow()
              val shouldShowEndTimePicker = remember { mutableStateOf(false) }
              SettingsRow(
                title = stringResource(CommonR.string.auto_sleep_timer_end),
                value = viewState.autoSleepTimeEnd,
                paddingBottom = 8.dp,
                click = {
                  shouldShowEndTimePicker.value = true
                }
              )
              if (shouldShowEndTimePicker.value) {
                val initialTime = LocalTime.parse(viewState.autoSleepTimeEnd)
                TimePickerDialog(
                  stringResource(CommonR.string.auto_sleep_timer_end),
                  initialTime.hour,
                  initialTime.minute,
                  { timePickerState: TimePickerState ->
                    listener.setAutoSleepTimerEnd(timePickerState.hour, timePickerState.minute)
                    shouldShowEndTimePicker.value = false
                  },
                  { shouldShowEndTimePicker.value = false },
                )
              }
            }
          }
        }
        Spacer(modifier = Modifier.size(16.dp))

        // Cover
        HeaderRow(stringResource(StringsR.string.cover))
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.inverseOnSurface)
        ) {
          Column {
            SettingsSwitchRow(
              title = stringResource(CommonR.string.pref_scan_chapter_covers),
              subtitle = stringResource(CommonR.string.pref_subtitle_scan_chapter_covers),
              initSwitch = viewState.scanCoverChapter,
              toggle = listener::onScanCoverChapterRowClick,
              paddingTop = 7.dp,
              paddingBottom = 6.dp,
            )
            DividerRow()
            SettingsRow(
              title = stringResource(CommonR.string.pref_delete_cover_files),
              subtitle = stringResource(CommonR.string.pref_subtitle_delete_cover_files),
              value = viewState.sizeCoversDirectory,
              showChevron = true,
              paddingBottom = 8.dp,
              click = { listener.onClearCoversDirectoryRowClick() }
            )
          }
        }
        Spacer(modifier = Modifier.size(16.dp))

        // General
        HeaderRow(stringResource(CommonR.string.pref_category_general))
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.inverseOnSurface)
        ) {
          Column {
            val useGestures = viewState.useGestures
            SettingsSwitchRow(
              title = stringResource(CommonR.string.onboarding_gestures_title),
              initSwitch = useGestures,
              toggle = listener::toggleUseSwipe,
              showFaq = true,
              faq = listener::onUseSwipeFaqClick,
              paddingTop = 5.dp,
            )
            if (useGestures) {
              DividerRow()
              SettingsSwitchRow(
                title = stringResource(CommonR.string.pref_use_haptic_feedback),
                initSwitch = viewState.useHapticFeedback,
                toggle = listener::toggleUseHapticFeedback,
                paddingBottom = 5.dp,
              )
            }
          }
        }
        Spacer(modifier = Modifier.size(16.dp))

        // Other
        HeaderRow(stringResource(CommonR.string.pref_category_other))
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.inverseOnSurface)
        ) {
          Column {
            SettingsRow(
              title = stringResource(StringsR.string.pref_report_issue),
              showChevron = true,
              paddingTop = 8.dp,
              click = { listener.openBugReport() }
            )
            if (viewState.isPro) {
              DividerRow()
              Card(
                modifier = Modifier.padding(horizontal = 4.dp),
                shape = RoundedCornerShape(10.dp),
              ) {
                SettingsRow(
                  title = stringResource(CommonR.string.tipping_jar_title),
                  showChevron = true,
                  color = MaterialTheme.colorScheme.background,
                  paddingStart = 12.dp,
                  paddingEnd = 8.dp,
                  click = { listener.onPurchaseClick() }
                )
              }
            }
            DividerRow()
            AppVersion(appVersion = viewState.appVersion, openAbout = listener::onAboutClick)
          }
        }
        Dialog(viewState, listener)
        Spacer(modifier = Modifier.size(bottom.dp + 24.dp))
      }
    }
  }
}

@Composable
private fun HeaderRow(text: String) {
  Text(
    text = text.uppercase(),
    modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 4.dp, end = 16.dp),
    color = MaterialTheme.colorScheme.primary,
    overflow = TextOverflow.Ellipsis,
    maxLines = 1,
    style = MaterialTheme.typography.bodyMedium,
  )
}



@Composable
private fun DividerRow() {
  HorizontalDivider(
    modifier = Modifier.padding(start = 16.dp, end = 16.dp),
    thickness = Dp.Hairline
  )
}

@ContributesTo(AppScope::class)
interface SettingsComponent {
  val settingsViewModel: SettingsViewModel
}

@Composable
fun Settings() {
  val viewModel = rememberScoped { rootComponentAs<SettingsComponent>().settingsViewModel }
  val viewState = viewModel.viewState()
  val lifecycleOwner = LocalLifecycleOwner.current
  DisposableEffect(lifecycleOwner) {
    val observer = LifecycleEventObserver { _, event ->
      if (event == Lifecycle.Event.ON_RESUME) {
        viewModel.attach()
      }
    }
    lifecycleOwner.lifecycle.addObserver(observer)
    onDispose {
      lifecycleOwner.lifecycle.removeObserver(observer)
    }
  }
  Settings(viewState, viewModel)
}

@Composable
private fun Dialog(
  viewState: SettingsViewState,
  listener: SettingsListener,
) {
  val dialog = viewState.dialog ?: return
  when (dialog) {
    SettingsViewState.Dialog.AutoRewindAmount -> {
      AutoRewindAmountDialog(
        currentSeconds = viewState.autoRewindInSeconds,
        onSecondsConfirm = listener::autoRewindAmountChanged,
        onDismiss = listener::dismissDialog,
      )
    }
    SettingsViewState.Dialog.SeekTime -> {
      SeekAmountDialog(
        currentSeconds = viewState.seekTimeInSeconds,
        onSecondsConfirm = listener::seekAmountChanged,
        onDismiss = listener::dismissDialog,
      )
    }
    SettingsViewState.Dialog.SeekTimeRewind -> {
      SeekAmountDialog(
        currentSeconds = viewState.seekTimeRewindInSeconds,
        forward = false,
        onSecondsConfirm = listener::seekRewindAmountChanged,
        onDismiss = listener::dismissDialog,
      )
    }
    SettingsViewState.Dialog.GridModeDialog -> {
      GridModeDialog(
        checkedItemId = viewState.gridMode,
        onDismiss = listener::dismissDialog,
        onSelected = listener::gridModeDialogChanged,
      )
    }
    SettingsViewState.Dialog.PlayButtonStyleDialog -> {
      PlayButtonStyleDialog(
        checkedItemId = viewState.playButtonStyle,
        onDismiss = listener::dismissDialog,
        onSelected = listener::playButtonStyleDialogChanged,
      )
    }
    SettingsViewState.Dialog.SkipButtonStyleDialog -> {
      SkipButtonStyleDialog(
        checkedItemId = viewState.skipButtonStyle,
        onDismiss = listener::dismissDialog,
        onSelected = listener::skipButtonStyleDialogChanged,
      )
    }
    SettingsViewState.Dialog.MiniPlayerStyleDialog -> {
      MiniPlayerStyleDialog(
        checkedItemId = viewState.miniPlayerStyle,
        onDismiss = listener::dismissDialog,
        onSelected = listener::miniPlayerStyleDialogChanged,
      )
    }
    SettingsViewState.Dialog.PlayerBackgroundDialog -> {
      PlayerBackgroundDialog(
        checkedItemId = viewState.playerBackground,
        onDismiss = listener::dismissDialog,
        onSelected = listener::playerBackgroundDialogChanged,
      )
    }
    SettingsViewState.Dialog.ThemeDialog -> {
      ThemeDialog(
        checkedItemId = viewState.theme,
        onDismiss = listener::dismissDialog,
        onSelected = listener::themeChanged,
      )
    }
    SettingsViewState.Dialog.ThemeWidgetDialog -> {
      ThemeDialog(
        checkedItemId = viewState.themeWidget,
        isWidget = true,
        onDismiss = listener::dismissDialog,
        onSelected = listener::themeWidgetChanged,
      )
    }
    SettingsViewState.Dialog.ColorThemeDialog -> {
      ColorThemeDialog(
        checkedItemId = viewState.colorTheme,
        onDismiss = listener::dismissDialog,
        onSelected = listener::colorThemeChanged,
      )
    }
    SettingsViewState.Dialog.ScanChapterCoverAlertDialog -> {
      AlertSettingDialog(
        text = stringResource(CommonR.string.pref_subtitle_scan_chapter_covers),
        onConfirm = listener::toggleScanCoverChapter,
        onDismiss = listener::dismissDialog,
      )
    }
    SettingsViewState.Dialog.ClearCoversDirectoryAlertDialog -> {
      AlertSettingDialog(
        text = stringResource(CommonR.string.pref_subtitle_delete_cover_files),
        onConfirm = listener::clearCoversDirectory,
        onDismiss = listener::dismissDialog,
      )
    }
  }
}

@Composable
fun Modifier.shake(enabled: Boolean, onAnimationFinish: () -> Unit): Modifier = then(
  composed(
    factory = {
      val distance by animateFloatAsState(
        targetValue = if (enabled) 12f else 0f,
        animationSpec = repeatable(
          iterations = 3,
          animation = tween(durationMillis = 70, easing = LinearEasing),
          repeatMode = RepeatMode.Reverse
        ),
        finishedListener = { onAnimationFinish.invoke() }, label = ""
      )

      Modifier.graphicsLayer {
        translationX = if (enabled) distance else 0f
      }
    },
    inspectorInfo = debugInspectorInfo {
      name = "shake"
      properties["enabled"] = enabled
    }
  )
)
