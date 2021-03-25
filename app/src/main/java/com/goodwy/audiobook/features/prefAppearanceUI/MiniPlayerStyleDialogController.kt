package com.goodwy.audiobook.features.prefAppearanceUI

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import de.paulwoitaschek.flowpref.Pref
import com.goodwy.audiobook.R
import com.goodwy.audiobook.common.pref.PrefKeys
import com.goodwy.audiobook.databinding.DialogMiniPlayerStyleBinding
import com.goodwy.audiobook.injection.appComponent
import com.goodwy.audiobook.misc.DialogController
import com.goodwy.audiobook.misc.onProgressChanged
import javax.inject.Inject
import javax.inject.Named

class MiniPlayerStyleDialogController : DialogController() {

  @field:[Inject Named(PrefKeys.SHOW_MINI_PLAYER)]
  lateinit var showMiniPlayerPref: Pref<Boolean>

  @field:[Inject Named(PrefKeys.MINI_PLAYER_STYLE)]
  lateinit var miniPlayerStylePref: Pref<Int>

  @SuppressLint("InflateParams")
  override fun onCreateDialog(savedViewState: Bundle?): Dialog {
    appComponent.inject(this)

    val binding = DialogMiniPlayerStyleBinding.inflate(activity!!.layoutInflater)

    val isFloatingButton: Boolean = miniPlayerStylePref.value == 1
    val isFloatingMiniPlayer: Boolean = miniPlayerStylePref.value == 2

    binding.floatingButton.setChecked(isFloatingButton)
    binding.floatingButton.setOnClickListener {
      miniPlayerStylePref.value = 1
      dismissDialog()
    }

    binding.miniPlayer.setChecked(isFloatingMiniPlayer)
    binding.miniPlayer.setOnClickListener {
      miniPlayerStylePref.value = 2
      dismissDialog()
    }

    return MaterialDialog(activity!!).apply {
      title(R.string.pref_show_mini_player_title)
      cornerRadius(4f)
      customView(view = binding.root, scrollable = true)
      //positiveButton(R.string.dialog_confirm)
      negativeButton(R.string.dialog_cancel)
    }
  }
}
