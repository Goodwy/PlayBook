package voice.app.features

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.annotation.StringRes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.rustore.sdk.billingclient.RuStoreBillingClient
import ru.rustore.sdk.billingclient.model.product.Product
import ru.rustore.sdk.billingclient.model.purchase.PaymentResult
import ru.rustore.sdk.billingclient.model.purchase.Purchase
import ru.rustore.sdk.billingclient.model.purchase.PurchaseState
import ru.rustore.sdk.billingclient.utils.pub.checkPurchasesAvailability
import ru.rustore.sdk.core.feature.model.FeatureAvailabilityResult
import voice.app.injection.RuStoreModule
import voice.common.R

class RuStoreHelper(
  val activity: Activity,
) {

  private val billingClientRuStore: RuStoreBillingClient = RuStoreModule.provideRuStoreBillingClient()

  //Start
  private val _stateStart = MutableStateFlow(StartPurchasesState())
//  val stateStart = _stateStart.asStateFlow()

  private val _eventStart = MutableSharedFlow<StartPurchasesEvent>(
    extraBufferCapacity = 1,
    onBufferOverflow = BufferOverflow.DROP_OLDEST
  )
  val eventStart = _eventStart.asSharedFlow()

  fun checkPurchasesAvailability(context: Context) {
    _stateStart.value = _stateStart.value.copy(isLoading = true)
    RuStoreBillingClient.checkPurchasesAvailability(context)
      .addOnSuccessListener { result ->
        _stateStart.value = _stateStart.value.copy(isLoading = false)
        _eventStart.tryEmit(StartPurchasesEvent.PurchasesAvailability(result))
      }
      .addOnFailureListener { throwable ->
        _stateStart.value = _stateStart.value.copy(isLoading = false)
        _eventStart.tryEmit(StartPurchasesEvent.Error(throwable))
      }
  }

  //Billing
  private val defaultProductIds = listOf(
    "product_x1",
    "product_x2",
    "product_x3",
    "subscription_x1",
    "subscription_x2",
    "subscription_x3",
    "subscription_year_x1",
    "subscription_year_x2",
    "subscription_year_x3"
  )

  private val _stateBilling = MutableStateFlow(BillingState())
  val stateBilling = _stateBilling.asStateFlow()

  private val _eventBilling = MutableSharedFlow<BillingEvent>(
    extraBufferCapacity = 1,
    onBufferOverflow = BufferOverflow.DROP_OLDEST
  )
//  val eventBilling = _eventBilling.asSharedFlow()

  private val _statePurchased = MutableStateFlow(PurchasedState())
  val statePurchased = _statePurchased.asStateFlow()

  fun getProducts(availableProductIds: List<String> = defaultProductIds) {
    _stateBilling.value = _stateBilling.value.copy(isLoading = true)
    _statePurchased.value = _statePurchased.value.copy(isLoading = true)
    CoroutineScope(Dispatchers.Main).launch {
      runCatching {
        withContext(Dispatchers.IO) {
          val products = billingClientRuStore.products.getProducts(
            productIds = availableProductIds
          ).await()

          val purchases = billingClientRuStore.purchases.getPurchases().await()

          purchases.forEach { purchase ->
            val purchaseId = purchase.purchaseId
            if (purchase.developerPayload?.isNotEmpty() == true) {
              Log.w("RuStoreBillingClient", "DeveloperPayloadInfo: ${purchase.developerPayload}")
            }
            if (purchaseId != null) {
              when (purchase.purchaseState) {
                PurchaseState.CREATED, PurchaseState.INVOICE_CREATED -> {
                  billingClientRuStore.purchases.deletePurchase(purchaseId).await()
                }

                PurchaseState.PAID -> {
                  billingClientRuStore.purchases.confirmPurchase(purchaseId).await()
                }

                else -> Unit
              }
            }
          }

//                    val nonBoughtProducts = products.filter { product ->
//                        purchases.none { product.productId == it.productId }
//                    }

          withContext(Dispatchers.Main) {
            _stateBilling.value = _stateBilling.value.copy(
              products = products, //nonBoughtProducts,
              isLoading = false
            )

            val purchasedProducts = purchases.filter { it.purchaseState == PurchaseState.PAID || it.purchaseState == PurchaseState.CONFIRMED }
            _statePurchased.value = _statePurchased.value.copy(
              purchases = purchasedProducts,
              isLoading = false
            )
          }
        }
      }.onFailure { throwable ->
        _eventBilling.tryEmit(BillingEvent.ShowError(throwable))
        _stateBilling.value = _stateBilling.value.copy(isLoading = false)
      }
    }
  }

  fun purchaseProduct(product: Product) {
    billingClientRuStore.purchases.purchaseProduct(productId = product.productId)
      .addOnSuccessListener { paymentResult ->
        handlePaymentResult(paymentResult)
      }
      .addOnFailureListener {
        setErrorStateOnFailure(it)
      }
  }

  private fun handlePaymentResult(paymentResult: PaymentResult) {
    when (paymentResult) {
      is PaymentResult.Cancelled -> {
        deletePurchase(paymentResult.purchaseId)
      }

      is PaymentResult.Failure -> {
        paymentResult.purchaseId?.let { deletePurchase(it) }
      }

      is PaymentResult.Success -> {
        confirmPurchase(paymentResult.purchaseId)
      }

      else -> Unit
    }
  }

  private fun confirmPurchase(purchaseId: String) {
    _stateBilling.value = _stateBilling.value.copy(
      isLoading = true,
      snackbarResId = R.string.billing_purchase_confirm_in_progress
    )
    billingClientRuStore.purchases.confirmPurchase(purchaseId, null)
      .addOnSuccessListener { response ->
        _eventBilling.tryEmit(
          BillingEvent.ShowDialog(
            InfoDialogState(
              titleRes = R.string.billing_product_confirmed,
              message = response.toString(),
            )
          )
        )
        _stateBilling.value = _stateBilling.value.copy(
          isLoading = false,
          snackbarResId = null
        )
      }
      .addOnFailureListener {
        setErrorStateOnFailure(it)
      }
  }

  private fun deletePurchase(purchaseId: String) {
    _stateBilling.value = _stateBilling.value.copy(
      isLoading = true,
      snackbarResId = R.string.billing_purchase_delete_in_progress
    )
    billingClientRuStore.purchases.deletePurchase(purchaseId)
      .addOnSuccessListener { response ->
        _eventBilling.tryEmit(
          BillingEvent.ShowDialog(InfoDialogState(
            titleRes = R.string.billing_product_deleted,
            message = response.toString()
          ))
        )
        _stateBilling.value = _stateBilling.value.copy(isLoading = false)
      }
      .addOnFailureListener {
        setErrorStateOnFailure(it)
      }
  }

  private fun setErrorStateOnFailure(error: Throwable) {
    _eventBilling.tryEmit(BillingEvent.ShowError(error))
    _stateBilling.value = _stateBilling.value.copy(isLoading = false)
  }
}

//data class
//start
data class StartPurchasesState(
  val isLoading: Boolean = false,
  val purchasesAvailability: FeatureAvailabilityResult? = null,
  val error: Throwable? = null
)

sealed class StartPurchasesEvent {
  data class PurchasesAvailability(val availability: FeatureAvailabilityResult) : StartPurchasesEvent()
  data class Error(val throwable: Throwable): StartPurchasesEvent()
}

//billing
data class InfoDialogState(
  @StringRes val titleRes: Int,
  val message: String
)

data class BillingState(
  val isLoading: Boolean = false,
  val products: List<Product> = emptyList(),
  @StringRes val snackbarResId: Int? = null
) {
//  val isEmpty: Boolean = products.isEmpty() && !isLoading
}

sealed class BillingEvent {
  data class ShowDialog(val dialogInfo: InfoDialogState): BillingEvent()
  data class ShowError(val error: Throwable): BillingEvent()
}

data class PurchasedState(
  val isLoading: Boolean = false,
  val purchases: List<Purchase> = emptyList(),
  @StringRes val snackbarResId: Int? = null
) {
//  val isEmpty: Boolean = purchases.isEmpty() && !isLoading
}

