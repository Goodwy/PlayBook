package com.goodwy.audiobook.features

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Resources
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.ControllerChangeHandler
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.attachRouter
import com.goodwy.audiobook.R
import com.goodwy.audiobook.common.pref.PrefKeys
import com.goodwy.audiobook.data.repo.BookRepository
import com.goodwy.audiobook.features.bookOverview.BookOverviewController
import com.goodwy.audiobook.features.bookPlaying.BookPlayController
import com.goodwy.audiobook.injection.appComponent
import com.goodwy.audiobook.misc.PermissionHelper
import com.goodwy.audiobook.misc.Permissions
import com.goodwy.audiobook.misc.RouterProvider
import com.goodwy.audiobook.misc.conductor.asTransaction
import com.goodwy.audiobook.playback.PlayerController
import com.goodwy.audiobook.playback.session.search.BookSearchHandler
import com.goodwy.audiobook.playback.session.search.BookSearchParser
import com.jaredrummler.cyanea.CyaneaResources
import com.jaredrummler.cyanea.app.BaseCyaneaActivity
import com.jaredrummler.cyanea.delegate.CyaneaDelegate
import com.jaredrummler.cyanea.prefs.CyaneaThemePickerActivity
import de.paulwoitaschek.flowpref.Pref
import kotlinx.android.synthetic.main.activity_book.*
import java.util.UUID
import javax.inject.Inject
import javax.inject.Named

/**
 * Activity that coordinates the book shelf and play screens.
 */
class MainActivity : BaseActivity(), RouterProvider,
  BaseCyaneaActivity {

  private lateinit var permissionHelper: PermissionHelper
  private lateinit var permissions: Permissions
  @field:[Inject Named(PrefKeys.CURRENT_BOOK)]
  lateinit var currentBookIdPref: Pref<UUID>
  @field:[Inject Named(PrefKeys.SINGLE_BOOK_FOLDERS)]
  lateinit var singleBookFolderPref: Pref<Set<String>>
  @field:[Inject Named(PrefKeys.COLLECTION_BOOK_FOLDERS)]
  lateinit var collectionBookFolderPref: Pref<Set<String>>
  @field:[Inject Named(PrefKeys.SCREEN_ORIENTATION)]
  lateinit var screenOrientationPref: Pref<Boolean>
  @Inject
  lateinit var repo: BookRepository
  @Inject
  lateinit var bookSearchParser: BookSearchParser
  @Inject
  lateinit var bookSearchHandler: BookSearchHandler
  @Inject
  lateinit var playerController: PlayerController

  private lateinit var router: Router

  override fun onCreate(savedInstanceState: Bundle?) {
    setTheme(R.style.splashScreenTheme)
    appComponent.inject(this)
    delegate.onCreate(savedInstanceState)
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_book)
    if (screenOrientationPref.value) {
      setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    } else {
      setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER)
    }

    permissions = Permissions(this)
    permissionHelper = PermissionHelper(this, permissions)

    router = attachRouter(root, savedInstanceState)
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
          handler: ControllerChangeHandler
        ) {
          from?.setOptionsMenuHidden(true)
        }

        override fun onChangeCompleted(
          to: Controller?,
          from: Controller?,
          isPush: Boolean,
          container: ViewGroup,
          handler: ControllerChangeHandler
        ) {
          from?.setOptionsMenuHidden(false)
        }
      }
    )

    setupFromIntent(intent)
  }

  override fun onNewIntent(intent: Intent?) {
    super.onNewIntent(intent)
    setupFromIntent(intent)
  }

  private fun setupFromIntent(intent: Intent?) {
    bookSearchParser.parse(intent)?.let { bookSearchHandler.handle(it) }
  }

  private fun setupRouter() {
    // if we should enter a book set the backstack and return early
    intent.getStringExtra(NI_GO_TO_BOOK)
      ?.let(UUID::fromString)
      ?.let(repo::bookById)
      ?.let {
        val bookShelf = RouterTransaction.with(BookOverviewController())
        val bookPlay = BookPlayController(it.id).asTransaction()
        router.setBackstack(listOf(bookShelf, bookPlay), null)
        return
      }

    // if we should play the current book, set the backstack and return early
    if (intent?.action == "playCurrent") {
      repo.bookById(currentBookIdPref.value)?.let {
        val bookShelf = RouterTransaction.with(BookOverviewController())
        val bookPlay = BookPlayController(it.id).asTransaction()
        router.setBackstack(listOf(bookShelf, bookPlay), null)
        playerController.play()
        return
      }
    }

    val rootTransaction = RouterTransaction.with(BookOverviewController())
    router.setRoot(rootTransaction)
  }

  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<String>,
    grantResults: IntArray
  ) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    this.permissions.onRequestPermissionsResult(requestCode, permissions, grantResults)
  }

  override fun provideRouter() = router

  override fun onStart() {
    super.onStart()

    val anyFolderSet = collectionBookFolderPref.value.size + singleBookFolderPref.value.size > 0
    if (anyFolderSet) {
      permissionHelper.storagePermission()
    }
  }

  override fun onBackPressed() {
    if (router.backstackSize == 1) {
      super.onBackPressed()
    } else router.handleBack()
  }

  /*override fun openOptionsMenu() {
    super.openOptionsMenu()
    startActivity(Intent(this, CyaneaThemePickerActivity::class.java))
    /*startActivity(Intent(this, CyaneaSettingsActivity::class.java))*/
    /*startActivity(Intent(this, CyaneaThemePickerActivity::class.java))*/
  }

  override fun closeContextMenu() {
    super.closeContextMenu()
    recreate()
  }

  override fun closeOptionsMenu() {
    super.closeOptionsMenu()
    if (contentsButtonMode.value) (
    cyanea.edit {
      shouldTintNavBar(true)
    }.recreate(this)
    ) else
    cyanea.edit {
      shouldTintNavBar(false)
    }.recreate(this)
  }*/

  override fun closeOptionsMenu() {
    super.closeOptionsMenu()
    if (screenOrientationPref.value) {
      setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    } else {
      setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER)
    }
  }

    private val delegate: CyaneaDelegate by lazy {
    CyaneaDelegate.create(this, cyanea, getThemeResId())
  }

  private val resources: CyaneaResources by lazy {
    CyaneaResources(super.getResources(), cyanea)
  }

  override fun attachBaseContext(newBase: Context) {
    super.attachBaseContext(delegate.wrap(newBase))
  }

  override fun getResources(): Resources = resources


  companion object {
    private const val NI_GO_TO_BOOK = "niGotoBook"

    /** Returns an intent that lets you go directly to the playback screen for a certain book **/
    fun goToBookIntent(context: Context, bookId: UUID) = Intent(context, MainActivity::class.java).apply {
      putExtra(NI_GO_TO_BOOK, bookId.toString())
      flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
    }
  }
}
