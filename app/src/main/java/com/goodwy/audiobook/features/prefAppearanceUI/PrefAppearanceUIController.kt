package com.goodwy.audiobook.features.prefAppearanceUI

import android.content.Context
import androidx.annotation.StringRes
import com.goodwy.audiobook.R
import com.goodwy.audiobook.databinding.PrefAppearanceUiBinding
import com.goodwy.audiobook.features.ViewBindingController
import com.goodwy.audiobook.features.contribute.ContributeViewModel
import com.goodwy.audiobook.injection.appComponent
import com.jaredrummler.cyanea.app.BaseCyaneaActivity
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class PrefAppearanceUIController : ViewBindingController<PrefAppearanceUiBinding>(PrefAppearanceUiBinding::inflate),
  BaseCyaneaActivity {

  @Inject
  lateinit var context: Context
  @Inject
  lateinit var viewModel: PrefAppearanceUIViewModel
  @Inject
  lateinit var contributeViewModel: ContributeViewModel

  init {
    appComponent.inject(this)
  }

  override fun PrefAppearanceUiBinding.onBindingCreated() {
    setupToolbar()

    miniPlayerStyle.onCheckedChanged {
      viewModel.changeMiniPlayerStyle()
    }
    rewindButtonStyle.onCheckedChanged {
      viewModel.changeRewindButtonStyle()
    }
  }

  fun getString(@StringRes resId: Int): String {
    return resources!!.getString(resId)
  }

  override fun PrefAppearanceUiBinding.onAttach() {
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

  private fun handleViewEffect(effect: PrefAppearanceUIViewEffect) {
    when (effect) {
      is PrefAppearanceUIViewEffect.ShowChangeMiniPlayerStyleDialog -> {
        MiniPlayerStyleDialogController().showDialog(router)
      }
      is PrefAppearanceUIViewEffect.ShowChangeRewindButtonStyleDialog -> {
        RewindStyleDialogController().showDialog(router)
      }
    }
  }

  private fun PrefAppearanceUiBinding.render(state: PrefAppearanceUIViewState) {
    Timber.d("render $state")
    if (state.miniPlayerStylePref == 1) {
      miniPlayerStyle.setValue(context.getString(R.string.pref_mini_player_floating_button))
    } else {
      miniPlayerStyle.setValue(context.getString(R.string.pref_mini_player_mini_player_floating))
    }
    if (state.rewindButtonStylePref == 1) {
      rewindButtonStyle.setValue(context.getString(R.string.pref_rewind_buttons_style_classic))
    } else {
      rewindButtonStyle.setValue(context.getString(R.string.pref_rewind_buttons_style_rounded))
    }
  }

  private fun PrefAppearanceUiBinding.setupToolbar() {
    toolbar.setNavigationOnClickListener {
      activity!!.onBackPressed()
    }
  }
}
