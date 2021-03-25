package com.goodwy.audiobook.features.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.bottomsheets.setPeekHeight
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.goodwy.audiobook.BuildConfig
import com.goodwy.audiobook.R
import com.goodwy.audiobook.common.pref.PrefKeys
import com.goodwy.audiobook.databinding.SettingsBinding
import com.goodwy.audiobook.features.ViewBindingController
import com.goodwy.audiobook.features.about.AboutController
import com.goodwy.audiobook.features.bookPlaying.SeekDialogController
import com.goodwy.audiobook.features.contribute.ContributeController
import com.goodwy.audiobook.features.contribute.ContributeViewModel
import com.goodwy.audiobook.features.prefAppearanceUI.PrefAppearanceUIController
import com.goodwy.audiobook.features.prefBeta.PrefBetaController
import com.goodwy.audiobook.features.prefSkipInterval.PrefSkipIntervalController
import com.goodwy.audiobook.features.settings.dialogs.AutoRewindDialogController
import com.goodwy.audiobook.features.settings.dialogs.LicenseDialogController
import com.goodwy.audiobook.features.settings.dialogs.ChangelogDialogController
import com.goodwy.audiobook.features.settings.dialogs.SupportDialogController
import com.goodwy.audiobook.features.settings.dialogs.ColorAccentPalettesDialogController
import com.goodwy.audiobook.features.settings.dialogs.ColorPrinaryPalettesDialogController
import com.goodwy.audiobook.injection.appComponent
import com.goodwy.audiobook.misc.conductor.asTransaction
import com.jaredrummler.cyanea.Cyanea
import com.jaredrummler.cyanea.app.BaseCyaneaActivity
import de.paulwoitaschek.flowpref.Pref
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class SettingsController : ViewBindingController<SettingsBinding>(SettingsBinding::inflate),
  BaseCyaneaActivity {

  @Inject
  lateinit var context: Context
  @Inject
  lateinit var viewModel: SettingsViewModel
  @Inject
  lateinit var contributeViewModel: ContributeViewModel

  @field:[Inject Named(PrefKeys.CONTENTS_BUTTON_MODE)]
  lateinit var contentsButtonMode: Pref<Boolean>

  @field:[Inject Named(PrefKeys.SHOW_RATING)]
  lateinit var showRatingPref: Pref<Boolean>

  @field:[Inject Named(PrefKeys.DEV_MODE)]
  lateinit var devModePref: Pref<Boolean>

  @field:[Inject Named(PrefKeys.DARK_THEME)]
  lateinit var darkThemePref: Pref<Boolean>

  @field:[Inject Named(PrefKeys.SCREEN_ORIENTATION)]
  lateinit var screenOrientationPref: Pref<Boolean>

  init {
    appComponent.inject(this)
  }

  private var count: Int = 0
  private val encod = "WW91IGFyZSBhbG1vc3QgdGhlcmUh"
  private val backgroundLight: Int = android.graphics.Color.parseColor("#FFFAFAFA")
  private val backgroundDark: Int = android.graphics.Color.parseColor("#FF1E2225")


  override fun SettingsBinding.onBindingCreated() {
    setupToolbar()

    pay.setOnClickListener {
     // PayDialogController().showDialog(router)
      val transaction = ContributeController().asTransaction()
      router.pushController(transaction)
    }

    darkTheme.onCheckedChanged {
      viewModel.toggleDarkTheme()
      if (darkThemePref.value) (
        cyanea.edit {
          background(backgroundDark)
          backgroundDark(backgroundDark)
          backgroundDarkDarker(backgroundDark)
          backgroundDarkLighter(backgroundDark) /*dialogs*/
          navigationBar(backgroundDark)
          baseTheme(Cyanea.BaseTheme.DARK)
        }
        ) else
        cyanea.edit {
          background(backgroundLight)
          backgroundLight(backgroundLight)
          backgroundLightLighter(backgroundLight) /*dialogs*/
          backgroundLightDarker(backgroundLight)
          navigationBar(backgroundLight)
          baseTheme(Cyanea.BaseTheme.LIGHT)
        }
      activity!!.recreate()
      //todo Lite
      /*val transaction = ContributeController().asTransaction()
      router.pushController(transaction)*/
    }
    tintNavBar.onCheckedChanged {
      viewModel.toggleTintNavBar()
      if (contentsButtonMode.value) (
        cyanea.edit {
          shouldTintNavBar(true)
        }
        ) else
        cyanea.edit {
          shouldTintNavBar(false)
        }
      activity!!.recreate()
    }
    colorPrimary.setOnClickListener {
      ColorPrinaryPalettesDialogController().showDialog(router)
    }
    colorAccent.setOnClickListener {
      ColorAccentPalettesDialogController().showDialog(router)
    }
    appearanceUI.setOnClickListener {
      val transaction = PrefAppearanceUIController().asTransaction()
      router.pushController(transaction)
    }

    skipInterval.setOnClickListener {
      val transaction = PrefSkipIntervalController().asTransaction()
      router.pushController(transaction)
    }
    /*skipAmount.setOnClickListener {
      viewModel.changeSkipAmount()
    }
    autoRewind.setOnClickListener {
      viewModel.changeAutoRewindAmount()
    }*/
    resumePlayback.onCheckedChanged { viewModel.toggleResumeOnReplug() }

    supportAndSuggestions.setOnClickListener {
      val address = getString(R.string.support_email)
      val subject = getString(R.string.email_support_lifebuoy)
      val chooserTitle = getString(R.string.email_chooser_title)
      val emailIntent = Intent(
        Intent.ACTION_SENDTO, Uri.fromParts(
          "mailto", address, null
        )
      )
      emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
      startActivity(Intent.createChooser(emailIntent, chooserTitle))
    }
    screenOrientation.setOnClickListener {
      screenOrientationDialog()
    }
    gridViewAuto.onCheckedChanged {
      viewModel.toggleGridViewAuto()
    }
    prefBeta.setOnClickListener {
      //showAboutDialog()
      val transaction = PrefBetaController().asTransaction()
      router.pushController(transaction)
    }
    aboutDialog.setOnClickListener {
      //showAboutDialog()
      val transaction = AboutController().asTransaction()
      router.pushController(transaction)
    }
    rateDismiss.setOnClickListener {
      contributeViewModel.toggleShowRating()
      activity!!.recreate()
    }
    rateOkay.setOnClickListener {
      contributeViewModel.rateIntent()
    }
    empty.setOnClickListener {
      contributeViewModel.toggleShowRating()
      activity!!.recreate()
    }
  }

  fun getString(@StringRes resId: Int): String {
    return resources!!.getString(resId)
  }

  override fun SettingsBinding.onAttach() {
    lifecycleScope.launch {
      viewModel.viewEffects.collect {
        handleViewEffect(it)
      }
    }

    lifecycleScope.launch {
      viewModel.viewState().collect {
        render(it)
      }
    }
  }

  private fun handleViewEffect(effect: SettingsViewEffect) {
    when (effect) {
      is SettingsViewEffect.ShowChangeSkipAmountDialog -> {
        SeekDialogController().showDialog(router)
      }
      is SettingsViewEffect.ShowChangeAutoRewindAmountDialog -> {
        AutoRewindDialogController().showDialog(router)
      }
    }
  }

  private fun SettingsBinding.render(state: SettingsViewState) {
    Timber.d("render $state")
    // todo Lite
    pay.isVisible = false
    tintNavBar.setChecked(state.tintNavBar)
    darkTheme.isVisible = state.showDarkThemePref
    darkTheme.setChecked(state.useDarkTheme)
    resumePlayback.setChecked(state.resumeOnReplug)
   // skipAmount.setDescription(resources!!.getQuantityString(R.plurals.seconds, state.seekTimeInSeconds, state.seekTimeInSeconds))
   // autoRewind.setDescription(resources!!.getQuantityString(R.plurals.seconds, state.autoRewindInSeconds, state.autoRewindInSeconds))
    aboutDialog.setDescription(context.getString(R.string.version) + " " + (BuildConfig.VERSION_NAME))
    rateLayout.isVisible = showRatingPref.value
    prefBeta.isVisible = devModePref.value
    if (state.screenOrientationPref) {
      screenOrientation.setDescription(context.getString(R.string.pref_screen_orientation_summary_portrait))
      screenOrientation.setValue(context.getString(R.string.pref_screen_orientation_value_portrait))
    } else {
      screenOrientation.setDescription(context.getString(R.string.pref_screen_orientation_summary_system))
      screenOrientation.setValue(context.getString(R.string.pref_screen_orientation_value_system))
    }
    gridViewAuto.setChecked(state.gridViewAutoPref)
  }

  private fun SettingsBinding.setupToolbar() {
    toolbar.setOnMenuItemClickListener {
      if (it.itemId == R.id.action_contribute) {
        SupportDialogController().showDialog(router)
        true
      } else
        false
      if (it.itemId == R.id.action_about) {
        showAboutDialog()
        true
      } else
        false
      if (it.itemId == R.id.action_license) {
        LicenseDialogController().showDialog(router)
        true
      } else
        false
      if (it.itemId == R.id.action_changelog) {
        ChangelogDialogController().showDialog(router)
        true
      } else
        false
      //todo Lite
     /* if (it.itemId == R.id.action_pay) {
        PayDialogController().showDialog(router)
        true
      } else
        false*/
    }
    toolbar.setNavigationOnClickListener {
      activity!!.onBackPressed()
    }
  }

  // Dialogs About
  private fun showAboutDialog() {
    MaterialDialog(activity!!, BottomSheet(LayoutMode.WRAP_CONTENT))
      .setPeekHeight(res = R.dimen.dialog_80)
      .title(R.string.pref_about_title)
      .message(R.string.pref_about_message) {
        html()
        lineSpacing(1.2f)
      }
      .neutralButton(R.string.special_thanks_to) { dialog ->
        showSpecialThanksDialog()
      }
      .negativeButton(R.string.pref_licenses_title) { dialog ->
        LicenseDialogController().showDialog(router)
      }
      .positiveButton(R.string.dialog_ok)
      .icon(R.drawable.ic_info)
      .show()
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

  // Dialogs Screen Orientation
  private fun screenOrientationDialog() {
    val initialSelect = if (screenOrientationPref.value) 1 else 0
    MaterialDialog(activity!!)
      .title(R.string.pref_screen_orientation_title)
      .listItemsSingleChoice(R.array.pref_screen_orientation, initialSelection = initialSelect) { _, index, _ ->
        when (index) {
          0 -> {screenOrientationPref.value = false
            activity!!.closeOptionsMenu()}
          1 -> {screenOrientationPref.value = true
            activity!!.closeOptionsMenu()}
          else -> error("Invalid index $index")
        }
      }
      .show()
  }
}
