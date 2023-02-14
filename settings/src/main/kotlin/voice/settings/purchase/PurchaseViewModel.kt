package voice.settings.purchase

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import de.paulwoitaschek.flowpref.Pref
import voice.common.AppInfoProvider
import voice.common.navigation.Destination
import voice.common.navigation.Navigator
import voice.common.pref.PrefKeys
import javax.inject.Inject
import javax.inject.Named

class PurchaseViewModel
@Inject constructor(
  private val navigator: Navigator,
  private val appInfoProvider: AppInfoProvider,
  @Named(PrefKeys.PADDING)
  private val paddingPref: Pref<String>,
  @Named(PrefKeys.PRICES)
  private val pricesPref: Pref<String>,
  @Named(PrefKeys.PRO)
  private val isProPref: Pref<Boolean>,
) : PurchaseListener {

  @Composable
  fun viewState(): PurchaseViewState {
    val paddings by remember { paddingPref.flow }.collectAsState(initial = "0;0;0;0")
    val prices by remember { pricesPref.flow }.collectAsState(initial = "0,0,0")
    val isProPref by remember { isProPref.flow }.collectAsState(initial = false)
    return PurchaseViewState(
      appVersion = appInfoProvider.versionName,
      paddings = paddings,
      prices = prices,
      isPro = isProPref,
    )
  }

  override fun close() {
    navigator.goBack()
  }

  override fun onPurchaseClick() {
    navigator.goTo(Destination.Purchase)
  }

  override fun onSmallClick() {
    navigator.goTo(Destination.BuyTip(1))
  }

  override fun onMediumClick() {
    navigator.goTo(Destination.BuyTip(2))
  }

  override fun onBigClick() {
    navigator.goTo(Destination.BuyTip(3))
  }
}
