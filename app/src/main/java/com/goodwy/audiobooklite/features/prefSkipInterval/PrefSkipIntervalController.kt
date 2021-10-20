package com.goodwy.audiobooklite.features.prefSkipInterval

import android.content.Context
import androidx.annotation.StringRes
import com.goodwy.audiobooklite.R
import com.goodwy.audiobooklite.databinding.PrefSkipIntervalBinding
import com.goodwy.audiobooklite.features.ViewBindingController
import com.goodwy.audiobooklite.features.bookPlaying.SeekDialogController
import com.goodwy.audiobooklite.features.bookPlaying.SeekRewindDialogController
import com.goodwy.audiobooklite.features.contribute.ContributeViewModel
import com.goodwy.audiobooklite.features.settings.dialogs.AutoRewindDialogController
import com.goodwy.audiobooklite.injection.appComponent
import com.jaredrummler.cyanea.app.BaseCyaneaActivity
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class PrefSkipIntervalController : ViewBindingController<PrefSkipIntervalBinding>(PrefSkipIntervalBinding::inflate),
  BaseCyaneaActivity {

  @Inject
  lateinit var context: Context
  @Inject
  lateinit var viewModel: PrefSkipIntervalViewModel
  @Inject
  lateinit var contributeViewModel: ContributeViewModel

  init {
    appComponent.inject(this)
  }

  override fun PrefSkipIntervalBinding.onBindingCreated() {
    setupToolbar()

    skipAmountForward.setOnClickListener {
      viewModel.changeSkipAmountForward()
    }
    skipAmountRewind.setOnClickListener {
      viewModel.changeskipAmountRewind()
    }
    autoRewind.setOnClickListener {
      viewModel.changeAutoRewindAmount()
    }
  }

  fun getString(@StringRes resId: Int): String {
    return resources!!.getString(resId)
  }

  override fun PrefSkipIntervalBinding.onAttach() {
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

  private fun handleViewEffect(effect: PrefSkipIntervalViewEffect) {
    when (effect) {
      is PrefSkipIntervalViewEffect.ShowChangeSkipAmountDialog -> {
        SeekDialogController().showDialog(router)
      }
      is PrefSkipIntervalViewEffect.ShowChangeSkipRewindAmountDialog -> {
        SeekRewindDialogController().showDialog(router)
      }
      is PrefSkipIntervalViewEffect.ShowChangeAutoRewindAmountDialog -> {
        AutoRewindDialogController().showDialog(router)
      }
    }
  }

  private fun PrefSkipIntervalBinding.render(state: PrefSkipIntervalViewState) {
    Timber.d("render $state")
    skipAmountForward.setValue(resources!!.getQuantityString(R.plurals.seconds, state.seekTimeInSeconds, state.seekTimeInSeconds))
    skipAmountRewind.setValue(resources!!.getQuantityString(R.plurals.seconds, state.seekTimeRewindInSeconds, state.seekTimeRewindInSeconds))
    autoRewind.setDescription(resources!!.getQuantityString(R.plurals.pref_auto_rewind_summary, state.autoRewindInSeconds, state.autoRewindInSeconds))
    autoRewind.setValue(resources!!.getQuantityString(R.plurals.seconds, state.autoRewindInSeconds, state.autoRewindInSeconds))
  }

  private fun PrefSkipIntervalBinding.setupToolbar() {
    toolbar.setNavigationOnClickListener {
      activity!!.onBackPressed()
    }
  }
}
