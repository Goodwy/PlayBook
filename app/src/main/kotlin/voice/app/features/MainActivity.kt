package voice.app.features

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.datastore.core.DataStore
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.bluelinelabs.conductor.ChangeHandlerFrameLayout
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import de.paulwoitaschek.flowpref.Pref
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.rustore.sdk.billingclient.RuStoreBillingClient
import ru.rustore.sdk.billingclient.model.product.Product
import ru.rustore.sdk.core.feature.model.FeatureAvailabilityResult
import voice.app.AppController
import voice.app.BuildConfig
import voice.app.R
import voice.app.features.bookOverview.EditCoverDialogController
import voice.app.features.bookmarks.BookmarkController
import voice.app.injection.RuStoreModule
import voice.app.injection.appComponent
import voice.app.misc.conductor.asVerticalChangeHandlerTransaction
import voice.app.uitools.SettingsContentObserver
import voice.app.uitools.setWindowTransparency
import voice.common.AppInfoProvider
import voice.common.BookId
import voice.common.convertPixelsToDp
import voice.common.navigation.Destination
import voice.common.navigation.NavigationCommand
import voice.common.navigation.Navigator
import voice.common.pref.CurrentBook
import voice.common.pref.PrefKeys
import voice.logging.core.Logger
import voice.playback.PlayerController
import voice.playback.session.search.BookSearchHandler
import voice.playback.session.search.BookSearchParser
import voice.playbackScreen.BookPlayController
import java.util.ArrayList
import javax.inject.Inject
import javax.inject.Named
import voice.common.R as CommonR

class MainActivity : AppCompatActivity() {

  @field:[
    Inject
    CurrentBook
  ]
  lateinit var currentBook: DataStore<BookId?>

  @field:[Inject Named(PrefKeys.CURRENT_VOLUME)]
  lateinit var currentVolumePref: Pref<Int>

  @field:[Inject Named(PrefKeys.TRANSPARENT_NAVIGATION)]
  lateinit var useTransparentNavigationPref: Pref<Boolean>

  @field:[Inject Named(PrefKeys.PADDING)]
  lateinit var paddingPref: Pref<String>

  @field:[Inject Named(PrefKeys.PRO)]
  lateinit var isProPref: Pref<Boolean>

  @field:[Inject Named(PrefKeys.PRO_SUBS)]
  lateinit var isProSubsPref: Pref<Boolean>

  @field:[Inject Named(PrefKeys.PRO_RUSTORE)]
  lateinit var isProRuPref: Pref<Boolean>

  @field:[Inject Named(PrefKeys.PRICES)]
  lateinit var pricesPref: Pref<String>

  @field:[Inject Named(PrefKeys.PRICES_SUBS)]
  lateinit var pricesSubsPref: Pref<String>

  @field:[Inject Named(PrefKeys.PURCHASED_LIST)]
  lateinit var purchasedList: Pref<String>

  @field:[Inject Named(PrefKeys.PURCHASED_SUBS_LIST)]
  lateinit var purchasedSubsList: Pref<String>

  @field:[Inject Named(PrefKeys.PRICES_RUSTORE)]
  lateinit var pricesRustorePref: Pref<String>

  @field:[Inject Named(PrefKeys.PURCHASED_LIST_RUSTORE)]
  lateinit var purchasedListRustore: Pref<String>

  @field:[Inject Named(PrefKeys.IS_PLAY_STORE_INSTALLED)]
  lateinit var isPlayStoreInstalledPref: Pref<Boolean>

  @field:[Inject Named(PrefKeys.IS_RU_STORE_INSTALLED)]
  lateinit var isRuStoreInstalledPref: Pref<Boolean>

  @field:[Inject Named(PrefKeys.USE_GOOGLE_PLAY)]
  lateinit var useGooglePlay: Pref<Boolean>

  @Inject
  lateinit var bookSearchParser: BookSearchParser

  @Inject
  lateinit var bookSearchHandler: BookSearchHandler

  @Inject
  lateinit var playerController: PlayerController

