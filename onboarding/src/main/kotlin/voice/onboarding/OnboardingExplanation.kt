package voice.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import voice.common.compose.SharedComponent
import voice.common.compose.VoiceTheme
import voice.common.rootComponentAs
import voice.strings.R as StringsR

@Composable
fun OnboardingExplanation(
  preview: Boolean = false,
  onNext: () -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val paddings = if (preview) "0;0;0;0"
  else {
    remember { rootComponentAs<SharedComponent>().paddingPref.flow }.collectAsState(initial = "0;0;0;0", context = Dispatchers.Unconfined).value
  }
  val top = paddings.substringBefore(';').toInt()
  val bottom = paddings.substringAfter(';').substringBefore(';').toInt()
  val start = paddings.substringBeforeLast(';').substringAfterLast(';').toInt()
  val end = paddings.substringAfterLast(';').toInt()
  Scaffold(
    modifier = modifier
      .padding(start = start.dp, end = end.dp),
    topBar = {
      TopAppBar(
        modifier = Modifier.padding(top = top.dp),
        title = { },
        navigationIcon = {
          IconButton(onClick = onBack) {
            Icon(
              imageVector = Icons.AutoMirrored.Rounded.ArrowBackIos,
              contentDescription = stringResource(id = StringsR.string.close),
            )
          }
        },
      )
    },
    floatingActionButton = {
      ExtendedFloatingActionButton(
        modifier = modifier
          .padding(bottom = bottom.dp),
        onClick = onNext
      ) {
        Text(text = stringResource(StringsR.string.onboarding_button_next))
      }
    },
    content = { contentPadding ->
      Column(Modifier.padding(contentPadding)) {
        if (LocalConfiguration.current.screenHeightDp > 500) {
          Image(
            modifier = Modifier
              .padding(top = 32.dp, bottom = 24.dp, start = 24.dp, end = 24.dp)
              .weight(1F)
              .heightIn(max = 400.dp)
              .padding(horizontal = 32.dp)
              .align(CenterHorizontally),
            painter = painterResource(id = R.drawable.bookshelf_artwork),
            contentDescription = null,
          )
        }
        Column(Modifier.weight(2F)) {
          Spacer(modifier = Modifier.size(16.dp))
          Text(
            modifier = Modifier.padding(horizontal = 24.dp),
            text = stringResource(StringsR.string.onboarding_explanation_title),
            style = MaterialTheme.typography.displayMedium,
          )
          Spacer(modifier = Modifier.size(4.dp))
          Text(
            modifier = Modifier.padding(horizontal = 24.dp),
            text = stringResource(StringsR.string.onboarding_explanation_subtitle),
            style = MaterialTheme.typography.bodyLarge,
          )
        }
      }
    },
  )
}

@Composable
@Preview
private fun OnboardingExplanationPreview() {
  VoiceTheme(true) {
    OnboardingExplanation(preview = true, onNext = {}, onBack = {})
  }
}
