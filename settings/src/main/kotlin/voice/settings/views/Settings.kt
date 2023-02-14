package voice.settings.views

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.squareup.anvil.annotations.ContributesTo
import voice.common.AppScope
import voice.common.compose.VoiceTheme
import voice.common.compose.rememberScoped
import voice.common.rootComponentAs
import voice.settings.R
import voice.settings.SettingsListener
import voice.settings.SettingsViewModel
import voice.settings.SettingsViewState

@Composable
@Preview
fun SettingsPreview() {
  val viewState = SettingsViewState(
    useDarkTheme = false,
    showDarkThemePref = true,
    resumeOnReplug = true,
    seekTimeInSeconds = 30,
    seekTimeRewindInSeconds = 20,
    autoRewindInSeconds = 12,
    dialog = null,
    appVersion = "1.2.3",
    useGrid = true,
    gridMode = 2,
    paddings = "0;0;0;0",
    useTransparentNavigation = true,
    playButtonStyle = 2,
    skipButtonStyle = 2,
    miniPlayerStyle = 0,
    playerBackground = 0,
    showSliderVolume = true,
    theme = 0,
    colorTheme = -0x1,
    isPro = false,
  )
  VoiceTheme(preview = true) {
    Settings(
      viewState,
      object : SettingsListener {
        override fun close() {}
        override fun toggleResumeOnReplug() {}
        override fun toggleDarkTheme() {}
        override fun seekAmountChanged(seconds: Int) {}
        override fun seekRewindAmountChanged(seconds: Int) {}
        override fun onSeekAmountRowClicked() {}
        override fun onSeekRewindAmountRowClicked() {}
        override fun autoRewindAmountChanged(seconds: Int) {}
        override fun onAutoRewindRowClicked() {}
        override fun dismissDialog() {}
        override fun openTranslations() {}
        override fun getSupport() {}
        override fun suggestIdea() {}
        override fun openBugReport() {}
        override fun toggleGrid() {}
        override fun gridModeDialog() {}
        override fun gridModeDialogChanged(item: Int) {}
        override fun onGridModeDialogRowClicked() {}
        override fun toggleTransparentNavigation() {}
        override fun onPlayButtonStyleDialogRowClicked() {}
        override fun playButtonStyleDialogChanged(item: Int) {}
        override fun onSkipButtonStyleDialogRowClicked() {}
        override fun skipButtonStyleDialogChanged(item: Int) {}
        override fun onMiniPlayerStyleDialogRowClicked() {}
        override fun miniPlayerStyleDialogChanged(item: Int) {}
        override fun onPlayerBackgroundDialogRowClicked() {}
        override fun playerBackgroundDialogChanged(item: Int) {}
        override fun toggleShowSliderVolume() {}
        override fun themeChanged(theme: Int) {}
        override fun onThemeDialogRowClicked() {}
        override fun colorThemeChanged(color: Int) {}
        override fun onColorThemeDialogRowClicked() {}
        override fun onAboutClick() {}
        override fun onPurchaseClick() {}
      },
    )
  }
}

