package com.goodwy.audiobook.features.contribute

import android.content.Intent
import android.net.Uri
import androidx.annotation.StringRes
import androidx.core.net.toUri
import androidx.core.view.isVisible
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.bottomsheets.setPeekHeight
import com.goodwy.audiobook.R
import com.goodwy.audiobook.common.pref.PrefKeys
import com.goodwy.audiobook.databinding.ContributeBinding
import com.goodwy.audiobook.features.ViewBindingController
import com.goodwy.audiobook.injection.appComponent
import com.jaredrummler.cyanea.Cyanea
import com.jaredrummler.cyanea.app.BaseCyaneaActivity
import de.paulwoitaschek.flowpref.Pref
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class ContributeController : ViewBindingController<ContributeBinding>(ContributeBinding::inflate),
  BaseCyaneaActivity {

  @Inject
  lateinit var viewModel: ContributeViewModel

  @field:[Inject Named(PrefKeys.DARK_THEME)]
  lateinit var darkThemePref: Pref<Boolean>

  @field:[Inject Named(PrefKeys.PADDING)]
  lateinit var paddingPref: Pref<String>

  init {
    appComponent.inject(this)
  }

  private val backgroundLight: Int = android.graphics.Color.parseColor("#FFFAFAFA")
  private val backgroundDark: Int = android.graphics.Color.parseColor("#FF1E2225")
  // todo Lite
  private val PLAYBOOKS_GP_URL = "https://play.google.com/store/apps/details?id=com.goodwy.audiobook".toUri()
  private val GOODWY_GP_URL = "https://play.google.com/store/apps/dev?id=8268163890866913014".toUri()


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

    val grey: Int = android.graphics.Color.parseColor("#FFE0E0E0")
    val darkColor: Int = android.graphics.Color.parseColor("#FF272f35")
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
          if (cyanea.primaryDark == grey) {primaryDark(darkColor)}
        }
        ) else
        cyanea.edit {
          background(backgroundLight)
          backgroundLight(backgroundLight)
          backgroundLightLighter(backgroundLight) /*dialogs*/
          backgroundLightDarker(backgroundLight)
          navigationBar(backgroundLight)
          baseTheme(Cyanea.BaseTheme.LIGHT)
          if (cyanea.primaryDark == darkColor) {primaryDark(grey)}
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

  override fun ContributeBinding.onAttach() {
    //padding for Edge-to-edge
    lifecycleScope.launch {
      paddingPref.flow.collect {
        val top = paddingPref.value.substringBefore(';').toInt()
        val bottom = paddingPref.value.substringAfter(';').substringBefore(';').toInt()
        val left = paddingPref.value.substringBeforeLast(';').substringAfterLast(';').toInt()
        val right = paddingPref.value.substringAfterLast(';').toInt()
        root.setPadding(left, top, right, bottom)
      }
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
