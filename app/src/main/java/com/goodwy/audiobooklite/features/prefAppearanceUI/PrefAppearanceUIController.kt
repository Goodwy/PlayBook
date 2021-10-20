package com.goodwy.audiobooklite.features.prefAppearanceUI

import android.content.Context
import androidx.annotation.StringRes
import com.goodwy.audiobooklite.R
import com.goodwy.audiobooklite.databinding.PrefAppearanceUiBinding
import com.goodwy.audiobooklite.features.ViewBindingController
import com.goodwy.audiobooklite.features.contribute.ContributeViewModel
import com.goodwy.audiobooklite.injection.appComponent
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
    showProgressBar.onCheckedChanged { viewModel.toggleProgressBar() }
    showDivider.onCheckedChanged { viewModel.toggleDivider() }
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
    }
  }

  private fun PrefAppearanceUiBinding.render(state: PrefAppearanceUIViewState) {
    Timber.d("render $state")
    if (state.miniPlayerStylePref == 1) {
      miniPlayerStyle.setValue(context.getString(R.string.pref_mini_player_floating_button))
    } else {
      miniPlayerStyle.setValue(context.getString(R.string.pref_mini_player_mini_player_floating))
    }
    showProgressBar.setChecked(state.showProgressBarPref)
    showDivider.setChecked(state.showDividerPref)
  }

  private fun PrefAppearanceUiBinding.setupToolbar() {
    toolbar.setNavigationOnClickListener {
      activity!!.onBackPressed()
    }
  }
}