@Composable
private fun Settings(viewState: SettingsViewState, listener: SettingsListener) {
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
          Text(stringResource(R.string.action_settings))
        },
        scrollBehavior = scrollBehavior,
        navigationIcon = {
          IconButton(
            onClick = {
              listener.close()
            },
          ) {
            Icon(
              imageVector = Icons.Rounded.ArrowBackIosNew,
              contentDescription = stringResource(R.string.close),
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
    ) {
      Column(Modifier.padding(vertical = 8.dp, horizontal = 16.dp)) {
        // Tip jar
        var enabledShake by remember { mutableStateOf(false) }
        if (!viewState.isPro) {
          Row(
            modifier = Modifier
              .shake(enabledShake) { enabledShake = false }
              .clickable { listener.onPurchaseClick() }
              .fillMaxWidth(),
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
                painter = painterResource(id = R.drawable.ic_plus_support),
                contentDescription = stringResource(id = R.string.action_support_project),
                tint = MaterialTheme.colorScheme.primary,
              )
            }
            Column() {
              Text(
                modifier = Modifier.padding(start = 16.dp),
                text = stringResource(R.string.action_support_project),
                fontSize = 18.sp,
              )
              Text(
                modifier = Modifier.padding(start = 16.dp),
                text = stringResource(R.string.action_support_project_summary),
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
                  text = stringResource(R.string.learn_more).toUpperCase(LocaleList.current),
                  fontSize = 12.sp,
                )
              }
            }
          }
          Spacer(modifier = Modifier.size(20.dp))
        }
        // Appearance
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.inverseOnSurface)
        ) {
          Column(modifier = Modifier.padding(bottom = 4.dp)) {
            HeaderRow(stringResource(R.string.pref_category_appearance))
            ThemeRow(viewState.theme, viewState.isPro) {
              if (viewState.isPro) listener.onThemeDialogRowClicked()
              else enabledShake = true
            }
            if (viewState.theme != 2) {
              ColorThemeRow(viewState.isPro) {
                if (viewState.isPro) listener.onColorThemeDialogRowClicked()
                else enabledShake = true
              }
            }
            if (false) { //viewState.showDarkThemePref
              DarkThemeRow(viewState.useDarkTheme, listener::toggleDarkTheme)
            }
            //TransparentNavigationRow(viewState.useTransparentNavigation, listener::toggleTransparentNavigation)
            GridModeRow(viewState.gridMode) {
              listener.onGridModeDialogRowClicked()
            }
            MiniPlayerStyleRow(viewState.miniPlayerStyle) {
              listener.onMiniPlayerStyleDialogRowClicked()
            }
          }
        }
        Spacer(modifier = Modifier.size(16.dp))
        // Appearance player
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.inverseOnSurface)
        ) {
          Column(modifier = Modifier.padding(bottom = 4.dp)) {
            HeaderRow(stringResource(R.string.pref_appearance_ui_player_title))
            PlayerBackgroundRow(viewState.playerBackground) {
              listener.onPlayerBackgroundDialogRowClicked()
            }
            PlayButtonStyleRow(viewState.playButtonStyle) {
              listener.onPlayButtonStyleDialogRowClicked()
            }
            SkipButtonStyleRow(viewState.skipButtonStyle) {
              listener.onSkipButtonStyleDialogRowClicked()
            }
            ShowSliderVolumeRow(viewState.showSliderVolume, listener::toggleShowSliderVolume)
          }
        }
        Spacer(modifier = Modifier.size(16.dp))
        /*ListItem(
          modifier = Modifier.clickable { listener.toggleGrid() },
          leadingContent = {
            val imageVector = if (viewState.useGrid) {
              Icons.Rounded.GridView
            } else {
              Icons.Rounded.ViewList
            }
            Icon(imageVector, stringResource(R.string.pref_use_grid))
          },
          headlineText = { Text(stringResource(R.string.pref_use_grid)) },
          trailingContent = {
            Switch(
              checked = viewState.useGrid,
              onCheckedChange = {
                listener.toggleGrid()
              },
            )
          },
        )*/
        // Playback
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.inverseOnSurface)
        ) {
          Column(modifier = Modifier.padding(bottom = 4.dp)) {
            HeaderRow(stringResource(R.string.music_notification))
            SeekTimeRow(viewState.seekTimeInSeconds) {
              listener.onSeekAmountRowClicked()
            }
            SeekTimeRow(viewState.seekTimeRewindInSeconds, false) {
              listener.onSeekRewindAmountRowClicked()
            }
            AutoRewindRow(viewState.autoRewindInSeconds) {
              listener.onAutoRewindRowClicked()
            }
            ResumeOnReplugRow(viewState.resumeOnReplug, listener::toggleResumeOnReplug)
          }
        }
        Spacer(modifier = Modifier.size(16.dp))
        /*ListItem(
          modifier = Modifier.clickable { listener.suggestIdea() },
          leadingContent = { Icon(Icons.Outlined.Lightbulb, stringResource(R.string.pref_suggest_idea)) },
          headlineText = { Text(stringResource(R.string.pref_suggest_idea)) },
        )
        ListItem(
          modifier = Modifier.clickable { listener.getSupport() },
          leadingContent = { Icon(Icons.Outlined.HelpOutline, stringResource(R.string.pref_get_support)) },
          headlineText = { Text(stringResource(R.string.pref_get_support)) },
        )
        ListItem(
          modifier = Modifier.clickable { listener.openBugReport() },
          leadingContent = { Icon(Icons.Outlined.BugReport, stringResource(R.string.pref_report_issue)) },
          headlineText = { Text(stringResource(R.string.pref_report_issue)) },
        )
        ListItem(
          modifier = Modifier.clickable { listener.openTranslations() },
          leadingContent = { Icon(Icons.Outlined.Language, stringResource(R.string.pref_help_translating)) },
          headlineText = { Text(stringResource(R.string.pref_help_translating)) },
        )*/
        // Other
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.inverseOnSurface)
        ) {
          Column(modifier = Modifier.padding(bottom = 4.dp)) {
            HeaderRow(stringResource(R.string.pref_category_other))
            ListItem(
              modifier = Modifier.clickable { listener.openBugReport() },
              colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.inverseOnSurface),
              headlineText = { Text(stringResource(R.string.pref_report_issue)) },
              trailingContent = { Icon(imageVector = Icons.Rounded.ChevronRight, stringResource(id = R.string.pref_report_issue)) },
            )
            if (viewState.isPro) {
              ListItem(
                modifier = Modifier.clickable { listener.onPurchaseClick() },
                colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.inverseOnSurface),
                headlineText = { Text(stringResource(R.string.tipping_jar_title)) },
                trailingContent = { Icon(imageVector = Icons.Rounded.ChevronRight, stringResource(id = R.string.tipping_jar_title)) },
              )
            }
            AppVersion(appVersion = viewState.appVersion, openAbout = listener::onAboutClick)
          }
        }
        Dialog(viewState, listener)
        Spacer(modifier = Modifier.size(bottom.dp))
      }
    }
  }
}

