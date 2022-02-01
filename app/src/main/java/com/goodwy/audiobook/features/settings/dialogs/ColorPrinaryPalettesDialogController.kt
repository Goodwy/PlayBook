package com.goodwy.audiobook.features.settings.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import com.goodwy.audiobook.R
import com.goodwy.audiobook.misc.DialogController
import com.goodwy.audiobook.uitools.*
import com.jaredrummler.cyanea.Cyanea
import com.jaredrummler.cyanea.app.BaseCyaneaActivity

class ColorPrinaryPalettesDialogController : DialogController(),
  BaseCyaneaActivity {

  @SuppressLint("CheckResult")
  override fun onCreateDialog(savedViewState: Bundle?): Dialog {
    return MaterialDialog(activity!!).apply {
      icon(R.drawable.ic_palettes)
      cornerRadius(4f)
      title(R.string.palettes)
      listItems(R.array.pref_palettes_values) { _, index, _ ->
        when (index) {
          0 -> ColorPrimaryOriginalDialogController().showDialog(router)
          1 -> ColorPrimaryMaterialDialogController().showDialog(router)
          2 -> ColorPrimaryIosDialogController().showDialog(router)
          else -> error("Invalid index $index")
        }
        dismiss()
      }
      negativeButton(R.string.dialog_cancel)
      neutralButton(R.string.pref_default_theme_title) {
        if (cyanea.baseTheme == Cyanea.BaseTheme.LIGHT) {
          cyanea.edit {
            primary(blue)
            primaryLight(blue)
            primaryDark(grey)
            accent(blue)
            accentLight(blue)
            accentDark(blue)
            navigationBar(cyanea.backgroundColor)
          }
        } else
          cyanea.edit {
            primary(blue)
            primaryLight(blue)
            primaryDark(dark)
            accent(blue)
            accentLight(blue)
            accentDark(blue)
            navigationBar(cyanea.backgroundColor)
          }
        activity!!.recreate()
      }
    }
  }
}
