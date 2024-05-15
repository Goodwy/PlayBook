package voice.settings.purchase

import android.util.TypedValue
import android.view.Gravity
import android.widget.TextView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.takeOrElse
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import com.squareup.anvil.annotations.ContributesTo
import voice.common.AppScope
import voice.common.R
import voice.common.compose.VoiceTheme
import voice.common.compose.rememberScoped
import voice.common.constants.THEME_LIGHT
import voice.common.rootComponentAs
import voice.strings.R as StringsR
import voice.common.R as CommonR

@Composable
@Preview
fun PurchasePreview() {
  val viewState = PurchaseViewState(
    appVersion = "1.2.3",
    paddings = "0;0;0;0",
    prices = "2$;5$;10$",
    pricesSubs = "1$;2$;3$;10$;20$;30$",
    pricesRustore = "150 RUB;300 RUB;450 RUB;150 RUB;150 RUB;150 RUB;150 RUB;150 RUB;150 RUB",
    purchasedList = "0;1;0",
    purchasedSubsList = "0;0;0;0;0;1",
    purchasedListRustore = "0;0;1;0;0;0;1;0;0",
    isPro = false,
    isPlayStoreInstalled = false,
    isRuStoreInstalled = true,
    useGooglePlay = false,
    theme = THEME_LIGHT,
  )
  VoiceTheme(preview = true) {
    Purchase(
      viewState,
      object : PurchaseListener {
        override fun close() {}
        override fun onPurchaseClick(usePlayStore: Boolean, tip: Int) {}
        override fun onRefreshPurchase(usePlayStore: Boolean) {}
        override fun themeChanged() {}
        override fun onUrlClick(url: String) {}
        override fun togglePro() {}
        override fun onChangeStore() {}
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

  val pricesList = viewState.prices.split(";").toTypedArray()
  val pricesValid = pricesList.size == 3
  val pricesSubsList = viewState.pricesSubs.split(";").toTypedArray()
  val pricesSubsValid = pricesSubsList.size == 6
  val purchasedList = viewState.purchasedList.split(";").toTypedArray()
  val purchasedSubsList = viewState.purchasedSubsList.split(";").toTypedArray()

  val pricesListRustore = viewState.pricesRustore.split(";").toTypedArray()
  val pricesValidRustore = pricesListRustore.size == 9
  val purchasedListRustore = viewState.purchasedListRustore.split(";").toTypedArray()

  val useGooglePlay = viewState.useGooglePlay

  val prices1 = if (useGooglePlay) {
    if (pricesValid) pricesList[0] else stringResource(CommonR.string.tipping_jar_dialog_error_title)
  } else {
    if (pricesValidRustore) pricesListRustore[0] else stringResource(CommonR.string.tipping_jar_dialog_error_title)
  }
  val prices2 = if (useGooglePlay) {
    if (pricesValid) pricesList[1] else stringResource(CommonR.string.tipping_jar_dialog_error_title)
  } else {
    if (pricesValidRustore) pricesListRustore[1] else stringResource(CommonR.string.tipping_jar_dialog_error_title)
  }
  val prices3 = if (useGooglePlay) {
    if (pricesValid) pricesList[2] else stringResource(CommonR.string.tipping_jar_dialog_error_title)
  } else {
    if (pricesValidRustore) pricesListRustore[2] else stringResource(CommonR.string.tipping_jar_dialog_error_title)
  }
  val prices4 = if (useGooglePlay) {
    if (pricesSubsValid) stringResource(id = R.string.per_month, pricesSubsList[0]) else stringResource(CommonR.string.tipping_jar_dialog_error_title)
  } else {
    if (pricesValidRustore) stringResource(id = R.string.per_month, pricesListRustore[3]) else stringResource(CommonR.string.tipping_jar_dialog_error_title)
  }
  val prices5 = if (useGooglePlay) {
    if (pricesSubsValid) stringResource(id = R.string.per_month, pricesSubsList[1]) else stringResource(CommonR.string.tipping_jar_dialog_error_title)
  } else {
    if (pricesValidRustore) stringResource(id = R.string.per_month, pricesListRustore[4]) else stringResource(CommonR.string.tipping_jar_dialog_error_title)
  }
  val prices6 = if (useGooglePlay) {
    if (pricesSubsValid) stringResource(id = R.string.per_month, pricesSubsList[2]) else stringResource(CommonR.string.tipping_jar_dialog_error_title)
  } else {
    if (pricesValidRustore) stringResource(id = R.string.per_month, pricesListRustore[5]) else stringResource(CommonR.string.tipping_jar_dialog_error_title)
  }
  val prices7 = if (useGooglePlay) {
    if (pricesSubsValid) stringResource(id = R.string.per_month, pricesSubsList[3]) else stringResource(CommonR.string.tipping_jar_dialog_error_title)
  } else {
    if (pricesValidRustore) stringResource(id = R.string.per_month, pricesListRustore[6]) else stringResource(CommonR.string.tipping_jar_dialog_error_title)
  }
  val prices8 = if (useGooglePlay) {
    if (pricesSubsValid) stringResource(id = R.string.per_month, pricesSubsList[4]) else stringResource(CommonR.string.tipping_jar_dialog_error_title)
  } else {
    if (pricesValidRustore) stringResource(id = R.string.per_month, pricesListRustore[7]) else stringResource(CommonR.string.tipping_jar_dialog_error_title)
  }
  val prices9 = if (useGooglePlay) {
    if (pricesSubsValid) stringResource(id = R.string.per_month, pricesSubsList[5]) else stringResource(CommonR.string.tipping_jar_dialog_error_title)
  } else {
    if (pricesValidRustore) stringResource(id = R.string.per_month, pricesListRustore[8]) else stringResource(CommonR.string.tipping_jar_dialog_error_title)
  }

  val isPurchased1 = if (useGooglePlay) purchasedList[0] == "1" else purchasedListRustore[0] == "1"
  val isPurchased2 = if (useGooglePlay) purchasedList[1] == "1" else purchasedListRustore[1] == "1"
  val isPurchased3 = if (useGooglePlay) purchasedList[2] == "1" else purchasedListRustore[2] == "1"
  val isPurchased4 = if (useGooglePlay) purchasedSubsList[0] == "1" else purchasedListRustore[3] == "1"
  val isPurchased5 = if (useGooglePlay) purchasedSubsList[1] == "1" else purchasedListRustore[4] == "1"
  val isPurchased6 = if (useGooglePlay) purchasedSubsList[2] == "1" else purchasedListRustore[5] == "1"
  val isPurchased7 = if (useGooglePlay) purchasedSubsList[3] == "1" else purchasedListRustore[6] == "1"
  val isPurchased8 = if (useGooglePlay) purchasedSubsList[4] == "1" else purchasedListRustore[7] == "1"
  val isPurchased9 = if (useGooglePlay) purchasedSubsList[5] == "1" else purchasedListRustore[8] == "1"

  val isEnabled1 = if (useGooglePlay) pricesValid else pricesValidRustore && !isPurchased1
  val isEnabled2 = if (useGooglePlay) pricesValid else pricesValidRustore && !isPurchased2
  val isEnabled3 = if (useGooglePlay) pricesValid else pricesValidRustore && !isPurchased3
  val isEnabled4 = if (useGooglePlay) pricesSubsValid else pricesValidRustore && !isPurchased4
  val isEnabled5 = if (useGooglePlay) pricesSubsValid else pricesValidRustore && !isPurchased5
  val isEnabled6 = if (useGooglePlay) pricesSubsValid else pricesValidRustore && !isPurchased6
  val isEnabled7 = if (useGooglePlay) pricesSubsValid else pricesValidRustore && !isPurchased7
  val isEnabled8 = if (useGooglePlay) pricesSubsValid else pricesValidRustore && !isPurchased8
  val isEnabled9 = if (useGooglePlay) pricesSubsValid else pricesValidRustore && !isPurchased9

  Scaffold(
    modifier = Modifier
      .nestedScroll(scrollBehavior.nestedScrollConnection)
      .padding(start = start.dp, end = end.dp),
    containerColor = MaterialTheme.colorScheme.inverseOnSurface,
    contentColor = contentColorFor(MaterialTheme.colorScheme.background),
    topBar = {
      val appBarActions: @Composable RowScope.() -> Unit = {
        if (viewState.isPlayStoreInstalled || viewState.isRuStoreInstalled) {
          OverflowMenu(
            showStoreChange = viewState.isPlayStoreInstalled && viewState.isRuStoreInstalled,
            useGooglePlay = viewState.useGooglePlay,
            isRuStore = !viewState.useGooglePlay && viewState.isRuStoreInstalled,
            onChangeStore = { listener.onChangeStore() },
            onRefreshPurchase = { listener.onRefreshPurchase(viewState.useGooglePlay) },
            onUrlClick = { listener.onUrlClick("rustore://profile/subscriptions") },
          )
        }
      }
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
              imageVector = Icons.AutoMirrored.Rounded.ArrowBackIos,
              contentDescription = stringResource(StringsR.string.close),
            )
          }
        },
        actions = appBarActions,
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
        Modifier.fillMaxWidth(),
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
            painter = painterResource(id = CommonR.drawable.ic_plus_support),
            contentDescription = stringResource(id = CommonR.string.action_support_project),
            tint = MaterialTheme.colorScheme.primary,
          )
        }
        Spacer(modifier = Modifier.size(16.dp))

        if (viewState.isPlayStoreInstalled || viewState.isRuStoreInstalled) {
          Text(
            text = stringResource(CommonR.string.action_support_project),
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
              modifier = Modifier
                .weight(1f)
                .wrapContentSize()
                .padding(top = 4.dp),
              shape = RoundedCornerShape(16.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
              elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
              Column(
                modifier = Modifier
                  .heightIn(max = 208.dp)
                  //.aspectRatio(1f, true)
                  .padding(horizontal = 6.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
              ) {
                Text(
                  modifier = Modifier
                    .height(52.dp)
                    .padding(horizontal = 2.dp)
                    .wrapContentHeight(),
                  text = stringResource(CommonR.string.tip_kind),
                  fontSize = 14.sp,
                  lineHeight = 14.sp,
                  textAlign = TextAlign.Center,
                  maxLines = 3,
                )
                Spacer(modifier = Modifier.size(4.dp))
                Button(
                  modifier = Modifier
                    .fillMaxWidth()
                    .height(28.dp)
                    .alpha(0.6f),
                  enabled = isEnabled1,
                  colors = ButtonDefaults.buttonColors(
                    disabledContainerColor = MaterialTheme.colorScheme.primary,
                    disabledContentColor = MaterialTheme.colorScheme.onPrimary
                  ),
                  onClick = { listener.onPurchaseClick(useGooglePlay, 0) },
                  shape = RoundedCornerShape(10.dp),
                  contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                  if (isPurchased1) {
                    Icon(
                      modifier = Modifier.size(24.dp),
                      imageVector = Icons.Rounded.CheckCircle,
                      contentDescription = null,
                    )
                  } else {
                    Text(
                      text = prices1,
                      overflow = TextOverflow.Ellipsis,
                      maxLines = 1,
                      fontSize = 11.sp,
                    )
                  }
                }
                Spacer(modifier = Modifier.size(6.dp))
                Button(
                  modifier = Modifier
                    .fillMaxWidth()
                    .height(28.dp)
                    .alpha(0.7f),
                  enabled = isEnabled4,
                  colors = ButtonDefaults.buttonColors(
                    disabledContainerColor = MaterialTheme.colorScheme.primary,
                    disabledContentColor = MaterialTheme.colorScheme.onPrimary
                  ),
                  onClick = { listener.onPurchaseClick(useGooglePlay, 3) },
                  shape = RoundedCornerShape(10.dp),
                  contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                  if (isPurchased4) {
                    Icon(
                      modifier = Modifier.size(24.dp),
                      imageVector = Icons.Rounded.CheckCircle,
                      contentDescription = null,
                    )
                  } else {
                    Text(
                      text = prices4,
                      overflow = TextOverflow.Ellipsis,
                      maxLines = 1,
                      fontSize = 11.sp,
                    )
                  }
                }
                Spacer(modifier = Modifier.size(6.dp))
                Button(
                  modifier = Modifier
                    .fillMaxWidth()
                    .height(28.dp)
                    .alpha(0.8f),
                  enabled = isEnabled7,
                  colors = ButtonDefaults.buttonColors(
                    disabledContainerColor = MaterialTheme.colorScheme.primary,
                    disabledContentColor = MaterialTheme.colorScheme.onPrimary
                  ),
                  onClick = { listener.onPurchaseClick(useGooglePlay, 6) },
                  shape = RoundedCornerShape(10.dp),
                  contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                  if (isPurchased7) {
                    Icon(
                      modifier = Modifier.size(24.dp),
                      imageVector = Icons.Rounded.CheckCircle,
                      contentDescription = null,
                    )
                  } else {
                    Text(
                      text = prices7,
                      overflow = TextOverflow.Ellipsis,
                      maxLines = 1,
                      fontSize = 11.sp,
                    )
                  }
                }
              }
            }
            Spacer(modifier = Modifier.size(16.dp))
            Card(
              modifier = Modifier
                .weight(1f)
                .wrapContentSize()
                .padding(top = 4.dp),
              shape = RoundedCornerShape(16.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
              elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
              Column(
                modifier = Modifier
                  .heightIn(max = 208.dp)
                  //.aspectRatio(1f, true)
                  .padding(horizontal = 6.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
              ) {
                Text(
                  modifier = Modifier
                    .height(52.dp)
                    .padding(horizontal = 2.dp)
                    .wrapContentHeight(),
                  text = stringResource(CommonR.string.tip_excellent),
                  fontSize = 14.sp,
                  lineHeight = 14.sp,
                  textAlign = TextAlign.Center,
                  maxLines = 3,
                )
                Spacer(modifier = Modifier.size(4.dp))
                Button(
                  modifier = Modifier
                    .fillMaxWidth()
                    .height(28.dp)
                    .alpha(0.7f),
                  enabled = isEnabled2,
                  colors = ButtonDefaults.buttonColors(
                    disabledContainerColor = MaterialTheme.colorScheme.primary,
                    disabledContentColor = MaterialTheme.colorScheme.onPrimary
                  ),
                  onClick = { listener.onPurchaseClick(useGooglePlay, 1) },
                  shape = RoundedCornerShape(10.dp),
                  contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                  if (isPurchased2) {
                    Icon(
                      modifier = Modifier.size(24.dp),
                      imageVector = Icons.Rounded.CheckCircle,
                      contentDescription = null,
                    )
                  } else {
                    Text(
                      text = prices2,
                      overflow = TextOverflow.Ellipsis,
                      maxLines = 1,
                      fontSize = 11.sp,
                    )
                  }
                }
                Spacer(modifier = Modifier.size(6.dp))
                Button(
                  modifier = Modifier
                    .fillMaxWidth()
                    .height(28.dp)
                    .alpha(0.8f),
                  enabled = isEnabled5,
                  colors = ButtonDefaults.buttonColors(
                    disabledContainerColor = MaterialTheme.colorScheme.primary,
                    disabledContentColor = MaterialTheme.colorScheme.onPrimary
                  ),
                  onClick = { listener.onPurchaseClick(useGooglePlay, 4) },
                  shape = RoundedCornerShape(10.dp),
                  contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                  if (isPurchased5) {
                    Icon(
                      modifier = Modifier.size(24.dp),
                      imageVector = Icons.Rounded.CheckCircle,
                      contentDescription = null,
                    )
                  } else {
                    Text(
                      text = prices5,
                      overflow = TextOverflow.Ellipsis,
                      maxLines = 1,
                      fontSize = 11.sp,
                    )
                  }
                }
                Spacer(modifier = Modifier.size(6.dp))
                Button(
                  modifier = Modifier
                    .fillMaxWidth()
                    .height(28.dp)
                    .alpha(0.9f),
                  enabled = isEnabled8,
                  colors = ButtonDefaults.buttonColors(
                    disabledContainerColor = MaterialTheme.colorScheme.primary,
                    disabledContentColor = MaterialTheme.colorScheme.onPrimary
                  ),
                  onClick = { listener.onPurchaseClick(useGooglePlay, 7) },
                  shape = RoundedCornerShape(10.dp),
                  contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                  if (isPurchased8) {
                    Icon(
                      modifier = Modifier.size(24.dp),
                      imageVector = Icons.Rounded.CheckCircle,
                      contentDescription = null,
                    )
                  } else {
                    Text(
                      text = prices8,
                      overflow = TextOverflow.Ellipsis,
                      maxLines = 1,
                      fontSize = 11.sp,
                    )
                  }
                }
              }
            }
            Spacer(modifier = Modifier.size(16.dp))
            Card(
              modifier = Modifier
                .weight(1f)
                .wrapContentSize()
                .padding(top = 4.dp),
              shape = RoundedCornerShape(16.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
              elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
              Column(
                modifier = Modifier
                  .heightIn(max = 208.dp)
                  //.aspectRatio(1f, true)
                  .padding(horizontal = 6.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
              ) {
                Text(
                  modifier = Modifier
                    .height(52.dp)
                    .padding(horizontal = 2.dp)
                    .wrapContentHeight(),
                  text = stringResource(CommonR.string.tip_incredible),
                  fontSize = 14.sp,
                  lineHeight = 14.sp,
                  textAlign = TextAlign.Center,
                  maxLines = 3,
                )
                Spacer(modifier = Modifier.size(4.dp))
                Button(
                  modifier = Modifier
                    .fillMaxWidth()
                    .height(28.dp)
                    .alpha(0.8f),
                  enabled = isEnabled3,
                  colors = ButtonDefaults.buttonColors(
                    disabledContainerColor = MaterialTheme.colorScheme.primary,
                    disabledContentColor = MaterialTheme.colorScheme.onPrimary
                  ),
                  onClick = { listener.onPurchaseClick(useGooglePlay, 2) },
                  shape = RoundedCornerShape(10.dp),
                  contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                  if (isPurchased3) {
                    Icon(
                      modifier = Modifier.size(24.dp),
                      imageVector = Icons.Rounded.CheckCircle,
                      contentDescription = null,
                    )
                  } else {
                    Text(
                      text = prices3,
                      overflow = TextOverflow.Ellipsis,
                      maxLines = 1,
                      fontSize = 11.sp,
                    )
                  }
                }
                Spacer(modifier = Modifier.size(6.dp))
                Button(
                  modifier = Modifier
                    .fillMaxWidth()
                    .height(28.dp)
                    .alpha(0.9f),
                  enabled = isEnabled6,
                  colors = ButtonDefaults.buttonColors(
                    disabledContainerColor = MaterialTheme.colorScheme.primary,
                    disabledContentColor = MaterialTheme.colorScheme.onPrimary
                  ),
                  onClick = { listener.onPurchaseClick(useGooglePlay, 5) },
                  shape = RoundedCornerShape(10.dp),
                  contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                  if (isPurchased6) {
                    Icon(
                      modifier = Modifier.size(24.dp),
                      imageVector = Icons.Rounded.CheckCircle,
                      contentDescription = null,
                    )
                  } else {
                    Text(
                      text = prices6,
                      overflow = TextOverflow.Ellipsis,
                      maxLines = 1,
                      fontSize = 11.sp,
                    )
                  }
                }
                Spacer(modifier = Modifier.size(6.dp))
                Button(
                  modifier = Modifier
                    .fillMaxWidth()
                    .height(28.dp)
                    .alpha(1f),
                  enabled = isEnabled9,
                  colors = ButtonDefaults.buttonColors(
                    disabledContainerColor = MaterialTheme.colorScheme.primary,
                    disabledContentColor = MaterialTheme.colorScheme.onPrimary
                  ),
                  onClick = { listener.onPurchaseClick(useGooglePlay, 8) },
                  shape = RoundedCornerShape(10.dp),
                  contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                  if (isPurchased9) {
                    Icon(
                      modifier = Modifier.size(24.dp),
                      imageVector = Icons.Rounded.CheckCircle,
                      contentDescription = null,
                    )
                  } else {
                    Text(
                      text = prices9,
                      overflow = TextOverflow.Ellipsis,
                      maxLines = 1,
                      fontSize = 11.sp,
                    )
                  }
                }
              }
            }
            Spacer(modifier = Modifier.size(16.dp))
          }
          Spacer(modifier = Modifier.size(24.dp))
          if (!viewState.isPro) {
            // Dark Theme
            ListItem(
              modifier = Modifier.clickable {
                listener.themeChanged()
              },
              colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.inverseOnSurface),
              leadingContent = {
                Icon(
                  modifier = Modifier.size(62.dp),
                  painter = painterResource(id = CommonR.drawable.ic_invert_colors),
                  contentDescription = stringResource(id = StringsR.string.pref_theme_dark),
                  tint = MaterialTheme.colorScheme.primary,
                )
              },
              headlineContent = {
                Column {
                  Text(
                    text = stringResource(StringsR.string.pref_theme_dark),
                    fontSize = 18.sp,
                  )
                  Spacer(modifier = Modifier.size(2.dp))
                  Text(
                    text = stringResource(CommonR.string.theme_summary),
                    lineHeight = 16.sp,
                    color = LocalContentColor.current.copy(alpha = 0.5F),
                  )
                }
              },
            )
            Spacer(modifier = Modifier.size(16.dp))
            // Colors
            ListItem(
              colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.inverseOnSurface),
              leadingContent = {
                Icon(
                  modifier = Modifier.size(62.dp),
                  painter = painterResource(id = CommonR.drawable.ic_palette),
                  contentDescription = stringResource(id = CommonR.string.color_title),
                  tint = MaterialTheme.colorScheme.primary,
                )
              },
              headlineContent = {
                Column {
                  Text(
                    text = stringResource(CommonR.string.color_title),
                    fontSize = 18.sp,
                  )
                  Spacer(modifier = Modifier.size(2.dp))
                  Text(
                    text = stringResource(CommonR.string.color_summary),
                    lineHeight = 16.sp,
                    color = LocalContentColor.current.copy(alpha = 0.5F),
                  )
                }
              },
            )
            Spacer(modifier = Modifier.size(16.dp))
          }
          // Support us
          ListItem(
            colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.inverseOnSurface),
            leadingContent = {
              Icon(
                modifier = Modifier
                  .size(62.dp)
                  .padding(5.dp),
                painter = painterResource(id = CommonR.drawable.ic_plus_round),
                contentDescription = stringResource(id = CommonR.string.plus_title),
                tint = MaterialTheme.colorScheme.primary,
              )
            },
            headlineContent = {
              Column {
                Text(
                  text = stringResource(CommonR.string.plus_title),
                  fontSize = 18.sp,
                )
                Spacer(modifier = Modifier.size(2.dp))
                Text(
                  text = stringResource(CommonR.string.plus_summary),
                  lineHeight = 16.sp,
                  color = LocalContentColor.current.copy(alpha = 0.5F),
                )
              }
            },
          )
          // Participants
          Spacer(modifier = Modifier.size(24.dp))
          Image(
            modifier = Modifier.size(82.dp),
            painter = painterResource(id = CommonR.drawable.logo_goodwy),
            contentDescription = null
          )
        } else {
          // no store
          Column(
            modifier = Modifier
              .wrapContentWidth(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
          ) {
            HtmlText(stringResource(CommonR.string.donate_text))
            Spacer(modifier = Modifier.size(16.dp))
            Button(
              //modifier = Modifier.height(28.dp),
              onClick = { listener.onUrlClick("https://sites.google.com/view/goodwy/support-project") },
              //shape = RoundedCornerShape(10.dp),
              contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
            ) {
              Text(stringResource(CommonR.string.action_support_project))
            }
            Spacer(modifier = Modifier.size(56.dp))
            Text(stringResource(CommonR.string.unlock))
            Spacer(modifier = Modifier.size(8.dp))
            Switch(
              modifier = Modifier.scale(2f),
              checked = viewState.isPro,
              onCheckedChange = { listener.togglePro() },
            )
            Spacer(modifier = Modifier.size(56.dp))
          }
        }
        Spacer(modifier = Modifier.size(bottom.dp))
      }
    }
  }
}

@Composable
fun HtmlText(
  html: String,
  modifier: Modifier = Modifier,
  color: Color = Color.Unspecified,
  style: TextStyle = LocalTextStyle.current,) {
  val textColor = color
    .takeOrElse { style.color }
    .takeOrElse { LocalContentColor.current.copy(alpha = 1f) }
    .toArgb()

  val density = LocalDensity.current
  val textSize = with(density) {
    style.fontSize
      .takeOrElse { LocalTextStyle.current.fontSize }
      .toPx()
  }
  AndroidView(
    modifier = modifier.padding(horizontal = 24.dp),
    factory = { context -> TextView(context).apply {
      setTextColor(textColor)
      setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
      gravity = Gravity.CENTER_HORIZONTAL
    }
              },
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