  @Inject
  lateinit var navigator: Navigator

  @Inject
  lateinit var appInfoProvider: AppInfoProvider

  private lateinit var mSettingsContentObserver: SettingsContentObserver

  private lateinit var router: Router

  private val purchaseHelper = PurchaseHelper(this)
  private val ruStoreHelper = RuStoreHelper(this)
  private val billingRuStoreClient: RuStoreBillingClient = RuStoreModule.provideRuStoreBillingClient()
  private var ruStoreIsConnected = false
  private val productsRuStore: MutableList<Product> = mutableListOf()
  private val productList: ArrayList<String> = arrayListOf(
    BuildConfig.PRODUCT_ID_X1,
    BuildConfig.PRODUCT_ID_X2,
    BuildConfig.PRODUCT_ID_X3,
    BuildConfig.SUBSCRIPTION_ID_X1,
    BuildConfig.SUBSCRIPTION_ID_X2,
    BuildConfig.SUBSCRIPTION_ID_X3,
    BuildConfig.SUBSCRIPTION_YEAR_ID_X1,
    BuildConfig.SUBSCRIPTION_YEAR_ID_X2,
    BuildConfig.SUBSCRIPTION_YEAR_ID_X3 )

  override fun onCreate(savedInstanceState: Bundle?) {
    appComponent.inject(this)
    setTheme(R.style.AppTheme)
    super.onCreate(savedInstanceState)

    if (isRuStoreInstalled()) {
      if (savedInstanceState == null) {
        billingRuStoreClient.onNewIntent(intent)
      }
    }
    val root = ChangeHandlerFrameLayout(this)
    setContentView(root)

    // Edge to edge
    if (useTransparentNavigationPref.value) {
      setWindowTransparency(false) { statusBarSize, bottomNavigationBarSize, leftNavigationBarSize, rightNavigationBarSize ->
        Log.i(
          "System Bar Size",
          "statusBarSize=$statusBarSize bottomNavigationBarSize=$bottomNavigationBarSize " +
            "leftNavigationBarSize=$leftNavigationBarSize rightNavigationBarSize=$rightNavigationBarSize",
        )
        paddingPref.value = "${convertPixelsToDp(statusBarSize)};${convertPixelsToDp(bottomNavigationBarSize)};" +
          "${convertPixelsToDp(leftNavigationBarSize)};${convertPixelsToDp(rightNavigationBarSize)}"
      }
    } else {
      //val color = SurfaceColors.SURFACE_0.getColor(this)
      //window.navigationBarColor = color
      paddingPref.value = "0;0;0;0"
    }

    mSettingsContentObserver = SettingsContentObserver(this, Handler(Looper.getMainLooper()))
    applicationContext.contentResolver.registerContentObserver(Settings.System.CONTENT_URI, true, mSettingsContentObserver)

    router = Conductor.attachRouter(this, root, savedInstanceState)
      .setOnBackPressedDispatcherEnabled(true)
      .setPopRootControllerMode(Router.PopRootControllerMode.NEVER)
    if (!router.hasRootController()) {
      setupRouter()
    }

    lifecycleScope.launch {
      navigator.navigationCommands.collect { command ->
        when (command) {
          NavigationCommand.GoBack -> {
            if (router.backstack.lastOrNull()?.controller is AppController) {
              // AppController handles it's own navigation commands
            } else {
              router.popCurrentController()
            }
          }
          is NavigationCommand.GoTo -> {
            when (val destination = command.destination) {
              is Destination.Compose -> {
                // no-op
              }
              is Destination.Bookmarks -> {
                router.pushController(BookmarkController(destination.bookId).asVerticalChangeHandlerTransaction())
              }
              is Destination.Playback -> {
                lifecycleScope.launch {
                  currentBook.updateData { destination.bookId }
                  router.pushController(BookPlayController(destination.bookId).asVerticalChangeHandlerTransaction())
                }
              }
              is Destination.Website -> {
                try {
                  startActivity(Intent(Intent.ACTION_VIEW, destination.url.toUri()))
                } catch (exception: ActivityNotFoundException) {
                  Logger.w(exception)
                }
              }
              is Destination.EditCover -> {
                val args = EditCoverDialogController.Arguments(destination.cover, destination.bookId)
                EditCoverDialogController(args).showDialog(router)
              }
              is Destination.Activity -> {
                startActivity(destination.intent)
              }
              is Destination.BuyTip -> {
                if (destination.usePlayStore) {
                  if (destination.tip < 3) purchaseHelper.getDonation(productList[destination.tip])
                  else purchaseHelper.getSubscription(productList[destination.tip])
                } else {
                  val product = productsRuStore.firstOrNull {  it.productId == productList[destination.tip]  }
                  if (product != null) ruStoreHelper.purchaseProduct(product)
                }
              }
              is Destination.RefreshPurchase -> {
                if (destination.usePlayStore) initPlayStore() else initRuStore()
              }
            }
          }
          is NavigationCommand.Execute -> {
            // handled in AppController
          }
        }
      }
    }

    setupFromIntent(intent)
  }

