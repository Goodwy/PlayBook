package voice.playbackScreen.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import voice.strings.R

@Composable
internal fun CloseIcon(onCloseClick: () -> Unit) {
  IconButton(onClick = onCloseClick) {
    Icon(
      modifier = Modifier.rotate(degrees = -90F),
      imageVector = Icons.Rounded.ArrowBackIosNew,
      contentDescription = stringResource(id = R.string.close),
    )
  }
}
