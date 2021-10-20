package com.goodwy.audiobooklite.features.prefAppearanceUIPlayer

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import de.paulwoitaschek.flowpref.Pref
import com.goodwy.audiobooklite.R
import com.goodwy.audiobooklite.common.pref.PrefKeys
import com.goodwy.audiobooklite.databinding.DialogPlayButtonStyleBinding
import com.goodwy.audiobooklite.injection.appComponent
import com.goodwy.audiobooklite.misc.DialogController
import javax.inject.Inject
import javax.inject.Named

class PlayStyleDialogController : DialogController() {

  @field:[Inject Named(PrefKeys.PLAY_BUTTON_STYLE)]
  lateinit var playButtonStylePref: Pref<Int>

  @SuppressLint("InflateParams")
  override fun onCreateDialog(savedViewState: Bundle?): Dialog {
    appComponent.inject(this)

    val binding = DialogPlayButtonStyleBinding.inflate(activity!!.layoutInflater)

    val isClassicButton: Boolean = playButtonStylePref.value == 1
    val isRoundedButton: Boolean = playButtonStylePref.value == 2

    binding.classicButton.setChecked(isClassicButton)
    binding.classicButton.setOnClickListener {
      playButtonStylePref.value = 1
      dismissDialog()
    }

    binding.roundedButton.setChecked(isRoundedButton)
    binding.roundedButton.setOnClickListener {
      playButtonStylePref.value = 2
      dismissDialog()
    }

    return MaterialDialog(activity!!).apply {
      title(R.string.pref_play_buttons_style_title)
      cornerRadius(4f)
      customView(view = binding.root, scrollable = true)
      //positiveButton(R.string.dialog_confirm)
      negativeButton(R.string.dialog_cancel)
    }
  }
}
