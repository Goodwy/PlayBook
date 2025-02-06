package voice.settings.purchase

data class PurchaseViewState(
  val appVersion: String,
  val paddings: String,
  val prices: String,
  val pricesSubs: String,
  val pricesRustore: String,
  val purchasedList: String,
  val purchasedSubsList: String,
  val purchasedListRustore: String,
  val isPro: Boolean,
  val isPlayStoreInstalled: Boolean,
  val isRuStoreInstalled: Boolean,
  val isProNoGp: Boolean,
  val useGooglePlay: Boolean,
  val theme: Int,
)
