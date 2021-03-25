package com.goodwy.audiobook.features.settings.dialogs

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.net.toUri
import com.afollestad.materialdialogs.MaterialDialog
import com.goodwy.audiobook.R
import com.goodwy.audiobook.misc.DialogController
import timber.log.Timber

private val pref_pay_url = "https://play.google.com/store/apps/details?id=com.goodwy.audiobook".toUri()
private val github_url = "https://github.com/Goodwy/PlayBooks".toUri()
class PayDialogController : DialogController() {

  override fun onCreateDialog(savedViewState: Bundle?): Dialog {
    return MaterialDialog(activity!!).apply {
      cornerRadius(4f)
      icon(R.mipmap.ic_launcher)
      title(R.string.app_name)
      message(R.string.pref_pay_message) {
        html { visitUri(github_url) }
        lineSpacing(1.4f)
      }
      positiveButton(R.string.pref_about_url1) {
        visitUri(pref_pay_url)
      }
      neutralButton(R.string.dialog_cancel) {
      }
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
