package com.goodwy.audiobook.features.settings.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import de.paulwoitaschek.flowpref.Pref
import com.goodwy.audiobook.R
import com.goodwy.audiobook.common.pref.PrefKeys
import com.goodwy.audiobook.databinding.DialogAmountChooserBinding
import com.goodwy.audiobook.injection.appComponent
import com.goodwy.audiobook.misc.DialogController
import com.goodwy.audiobook.misc.onProgressChanged
import javax.inject.Inject
import javax.inject.Named

class AutoRewindDialogController : DialogController() {

  @field:[Inject Named(PrefKeys.AUTO_REWIND_AMOUNT)]
  lateinit var autoRewindAmountPref: Pref<Int>

  @SuppressLint("InflateParams")
  override fun onCreateDialog(savedViewState: Bundle?): Dialog {
    appComponent.inject(this)

    val binding = DialogAmountChooserBinding.inflate(activity!!.layoutInflater)

    val oldRewindAmount = autoRewindAmountPref.value
    binding.seekBar.max = (MAX - MIN) * FACTOR
    binding.seekBar.progress = (oldRewindAmount - MIN) * FACTOR
    binding.seekBar.onProgressChanged(initialNotification = true) {
      val progress = it / FACTOR
      val autoRewindSummary = activity!!.resources.getQuantityString(
        R.plurals.pref_auto_rewind_summary,
        progress,
        progress
      )
      binding.textView.text = autoRewindSummary
    }

    return MaterialDialog(activity!!).apply {
      title(R.string.pref_auto_rewind_title)
	  cornerRadius(16f)
      customView(view = binding.root, scrollable = true)
      positiveButton(R.string.dialog_confirm) {
        val newRewindAmount = binding.seekBar.progress / FACTOR + MIN
        autoRewindAmountPref.value = newRewindAmount
      }
      negativeButton(R.string.dialog_cancel)
    }
  }

  companion object {
    private const val MIN = 0
    private const val MAX = 20
    private const val FACTOR = 10
  }
}
