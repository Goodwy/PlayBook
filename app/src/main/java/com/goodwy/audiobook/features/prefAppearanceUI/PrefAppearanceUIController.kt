package com.goodwy.audiobook.features.prefAppearanceUI

import android.content.Context
import androidx.annotation.StringRes
import com.goodwy.audiobook.R
import com.goodwy.audiobook.common.pref.PrefKeys
import com.goodwy.audiobook.databinding.PrefAppearanceUiBinding
import com.goodwy.audiobook.features.ViewBindingController
import com.goodwy.audiobook.features.contribute.ContributeViewModel
import com.goodwy.audiobook.injection.appComponent
import com.jaredrummler.cyanea.app.BaseCyaneaActivity
import de.paulwoitaschek.flowpref.Pref
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class PrefAppearanceUIController : ViewBindingController<PrefAppearanceUiBinding>(PrefAppearanceUiBinding::inflate),
  BaseCyaneaActivity {

  @Inject
  lateinit var context: Context
  @Inject
  lateinit var viewModel: PrefAppearanceUIViewModel
  @Inject
  lateinit var contributeViewModel: ContributeViewModel

  @field:[Inject Named(PrefKeys.PADDING)]
  lateinit var paddingPref: Pref<String>

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
    iconMode.onCheckedChanged { viewModel.toggleIconMode() }
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
    iconMode.setChecked(state.iconModePref)
  }

  private fun PrefAppearanceUiBinding.setupToolbar() {
    toolbar.setNavigationOnClickListener {
      activity!!.onBackPressed()
    }
  }
}
