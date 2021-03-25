package com.goodwy.audiobook.features.prefBeta

import android.content.Context
import androidx.annotation.StringRes
import com.goodwy.audiobook.databinding.PrefBetaBinding
import com.goodwy.audiobook.features.ViewBindingController
import com.goodwy.audiobook.features.contribute.ContributeViewModel
import com.goodwy.audiobook.injection.appComponent
import com.jaredrummler.cyanea.app.BaseCyaneaActivity
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class PrefBetaController : ViewBindingController<PrefBetaBinding>(PrefBetaBinding::inflate),
  BaseCyaneaActivity {

  @Inject
  lateinit var context: Context
  @Inject
  lateinit var viewModel: PrefBetaViewModel
  @Inject
  lateinit var contributeViewModel: ContributeViewModel

  init {
    appComponent.inject(this)
  }

  override fun PrefBetaBinding.onBindingCreated() {
    setupToolbar()

    showSliderVolume.onCheckedChanged {
      viewModel.toggleShowSliderVolume()
    }
    /*iconMode.onCheckedChanged {
      viewModel.toggleIconMode()
    }
    screenOrientation.onCheckedChanged {
      viewModel.toggleScreenOrientation()
      activity!!.closeOptionsMenu()
    }
    showMiniPlayer.onCheckedChanged {
      viewModel.toggleShowMiniPlayer()
    }
    gridViewAuto.onCheckedChanged {
      viewModel.toggleGridViewAuto()
    }*/
  }

  fun getString(@StringRes resId: Int): String {
    return resources!!.getString(resId)
  }

  override fun PrefBetaBinding.onAttach() {
/*    lifecycleScope.launch {
      viewModel.viewEffects.collect {
        handleViewEffect(it)
      }
    }
*/
    lifecycleScope.launch {
      viewModel.viewState().collect {
        render(it)
      }
    }
  }

  /*private fun handleViewEffect(effect: PrefBetaViewEffect) {
    when (effect) {
      is PrefBetaViewEffect.ShowChangeSkipAmountDialog -> {
        SeekDialogController().showDialog(router)
      }
      is PrefBetaViewEffect.ShowChangeAutoRewindAmountDialog -> {
        AutoRewindDialogController().showDialog(router)
      }
    }
  }*/

  private fun PrefBetaBinding.render(state: PrefBetaViewState) {
    Timber.d("render $state")
    showSliderVolume.setChecked(state.showSliderVolumePref)
   // iconMode.setChecked(state.iconModePref)
   // screenOrientation.setChecked(state.screenOrientationPref)
   // showMiniPlayer.setChecked(state.showMiniPlayerPref)
   // gridViewAuto.setChecked(state.gridViewAutoPref)
  }

  private fun PrefBetaBinding.setupToolbar() {
    toolbar.setNavigationOnClickListener {
      activity!!.onBackPressed()
    }
  }
}
