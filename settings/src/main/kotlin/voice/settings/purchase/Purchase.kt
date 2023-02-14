package voice.settings.purchase

import android.widget.TextView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Circle
import androidx.compose.material.icons.rounded.Policy
import androidx.compose.material.icons.rounded.Shop
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import com.squareup.anvil.annotations.ContributesTo
import voice.common.AppScope
import voice.common.compose.VoiceTheme
import voice.common.compose.rememberScoped
import voice.common.rootComponentAs
import voice.settings.R

@Composable
@Preview
fun PurchasePreview() {
  val viewState = PurchaseViewState(
    appVersion = "1.2.3",
    paddings = "0;0;0;0",
    prices = "[1$, 2$, 3$]",
    isPro = true,
  )
  VoiceTheme(preview = true) {
    Purchase(
      viewState,
      object : PurchaseListener {
        override fun close() {}
        override fun onPurchaseClick() {}
        override fun onSmallClick() {}
        override fun onMediumClick() {}
        override fun onBigClick() {}
      },
    )
  }
}

@Composable
private fun Purchase(viewState: PurchaseViewState, listener: PurchaseListener) {
  val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

  val top = viewState.paddings.substringBefore(';').toInt()
  val bottom = viewState.paddings.substringAfter(';').substringBefore(';').toInt()
  val start = viewState.paddings.substringBeforeLast(';').substringAfterLast(';').toInt()
  val end = viewState.paddings.substringAfterLast(';').toInt()

  val priceOne = viewState.prices.substringBefore(',').replace("[", "").trim()
  val priceTwo = viewState.prices.substringAfter(',').substringBefore(',').trim()
  val priceThree = viewState.prices.substringAfterLast(',').replace("]", "").trim()
  Scaffold(
    modifier = Modifier
      .nestedScroll(scrollBehavior.nestedScrollConnection)
      .padding(start = start.dp, end = end.dp),
    containerColor = MaterialTheme.colorScheme.inverseOnSurface,
    contentColor = contentColorFor(MaterialTheme.colorScheme.background),
    topBar = {
      CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.largeTopAppBarColors(containerColor = MaterialTheme.colorScheme.inverseOnSurface, scrolledContainerColor = MaterialTheme.colorScheme.inverseOnSurface),
        scrollBehavior = scrollBehavior,
        title = {
          //Text(text = stringResource(R.string.action_support_project))
        },
        navigationIcon = {
          IconButton(
            onClick = {
              listener.close()
            },
          ) {
            Icon(
              imageVector = Icons.Rounded.ArrowBackIosNew,
              contentDescription = stringResource(R.string.close),
            )
          }
        },
        windowInsets = WindowInsets(top = top.dp),
      )
    },
  ) { contentPadding ->
    Box(
      Modifier
        .background(MaterialTheme.colorScheme.inverseOnSurface)
        .padding(contentPadding)
        .verticalScroll(rememberScrollState())
    ) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        Card(
          shape = RoundedCornerShape(24.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
          elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
          Icon(
            modifier = Modifier
              .size(108.dp)
              .padding(12.dp),
            painter = painterResource(id = R.drawable.ic_plus_support),
            contentDescription = stringResource(id = R.string.action_support_project),
            tint = MaterialTheme.colorScheme.primary,
          )
        }
        Spacer(modifier = Modifier.size(16.dp))
        Text(
          text = stringResource(R.string.action_support_project),
          fontSize = 18.sp,
          textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.size(16.dp))
        Row(
          modifier = Modifier
            .wrapContentWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center,
        ) {
          Spacer(modifier = Modifier.size(16.dp))
          Card(
            modifier = Modifier.weight(1f).wrapContentSize(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
          ) {
            Column(
              modifier = Modifier
                .heightIn(max = 128.dp)
                .aspectRatio(1f, true)
                .padding(horizontal = 8.dp, vertical = 4.dp),
              verticalArrangement = Arrangement.Bottom,
              horizontalAlignment = Alignment.CenterHorizontally,
            ) {
              Text(
                modifier = Modifier.height(32.dp).padding(horizontal = 2.dp),
                text = stringResource(R.string.tip_kind),
                fontSize = 14.sp,
                lineHeight = 14.sp,
                textAlign = TextAlign.Center,
                maxLines = 3,
              )
              Spacer(modifier = Modifier.size(6.dp))
              Button(
                modifier = Modifier.fillMaxWidth().alpha(0.6f),
                enabled = priceOne != "0",
                onClick = { listener.onSmallClick() },
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
              ) {
                Text(
                  text = if (priceOne != "0") priceOne else stringResource(R.string.tipping_jar_dialog_error_title),
                  overflow = TextOverflow.Ellipsis,
                  maxLines = 1,
                )
              }
            }
          }
          Spacer(modifier = Modifier.size(16.dp))
          Card(
            modifier = Modifier.weight(1f).wrapContentSize(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
          ) {
            Column(
              modifier = Modifier
                .heightIn(max = 128.dp)
                .aspectRatio(1f, true)
                .padding(horizontal = 8.dp, vertical = 4.dp),
              verticalArrangement = Arrangement.Bottom,
              horizontalAlignment = Alignment.CenterHorizontally,
            ) {
              Text(
                modifier = Modifier.height(32.dp).padding(horizontal = 2.dp),
                text = stringResource(R.string.tip_excellent),
                fontSize = 14.sp,
                lineHeight = 14.sp,
                textAlign = TextAlign.Center,
                maxLines = 3,
              )
              Spacer(modifier = Modifier.size(6.dp))
              Button(
                modifier = Modifier.fillMaxWidth().alpha(0.75f),
                enabled = priceTwo != "0",
                onClick = { listener.onMediumClick() },
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
              ) {
                Text(
                  text = if (priceTwo != "0") priceTwo else stringResource(R.string.tipping_jar_dialog_error_title),
                  overflow = TextOverflow.Ellipsis,
                  maxLines = 1,
                )
              }
            }
          }
          Spacer(modifier = Modifier.size(16.dp))
          Card(
            modifier = Modifier.weight(1f).wrapContentSize(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
          ) {
            Column(
              modifier = Modifier
                .heightIn(max = 128.dp)
                .aspectRatio(1f, true)
                .padding(horizontal = 8.dp, vertical = 4.dp),
              verticalArrangement = Arrangement.Bottom,
              horizontalAlignment = Alignment.CenterHorizontally,
            ) {
              Text(
                modifier = Modifier.height(32.dp).padding(horizontal = 2.dp),
                text = stringResource(R.string.tip_incredible),
                fontSize = 14.sp,
                lineHeight = 14.sp,
                textAlign = TextAlign.Center,
                maxLines = 3,
              )
              Spacer(modifier = Modifier.size(6.dp))
              Button(
                modifier = Modifier.fillMaxWidth().alpha(0.9f),
                enabled = priceThree != "0",
                onClick = { listener.onBigClick() },
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
              ) {
                Text(
                  text = if (priceThree != "0") priceThree else stringResource(R.string.tipping_jar_dialog_error_title),
                  overflow = TextOverflow.Ellipsis,
                  maxLines = 1,
                )
              }
            }
          }
          Spacer(modifier = Modifier.size(16.dp))
        }
        Spacer(modifier = Modifier.size(24.dp))
        // Dark Theme
        if (!viewState.isPro) {
          ListItem(
            colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.inverseOnSurface),
            leadingContent = {
              Icon(
                modifier = Modifier.size(62.dp),
                painter = painterResource(id = R.drawable.ic_invert_colors),
                contentDescription = stringResource(id = R.string.pref_theme_dark),
                tint = MaterialTheme.colorScheme.primary,
              )
            },
            headlineText = {
              Text(
                text = stringResource(R.string.pref_theme_dark),
                fontSize = 18.sp,
              )
            },
            supportingText = {
              Text(
                text = stringResource(R.string.theme_summary),
                lineHeight = 16.sp,
                color = LocalContentColor.current.copy(alpha = 0.5F),
              )
            },
          )
          Spacer(modifier = Modifier.size(16.dp))
          // Colors
          ListItem(
            colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.inverseOnSurface),
            leadingContent = {
              Icon(
                modifier = Modifier.size(62.dp),
                painter = painterResource(id = R.drawable.ic_palette),
                contentDescription = stringResource(id = R.string.color_title),
                tint = MaterialTheme.colorScheme.primary,
              )
            },
            headlineText = {
              Text(
                text = stringResource(R.string.color_title),
                fontSize = 18.sp,
              )
            },
            supportingText = {
              Text(
                text = stringResource(R.string.color_summary),
                lineHeight = 16.sp,
                color = LocalContentColor.current.copy(alpha = 0.5F),
              )
            },
          )
          Spacer(modifier = Modifier.size(16.dp))
        }
        // Support us
        ListItem(
          colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.inverseOnSurface),
          leadingContent = {
            Icon(
              modifier = Modifier.size(62.dp).padding(5.dp),
              painter = painterResource(id = R.drawable.ic_plus_round),
              contentDescription = stringResource(id = R.string.plus_title),
              tint = MaterialTheme.colorScheme.primary,
            )
          },
          headlineText = {
            Text(
              text = stringResource(R.string.plus_title),
              fontSize = 18.sp,
            )
          },
          supportingText = {
            Text(
              text = stringResource(R.string.plus_summary),
              lineHeight = 16.sp,
              color = LocalContentColor.current.copy(alpha = 0.5F),
            )
          },
        )
        // Participants
        Spacer(modifier = Modifier.size(24.dp))
        Image(
          modifier = Modifier.size(82.dp),
          painter = painterResource(id = R.drawable.logo_goodwy),
          contentDescription = null
        )
        Spacer(modifier = Modifier.size(bottom.dp))
      }
    }
  }
}

@Composable
fun HtmlText(html: String, modifier: Modifier = Modifier) {
  AndroidView(
    modifier = modifier.padding(horizontal = 8.dp),
    factory = { context -> TextView(context) },
    update = { it.text = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_COMPACT) }
  )
}

@ContributesTo(AppScope::class)
interface PurchaseComponent {
  val purchaseViewModel: PurchaseViewModel
}

@Composable
fun Purchase() {
  val viewModel = rememberScoped { rootComponentAs<PurchaseComponent>().purchaseViewModel }
  val viewState = viewModel.viewState()
  Purchase(viewState, viewModel)
}
