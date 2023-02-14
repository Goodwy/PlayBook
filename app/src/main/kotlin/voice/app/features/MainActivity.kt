package voice.app.features

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.lifecycle.lifecycleScope
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.ControllerChangeHandler
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.google.android.material.elevation.SurfaceColors
import de.paulwoitaschek.flowpref.Pref
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import voice.app.AppController
import voice.app.R
import voice.app.databinding.ActivityBookBinding
import voice.app.features.bookOverview.EditCoverDialogController
import voice.app.features.bookmarks.BookmarkController
import voice.app.features.imagepicker.CoverFromInternetController
import voice.app.injection.appComponent
import voice.app.misc.conductor.asTransaction
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
import javax.inject.Inject
import javax.inject.Named


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

  @field:[Inject Named(PrefKeys.PRICES)]
  lateinit var pricesPref: Pref<String>

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

  private val billingViewModel by viewModels<BillingViewModel>()

  override fun onCreate(savedInstanceState: Bundle?) {
    appComponent.inject(this)
    setTheme(R.style.AppTheme)
    super.onCreate(savedInstanceState)
    val binding = ActivityBookBinding.inflate(layoutInflater)
    setContentView(binding.root)

    //Billing
    billingViewModel.billingConnectionState.observe(this) {
      when (it) {
        BillingConnectionState.Connected -> {
          Logger.v("Billing Connected")
        }
        BillingConnectionState.Failed -> {
          Logger.v("Billing Failed")
        }
        BillingConnectionState.GettingDetails,
        BillingConnectionState.Connecting -> {
          Logger.v("Billing Connecting...")
        }
      }
    }

    billingViewModel.skuDetails.observe(this) {
      val prices = mutableListOf<String>()
      it.forEach { (tip, details) ->
        Logger.v("Billing details: $tip\"${details.title}\\n${details.price}\"")
        prices.add(details.price)
      }
      pricesPref.value = prices.toString()
      Logger.v("Billing details: $prices.toString()")
    }

    billingViewModel.purchaseResult.observe(this) { event ->
      if (event.result is PurchaseResult.Success) {
        isProPref.value = true
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setTitle(R.string.tipping_jar_dialog_sucess_title)
        alertDialog.setMessage(getString(R.string.tipping_jar_dialog_sucess_message))
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.tipping_jar_dialog_sucess_button_title)) { dialog: DialogInterface, _ ->
          dialog.dismiss()
        }
        alertDialog.show()
      } else if (event.result is PurchaseResult.Fail) {
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setTitle(R.string.tipping_jar_dialog_error_title)
        alertDialog.setMessage(getString(R.string.tipping_jar_dialog_error_message))
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.dialog_cancel)) { dialog: DialogInterface, _ ->
          dialog.dismiss()
        }
        alertDialog.show()
      }
    }

    if (appInfoProvider.applicationID == "kooboidua.ywdoog.moc".reversed()) isProPref.value = false
    else {
      billingViewModel.tippingSum.observe(this) { sum ->
        when (sum) {
          is TippingSum.FailedToLoad -> {
            Logger.v("Billing history FailedToLoad")
            isProPref.value = false
          }
          is TippingSum.Succeeded -> {
            Logger.v("Billing history Succeeded: ${sum.sum}")
            isProPref.value = true
          }
          is TippingSum.NoTips -> {
            Logger.v("Billing history NoTips")
            isProPref.value = false
          }
        }
      }
    }

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
      val color = SurfaceColors.SURFACE_0.getColor(this)
      window.navigationBarColor = color
      paddingPref.value = "0;0;0;0"
    }

    mSettingsContentObserver = SettingsContentObserver(this, Handler(Looper.getMainLooper()))
    applicationContext.contentResolver.registerContentObserver(Settings.System.CONTENT_URI, true, mSettingsContentObserver)

    router = Conductor.attachRouter(this, binding.root, savedInstanceState)
    if (!router.hasRootController()) {
      setupRouter()
    }

    router.addChangeListener(
      object : ControllerChangeHandler.ControllerChangeListener {
        override fun onChangeStarted(
          to: Controller?,
          from: Controller?,
          isPush: Boolean,
          container: ViewGroup,
          handler: ControllerChangeHandler,
        ) {
          from?.setOptionsMenuHidden(true)
        }

        override fun onChangeCompleted(
          to: Controller?,
          from: Controller?,
          isPush: Boolean,
          container: ViewGroup,
          handler: ControllerChangeHandler,
        ) {
          from?.setOptionsMenuHidden(false)
        }
      },
    )

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
                router.pushController(BookmarkController(destination.bookId).asTransaction())
              }
              is Destination.CoverFromInternet -> {
                router.pushController(CoverFromInternetController(destination.bookId).asTransaction())
              }
              is Destination.Playback -> {
                lifecycleScope.launch {
                  currentBook.updateData { destination.bookId }
                  router.pushController(BookPlayController(destination.bookId).asTransaction())
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
              is Destination.BuyTip -> {
                val tip = when (destination.tip) {
                  1 -> Tip.Small
                  2 -> Tip.Medium
                  else -> Tip.Big
                }
                billingViewModel.buyTip(this@MainActivity, tip)
              }
            }
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
  }

  override fun onDestroy() {
    super.onDestroy()
    applicationContext.contentResolver.unregisterContentObserver(mSettingsContentObserver)
  }

  override fun onNewIntent(intent: Intent?) {
    super.onNewIntent(intent)
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
    intent.getStringExtra(NI_GO_TO_BOOK)
      ?.let {
        val bookId = BookId(it)
        val bookShelf = RouterTransaction.with(AppController())
        val bookPlay = BookPlayController(bookId).asTransaction()
        router.setBackstack(listOf(bookShelf, bookPlay), null)
        return
      }

    // if we should play the current book, set the backstack and return early
    if (intent?.action == "playCurrent") {
      runBlocking { currentBook.data.first() }?.let { bookId ->
        val bookShelf = RouterTransaction.with(AppController())
        val bookPlay = BookPlayController(bookId).asTransaction()
        router.setBackstack(listOf(bookShelf, bookPlay), null)
        playerController.play()
        return
      }
    }

    val rootTransaction = RouterTransaction.with(AppController())
    router.setRoot(rootTransaction)
  }

  @Deprecated("Deprecated in Java")
  override fun onBackPressed() {
    if (router.backstackSize == 1) {
      super.onBackPressed()
    } else {
      router.handleBack()
    }
  }

  companion object {
    private const val NI_GO_TO_BOOK = "niGotoBook"

    /** Returns an intent that lets you go directly to the playback screen for a certain book **/
    fun goToBookIntent(context: Context, bookId: BookId) = Intent(context, MainActivity::class.java).apply {
      putExtra(NI_GO_TO_BOOK, bookId.value)
      flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
    }
  }
}
