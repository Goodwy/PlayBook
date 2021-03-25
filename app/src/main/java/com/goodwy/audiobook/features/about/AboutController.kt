package com.goodwy.audiobook.features.about

import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.net.toUri
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.bottomsheets.setPeekHeight
import com.goodwy.audiobook.BuildConfig
import com.goodwy.audiobook.R
import com.goodwy.audiobook.common.pref.PrefKeys
import com.goodwy.audiobook.databinding.AboutBinding
import com.goodwy.audiobook.features.ViewBindingController
import com.goodwy.audiobook.features.settings.dialogs.LicenseDialogController
import com.goodwy.audiobook.features.settings.dialogs.ChangelogDialogController
import com.goodwy.audiobook.injection.appComponent
import com.jaredrummler.cyanea.Cyanea
import com.jaredrummler.cyanea.app.BaseCyaneaActivity
import de.paulwoitaschek.flowpref.Pref
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Base64
import javax.inject.Inject
import javax.inject.Named

class AboutController : ViewBindingController<AboutBinding>(AboutBinding::inflate),
  BaseCyaneaActivity {

  @Inject
  lateinit var context: Context
  @Inject
  lateinit var viewModel: AboutViewModel

  init {
    appComponent.inject(this)
  }

  private var count: Int = 0
  private val encod1 = "WW91IGFyZSBhbG1vc3QgdGhlcmUh"
  private val encod2 = "RGV2ZWxvcGVyIG1vZGU="
  // todo Lite
  private val PLAYBOOKS_GP_URL = "https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}".toUri()
  private val GOODWY_GP_URL = "https://play.google.com/store/apps/dev?id=8268163890866913014".toUri()
  private val PLAYBOOKS_GH_URL = "https://github.com/Goodwy/PlayBooks".toUri()
  private val PRIVACY_GH_URL = "https://github.com/Goodwy/PlayBooks/blob/master/PRIVACY.md".toUri()


  override fun AboutBinding.onBindingCreated() {
    setupToolbar()

    appLayout.setOnClickListener {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val decod1 = Base64.getDecoder().decode(encod1)
        val decod2 = Base64.getDecoder().decode(encod2)
        val string1 = String(decod1)
        val string2 = String(decod2)
        ++count
        if (count == 9) {
          Toast.makeText(applicationContext, string1, Toast.LENGTH_SHORT).show()
        }
        if (count == 12) {
          count = count - 12
          viewModel.toggleDevMode()
          Toast.makeText(applicationContext, string2, Toast.LENGTH_LONG).show()
        }
      } else {
        ++count
        if (count == 9) {
          Toast.makeText(applicationContext, "9", Toast.LENGTH_SHORT).show()
        }
        if (count == 12) {
          count = count - 12
          viewModel.toggleDevMode()
          Toast.makeText(applicationContext, "12", Toast.LENGTH_LONG).show()
        }
      }
    }
    rateButton.setOnClickListener {
      val intent = Intent(Intent.ACTION_VIEW)
      intent.data = PLAYBOOKS_GP_URL
      startActivity(intent)
    }
    otherAppButton.setOnClickListener {
      val intent = Intent(Intent.ACTION_VIEW)
      intent.data = GOODWY_GP_URL
      startActivity(intent)
    }
    changelogLayout.setOnClickListener {
      ChangelogDialogController().showDialog(router)
    }
    licensesLayout.setOnClickListener {
      LicenseDialogController().showDialog(router)
    }
    privacyLayout.setOnClickListener {
      val intent = Intent(Intent.ACTION_VIEW)
      intent.data = PRIVACY_GH_URL
      startActivity(intent)
    }
    ginhubLayout.setOnClickListener {
      val intent = Intent(Intent.ACTION_VIEW)
      intent.data = PLAYBOOKS_GH_URL
      startActivity(intent)
    }
    participants.setOnClickListener {
      val intent = Intent(Intent.ACTION_VIEW)
      intent.data = GOODWY_GP_URL
      startActivity(intent)
    }
    specialThanks.setOnClickListener {
      showSpecialThanksDialog()
    }
  }

  fun getString(@StringRes resId: Int): String {
    return resources!!.getString(resId)
  }

  override fun AboutBinding.onAttach() {
    lifecycleScope.launch {
      viewModel.viewState().collect {
        render(it)
      }
    }
  }

  fun String.toSpanned(): Spanned {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      return Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
      @Suppress("DEPRECATION")
      return Html.fromHtml(this)
    }
  }

  private fun AboutBinding.render(state: AboutViewState) {
    Timber.d("render $state")
    rateTitle.text = getString(R.string.pref_about_message).toSpanned()
    appDialog.setDescription(context.getString(R.string.version) + " " + (BuildConfig.VERSION_NAME))
  }

  private fun AboutBinding.setupToolbar() {
    toolbar.setOnMenuItemClickListener {
      if (it.itemId == R.id.action_share) {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(
          Intent.EXTRA_TEXT,
          "https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"
        )
        sendIntent.type = "text/plain"
        startActivity(sendIntent)
        true
      } else
        false
    }
    toolbar.setNavigationOnClickListener {
      activity!!.onBackPressed()
    }
  }

  // Dialogs Special Thanks
  private fun showSpecialThanksDialog() {
    MaterialDialog(activity!!, BottomSheet(LayoutMode.WRAP_CONTENT))
      .setPeekHeight(res = R.dimen.dialog_80)
      .title(R.string.special_thanks_to)
      .message(R.string.special_thanks_message) {
        html()
        lineSpacing(1.2f)
      }
      .negativeButton(R.string.dialog_ok)
      // .icon(R.drawable.ic_info)
      .show()
  }
}
