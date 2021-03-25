package com.goodwy.audiobook.features.prefAppearanceUI

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import de.paulwoitaschek.flowpref.Pref
import com.goodwy.audiobook.R
import com.goodwy.audiobook.common.pref.PrefKeys
import com.goodwy.audiobook.databinding.DialogRewindButtonStyleBinding
import com.goodwy.audiobook.injection.appComponent
import com.goodwy.audiobook.misc.DialogController
import javax.inject.Inject
import javax.inject.Named

class RewindStyleDialogController : DialogController() {

  @field:[Inject Named(PrefKeys.REWIND_BUTTON_STYLE)]
  lateinit var rewindButtonStylePref: Pref<Int>

  @SuppressLint("InflateParams")
  override fun onCreateDialog(savedViewState: Bundle?): Dialog {
    appComponent.inject(this)

    val binding = DialogRewindButtonStyleBinding.inflate(activity!!.layoutInflater)

    val isClassicButton: Boolean = rewindButtonStylePref.value == 1
    val isRoundedButton: Boolean = rewindButtonStylePref.value == 2

    binding.classicButton.setChecked(isClassicButton)
    binding.classicButton.setOnClickListener {
      rewindButtonStylePref.value = 1
      dismissDialog()
    }

    binding.roundedButton.setChecked(isRoundedButton)
    binding.roundedButton.setOnClickListener {
      rewindButtonStylePref.value = 2
      dismissDialog()
    }

    return MaterialDialog(activity!!).apply {
      title(R.string.pref_rewind_buttons_style_title)
      cornerRadius(4f)
      customView(view = binding.root, scrollable = true)
      //positiveButton(R.string.dialog_confirm)
      negativeButton(R.string.dialog_cancel)
    }
  }
}
