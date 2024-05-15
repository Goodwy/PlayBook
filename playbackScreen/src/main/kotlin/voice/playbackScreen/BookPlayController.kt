package voice.playbackScreen

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.content.res.Configuration.SCREENLAYOUT_SIZE_NORMAL
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.DisplayMetrics
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import com.squareup.anvil.annotations.ContributesTo
import voice.common.AppScope
import voice.common.BookId
import voice.common.compose.ComposeController
import voice.common.rootComponentAs
import voice.data.getBookId
import voice.data.putBookId
import voice.logging.core.Logger
import voice.playbackScreen.view.BookPlayView
import voice.playbackScreen.view.jumpToPosition.JumpToPosition
import voice.sleepTimer.SleepTimerDialog
import voice.strings.R as StringsR

private const val NI_BOOK_ID = "niBookId"

class BookPlayController(bundle: Bundle) : ComposeController(bundle) {

  constructor(bookId: BookId) : this(Bundle().apply { putBookId(NI_BOOK_ID, bookId) })

  private val bookId = bundle.getBookId(NI_BOOK_ID)!!
  private val viewModel: BookPlayViewModel = rootComponentAs<Component>()
    .bookPlayViewModelFactory
    .create(bookId)

  @Composable
  override fun Content() {
    val snackbarHostState = remember { SnackbarHostState() }
    val dialogState = viewModel.dialogState.value
    val viewState = viewModel.viewState()
      ?: return
    val prefViewState = PrefViewState(
      repeatMode = viewModel.state().repeatMode
    )
    val context = LocalContext.current
    LaunchedEffect(viewModel) {
      viewModel.viewEffects.collect { viewEffect ->
        when (viewEffect) {
          BookPlayViewEffect.BookmarkAdded -> {
            snackbarHostState.showSnackbar(message = context.getString(StringsR.string.bookmark_added))
          }

          BookPlayViewEffect.RequestIgnoreBatteryOptimization -> {
            val result = snackbarHostState.showSnackbar(
              message = context.getString(StringsR.string.battery_optimization_rationale),
              duration = SnackbarDuration.Long,
              actionLabel = context.getString(StringsR.string.battery_optimization_action),
            )
            if (result == SnackbarResult.ActionPerformed) {
              toBatteryOptimizations()
            }
          }
        }
      }
    }
    BookPlayView(
      viewState,
      prefViewState,
      onPlayClick = viewModel::playPause,
      onFastForwardClick = viewModel::fastForward,
      onRewindClick = viewModel::rewind,
      onSeek = viewModel::seekTo,
      onSeekVolume = viewModel::seekVolumeTo,
      onBookmarkClick = viewModel::onBookmarkClicked,
      onBookmarkLongClick = viewModel::onBookmarkLongClicked,
      onSkipSilenceClick = viewModel::toggleSkipSilence,
      onRepeatClick = viewModel::toggleRepeat,
      onShowChapterNumbersClick = viewModel::toggleShowChapterNumbers,
      onUseChapterCoverClick = viewModel::toggleUseChapterCover,
      onSleepTimerClick = viewModel::toggleSleepTimer,
      onAcceptSleepTime = viewModel::onAcceptSleepTime,
      onVolumeBoostClick = viewModel::onVolumeGainIconClicked,
      onSpeedChangeClick = viewModel::onPlaybackSpeedIconClicked,
      onCloseClick = { router.popController(this@BookPlayController) },
      onSkipToNext = viewModel::next,
      onSkipToPrevious = viewModel::previous,
      //onCurrentChapterClick = viewModel::onCurrentChapterClicked,
      useLandscapeLayout = LocalConfiguration.current.orientation == ORIENTATION_LANDSCAPE,
      isNotLong = isNotLong(), //LocalConfiguration.current.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK < SCREENLAYOUT_SIZE_LARGE,
      isSmallScreen = isSmallScreen(),
      snackbarHostState = snackbarHostState,
      onCurrentTimeClick = viewModel::onCurrentTimeClicked,
      onChapterClick = viewModel::onChapterClicked,
    )
    if (dialogState != null) {
      when (dialogState) {
        is BookPlayDialogViewState.SpeedDialog -> {
          SpeedDialog(dialogState, viewModel)
        }

        is BookPlayDialogViewState.VolumeGainDialog -> {
          VolumeGainDialog(dialogState, viewModel)
        }

        is BookPlayDialogViewState.SelectChapterDialog -> {
          SelectChapterDialog(dialogState, viewModel)
        }

        is BookPlayDialogViewState.SleepTimer -> {
          SleepTimerDialog(
            viewState = dialogState.viewState,
            onDismiss = viewModel::dismissDialog,
            onIncrementSleepTime = viewModel::incrementSleepTime,
            onDecrementSleepTime = viewModel::decrementSleepTime,
            onAcceptSleepTime = viewModel::onAcceptSleepTime,
            onAcceptSleepEoc = viewModel::onAcceptSleepEoc,
          )
        }

        is BookPlayDialogViewState.JumpToPosition -> {
          JumpToPosition(
            onDismiss = viewModel::dismissDialog,
            onPositionSelected = viewModel::onJumpToPositionTimeSelected,
          )
        }
      }
    }
  }

  private fun toBatteryOptimizations() {
    val intent = Intent()
      .apply {
        @Suppress("BatteryLife")
        action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
        data = Uri.parse("package:${activity!!.packageName}")
      }
    try {
      startActivity(intent)
    } catch (e: ActivityNotFoundException) {
      Logger.e(e, "Can't request ignoring battery optimizations")
    }
  }

  private fun isNotLong(): Boolean {
    val height: Double
    val width: Double
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
      val windowMetrics = activity!!.windowManager.currentWindowMetrics
      val rect: Rect = windowMetrics.bounds
      width = rect.right.toDouble()
      height = rect.bottom.toDouble()
    } else {
      val displayMetrics = DisplayMetrics()
      @Suppress("DEPRECATION")
      activity!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
      height = displayMetrics.heightPixels.toDouble()
      width = displayMetrics.widthPixels.toDouble()
    }
    return if (width > height) width/height < 2 else height/width < 2
  }

  private fun isSmallScreen(): Boolean {
    val screenSize = resources!!.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK
    if (screenSize > SCREENLAYOUT_SIZE_NORMAL) return false //is tablet
    else {
      val height: Double
      val width: Double
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowMetrics = activity!!.windowManager.currentWindowMetrics
        val rect: Rect = windowMetrics.bounds
        width = rect.right.toDouble()
        height = rect.bottom.toDouble()
      } else {
        val displayMetrics = DisplayMetrics()
        @Suppress("DEPRECATION")
        activity!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        height = displayMetrics.heightPixels.toDouble()
        width = displayMetrics.widthPixels.toDouble()
      }
      return if (width > height) width / height < 1.5 else height / width < 1.5
    }
  }

  @ContributesTo(AppScope::class)
  interface Component {
    val bookPlayViewModelFactory: BookPlayViewModel.Factory
  }
}
