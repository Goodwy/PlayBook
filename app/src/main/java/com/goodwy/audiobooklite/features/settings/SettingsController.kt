package com.goodwy.audiobooklite.features.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Process
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.bottomsheets.setPeekHeight
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.goodwy.audiobooklite.BuildConfig
import com.goodwy.audiobooklite.R
import com.goodwy.audiobooklite.common.pref.PrefKeys
import com.goodwy.audiobooklite.databinding.SettingsBinding
import com.goodwy.audiobooklite.features.ViewBindingController
import com.goodwy.audiobooklite.features.about.AboutController
import com.goodwy.audiobooklite.features.bookPlaying.SeekDialogController
import com.goodwy.audiobooklite.features.contribute.ContributeController
import com.goodwy.audiobooklite.features.contribute.ContributeViewModel
import com.goodwy.audiobooklite.features.prefAppearanceUI.PrefAppearanceUIController
import com.goodwy.audiobooklite.features.prefAppearanceUIPlayer.CoverSettingsDialogController
import com.goodwy.audiobooklite.features.prefAppearanceUIPlayer.PrefAppearanceUIPlayerController
import com.goodwy.audiobooklite.features.prefAppearanceUIPlayer.PrefAppearanceUIPlayerViewEffect
import com.goodwy.audiobooklite.features.prefBeta.PrefBetaController
import com.goodwy.audiobooklite.features.prefSkipInterval.PrefSkipIntervalController
import com.goodwy.audiobooklite.features.settings.dialogs.AutoRewindDialogController
import com.goodwy.audiobooklite.features.settings.dialogs.LicenseDialogController
import com.goodwy.audiobooklite.features.settings.dialogs.ChangelogDialogController
import com.goodwy.audiobooklite.features.settings.dialogs.SupportDialogController
import com.goodwy.audiobooklite.features.settings.dialogs.ColorAccentPalettesDialogController
import com.goodwy.audiobooklite.features.settings.dialogs.ColorPrinaryPalettesDialogController
import com.goodwy.audiobooklite.injection.appComponent
import com.goodwy.audiobooklite.misc.conductor.asTransaction
import com.jaredrummler.cyanea.Cyanea
import com.jaredrummler.cyanea.app.BaseCyaneaActivity
import de.paulwoitaschek.flowpref.Pref
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Timer
import javax.inject.Inject
import javax.inject.Named
import kotlin.concurrent.schedule

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

  @field:[Inject Named(PrefKeys.USE_ENGLISH)]
  lateinit var useEnglishPref: Pref<Boolean>

  init {
    appComponent.inject(this)
  }

  private var count: Int = 0
  private val encod = "WW91IGFyZSBhbG1vc3QgdGhlcmUh"
  private val backgroundLight: Int = android.graphics.Color.parseColor("#FFFAFAFA")
  private val backgroundDark: Int = android.graphics.Color.parseColor("#FF1E2225")
  private val grey: Int = android.graphics.Color.parseColor("#FFE0E0E0")
  private val dark: Int = android.graphics.Color.parseColor("#FF272f35")


  override fun SettingsBinding.onBindingCreated() {
    setupToolbar()

    pay.setOnClickListener {
     // PayDialogController().showDialog(router)
      val transaction = ContributeController().asTransaction()
      router.pushController(transaction)
    }

    darkTheme.onCheckedChanged {
      /*viewModel.toggleDarkTheme()
      if (darkThemePref.value) (
        cyanea.edit {
          background(backgroundDark)
          backgroundDark(backgroundDark)
          backgroundDarkDarker(backgroundDark)
          backgroundDarkLighter(backgroundDark) /*dialogs*/
          navigationBar(backgroundDark)
          baseTheme(Cyanea.BaseTheme.DARK)
          if (cyanea.primaryDark == grey) {primaryDark(dark)}
        }
        ) else
        cyanea.edit {
          background(backgroundLight)
          backgroundLight(backgroundLight)
          backgroundLightLighter(backgroundLight) /*dialogs*/
          backgroundLightDarker(backgroundLight)
          navigationBar(backgroundLight)
          baseTheme(Cyanea.BaseTheme.LIGHT)
          if (cyanea.primaryDark == dark) {primaryDark(grey)}
        }
      activity!!.recreate()*/
      //todo Lite
      val transaction = ContributeController().asTransaction()
      router.pushController(transaction)
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
    coverSettings.setOnClickListener {
      viewModel.changeCoverSettings()
    }
    appearanceUI.setOnClickListener {
      val transaction = PrefAppearanceUIController().asTransaction()
      router.pushController(transaction)
    }
    appearanceUIPlayer.setOnClickListener {
      val transaction = PrefAppearanceUIPlayerController().asTransaction()
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

    useEnglish.onCheckedChanged {
      viewModel.toggleUseEnglish()
      restartApp()
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

    supportAndSuggestions.setOnClickListener {
      val emailIntent = Intent(Intent.ACTION_SENDTO)
      emailIntent.data = Uri.parse("mailto:")
      emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_email)))
      emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name) + ": " + getString(R.string.email_subject_support))
      emailIntent.putExtra(Intent.EXTRA_TEXT, StringBuilder("\n\n")
        .append("\n\n--- Please write your message above this line ---\n\n")
        .append("Package: ${context.packageName}\n")
        .append("Version: ${BuildConfig.VERSION_NAME}\n")
        .append("Device: ${Build.BRAND} ${Build.MODEL}\n")
        .append("SDK: ${Build.VERSION.SDK_INT}\n")
        .toString())
      startActivity(Intent.createChooser(emailIntent, getString(R.string.email_chooser_title)))
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

  fun restartApp() {
    Timer("SettingUp", false).schedule(500) {
      val intent = activity!!.baseContext.packageManager.getLaunchIntentForPackage(activity!!.baseContext.packageName)
      intent!!.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
      startActivity(intent!!)
      Process.killProcess(Process.myPid())
      System.exit(0)
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
      is SettingsViewEffect.ShowChangeCoverSettingsDialog -> {
        CoverSettingsDialogController().showDialog(router)
      }
    }
  }

  private fun SettingsBinding.render(state: SettingsViewState) {
    Timber.d("render $state")
    // todo Lite
    pay.isVisible = true
    darkTheme.isVisible = state.showDarkThemePref
    darkTheme.setChecked(state.useDarkTheme)
    tintNavBar.setChecked(state.tintNavBar)
    coverSettings.setDescription(activity!!.resources.getString(R.string.pref_cover_corner_radius_title) + ", " + activity!!.resources.getString(R.string.pref_cover_elevation_title))

    resumePlayback.setChecked(state.resumeOnReplug)
   // skipAmount.setDescription(resources!!.getQuantityString(R.plurals.seconds, state.seekTimeInSeconds, state.seekTimeInSeconds))
   // autoRewind.setDescription(resources!!.getQuantityString(R.plurals.seconds, state.autoRewindInSeconds, state.autoRewindInSeconds))

    useEnglish.isVisible = useEnglishPref.value || java.util.Locale.getDefault().language != "en"
    useEnglish.setChecked(useEnglishPref.value)
    if (state.screenOrientationPref) {
      screenOrientation.setDescription(context.getString(R.string.pref_screen_orientation_summary_portrait))
      screenOrientation.setValue(context.getString(R.string.pref_screen_orientation_value_portrait))
    } else {
      screenOrientation.setDescription(context.getString(R.string.pref_screen_orientation_summary_system))
      screenOrientation.setValue(context.getString(R.string.pref_screen_orientation_value_system))
    }
    gridViewAuto.setChecked(state.gridViewAutoPref)
    prefBeta.isVisible = devModePref.value

    aboutDialog.setDescription(context.getString(R.string.version) + " " + (BuildConfig.VERSION_NAME))
    rateLayout.isVisible = showRatingPref.value
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
