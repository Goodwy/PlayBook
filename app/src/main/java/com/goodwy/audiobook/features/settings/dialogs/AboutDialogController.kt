package com.goodwy.audiobook.features.settings.dialogs

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.net.toUri
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.bottomsheets.setPeekHeight
import com.afollestad.materialdialogs.list.listItems
import com.goodwy.audiobook.R
import com.goodwy.audiobook.misc.DialogController
import timber.log.Timber

private val about_url1 = "https://play.google.com/store/apps/details?id=com.goodwy.audiobook".toUri()
private val about_url2 = "https://github.com/Goodwy/PlayBooks".toUri()
private val about_url3 = "https://play.google.com/store/apps/dev?id=8268163890866913014&hl=ru".toUri()
private val about_url4 = "https://github.com/PaulWoitaschek/Voice".toUri()

class AboutDialogController : DialogController() {

  override fun onCreateDialog(savedViewState: Bundle?): Dialog {
    return MaterialDialog(activity!!, BottomSheet(LayoutMode.WRAP_CONTENT)).apply {
      setPeekHeight(res = R.dimen.dialog_80)
      icon(R.drawable.ic_info)
      cornerRadius(4f)
      title(R.string.pref_about_title)
      message(R.string.pref_about_message) {
        html { visitUri(about_url4)}
      }
      listItems(R.array.pref_about_values) { _, index, _ ->
        when (index) {
          0 -> visitUri(about_url1)
          1 -> visitUri(about_url2)
          2 -> visitUri(about_url3)
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
