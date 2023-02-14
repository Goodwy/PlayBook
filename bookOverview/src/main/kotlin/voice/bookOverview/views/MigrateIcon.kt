package voice.bookOverview.views

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CompareArrows
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import voice.bookOverview.R

@Composable
internal fun MigrateIcon(
  withHint: Boolean,
  onClick: () -> Unit,
  onHintClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Box {
    IconButton(modifier = modifier, onClick = onClick) {
      Icon(
        imageVector = Icons.Rounded.CompareArrows,
        contentDescription = stringResource(R.string.migration_hint_title),
        tint = MaterialTheme.colorScheme.primary
      )
    }
    if (withHint) {
      MigrateHint(onHintClick)
    }
  }
}