@Composable
private fun HeaderRow(text: String) {
  Text(
    text = text,
    modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 6.dp, end = 16.dp),
    color = MaterialTheme.colorScheme.primary,
    style = MaterialTheme.typography.bodyLarge,
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
        onSecondsConfirmed = listener::autoRewindAmountChanged,
        onDismiss = listener::dismissDialog,
      )
    }
    SettingsViewState.Dialog.SeekTime -> {
      SeekAmountDialog(
        currentSeconds = viewState.seekTimeInSeconds,
        onSecondsConfirmed = listener::seekAmountChanged,
        onDismiss = listener::dismissDialog,
      )
    }
    SettingsViewState.Dialog.SeekTimeRewind -> {
      SeekAmountDialog(
        currentSeconds = viewState.seekTimeRewindInSeconds,
        forward = false,
        onSecondsConfirmed = listener::seekRewindAmountChanged,
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
    SettingsViewState.Dialog.ColorThemeDialog -> {
      ColorThemeDialog(
        checkedItemId = viewState.colorTheme,
        onDismiss = listener::dismissDialog,
        onSelected = listener::colorThemeChanged,
      )
    }
  }
}

fun Modifier.shake(enabled: Boolean, onAnimationFinish: () -> Unit) = composed(
  factory = {
    val distance by animateFloatAsState(
      targetValue = if (enabled) 12f else 0f,
      animationSpec = repeatable(
        iterations = 3,
        animation = tween(durationMillis = 70, easing = LinearEasing),
        repeatMode = RepeatMode.Reverse
      ),
      finishedListener = { onAnimationFinish.invoke() }
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
