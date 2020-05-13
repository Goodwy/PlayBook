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

private val license_url = "https://www.gnu.org/licenses/".toUri()

class LicenseDialogController : DialogController() {

  override fun onCreateDialog(savedViewState: Bundle?): Dialog {
    return MaterialDialog(activity!!).apply {
      cornerRadius(16f)
      title(R.string.pref_licenses_title)
      message(R.string.pref_licenses_message) {
        html { visitUri(license_url) }
        lineSpacing(1.4f)
      }
      positiveButton (R.string.dialog_ок)
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
