package voice.settings.about

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import voice.common.AppInfoProvider
import voice.common.navigation.Destination
import voice.common.navigation.Navigator
import voice.common.pref.PrefKeys
import voice.pref.Pref
import javax.inject.Inject
import javax.inject.Named

class AboutViewModel
@Inject constructor(
  private val navigator: Navigator,
  private val appInfoProvider: AppInfoProvider,
  @Named(PrefKeys.PADDING)
  private val paddingPref: Pref<String>,
  @Named(PrefKeys.IS_RU_STORE_INSTALLED)
  private val isRuStoreInstalledPref: Pref<Boolean>,
  @Named(PrefKeys.USE_GOOGLE_PLAY)
  private val useGooglePlay: Pref<Boolean>,
) : AboutListener {

  @Composable
  fun viewState(): AboutViewState {
    val paddings by remember { paddingPref.flow }.collectAsState(initial = "0;0;0;0")
    return AboutViewState(
      appVersion = appInfoProvider.versionName,
      paddings = paddings,
    )
  }

  override fun close() {
    navigator.goBack()
  }

  override fun onPurchaseClick() {
    navigator.goTo(Destination.Purchase)
  }

  override fun onRateClick() {
    val rateUrl = if (isRuStoreInstalledPref.value && !useGooglePlay.value) "https://apps.rustore.ru/app/${appInfoProvider.applicationID}"
    else "https://play.google.com/store/apps/details?id=${appInfoProvider.applicationID}"
    navigator.goTo(Destination.Website(rateUrl))
  }

  override fun onMoreAppClick() {
    val otherAppUrl = if (isRuStoreInstalledPref.value && !useGooglePlay.value) "rustore://apps.rustore.ru/developer/d01f495d"
    else "https://play.google.com/store/apps/dev?id=8268163890866913014"
    navigator.goTo(Destination.Website(otherAppUrl))
  }

  override fun onPrivacyClick() {
    navigator.goTo(Destination.Website("https://github.com/Goodwy/PlayBooks/blob/master/PRIVACY.md"))
  }
}
