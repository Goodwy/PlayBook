package voice.playbackScreen.view

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import voice.playbackScreen.BookPlayViewState
import voice.playbackScreen.PrefViewState
import voice.strings.R
import java.text.DecimalFormat
import kotlin.time.Duration

@Composable
internal fun BookPlayBottomBar(
  viewState: BookPlayViewState,
  prefViewState: PrefViewState,
  onSleepTimerClick: () -> Unit,
  onAcceptSleepTime: (Int) -> Unit,
  onBookmarkClick: () -> Unit,
  onBookmarkLongClick: () -> Unit,
  onSpeedChangeClick: () -> Unit,
  onSkipSilenceClick: () -> Unit,
  onRepeatClick: () -> Unit,
  onShowChapterNumbersClick: () -> Unit,
  onUseChapterCoverClick: () -> Unit,
  onVolumeBoostClick: () -> Unit,
  onCurrentChapterClick: () -> Unit,
  showOverflowMenu: Boolean = false,
  useLandscapeLayout: Boolean = true,
) {
  val bottom = viewState.paddings.substringAfter(';').substringBefore(';').toInt()
  Column(
    modifier = Modifier
      .fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Row(
      modifier = Modifier
        .padding(start = 12.dp, end = 6.dp)
        .fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween,
    ) {
      IconButton(
        onClick = onSpeedChangeClick,
        modifier = Modifier
          .weight(1F)
      ) {
        var textSpeed = DecimalFormat("0.00x").format(viewState.playbackSpeed)
        if (textSpeed[3].toString() == "0") textSpeed = textSpeed.removeRange(3..3)
        //else if (textSpeed[3].toString() != "0") textSpeed = textSpeed.removeSuffix("x")
        Text(
          text = textSpeed,
          fontSize = 18.sp,
          //fontWeight = FontWeight.Bold,
          letterSpacing = 0.sp,
          textAlign = TextAlign.Center,
          maxLines = 1,
        )
        /*Icon(
      imageVector = Icons.Rounded.Speed,
      contentDescription = stringResource(id = StringsR.string.playback_speed),
    )*/
      }
      IconButton(
        onClick = onRepeatClick,
        modifier = Modifier
          .weight(1F)
      ) {
        val repeatMode = if (true) viewState.repeatModeBook else prefViewState.repeatMode
        Icon(
          painter = when (repeatMode) {
            1 -> painterResource(id = voice.common.R.drawable.ic_repeat_one)
            2 -> painterResource(id = voice.common.R.drawable.ic_repeat_all)
            else -> painterResource(id = voice.common.R.drawable.ic_repeat_off)
          },
          contentDescription = stringResource(id = voice.common.R.string.repeat),
        )
      }
      Box(
        modifier = Modifier
          .weight(1F)
          .size(40.dp)
          .combinedClickable(
            onClick = onSleepTimerClick,
            onLongClick = { onAcceptSleepTime(viewState.customSleepTime) },
            indication = ripple(bounded = false, radius = 20.dp),
            interactionSource = remember { MutableInteractionSource() },
          ),
        contentAlignment = Alignment.Center,
      ) {
        Icon(
          painter = if (!viewState.sleepEoc && viewState.sleepTime == Duration.ZERO) {
            painterResource(id = voice.common.R.drawable.alarm)
          } else {
            painterResource(id = voice.common.R.drawable.alarm_off)
          },
          contentDescription = stringResource(id = R.string.action_sleep),
        )
      }
      Box(
        modifier = Modifier
          .weight(1F)
          .size(40.dp)
          .combinedClickable(
            onClick = onBookmarkClick,
            onLongClick = onBookmarkLongClick,
            indication = ripple(bounded = false, radius = 20.dp),
            interactionSource = remember { MutableInteractionSource() },
          ),
        contentAlignment = Alignment.Center,
      ) {
        Icon(
          imageVector = Icons.Rounded.BookmarkBorder,
          contentDescription = stringResource(id = R.string.bookmark),
        )
      }
      IconButton(
        onClick = onCurrentChapterClick,
        modifier = Modifier
          .weight(1F)
      ) {
        Icon(
          //imageVector = Icons.AutoMirrored.Rounded.FormatListBulleted,
          painter = painterResource(id = voice.common.R.drawable.ic_playlist),
          contentDescription = stringResource(id = voice.common.R.string.onboarding_show_list_chapters),
        )
      }
      if (showOverflowMenu) {
        Box(
          modifier = Modifier
            .weight(1F)
            .size(40.dp),
          contentAlignment = Alignment.Center,
        ) {
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
      }
    }
    val bottomPadding = if (useLandscapeLayout) 8.dp else bottom.dp + 8.dp
    Spacer(modifier = Modifier.size(bottomPadding))
  }
}
