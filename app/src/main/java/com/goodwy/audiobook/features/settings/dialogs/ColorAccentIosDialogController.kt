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

class ColorAccentIosDialogController : DialogController(),
  BaseCyaneaActivity {

  @field:[Inject Named(PrefKeys.PRO)]
  lateinit var isProPref: Pref<Boolean>

  private val colors = intArrayOf(ios_red_b, ios_red_w, ios_orange_b,
    ios_orange_w, ios_yellow_b, ios_yellow_w, ios_green_b, ios_green_w, ios_teal_b,
    ios_teal_w, ios_blue_b, ios_blue_w, ios_indigo_b, ios_indigo_w, ios_purple_b,
    ios_purple_w, ios_pink_b, ios_pink_w)

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
            ios_red_b -> cyanea.edit {
              primaryDark(ios_red_b) /*statusbar*/
            }
            ios_red_w -> cyanea.edit {
              primaryDark(ios_red_w) /*statusbar*/
            }
            ios_orange_b -> cyanea.edit {
              primaryDark(ios_orange_b) /*statusbar*/
            }
            ios_orange_w -> cyanea.edit {
              primaryDark(ios_orange_w) /*statusbar*/
            }
            ios_yellow_b -> cyanea.edit {
              primaryDark(ios_yellow_b) /*statusbar*/
            }
            ios_yellow_w -> cyanea.edit {
              primaryDark(ios_yellow_w) /*statusbar*/
            }
            ios_green_b -> cyanea.edit {
              primaryDark(ios_green_b) /*statusbar*/
            }
            ios_green_w -> cyanea.edit {
              primaryDark(ios_green_w) /*statusbar*/
            }
            ios_teal_b -> cyanea.edit {
              primaryDark(ios_teal_b) /*statusbar*/
            }
            ios_teal_w -> cyanea.edit {
              primaryDark(ios_teal_w) /*statusbar*/
            }
            ios_blue_b -> cyanea.edit {
              primaryDark(ios_blue_b) /*statusbar*/
            }
            ios_blue_w -> cyanea.edit {
              primaryDark(ios_blue_w) /*statusbar*/
            }
            ios_indigo_b -> cyanea.edit {
              primaryDark(ios_indigo_b) /*statusbar*/
            }
            ios_indigo_w -> cyanea.edit {
              primaryDark(ios_indigo_w) /*statusbar*/
            }
            ios_purple_b -> cyanea.edit {
              primaryDark(ios_purple_b) /*statusbar*/
            }
            ios_purple_w -> cyanea.edit {
              primaryDark(ios_purple_w) /*statusbar*/
            }
            ios_pink_b -> cyanea.edit {
              primaryDark(ios_pink_b) /*statusbar*/
            }
            ios_pink_w -> cyanea.edit {
              primaryDark(ios_pink_w) /*statusbar*/
            }
          }
        } else { //lite
          when (color) {
            ios_red_b -> cyanea.edit {
              primaryDark(ios_red_b) /*statusbar*/
            }
            ios_red_w -> cyanea.edit {
              primaryDark(ios_red_w) /*statusbar*/
            }
            ios_orange_b -> cyanea.edit {
              primaryDark(ios_orange_b) /*statusbar*/
            }
            ios_orange_w -> cyanea.edit {
              primaryDark(ios_orange_w) /*statusbar*/
            }
            ios_yellow_b -> cyanea.edit {
              primaryDark(ios_yellow_b) /*statusbar*/
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
