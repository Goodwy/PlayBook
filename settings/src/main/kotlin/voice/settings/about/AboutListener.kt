package voice.settings.about

interface AboutListener {
  fun close()
  fun onPurchaseClick()
  fun onRateClick()
  fun onMoreAppClick()
  fun onPrivacyClick()
}
