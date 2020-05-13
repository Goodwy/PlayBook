package com.goodwy.audiobook.features.settings.dialogs

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.net.toUri
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import com.goodwy.audiobook.R
import com.goodwy.audiobook.misc.DialogController
import timber.log.Timber

private val pref_pay_url = "https://play.google.com/store/apps/details?id=com.goodwy.audiobook".toUri()
class PayDialogController : DialogController() {

  override fun onCreateDialog(savedViewState: Bundle?): Dialog {
    return MaterialDialog(activity!!).apply {
      icon(R.drawable.ic_pay)
      cornerRadius(16f)
      message(R.string.pref_pay_message) {
        html {  }
        lineSpacing(1.4f)
      }
      listItems(R.array.pref_pay_values) { _, index, _ ->
        when (index) {
          0 -> visitUri(pref_pay_url)
          else -> error("Invalid index $index")
        }
      }
      negativeButton(R.string.dialog_cancel)
    }
  }

  private fun visitUri(uri: Uri) {
    try {
      startActivity(Intent(Intent.ACTION_VIEW, uri))
    } catch (exception: ActivityNotFoundException) {
      Timber.e(exception)
    }
  }
}
