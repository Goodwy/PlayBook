package voice.settings.purchase

interface PurchaseListener {
  fun close()
  fun onPurchaseClick()
  fun onSmallClick()
  fun onMediumClick()
  fun onBigClick()
}
