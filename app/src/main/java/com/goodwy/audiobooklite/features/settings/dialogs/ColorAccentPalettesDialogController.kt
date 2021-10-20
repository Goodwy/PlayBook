package com.goodwy.audiobooklite.features.settings.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import com.goodwy.audiobooklite.R
import com.goodwy.audiobooklite.misc.DialogController
import com.jaredrummler.cyanea.Cyanea
import com.jaredrummler.cyanea.app.BaseCyaneaActivity

class ColorAccentPalettesDialogController : DialogController(),
  BaseCyaneaActivity {

  private val grey: Int = android.graphics.Color.parseColor("#FFE0E0E0")
  private val dark: Int = android.graphics.Color.parseColor("#FF272f35")

  @SuppressLint("CheckResult")
  override fun onCreateDialog(savedViewState: Bundle?): Dialog {
    return MaterialDialog(activity!!).apply {
      icon(R.drawable.ic_palettes)
      cornerRadius(4f)
      title(R.string.palettes)
      listItems(R.array.pref_palettes_values) { _, index, _ ->
        when (index) {
          0 -> ColorAccentOriginalDialogController().showDialog(router)
          1 -> ColorAccentMaterialDialogController().showDialog(router)
          2 -> ColorAccentIosDialogController().showDialog(router)
          else -> error("Invalid index $index")
        }
        dismiss()
      }
      negativeButton(R.string.dialog_cancel)
      neutralButton(R.string.pref_default_theme_title) {
        if (cyanea.baseTheme == Cyanea.BaseTheme.LIGHT) {
          cyanea.edit {
            primaryDark(grey)
          }
        } else
          cyanea.edit {
            primaryDark(dark)
          }
        activity!!.recreate()
      }
    }
  }
}
