package com.goodwy.audiobook.features.settings

import android.os.Build
import android.widget.Toast
import androidx.core.view.isVisible
import com.goodwy.audiobook.R
import com.goodwy.audiobook.common.pref.PrefKeys
import com.goodwy.audiobook.databinding.SettingsBinding
import com.goodwy.audiobook.features.ViewBindingController
import com.goodwy.audiobook.features.bookPlaying.SeekDialogController
import com.goodwy.audiobook.features.settings.dialogs.AutoRewindDialogController
import com.goodwy.audiobook.features.settings.dialogs.LicenseDialogController
import com.goodwy.audiobook.features.settings.dialogs.ChangelogDialogController
import com.goodwy.audiobook.features.settings.dialogs.PayDialogController
import com.goodwy.audiobook.features.settings.dialogs.SupportDialogController
import com.goodwy.audiobook.features.settings.dialogs.AboutDialogController
import com.goodwy.audiobook.features.settings.dialogs.ColorAccentDialogController
import com.goodwy.audiobook.features.settings.dialogs.ColorPrimaryDialogController
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

class SettingsController : ViewBindingController<SettingsBinding>(SettingsBinding::inflate),
  BaseCyaneaActivity {

  @Inject
  lateinit var viewModel: SettingsViewModel

  @field:[Inject Named(PrefKeys.CONTENTS_BUTTON_MODE)]
  lateinit var contentsButtonMode: Pref<Boolean>

  @field:[Inject Named(PrefKeys.DARK_THEME)]
  lateinit var darkThemePref: Pref<Boolean>

  init {
    appComponent.inject(this)
  }

  private var count: Int = 0
  private val encod = "WW91IGFyZSBhbG1vc3QgdGhlcmUh"
  private val backgroundLight: Int = android.graphics.Color.parseColor("#FFFAFAFA")
  private val backgroundDark: Int = android.graphics.Color.parseColor("#FF1E2225")


  override fun SettingsBinding.onBindingCreated() {
    setupToolbar()

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
      /*PayDialogController().showDialog(router)*/
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
      ColorPrimaryDialogController().showDialog(router)
    }
    colorAccent.setOnClickListener {
      ColorAccentDialogController().showDialog(router)
    }

    skipAmount.setOnClickListener {
      viewModel.changeSkipAmount()
    }
    autoRewind.setOnClickListener {
      viewModel.changeAutoRewindAmount()
    }
    resumePlayback.onCheckedChanged { viewModel.toggleResumeOnReplug() }

    changelogDialog.setOnClickListener {
      ChangelogDialogController().showDialog(router)
    }
    aboutDialog.setOnClickListener {
      AboutDialogController().showDialog(router)
    }
    //todo Lite
   /* version.setOnClickListener {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val decod = Base64.getDecoder().decode(encod)
        val string = String(decod)
        ++count
        if (count == 9) {
          Toast.makeText(applicationContext, string, Toast.LENGTH_LONG).show()
        }
        if (count == 12) {
          count = count - 12
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
        }
      }
    }*/
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
    tintNavBar.setChecked(state.tintNavBar)
    darkTheme.isVisible = state.showDarkThemePref
    darkTheme.setChecked(state.useDarkTheme)
    resumePlayback.setChecked(state.resumeOnReplug)
    skipAmount.setDescription(resources!!.getQuantityString(R.plurals.seconds, state.seekTimeInSeconds, state.seekTimeInSeconds))
    autoRewind.setDescription(resources!!.getQuantityString(R.plurals.seconds, state.autoRewindInSeconds, state.autoRewindInSeconds))
  }

  private fun SettingsBinding.setupToolbar() {
    toolbar.setOnMenuItemClickListener {
      if (it.itemId == R.id.action_contribute) {
        SupportDialogController().showDialog(router)
        true
      } else
        false
      if (it.itemId == R.id.action_about) {
        AboutDialogController().showDialog(router)
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
      if (it.itemId == R.id.action_pay) {
        PayDialogController().showDialog(router)
        true
      } else
        false
    }
    toolbar.setNavigationOnClickListener {
      activity!!.onBackPressed()
    }
  }
}
