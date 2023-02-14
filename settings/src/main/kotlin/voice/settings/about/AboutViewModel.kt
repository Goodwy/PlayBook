package voice.settings.about

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

class AboutViewModel
@Inject constructor(
  private val navigator: Navigator,
  private val appInfoProvider: AppInfoProvider,
  @Named(PrefKeys.PADDING)
  private val paddingPref: Pref<String>,
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
    navigator.goTo(Destination.Website("https://play.google.com/store/apps/details?id=${appInfoProvider.applicationID}"))
  }

  override fun onMoreAppClick() {
    navigator.goTo(Destination.Website("https://play.google.com/store/apps/dev?id=8268163890866913014"))
  }

  override fun onPrivacyClick() {
    navigator.goTo(Destination.Website("https://github.com/Goodwy/PlayBooks/blob/master/PRIVACY.md"))
  }
}
