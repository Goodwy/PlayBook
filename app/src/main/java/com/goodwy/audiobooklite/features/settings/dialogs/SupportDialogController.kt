package com.goodwy.audiobooklite.features.settings.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.net.toUri
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import com.goodwy.audiobooklite.R
import com.goodwy.audiobooklite.misc.DialogController
import timber.log.Timber

private val GITHUB_URL = "https://play.google.com/store/apps/details?id=com.goodwy.audiobooklite".toUri()
private val TRANSLATION_URL = "https://play.google.com/store/apps/dev?id=8268163890866913014".toUri()

class SupportDialogController : DialogController() {

  @SuppressLint("CheckResult")
  override fun onCreateDialog(savedViewState: Bundle?): Dialog {
    return MaterialDialog(activity!!).apply {
      title(R.string.pref_support_title)
	  cornerRadius(4f)
      listItems(R.array.pref_support_values) { _, index, _ ->
        when (index) {
          0 -> visitUri(GITHUB_URL)
          1 -> visitUri(TRANSLATION_URL)
          else -> error("Invalid index $index")
        }
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
