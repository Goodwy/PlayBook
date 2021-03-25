package com.goodwy.audiobook.features.bookPlaying

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

class SeekRewindDialogController : DialogController() {

  @field:[Inject Named(PrefKeys.SEEK_TIME_REWIND)]
  lateinit var seekTimeRewindPref: Pref<Int>

  @SuppressLint("InflateParams")
  override fun onCreateDialog(savedViewState: Bundle?): Dialog {
    appComponent.inject(this)

    val binding = DialogAmountChooserBinding.inflate(activity!!.layoutInflater)

    // init
    val oldSeekTime = seekTimeRewindPref.value
    binding.seekBar.max = (MAX - MIN) * FACTOR
    binding.seekBar.onProgressChanged(initialNotification = true) {
      val value = it / FACTOR + MIN
      binding.textView.text =
        activity!!.resources.getQuantityString(R.plurals.seconds, value, value)
    }
    binding.seekBar.progress = (oldSeekTime - MIN) * FACTOR

    return MaterialDialog(activity!!).apply {
      title(R.string.pref_seek_time)
	  cornerRadius(4f)
      customView(view = binding.root, scrollable = true)
      positiveButton(R.string.dialog_confirm) {
        val newSeekTime = binding.seekBar.progress / FACTOR + MIN
        seekTimeRewindPref.value = newSeekTime
      }
      negativeButton(R.string.dialog_cancel)
    }
  }

  companion object {
    private const val FACTOR = 10
    private const val MIN = 3
    private const val MAX = 60
  }
}