  override fun recreate() {
    if (Build.VERSION.SDK_INT <= 30) {
      super.recreate()
    } else {
      finish()
      startActivity(intent)
    }
  }

  override fun onResume() {
    super.onResume()
    currentVolumePref.value = mSettingsContentObserver.previousVolume

    val isProApp = resources.getBoolean(CommonR.bool.is_pro_app)
    val isPlayStoreInstalled = isPlayStoreInstalled()
    val isRuStoreInstalled = isRuStoreInstalled()
    isPlayStoreInstalledPref.value = isPlayStoreInstalled
    isRuStoreInstalledPref.value = isRuStoreInstalled
    if (!isPlayStoreInstalled) useGooglePlay.value = false

    //Billing
    if (isProApp) isProPref.value = true
    if (isPlayStoreInstalled) initPlayStore()
    if (isRuStoreInstalled) initRuStore()
  }

  override fun onDestroy() {
    super.onDestroy()
    applicationContext.contentResolver.unregisterContentObserver(mSettingsContentObserver)
  }

  override fun onNewIntent(intent: Intent) {
    super.onNewIntent(intent)
    if (isRuStoreInstalled()) {
      billingRuStoreClient.onNewIntent(intent)
    }
    setupFromIntent(intent)
  }

  private fun setupFromIntent(intent: Intent?) {
    bookSearchParser.parse(intent)?.let {
      runBlocking {
        bookSearchHandler.handle(it)
      }
    }
  }

  private fun setupRouter() {
    // if we should enter a book set the backstack and return early
    val goToBook = intent.getBooleanExtra(NI_GO_TO_BOOK, false)
    if (goToBook) {
      val bookId = runBlocking { currentBook.data.first() }
      if (bookId != null) {
        val bookShelf = RouterTransaction.with(AppController())
        val bookPlay = BookPlayController(bookId).asVerticalChangeHandlerTransaction()
        router.setBackstack(listOf(bookShelf, bookPlay), null)
        return
      }
    }

    // if we should play the current book, set the backstack and return early
    if (intent?.action == "playCurrent") {
      runBlocking { currentBook.data.first() }?.let { bookId ->
        val bookShelf = RouterTransaction.with(AppController())
        val bookPlay = BookPlayController(bookId).asVerticalChangeHandlerTransaction()
        router.setBackstack(listOf(bookShelf, bookPlay), null)
        playerController.play()
        return
      }
    }

    val rootTransaction = RouterTransaction.with(AppController())
    router.setRoot(rootTransaction)
  }

  private fun isPlayStoreInstalled(): Boolean {
    return isPackageInstalled("com.android.vending")
      || isPackageInstalled("com.google.market")
  }

  private fun isRuStoreInstalled(): Boolean {
    return isPackageInstalled("ru.vk.store")
  }

  private fun isPackageInstalled(packageName: String?): Boolean {
    val packageManager = packageManager
    val intent = packageManager.getLaunchIntentForPackage(packageName!!) ?: return false
    val list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
    return list.isNotEmpty()
  }

