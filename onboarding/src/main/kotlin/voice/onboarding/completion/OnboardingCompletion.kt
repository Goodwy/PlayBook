package voice.onboarding.completion

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material.icons.rounded.ArrowCircleLeft
import androidx.compose.material.icons.rounded.ArrowCircleRight
import androidx.compose.material.icons.rounded.TouchApp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.squareup.anvil.annotations.ContributesTo
import kotlinx.coroutines.Dispatchers
import voice.common.AppScope
import voice.common.compose.SharedComponent
import voice.common.compose.VoiceTheme
import voice.common.compose.rememberScoped
import voice.common.rootComponentAs
import voice.onboarding.R
import voice.strings.R as StringsR
import voice.common.R as CommonR

@ContributesTo(AppScope::class)
interface OnboardingCompletionComponent {
  val viewModel: OnboardingCompletionViewModel
}

@Composable
fun OnboardingCompletion(
  modifier: Modifier = Modifier,
  isFaq: Boolean = false,
) {
  val viewModel = rememberScoped {
    rootComponentAs<OnboardingCompletionComponent>().viewModel
  }
  OnboardingCompletion(
    modifier = modifier,
    isFaq = isFaq,
    onNext = viewModel::next,
    onBack = viewModel::back,
  )
}

@Composable
private fun OnboardingCompletion(
  preview: Boolean = false,
  onNext: () -> Unit,
  onBack: () -> Unit,
  isFaq: Boolean,
  modifier: Modifier = Modifier,
) {
  val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
  val paddings = if (preview) "0;0;0;0"
      else remember { rootComponentAs<SharedComponent>().paddingPref.flow }.collectAsState(initial = "0;0;0;0", context = Dispatchers.Unconfined).value
  val top = paddings.substringBefore(';').toInt()
  val bottom = paddings.substringAfter(';').substringBefore(';').toInt()
  val start = paddings.substringBeforeLast(';').substringAfterLast(';').toInt()
  val end = paddings.substringAfterLast(';').toInt()
  Scaffold(
    modifier = modifier
      .nestedScroll(scrollBehavior.nestedScrollConnection)
      .padding(start = start.dp, end = end.dp),
    topBar = {
      MediumTopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
          Text(text = stringResource(CommonR.string.onboarding_gestures_title))
        },
        navigationIcon = {
          IconButton(onClick = onBack) {
            Icon(
              imageVector = Icons.AutoMirrored.Rounded.ArrowBackIos,
              contentDescription = stringResource(StringsR.string.close),
            )
          }
        },
        windowInsets = WindowInsets(top = top.dp),
      )
    },
    floatingActionButton = {
      ExtendedFloatingActionButton(
        modifier = modifier
          .padding(bottom = bottom.dp),
        onClick = if (isFaq) onBack else onNext
      ) {
        Text(text = if (isFaq) stringResource(StringsR.string.migration_hint_confirm) else stringResource(StringsR.string.onboarding_completion_next_button))
      }
    },
  ) { contentPadding ->
    val layoutDirection = LocalLayoutDirection.current
    val isRtl = layoutDirection == LayoutDirection.Rtl
    Column(
      Modifier.padding(contentPadding)
        .verticalScroll(rememberScrollState())
    ) {
      Spacer(modifier = Modifier.size(12.dp))
      Card(
        modifier = Modifier.padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.inverseOnSurface)
      ) {
        Row(
          horizontalArrangement = Arrangement.Center,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Image(
            modifier = Modifier
              .padding(vertical = 16.dp)
              .weight(1F)
              .heightIn(max = 200.dp),
            painter = painterResource(id = R.drawable.gestures_overview),
            contentDescription = null,
          )
          Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp).weight(1F),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
          ) {
            Row(
              horizontalArrangement = Arrangement.Start,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                modifier = Modifier.rotate(degrees = 90F),
                imageVector = Icons.Rounded.ArrowCircleLeft,
                contentDescription = stringResource(id = CommonR.string.onboarding_show_player),
                tint = colorResource(CommonR.color.blue)
              )
              Spacer(modifier = Modifier.size(8.dp))
              Text(
                text = stringResource(CommonR.string.onboarding_show_player),
                lineHeight = 14.sp,
                textAlign = TextAlign.Start
              )
            }
            Spacer(modifier = Modifier.size(8.dp))
            Row(
              horizontalArrangement = Arrangement.Start,
              verticalAlignment = Alignment.CenterVertically
            ) {
              val text = if (isRtl) stringResource(StringsR.string.next_track) else stringResource(StringsR.string.previous_track)
              Icon(
                imageVector = Icons.Rounded.ArrowCircleLeft,
                contentDescription = text,
                tint = colorResource(CommonR.color.blue)
              )
              Spacer(modifier = Modifier.size(8.dp))
              Text(
                text = text,
                lineHeight = 14.sp,
                textAlign = TextAlign.Start
              )
            }
            Spacer(modifier = Modifier.size(8.dp))
            Row(
              horizontalArrangement = Arrangement.Start,
              verticalAlignment = Alignment.CenterVertically
            ) {
              val text = if (isRtl) stringResource(StringsR.string.previous_track) else stringResource(StringsR.string.next_track)
              Icon(
                imageVector = Icons.Rounded.ArrowCircleRight,
                contentDescription = text,
                tint = colorResource(CommonR.color.blue)
              )
              Spacer(modifier = Modifier.size(8.dp))
              Text(
                text = text,
                lineHeight = 14.sp,
                textAlign = TextAlign.Start
              )
            }
          }
        }
      }
      Spacer(modifier = Modifier.size(24.dp))
      Card(
        modifier = Modifier.padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.inverseOnSurface)
      ) {
        Row(
          horizontalArrangement = Arrangement.Center,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Image(
            modifier = Modifier
              .padding(vertical = 16.dp)
              .weight(1F)
              .heightIn(max = 200.dp),
            painter = painterResource(id = R.drawable.gestures_player),
            contentDescription = null,
          )
          Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp).weight(1F),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
          ) {
            Row(
              horizontalArrangement = Arrangement.Start,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Rounded.TouchApp,
                contentDescription = stringResource(id = CommonR.string.onboarding_double_tap),
                tint = colorResource(CommonR.color.blue)
              )
              Spacer(modifier = Modifier.size(8.dp))
              Text(
                text = stringResource(CommonR.string.onboarding_double_tap),
                lineHeight = 14.sp,
                textAlign = TextAlign.Start
              )
            }
            Spacer(modifier = Modifier.size(8.dp))
            Row(
              horizontalArrangement = Arrangement.Start,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                modifier = Modifier.rotate(degrees = 90F),
                imageVector = Icons.Rounded.ArrowCircleLeft,
                contentDescription = stringResource(id = CommonR.string.onboarding_show_list_chapters),
                tint = colorResource(CommonR.color.blue)
              )
              Spacer(modifier = Modifier.size(8.dp))
              Text(
                text = stringResource(CommonR.string.onboarding_show_list_chapters),
                lineHeight = 14.sp,
                textAlign = TextAlign.Start
              )
            }
            Spacer(modifier = Modifier.size(8.dp))
            Row(
              horizontalArrangement = Arrangement.Start,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                modifier = Modifier.rotate(degrees = -90F),
                imageVector = Icons.Rounded.ArrowCircleLeft,
                contentDescription = stringResource(id = StringsR.string.close),
                tint = colorResource(CommonR.color.blue)
              )
              Spacer(modifier = Modifier.size(8.dp))
              Text(
                text = stringResource(StringsR.string.close),
                lineHeight = 14.sp,
                textAlign = TextAlign.Start
              )
            }
            Spacer(modifier = Modifier.size(8.dp))
            Row(
              horizontalArrangement = Arrangement.Start,
              verticalAlignment = Alignment.CenterVertically
            ) {
              val text = if (isRtl) stringResource(StringsR.string.next_track) else stringResource(StringsR.string.previous_track)
              Icon(
                imageVector = Icons.Rounded.ArrowCircleLeft,
                contentDescription = text,
                tint = colorResource(CommonR.color.blue)
              )
              Spacer(modifier = Modifier.size(8.dp))
              Text(
                text = text,
                lineHeight = 14.sp,
                textAlign = TextAlign.Start
              )
            }
            Spacer(modifier = Modifier.size(8.dp))
            Row(
              horizontalArrangement = Arrangement.Start,
              verticalAlignment = Alignment.CenterVertically
            ) {
              val text = if (isRtl) stringResource(StringsR.string.previous_track) else stringResource(StringsR.string.next_track)
              Icon(
                imageVector = Icons.Rounded.ArrowCircleRight,
                contentDescription = text,
                tint = colorResource(CommonR.color.blue)
              )
              Spacer(modifier = Modifier.size(8.dp))
              Text(
                text = text,
                lineHeight = 14.sp,
                textAlign = TextAlign.Start
              )
            }
          }
        }
      }
      Spacer(modifier = Modifier.size(bottom.dp + 42.dp))
    }
  }
}

@Composable
@Preview
private fun OnboardingCompletionPreview() {
  VoiceTheme(true) {
    OnboardingCompletion(preview = true, {}, {}, isFaq = false)
  }
}
