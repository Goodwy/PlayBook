package com.goodwy.audiobook.features.settings.dialogs

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.net.toUri
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.bottomsheets.setPeekHeight
import com.goodwy.audiobook.R
import com.goodwy.audiobook.misc.DialogController
import timber.log.Timber

private val license_url = "https://www.gnu.org/licenses/".toUri()

class ChangelogDialogController : DialogController() {

  override fun onCreateDialog(savedViewState: Bundle?): Dialog {
    return MaterialDialog(activity!!, BottomSheet()).apply {
      setPeekHeight(res = R.dimen.dialog_80)
      cornerRadius(4f)
      icon(R.drawable.ic_changelog)
      title(R.string.pref_changelog_title)
      message(R.string.pref_changelog_message) {
        html { visitUri(license_url) }
        lineSpacing(1.0f)
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
