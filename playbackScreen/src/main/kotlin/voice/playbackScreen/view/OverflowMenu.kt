package voice.playbackScreen.view

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import voice.strings.R

@Composable
internal fun OverflowMenu(
  skipSilence: Boolean,
  onSkipSilenceClick: () -> Unit,
  showChapterNumbers: Boolean,
  onShowChapterNumbersClick: () -> Unit,
  useChapterCover: Boolean,
  scanCoverChapter: Boolean,
  onUseChapterCoverClick: () -> Unit,
  onVolumeBoostClick: () -> Unit,
) {
  Box {
    var expanded by remember { mutableStateOf(false) }
    IconButton(
      onClick = {
        expanded = !expanded
      },
    ) {
      Icon(
        imageVector = Icons.Rounded.MoreHoriz,
        contentDescription = stringResource(id = R.string.more),
      )
    }
    DropdownMenu(
      expanded = expanded,
      onDismissRequest = { expanded = false },
    ) {
      DropdownMenuItem(
        onClick = {
          expanded = false
          onSkipSilenceClick()
        },
        text = {
          Text(text = stringResource(id = R.string.skip_silence))
        },
        trailingIcon = {
          Checkbox(
            checked = skipSilence,
            onCheckedChange = {
              expanded = false
              onSkipSilenceClick()
            },
          )
        },
      )
      DropdownMenuItem(
        onClick = {
          expanded = false
          onShowChapterNumbersClick()
        },
        text = {
          Text(text = stringResource(id = voice.common.R.string.show_chapter_numbers))
        },
        trailingIcon = {
          Checkbox(
            checked = showChapterNumbers,
            onCheckedChange = {
              expanded = false
              onShowChapterNumbersClick()
            },
          )
        },
      )
      if (scanCoverChapter) DropdownMenuItem(
        onClick = {
          expanded = false
          onUseChapterCoverClick()
        },
        text = {
          Text(text = stringResource(id = voice.common.R.string.pref_use_chapter_cover))
        },
        trailingIcon = {
          Checkbox(
            checked = useChapterCover,
            onCheckedChange = {
              expanded = false
              onUseChapterCoverClick()
            },
          )
        },
      )
      DropdownMenuItem(
        onClick = {
          expanded = false
          onVolumeBoostClick()
        },
        text = {
          Text(text = stringResource(id = R.string.volume_boost))
        },
      )
    }
  }
}
