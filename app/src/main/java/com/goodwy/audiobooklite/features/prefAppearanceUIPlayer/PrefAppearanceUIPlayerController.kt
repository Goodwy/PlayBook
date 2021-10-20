package com.goodwy.audiobooklite.features.prefAppearanceUIPlayer

import android.content.Context
import androidx.annotation.StringRes
import com.goodwy.audiobooklite.R
import com.goodwy.audiobooklite.databinding.PrefAppearanceUiPlayerBinding
import com.goodwy.audiobooklite.features.ViewBindingController
import com.goodwy.audiobooklite.features.contribute.ContributeViewModel
import com.goodwy.audiobooklite.injection.appComponent
import com.jaredrummler.cyanea.app.BaseCyaneaActivity
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class PrefAppearanceUIPlayerController : ViewBindingController<PrefAppearanceUiPlayerBinding>(PrefAppearanceUiPlayerBinding::inflate),
  BaseCyaneaActivity {

  @Inject
  lateinit var context: Context
  @Inject
  lateinit var viewModel: PrefAppearanceUIPlayerViewModel
  @Inject
  lateinit var contributeViewModel: ContributeViewModel

  init {
    appComponent.inject(this)
  }

  override fun PrefAppearanceUiPlayerBinding.onBindingCreated() {
    setupToolbar()

    playButtonStyle.onCheckedChanged {
      viewModel.changePlayButtonStyle()
    }
    rewindButtonStyle.onCheckedChanged {
      viewModel.changeRewindButtonStyle()
    }
    showSliderVolume.onCheckedChanged { viewModel.toggleShowSliderVolume() }
  }

  fun getString(@StringRes resId: Int): String {
    return resources!!.getString(resId)
  }

  override fun PrefAppearanceUiPlayerBinding.onAttach() {
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

  private fun handleViewEffect(effect: PrefAppearanceUIPlayerViewEffect) {
    when (effect) {
      is PrefAppearanceUIPlayerViewEffect.ShowChangePlayButtonStyleDialog -> {
        PlayStyleDialogController().showDialog(router)
      }
      is PrefAppearanceUIPlayerViewEffect.ShowChangeRewindButtonStyleDialog -> {
        RewindStyleDialogController().showDialog(router)
      }
    }
  }

  private fun PrefAppearanceUiPlayerBinding.render(state: PrefAppearanceUIPlayerViewState) {
    Timber.d("render $state")
    if (state.playButtonStylePref == 1) {
      playButtonStyle.setValue(context.getString(R.string.pref_rewind_buttons_style_classic))
    } else {
      playButtonStyle.setValue(context.getString(R.string.pref_rewind_buttons_style_rounded))
    }
    if (state.rewindButtonStylePref == 1) {
      rewindButtonStyle.setValue(context.getString(R.string.pref_rewind_buttons_style_classic))
    } else {
      rewindButtonStyle.setValue(context.getString(R.string.pref_rewind_buttons_style_rounded))
    }
    showSliderVolume.setChecked(state.showSliderVolumePref)
  }

  private fun PrefAppearanceUiPlayerBinding.setupToolbar() {
    toolbar.setNavigationOnClickListener {
      activity!!.onBackPressed()
    }
  }
}
