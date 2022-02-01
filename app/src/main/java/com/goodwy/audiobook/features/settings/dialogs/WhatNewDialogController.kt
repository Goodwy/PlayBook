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
import com.goodwy.audiobook.R
import com.goodwy.audiobook.misc.DialogController
import timber.log.Timber


class WhatNewDialogController : DialogController() {

  override fun onCreateDialog(savedViewState: Bundle?): Dialog {
    return MaterialDialog(activity!!, BottomSheet(LayoutMode.WRAP_CONTENT)).apply {
      cornerRadius(res = R.dimen.md_corner_radius)
      icon(R.drawable.ic_new)
      title(R.string.pref_what_new_title)
      message(R.string.pref_what_new_message) {
        html {  }
        lineSpacing(1.0f)
      }
      positiveButton (R.string.dialog_ok)
    }
  }
}