  private fun initPlayStore() {
    purchaseHelper.initBillingClient()
    val iapList: ArrayList<String> = arrayListOf(BuildConfig.PRODUCT_ID_X1, BuildConfig.PRODUCT_ID_X2, BuildConfig.PRODUCT_ID_X3)
    val subList: ArrayList<String> = arrayListOf(BuildConfig.SUBSCRIPTION_ID_X1, BuildConfig.SUBSCRIPTION_ID_X2, BuildConfig.SUBSCRIPTION_ID_X3,
      BuildConfig.SUBSCRIPTION_YEAR_ID_X1, BuildConfig.SUBSCRIPTION_YEAR_ID_X2, BuildConfig.SUBSCRIPTION_YEAR_ID_X3)
    purchaseHelper.retrieveDonation(iapList, subList)

    purchaseHelper.iapSkuDetailsInitialized.observe(this) {
      if (it) {
        var prices = ""
        iapList.forEach { item ->
          val price = purchaseHelper.getPriceDonation(item)
          prices += if (price != "???") {
            val resultPrice = price.replace(".00", "", true)
            if (item == iapList.last()) resultPrice else "$resultPrice;"
          } else {
            if (item == subList.last()) "???" else "???;"
          }
        }
        pricesPref.value = prices
        Logger.v("Billing price: $prices")
      }
    }
    purchaseHelper.subSkuDetailsInitialized.observe(this) {
      if (it) {
        var prices = ""
        subList.forEach { item ->
          val price = purchaseHelper.getPriceSubscription(item)
          prices += if (price != "???") {
            val resultPrice = price.replace(".00", "", true)
            if (item == subList.last()) resultPrice else "$resultPrice;"
          } else {
            if (item == subList.last()) "???" else "???;"
          }
        }
        pricesSubsPref.value = prices
        Logger.v("Billing price subs: $prices")
      }
    }

    purchaseHelper.isIapPurchased.observe(this) {
      when (it) {
        is Tipping.Succeeded -> {
          isProPref.value = true
          Logger.v("Billing isProPref: true")
        }
        is Tipping.NoTips -> {
          val isProApp = resources.getBoolean(CommonR.bool.is_pro_app)
          isProPref.value = isProApp
          Logger.v("Billing isProPref: $isProApp")
        }
        is Tipping.FailedToLoad -> {
        }
      }
    }

    purchaseHelper.isSupPurchased.observe(this) {
      when (it) {
        is Tipping.Succeeded -> {
          isProSubsPref.value = true
          Logger.v("Billing isProSubsPref: true")
        }
        is Tipping.NoTips -> {
          isProSubsPref.value = false
          Logger.v("Billing isProSubsPref: false")
        }
        is Tipping.FailedToLoad -> {
        }
      }
    }

    updateCheckedPurchases()
  }

  private fun updateCheckedPurchases() {
    val iapList: ArrayList<String> = arrayListOf(BuildConfig.PRODUCT_ID_X1, BuildConfig.PRODUCT_ID_X2, BuildConfig.PRODUCT_ID_X3)
    val subList: ArrayList<String> = arrayListOf(BuildConfig.SUBSCRIPTION_ID_X1, BuildConfig.SUBSCRIPTION_ID_X2, BuildConfig.SUBSCRIPTION_ID_X3,
      BuildConfig.SUBSCRIPTION_YEAR_ID_X1, BuildConfig.SUBSCRIPTION_YEAR_ID_X2, BuildConfig.SUBSCRIPTION_YEAR_ID_X3)

    purchaseHelper.isIapPurchasedList.observe(this) {
      var purchased = ""
      iapList.forEach { item ->
        val result = if (purchaseHelper.isIapPurchased(item)) "1" else "0"
        purchased += if (item == iapList.last()) result else "$result;"
      }
      purchasedList.value = purchased
      Logger.v("Billing purchased: $purchased")
    }

    purchaseHelper.isSupPurchasedList.observe(this) {
      var purchased = ""
      subList.forEach { item ->
        val result = if (purchaseHelper.isSubPurchased(item)) "1" else "0"
        purchased += if (item == iapList.last()) result else "$result;"
      }
      purchasedSubsList.value = purchased
      Logger.v("Billing purchased subs: $purchased")
    }
  }

