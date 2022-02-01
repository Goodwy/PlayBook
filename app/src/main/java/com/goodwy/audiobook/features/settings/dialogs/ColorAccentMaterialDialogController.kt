package com.goodwy.audiobook.features.settings.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.color.colorChooser
import com.goodwy.audiobook.R
import com.goodwy.audiobook.common.pref.PrefKeys
import com.goodwy.audiobook.features.contribute.ContributeController
import com.goodwy.audiobook.injection.appComponent
import com.goodwy.audiobook.misc.DialogController
import com.goodwy.audiobook.misc.conductor.asTransaction
import com.goodwy.audiobook.uitools.*
import com.jaredrummler.cyanea.app.BaseCyaneaActivity
import de.paulwoitaschek.flowpref.Pref
import javax.inject.Inject
import javax.inject.Named

class ColorAccentMaterialDialogController : DialogController(),
  BaseCyaneaActivity {

  @field:[Inject Named(PrefKeys.PRO)]
  lateinit var isProPref: Pref<Boolean>

  private val colors = intArrayOf(md_red_500, md_pink_500, md_purple_500,
    md_deep_purple_500, md_indigo_500, md_blue_500, md_light_blue_500, md_cyan_500, md_teal_500,
    md_green_500, md_light_green_500, md_lime_500, md_yellow_500, md_amber_500, md_orange_500,
    md_deep_orange_500, md_brown_500, md_grey_500, md_blue_grey_500)

  init {
    appComponent.inject(this)
  }


  @SuppressLint("CheckResult")
  override fun onCreateDialog(savedViewState: Bundle?): Dialog {
    return MaterialDialog(activity!!).apply {
      cornerRadius(4f)
      title(R.string.pref_statusbar_color_title)
      colorChooser(colors, initialSelection = cyanea.primaryDark) { _, color ->
        if (isProPref.value) {
          when (color) {
            md_red_500 -> cyanea.edit {
              primaryDark(md_red_500) /*statusbar*/
            }
            md_pink_500 -> cyanea.edit {
              primaryDark(md_pink_500) /*statusbar*/
            }
            md_purple_500 -> cyanea.edit {
              primaryDark(md_purple_500) /*statusbar*/
            }
            md_deep_purple_500 -> cyanea.edit {
              primaryDark(md_deep_purple_500) /*statusbar*/
            }
            md_indigo_500 -> cyanea.edit {
              primaryDark(md_indigo_500) /*statusbar*/
            }
            md_blue_500 -> cyanea.edit {
              primaryDark(md_blue_500) /*statusbar*/
            }
            md_light_blue_500 -> cyanea.edit {
              primaryDark(md_light_blue_500) /*statusbar*/
            }
            md_cyan_500 -> cyanea.edit {
              primaryDark(md_cyan_500) /*statusbar*/
            }
            md_teal_500 -> cyanea.edit {
              primaryDark(md_teal_500) /*statusbar*/
            }
            md_green_500 -> cyanea.edit {
              primaryDark(md_green_500) /*statusbar*/
            }
            md_light_green_500 -> cyanea.edit {
              primaryDark(md_light_green_500) /*statusbar*/
            }
            md_lime_500 -> cyanea.edit {
              primaryDark(md_lime_500) /*statusbar*/
            }
            md_yellow_500 -> cyanea.edit {
              primaryDark(md_yellow_500) /*statusbar*/
            }
            md_amber_500 -> cyanea.edit {
              primaryDark(md_amber_500) /*statusbar*/
            }
            md_orange_500 -> cyanea.edit {
              primaryDark(md_orange_500) /*statusbar*/
            }
            md_deep_orange_500 -> cyanea.edit {
              primaryDark(md_deep_orange_500) /*statusbar*/
            }
            md_brown_500 -> cyanea.edit {
              primaryDark(md_brown_500) /*statusbar*/
            }
            md_grey_500 -> cyanea.edit {
              primaryDark(md_grey_500) /*statusbar*/
            }
            md_blue_grey_500 -> cyanea.edit {
              primaryDark(md_blue_grey_500) /*statusbar*/
            }
          }
        } else { //lite
          when (color) {
            md_red_500 -> cyanea.edit {
              primaryDark(md_red_500) /*statusbar*/
            }
            md_pink_500 -> cyanea.edit {
              primaryDark(md_pink_500) /*statusbar*/
            }
            md_purple_500 -> cyanea.edit {
              primaryDark(md_purple_500) /*statusbar*/
            }
            md_deep_purple_500 -> cyanea.edit {
              primaryDark(md_deep_purple_500) /*statusbar*/
            }
            md_indigo_500 -> cyanea.edit {
              primaryDark(md_indigo_500) /*statusbar*/
            }
            else -> {
              val text = (R.string.only_the_first_5_colors_available)
              val duration = Toast.LENGTH_LONG
              Toast.makeText(applicationContext, text, duration).show()
              val transaction = ContributeController().asTransaction()
              router.pushController(transaction)
            }
          }
        }
      }
      positiveButton(R.string.dialog_ok) {
        //Handler().postDelayed({
        activity!!.recreate()
        //}, 1000)
      }
      negativeButton(R.string.dialog_cancel)
      neutralButton(R.string.palettes) {
        ColorAccentPalettesDialogController().showDialog(router)
      }
    }
  }
}
