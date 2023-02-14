@file:Suppress("DEPRECATION")

package voice.app.features

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.billingclient.api.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import voice.app.BuildConfig

class BillingViewModel(application: Application) : AndroidViewModel(application),
  PurchasesUpdatedListener,
  BillingClientStateListener,
  SkuDetailsResponseListener {

  val billingConnectionState = MutableLiveData<BillingConnectionState>().also {
    it.postValue(BillingConnectionState.Connecting)
  }

  val skuDetails = MutableLiveData<Map<Tip, Details>>()
  val tippingSum = MutableLiveData<TippingSum>()

  val purchaseResult = MutableLiveData<PurchaseEvent>()

  private val billingClient = BillingClient
    .newBuilder(application.applicationContext)
    .enablePendingPurchases()
    .setListener(this)
    .build()

  init {
    billingClient.startConnection(this)
  }

  override fun onBillingSetupFinished(billingResult: BillingResult) {
    val responseCode = billingResult.responseCode
    Log.d("BillingViewModel", "Billing setup finished with response $responseCode")

    when (responseCode) {
      BillingClient.BillingResponseCode.OK -> {
        billingConnectionState.postValue(BillingConnectionState.GettingDetails)
        querySkuDetails()
        viewModelScope.launch {
          Log.d("BillingViewModel", "TEST")
        }
      }
      BillingClient.BillingResponseCode.FEATURE_NOT_SUPPORTED,
      BillingClient.BillingResponseCode.SERVICE_DISCONNECTED,
      BillingClient.BillingResponseCode.USER_CANCELED,
      BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE,
      BillingClient.BillingResponseCode.BILLING_UNAVAILABLE,
      BillingClient.BillingResponseCode.ITEM_UNAVAILABLE,
      BillingClient.BillingResponseCode.DEVELOPER_ERROR,
      BillingClient.BillingResponseCode.ERROR,
      BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED,
      BillingClient.BillingResponseCode.ITEM_NOT_OWNED -> {
        billingConnectionState.postValue(BillingConnectionState.Failed)
      }
    }
  }

    private fun updatePurchaseHistory() = viewModelScope.launch {
//        this does not work as expected
        val listener = PurchaseHistoryResponseListener { result, purchasesList ->
            when (result.responseCode) {
                BillingClient.BillingResponseCode.OK -> {
                    purchasesList?.forEach {
                        val consumeParams = ConsumeParams.newBuilder().setPurchaseToken(it.purchaseToken).build()
                        billingClient.consumeAsync(consumeParams) { _, _ -> }
                    }

                    val tips = purchasesList
                        //?.filter { it. }
                        ?.map { purchase ->
                            Tip.fromSku(purchase.skus.firstOrNull())
                        }

                    val sumOfTips = tips?.mapNotNull {
                        skuDetails.value?.get(it)
                    }?.sumOf {
                        it.skuDetails.priceAmountMicros.toDouble() / 1_000_000
                    } ?: 0

                    val value = if (tips!!.isEmpty()) {
                        TippingSum.NoTips
                    } else {
                        skuDetails.value?.get(Tip.Big)?.currencyCode?.let {
                            val formattedSum = "%.2f".format(sumOfTips) + " $it"
                            TippingSum.Succeeded(formattedSum)
                        } ?: TippingSum.FailedToLoad
                    }
                    tippingSum.postValue(value)
                }
                BillingClient.BillingResponseCode.FEATURE_NOT_SUPPORTED,
                BillingClient.BillingResponseCode.SERVICE_DISCONNECTED,
                BillingClient.BillingResponseCode.USER_CANCELED,
                BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE,
                BillingClient.BillingResponseCode.BILLING_UNAVAILABLE,
                BillingClient.BillingResponseCode.ITEM_UNAVAILABLE,
                BillingClient.BillingResponseCode.DEVELOPER_ERROR,
                BillingClient.BillingResponseCode.ERROR,
                BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED,
                BillingClient.BillingResponseCode.ITEM_NOT_OWNED -> {
                    tippingSum.postValue(TippingSum.FailedToLoad)
                }
            }
        }

        billingClient.queryPurchaseHistoryAsync(BillingClient.SkuType.INAPP, listener)
    }

  private fun querySkuDetails() = viewModelScope.launch {
    Log.d("BillingViewModel", "Querying sku details")

    val queryParams = SkuDetailsParams
      .newBuilder()
      .setType(BillingClient.SkuType.INAPP)
      .setSkusList(Tip.values.map { it.id })
      .build()

    billingClient.querySkuDetailsAsync(queryParams, this@BillingViewModel)
  }

  override fun onSkuDetailsResponse(billingResult: BillingResult, skuDetailsList: MutableList<SkuDetails>?) {
    val responseCode = billingResult.responseCode
    Log.d("BillingViewModel", "Received Sku Details Response $responseCode")

    when (responseCode) {
      BillingClient.BillingResponseCode.OK -> {
        billingConnectionState.postValue(BillingConnectionState.Connected)
        skuDetails.postValue(skuDetailsList?.asPriceMap())
                updatePurchaseHistory()
      }
      BillingClient.BillingResponseCode.FEATURE_NOT_SUPPORTED,
      BillingClient.BillingResponseCode.SERVICE_DISCONNECTED,
      BillingClient.BillingResponseCode.USER_CANCELED,
      BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE,
      BillingClient.BillingResponseCode.BILLING_UNAVAILABLE,
      BillingClient.BillingResponseCode.ITEM_UNAVAILABLE,
      BillingClient.BillingResponseCode.DEVELOPER_ERROR,
      BillingClient.BillingResponseCode.ERROR,
      BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED,
      BillingClient.BillingResponseCode.ITEM_NOT_OWNED -> {
        billingConnectionState.postValue(BillingConnectionState.Failed)
      }
    }
  }

  override fun onBillingServiceDisconnected() {
    Log.d("BillingViewModel", "Billing Service disconnected")

    billingConnectionState.postValue(BillingConnectionState.Failed)
  }

  fun buyTip(activity: Activity, tip: Tip) {
    skuDetails.value?.get(tip)?.let { details ->
      val purchaseParams = BillingFlowParams
        .newBuilder()
        .setSkuDetails(details.skuDetails)
        .build()

      billingClient.launchBillingFlow(activity, purchaseParams)
    } ?: run {
      purchaseResult.postValue(PurchaseEvent(PurchaseResult.Fail, tip))
    }
  }

  override fun onPurchasesUpdated(billingResult: BillingResult, purchases: MutableList<Purchase>?) {
    val purchase = purchases?.firstOrNull()
    val tip = Tip.fromSku(purchase?.skus?.firstOrNull())
    when (billingResult.responseCode) {
      BillingClient.BillingResponseCode.OK -> {
        val value = tip?.let {
          purchase?.purchaseToken?.let { token ->
            val params = ConsumeParams.newBuilder().setPurchaseToken(token).build()
            billingClient.consumeAsync(params) { _, _ -> }
          }
          PurchaseEvent(PurchaseResult.Success, it)
        } ?: PurchaseEvent(PurchaseResult.Fail, null)

        purchaseResult.postValue(value)

                updatePurchaseHistory()
      }
      BillingClient.BillingResponseCode.USER_CANCELED -> {
        purchaseResult.postValue(PurchaseEvent(PurchaseResult.Cancelled, tip))
      }
      BillingClient.BillingResponseCode.SERVICE_TIMEOUT,
      BillingClient.BillingResponseCode.FEATURE_NOT_SUPPORTED,
      BillingClient.BillingResponseCode.SERVICE_DISCONNECTED,
      BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE,
      BillingClient.BillingResponseCode.BILLING_UNAVAILABLE,
      BillingClient.BillingResponseCode.ITEM_UNAVAILABLE,
      BillingClient.BillingResponseCode.DEVELOPER_ERROR,
      BillingClient.BillingResponseCode.ERROR,
      BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED,
      BillingClient.BillingResponseCode.ITEM_NOT_OWNED -> {
        purchaseResult.postValue(PurchaseEvent(PurchaseResult.Fail, tip))
      }
    }
  }

  private fun MutableList<SkuDetails>.asPriceMap(): Map<Tip, Details> {
    return mapNotNull { skuDetail ->
      val tip = Tip.fromSku(skuDetail.sku)
      val price = Details(skuDetail.description, skuDetail.price, skuDetail.priceCurrencyCode, skuDetail)
      tip?.let { it to price }
    }.toMap()
  }
}

data class Details(val title: String, val price: String, val currencyCode: String, val skuDetails: SkuDetails)

sealed class Tip(val id: String) {
  object Small : Tip(BuildConfig.PRODUCT_ID_X1)
  object Medium : Tip(BuildConfig.PRODUCT_ID_X2)
  object Big : Tip(BuildConfig.PRODUCT_ID_X3)

  companion object {
    val values = listOf(Small, Medium, Big)

    fun fromSku(sku: String?): Tip? {
      return Tip.values.firstOrNull { sku == it.id }
    }
  }
}

sealed class BillingConnectionState {
  object Connected : BillingConnectionState()
  object GettingDetails : BillingConnectionState()
  object Failed : BillingConnectionState()
  object Connecting : BillingConnectionState()
}

class PurchaseEvent(val result: PurchaseResult, val tip: Tip?)
sealed class PurchaseResult {
  object Success : PurchaseResult()
  object Fail : PurchaseResult()
  object Cancelled : PurchaseResult()
}

sealed class TippingSum {
  object FailedToLoad : TippingSum()
  class Succeeded(val sum: String) : TippingSum()
  object NoTips : TippingSum()
}