  private fun initRuStore() {
    ruStoreHelper.checkPurchasesAvailability(this)

    lifecycleScope.launch {
      ruStoreHelper.eventStart
        .flowWithLifecycle(lifecycle)
        .collect { event ->
          handleEventStart(event)
        }
    }

    lifecycleScope.launch {
      ruStoreHelper.stateBilling
        .flowWithLifecycle(lifecycle)
        .collect { state ->
          if (!state.isLoading) {
            productsRuStore.clear()
            productsRuStore.addAll(state.products)
            Logger.v("Billing RuStore products: $productsRuStore")
            //price update
            var prices = ""
            productList.forEach { item ->
              val product = state.products.firstOrNull {  it.productId == item  }
              val price = product?.priceLabel ?: "???"
              val resultPrice = price.replace(".00","",true)
              prices += if (item == productList.last()) resultPrice else "$resultPrice;"
            }
            pricesRustorePref.value = prices
            Logger.v("Billing RuStore price: $prices")
          }
        }
    }
//      lifecycleScope.launch {
//        ruStoreHelper.eventBilling
//          .flowWithLifecycle(lifecycle)
//          .collect { event ->
//            handleEventBilling(event)
//          }
//      }

    updateCheckedPurchasesRuStore()
  }

  private fun updateCheckedPurchasesRuStore() {
    lifecycleScope.launch {
      ruStoreHelper.statePurchased
        .flowWithLifecycle(lifecycle)
        .collect { state ->
          if (!state.isLoading && ruStoreIsConnected) {
            //update pro version
            val isProRu = state.purchases.isNotEmpty()
            isProRuPref.value = isProRu
            Logger.v("Billing isProSubsPref: $isProRu")

            //update of purchased
            var purchased = ""
            productList.forEach { item ->
              val result = if (state.purchases.firstOrNull { it.productId == item } != null) "1" else "0"
              purchased += if (item == productList.last()) result else "$result;"
            }
            purchasedListRustore.value = purchased
            Logger.v("Billing RuStore purchased: $purchased")
          }
        }
    }
  }

  private fun updateProducts() {
    pricesRustorePref.value = "???;???;???;???;???;???;???;???;???"
    purchasedListRustore.value = "0;0;0;0;0;0;0;0;0"
    ruStoreHelper.getProducts(productList)
  }

  private fun handleEventStart(event: StartPurchasesEvent) {
    when (event) {
      is StartPurchasesEvent.PurchasesAvailability -> {
        when (event.availability) {
          is FeatureAvailabilityResult.Available -> {
            //Process purchases available
            updateProducts()
            ruStoreIsConnected = true
          }

          is FeatureAvailabilityResult.Unavailable -> {
            //toast(event.availability.cause.message ?: "Process purchases unavailable", Toast.LENGTH_LONG)
          }

          else -> {}
        }
      }

      is StartPurchasesEvent.Error -> {
        //toast(event.throwable.message ?: "Process unknown error", Toast.LENGTH_LONG)
      }
    }
  }

//  private fun handleEventBilling(event: BillingEvent) {
//    when (event) {
//      is BillingEvent.ShowDialog -> {
//        Logger.v("RuStore Billing No Error: ${event.dialogInfo.titleRes}")
//      }
//
//      is BillingEvent.ShowError -> {
//        if (event.error is RuStoreException) {
//          event.error.resolveForBilling(this)
//        }
//        Logger.v("RuStore Billing Error: ${event.error.message.orEmpty()}")
//      }
//    }
//  }

  companion object {

    private const val NI_GO_TO_BOOK = "niGotoBook"

    fun goToBookIntent(context: Context) = Intent(context, MainActivity::class.java).apply {
      putExtra(NI_GO_TO_BOOK, true)
      flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
    }
  }
}
