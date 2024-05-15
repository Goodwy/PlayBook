package voice.settings.purchase

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import de.paulwoitaschek.flowpref.Pref
import voice.common.AppInfoProvider
import voice.common.constants.THEME_DARK
import voice.common.constants.THEME_LIGHT
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
  @Named(PrefKeys.PRICES_SUBS)
  private val pricesSubsPref: Pref<String>,
  @Named(PrefKeys.PRICES_RUSTORE)
  private val pricesRustorePref: Pref<String>,
  @Named(PrefKeys.PURCHASED_LIST)
  private val purchasedList: Pref<String>,
  @Named(PrefKeys.PURCHASED_SUBS_LIST)
  private val purchasedSubsList: Pref<String>,
  @Named(PrefKeys.PURCHASED_LIST_RUSTORE)
  private val purchasedListRustore: Pref<String>,
  @Named(PrefKeys.PRO)
  private val isProPref: Pref<Boolean>,
  @Named(PrefKeys.PRO_SUBS)
  private val isProSubsPref: Pref<Boolean>,
  @Named(PrefKeys.PRO_RUSTORE)
  private val isProRuPref: Pref<Boolean>,
  @Named(PrefKeys.IS_PLAY_STORE_INSTALLED)
  private val isPlayStoreInstalledPref: Pref<Boolean>,
  @Named(PrefKeys.IS_RU_STORE_INSTALLED)
  private val isRuStoreInstalledPref: Pref<Boolean>,
  @Named(PrefKeys.USE_GOOGLE_PLAY)
  private val useGooglePlay: Pref<Boolean>,
  @Named(PrefKeys.THEME)
  private val themePref: Pref<Int>,
) : PurchaseListener {

  @Composable
  fun viewState(): PurchaseViewState {
    val paddings by remember { paddingPref.flow }.collectAsState(initial = "0;0;0;0")
    val prices by remember { pricesPref.flow }.collectAsState(initial = "0;0;0")
    val pricesSubs by remember { pricesSubsPref.flow }.collectAsState(initial = "0;0;0;0;0;0")
    val pricesRustorePref by remember { pricesRustorePref.flow }.collectAsState(initial = "0;0;0;0;0;0;0;0;0")
    val purchasedList by remember { purchasedList.flow }.collectAsState(initial = "0;0;0;")
    val purchasedSubsList by remember { purchasedSubsList.flow }.collectAsState(initial = "0;0;0;0;0;0")
    val purchasedListRustore by remember { purchasedListRustore.flow }.collectAsState(initial = "0;0;0;0;0;0;0;0;0")
    val isProPref by remember { isProPref.flow }.collectAsState(initial = false)
    val isProSubsPref by remember { isProSubsPref.flow }.collectAsState(initial = false)
    val isProRuPref by remember { isProRuPref.flow }.collectAsState(initial = false)
    val isPlayStoreInstalledPref by remember { isPlayStoreInstalledPref.flow }.collectAsState(initial = false)
    val isRuStoreInstalledPref by remember { isRuStoreInstalledPref.flow }.collectAsState(initial = false)
    val useGooglePlay by remember { useGooglePlay.flow }.collectAsState(initial = false)
    val themePref by remember { themePref.flow }.collectAsState(initial = THEME_LIGHT)
    return PurchaseViewState(
      appVersion = appInfoProvider.versionName,
      paddings = paddings,
      prices = prices,
      pricesSubs = pricesSubs,
      pricesRustore = pricesRustorePref,
      purchasedList = purchasedList,
      purchasedSubsList = purchasedSubsList,
      purchasedListRustore = purchasedListRustore,
      isPro = isProPref || isProSubsPref || isProRuPref,
      isPlayStoreInstalled = isPlayStoreInstalledPref,
      isRuStoreInstalled = isRuStoreInstalledPref,
      useGooglePlay = useGooglePlay,
      theme = themePref,
    )
  }

  override fun close() {
    navigator.goBack()
  }

  override fun onPurchaseClick(usePlayStore: Boolean, tip: Int) {
    navigator.goTo(Destination.BuyTip(usePlayStore, tip))
  }

  override fun onRefreshPurchase(usePlayStore: Boolean) {
    navigator.goTo(Destination.RefreshPurchase(usePlayStore))
  }

  override fun themeChanged() {
    if (themePref.value != THEME_DARK) themePref.value = THEME_DARK
    else themePref.value = THEME_LIGHT
  }

  override fun onUrlClick(url: String) {
    navigator.goTo(Destination.Website(url))
  }

  override fun togglePro() {
    isProPref.value = !isProPref.value
  }

  override fun onChangeStore() {
    useGooglePlay.value = !useGooglePlay.value
  }
}
