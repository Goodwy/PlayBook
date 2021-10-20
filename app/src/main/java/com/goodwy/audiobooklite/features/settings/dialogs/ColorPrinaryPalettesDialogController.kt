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

class ColorPrinaryPalettesDialogController : DialogController(),
  BaseCyaneaActivity {

  private val grey: Int = android.graphics.Color.parseColor("#FFE0E0E0")
  private val dark: Int = android.graphics.Color.parseColor("#FF272f35")
  private val blue: Int = android.graphics.Color.parseColor("#FF4C86CB")

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
