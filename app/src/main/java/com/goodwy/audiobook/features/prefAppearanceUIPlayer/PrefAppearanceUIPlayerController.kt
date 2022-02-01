package com.goodwy.audiobook.features.prefAppearanceUIPlayer

import android.content.Context
import androidx.annotation.StringRes
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.goodwy.audiobook.R
import com.goodwy.audiobook.common.pref.PrefKeys
import com.goodwy.audiobook.databinding.PrefAppearanceUiPlayerBinding
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

class PrefAppearanceUIPlayerController : ViewBindingController<PrefAppearanceUiPlayerBinding>(PrefAppearanceUiPlayerBinding::inflate),
  BaseCyaneaActivity {

  @Inject
  lateinit var context: Context
  @Inject
  lateinit var viewModel: PrefAppearanceUIPlayerViewModel
  @Inject
  lateinit var contributeViewModel: ContributeViewModel

  @field:[Inject Named(PrefKeys.PLAYER_BACKGROUND)]
  lateinit var playerBackgroundPref: Pref<Int>

  @field:[Inject Named(PrefKeys.PADDING)]
  lateinit var paddingPref: Pref<String>

  init {
    appComponent.inject(this)
  }

  override fun PrefAppearanceUiPlayerBinding.onBindingCreated() {
    setupToolbar()

    playerBackground.onCheckedChanged {
      viewModel.changePlayerBackground()
    }
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

  private fun handleViewEffect(effect: PrefAppearanceUIPlayerViewEffect) {
    when (effect) {
      is PrefAppearanceUIPlayerViewEffect.ShowChangePlayerBackgroundDialog -> {
        PlayerBackgroundDialog()
      }
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
    when (state.playerBackgroundPref) {
      0 -> playerBackground.setValue(context.getString(R.string.pref_player_background_theme))
      1 -> playerBackground.setValue(context.getString(R.string.pref_player_background_cover_color))
      2 -> playerBackground.setValue(context.getString(R.string.pref_player_background_blurred_cover))
    }

    when (state.playButtonStylePref) {
      1 -> playButtonStyle.setValue(context.getString(R.string.pref_rewind_buttons_style_classic))
      2 -> playButtonStyle.setValue(context.getString(R.string.pref_rewind_buttons_style_rounded))
    }

    when (state.rewindButtonStylePref) {
      1 -> rewindButtonStyle.setValue(context.getString(R.string.pref_rewind_buttons_style_classic))
      2 -> rewindButtonStyle.setValue(context.getString(R.string.pref_rewind_buttons_style_rounded))
    }

    showSliderVolume.setChecked(state.showSliderVolumePref)
  }

  private fun PrefAppearanceUiPlayerBinding.setupToolbar() {
    toolbar.setNavigationOnClickListener {
      activity!!.onBackPressed()
    }
  }

  // Dialogs Screen Orientation
  private fun PlayerBackgroundDialog() {
    MaterialDialog(activity!!)
      .title(R.string.pref_player_background_title)
      .listItemsSingleChoice(R.array.pref_player_background, initialSelection = playerBackgroundPref.value) { _, index, _ ->
        when (index) {
          0 -> {playerBackgroundPref.value = 0
            activity!!.closeOptionsMenu()}
          1 -> {playerBackgroundPref.value = 1
            activity!!.closeOptionsMenu()}
          2 -> {playerBackgroundPref.value = 2
            activity!!.closeOptionsMenu()}
          else -> error("Invalid index $index")
        }
      }
      .show()
  }
}
