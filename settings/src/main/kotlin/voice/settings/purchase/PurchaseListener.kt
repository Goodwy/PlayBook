package voice.settings.purchase

interface PurchaseListener {
  fun close()
  fun onPurchaseClick(usePlayStore: Boolean, tip: Int)
  fun onRefreshPurchase(usePlayStore: Boolean)
  fun themeChanged()
  fun onUrlClick(url: String)
  fun togglePro()
  fun toggleProNoGp()
  fun onChangeStore()
}
