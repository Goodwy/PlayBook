package com.goodwy.audiobook.features.settings

import androidx.core.view.isVisible
import com.goodwy.audiobook.R
import com.goodwy.audiobook.databinding.SettingsBinding
import com.goodwy.audiobook.features.ViewBindingController
import com.goodwy.audiobook.features.bookPlaying.SeekDialogController
import com.goodwy.audiobook.features.settings.dialogs.AutoRewindDialogController
import com.goodwy.audiobook.features.settings.dialogs.LicenseDialogController
import com.goodwy.audiobook.features.settings.dialogs.ChangelogDialogController
import com.goodwy.audiobook.features.settings.dialogs.PayDialogController
import com.goodwy.audiobook.features.settings.dialogs.SupportDialogController
import com.goodwy.audiobook.features.settings.dialogs.AboutDialogController
import com.goodwy.audiobook.injection.appComponent
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class SettingsController : ViewBindingController<SettingsBinding>(SettingsBinding::inflate) {

  @Inject
  lateinit var viewModel: SettingsViewModel

  init {
    appComponent.inject(this)
  }

  override fun SettingsBinding.onBindingCreated() {
    setupToolbar()

    resumePlayback.onCheckedChanged { viewModel.toggleResumeOnReplug() }
    darkTheme.onCheckedChanged { viewModel.toggleDarkTheme() }

    skipAmount.setOnClickListener {
      viewModel.changeSkipAmount()
    }
    autoRewind.setOnClickListener {
      viewModel.changeAutoRewindAmount()
    }
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
