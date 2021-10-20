package com.goodwy.audiobooklite.features.contribute

import android.content.Intent
import android.net.Uri
import androidx.annotation.StringRes
import androidx.core.net.toUri
import androidx.core.view.isVisible
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.bottomsheets.setPeekHeight
import com.goodwy.audiobooklite.R
import com.goodwy.audiobooklite.common.pref.PrefKeys
import com.goodwy.audiobooklite.databinding.ContributeBinding
import com.goodwy.audiobooklite.features.ViewBindingController
import com.goodwy.audiobooklite.injection.appComponent
import com.jaredrummler.cyanea.Cyanea
import com.jaredrummler.cyanea.app.BaseCyaneaActivity
import de.paulwoitaschek.flowpref.Pref
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class ContributeController : ViewBindingController<ContributeBinding>(ContributeBinding::inflate),
  BaseCyaneaActivity {

  @Inject
  lateinit var viewModel: ContributeViewModel

  @field:[Inject Named(PrefKeys.DARK_THEME)]
  lateinit var darkThemePref: Pref<Boolean>

  init {
    appComponent.inject(this)
  }

  private val backgroundLight: Int = android.graphics.Color.parseColor("#FFFAFAFA")
  private val backgroundDark: Int = android.graphics.Color.parseColor("#FF1E2225")
  // todo Lite
  private val PLAYBOOKS_GP_URL = "https://play.google.com/store/apps/details?id=com.goodwy.audiobook".toUri()
  private val GOODWY_GP_URL = "https://play.google.com/store/apps/dev?id=8268163890866913014".toUri()
  private val grey: Int = android.graphics.Color.parseColor("#FFE0E0E0")
  private val darks: Int = android.graphics.Color.parseColor("#FF272f35")


  override fun ContributeBinding.onBindingCreated() {
    setupToolbar()

    icon.setOnClickListener {
      val intent = Intent(Intent.ACTION_VIEW)
      intent.data = PLAYBOOKS_GP_URL
      startActivity(intent)
    }
    button.setOnClickListener {
      val intent = Intent(Intent.ACTION_VIEW)
      intent.data = PLAYBOOKS_GP_URL
      startActivity(intent)
    }

    dark.onCheckedChanged {
      viewModel.toggleDarkTheme()
      if (darkThemePref.value) (
        cyanea.edit {
          background(backgroundDark)
          backgroundDark(backgroundDark)
          backgroundDarkDarker(backgroundDark)
          backgroundDarkLighter(backgroundDark) /*dialogs*/
          navigationBar(backgroundDark)
          baseTheme(Cyanea.BaseTheme.DARK)
          if (cyanea.primaryDark == grey) {primaryDark(darks)}
        }
        ) else
        cyanea.edit {
          background(backgroundLight)
          backgroundLight(backgroundLight)
          backgroundLightLighter(backgroundLight) /*dialogs*/
          backgroundLightDarker(backgroundLight)
          navigationBar(backgroundLight)
          baseTheme(Cyanea.BaseTheme.LIGHT)
          if (cyanea.primaryDark == darks) {primaryDark(grey)}
        }
      activity!!.recreate()
    }
    color.onCheckedChanged {
    }
    plus.setOnClickListener {
    }
    lifebuoy.setOnClickListener {
      val emailIntent = Intent(Intent.ACTION_SENDTO)
      emailIntent.data = Uri.parse("mailto:")
      emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_email)))
      emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject_lifebuoy))
      startActivity(Intent.createChooser(emailIntent, getString(R.string.email_chooser_title)))
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

  private fun ContributeBinding.render(state: ContributeViewState) {
    Timber.d("render $state")
    dark.isVisible = state.showDarkThemePref
  }

  private fun ContributeBinding.setupToolbar() {
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
