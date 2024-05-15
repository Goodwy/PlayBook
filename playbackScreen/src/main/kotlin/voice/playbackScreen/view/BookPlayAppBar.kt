package voice.playbackScreen.view

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bedtime
import androidx.compose.material.icons.outlined.BedtimeOff
import androidx.compose.material.icons.outlined.CollectionsBookmark
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import voice.playbackScreen.BookPlayViewState
import voice.strings.R
import kotlin.time.Duration

@Composable
internal fun BookPlayAppBar(
  viewState: BookPlayViewState,
  //onSleepTimerClick: () -> Unit,
  //onBookmarkClick: () -> Unit,
  //onBookmarkLongClick: () -> Unit,
  //onSpeedChangeClick: () -> Unit,
  onSkipSilenceClick: () -> Unit,
  onShowChapterNumbersClick: () -> Unit,
  onUseChapterCoverClick: () -> Unit,
  onVolumeBoostClick: () -> Unit,
  onCloseClick: () -> Unit,
  useLandscapeLayout: Boolean,
) {
  val appBarActions: @Composable RowScope.() -> Unit = {
    OverflowMenu(
      skipSilence = viewState.skipSilence,
      onSkipSilenceClick = onSkipSilenceClick,
      showChapterNumbers = viewState.showChapterNumbers,
      onShowChapterNumbersClick = onShowChapterNumbersClick,
      useChapterCover = viewState.useChapterCover,
      scanCoverChapter = viewState.scanCoverChapter,
      onUseChapterCoverClick = onUseChapterCoverClick,
      onVolumeBoostClick = onVolumeBoostClick,
    )
  }
  if (useLandscapeLayout) {
    TopAppBar(
      navigationIcon = {
        CloseIcon(onCloseClick)
      },
      actions = appBarActions,
      title = {
        AppBarTitle(viewState)
      },
      colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
    )
  } else {
    TopAppBar(
      navigationIcon = {
        CloseIcon(onCloseClick)
      },
      actions = appBarActions,
      title = {
        AppBarTitle(viewState)
      },
      colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
    )
  }
}
