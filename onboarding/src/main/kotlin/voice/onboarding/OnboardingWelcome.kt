package voice.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import voice.common.R as CommonR

@Composable
fun OnboardingWelcome(
  modifier: Modifier = Modifier,
  preview: Boolean = false,
  onNext: () -> Unit,
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
    floatingActionButton = {
      ExtendedFloatingActionButton(
        modifier = modifier
          .padding(bottom = bottom.dp),
        onClick = onNext
      ) {
        Text(text = stringResource(StringsR.string.onboarding_button_next))
      }
    },
    topBar = {
      TopAppBar(
        modifier = Modifier.padding(top = top.dp),
        title = { }
      )
    },
    content = { contentPadding ->
      Column(Modifier.padding(contentPadding), verticalArrangement = Arrangement.Center) {
        if (LocalConfiguration.current.screenHeightDp > 440) {
          Image(
            modifier = Modifier
            .weight(1F)
            .padding(top = 32.dp, bottom = 24.dp, start = 24.dp, end = 24.dp)
            .heightIn(max = 400.dp)
            .aspectRatio(1F)
            .fillMaxSize()
            .align(Alignment.CenterHorizontally),
            //.clip(CircleShape),
          painter = painterResource(id = R.drawable.welcome),
          contentDescription = null,
          )
        }
        Column(Modifier.weight(1F)) {
          Spacer(modifier = Modifier.size(16.dp))
          Text(
            modifier = Modifier.padding(horizontal = 32.dp),
            text = stringResource(CommonR.string.onboarding_welcome_title_g),
            style = MaterialTheme.typography.displayMedium,
          )
          Spacer(modifier = Modifier.size(4.dp))
          Text(
            modifier = Modifier.padding(horizontal = 32.dp),
            text = stringResource(id = StringsR.string.onboarding_welcome_subtitle),
            style = MaterialTheme.typography.bodyLarge,
          )
        }
      }
    },
  )
}

@Composable
@Preview
private fun OnboardingWelcomePreview() {
  VoiceTheme(true) {
    OnboardingWelcome(
      preview = true,
      onNext = {},
    )
  }
}
