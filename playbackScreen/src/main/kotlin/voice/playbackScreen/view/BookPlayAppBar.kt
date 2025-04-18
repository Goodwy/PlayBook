package voice.playbackScreen.view

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import voice.playbackScreen.BookPlayViewState

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
