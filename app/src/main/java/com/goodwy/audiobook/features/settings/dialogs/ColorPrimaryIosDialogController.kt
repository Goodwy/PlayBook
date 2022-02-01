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

class ColorPrimaryIosDialogController : DialogController(),
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
      title(R.string.pref_primary_color_title)
      colorChooser(colors, initialSelection = cyanea.primary) { _, color ->
        if (isProPref.value) {
          when (color) {
            ios_red_b -> cyanea.edit {
              primary(ios_red_b)
              primaryLight(ios_red_b)
              primaryDark(ios_red_w) /*statusbar*/
              navigationBar(cyanea.backgroundColor)
              accent(ios_red_b)
              accentLight(ios_red_b)
              accentDark(ios_red_b)
            }
            ios_red_w -> cyanea.edit {
              primary(ios_red_w)
              primaryLight(ios_red_w)
              primaryDark(ios_red_b) /*statusbar*/
              navigationBar(cyanea.backgroundColor)
              accent(ios_red_w)
              accentLight(ios_red_w)
              accentDark(ios_red_w)
            }
            ios_orange_b -> cyanea.edit {
              primary(ios_orange_b)
              primaryLight(ios_orange_b)
              primaryDark(ios_orange_w) /*statusbar*/
              navigationBar(cyanea.backgroundColor)
              accent(ios_orange_b)
              accentLight(ios_orange_b)
              accentDark(ios_orange_b)
            }
            ios_orange_w -> cyanea.edit {
              primary(ios_orange_w)
              primaryLight(ios_orange_w)
              primaryDark(ios_orange_b) /*statusbar*/
              navigationBar(cyanea.backgroundColor)
              accent(ios_orange_w)
              accentLight(ios_orange_w)
              accentDark(ios_orange_w)
            }
            ios_yellow_b -> cyanea.edit {
              primary(ios_yellow_b)
              primaryLight(ios_yellow_b)
              primaryDark(ios_yellow_w) /*statusbar*/
              navigationBar(cyanea.backgroundColor)
              accent(ios_yellow_b)
              accentLight(ios_yellow_b)
              accentDark(ios_yellow_b)
            }
            ios_yellow_w -> cyanea.edit {
              primary(ios_yellow_w)
              primaryLight(ios_yellow_w)
              primaryDark(ios_yellow_b) /*statusbar*/
              navigationBar(cyanea.backgroundColor)
              accent(ios_yellow_w)
              accentLight(ios_yellow_w)
              accentDark(ios_yellow_w)
            }
            ios_green_b -> cyanea.edit {
              primary(ios_green_b)
              primaryLight(ios_green_b)
              primaryDark(ios_green_w) /*statusbar*/
              navigationBar(cyanea.backgroundColor)
              accent(ios_green_b)
              accentLight(ios_green_b)
              accentDark(ios_green_b)
            }
            ios_green_w -> cyanea.edit {
              primary(ios_green_w)
              primaryLight(ios_green_w)
              primaryDark(ios_green_b) /*statusbar*/
              navigationBar(cyanea.backgroundColor)
              accent(ios_green_w)
              accentLight(ios_green_w)
              accentDark(ios_green_w)
            }
            ios_teal_b -> cyanea.edit {
              primary(ios_teal_b)
              primaryLight(ios_teal_b)
              primaryDark(ios_teal_w) /*statusbar*/
              navigationBar(cyanea.backgroundColor)
              accent(ios_teal_b)
              accentLight(ios_teal_b)
              accentDark(ios_teal_b)
            }
            ios_teal_w -> cyanea.edit {
              primary(ios_teal_w)
              primaryLight(ios_teal_w)
              primaryDark(ios_teal_b) /*statusbar*/
              navigationBar(cyanea.backgroundColor)
              accent(ios_teal_w)
              accentLight(ios_teal_w)
              accentDark(ios_teal_w)
            }
            ios_blue_b -> cyanea.edit {
              primary(ios_blue_b)
              primaryLight(ios_blue_b)
              primaryDark(ios_blue_w) /*statusbar*/
              navigationBar(cyanea.backgroundColor)
              accent(ios_blue_b)
              accentLight(ios_blue_b)
              accentDark(ios_blue_b)
            }
            ios_blue_w -> cyanea.edit {
              primary(ios_blue_w)
              primaryLight(ios_blue_w)
              primaryDark(ios_blue_b) /*statusbar*/
              navigationBar(cyanea.backgroundColor)
              accent(ios_blue_w)
              accentLight(ios_blue_w)
              accentDark(ios_blue_w)
            }
            ios_indigo_b -> cyanea.edit {
              primary(ios_indigo_b)
              primaryLight(ios_indigo_b)
              primaryDark(ios_indigo_w) /*statusbar*/
              navigationBar(cyanea.backgroundColor)
              accent(ios_indigo_b)
              accentLight(ios_indigo_b)
              accentDark(ios_indigo_b)
            }
            ios_indigo_w -> cyanea.edit {
              primary(ios_indigo_w)
              primaryLight(ios_indigo_w)
              primaryDark(ios_indigo_b) /*statusbar*/
              navigationBar(cyanea.backgroundColor)
              accent(ios_indigo_w)
              accentLight(ios_indigo_w)
              accentDark(ios_indigo_w)
            }
            ios_purple_b -> cyanea.edit {
              primary(ios_purple_b)
              primaryLight(ios_purple_b)
              primaryDark(ios_purple_w) /*statusbar*/
              navigationBar(cyanea.backgroundColor)
              accent(ios_purple_b)
              accentLight(ios_purple_b)
              accentDark(ios_purple_b)
            }
            ios_purple_w -> cyanea.edit {
              primary(ios_purple_w)
              primaryLight(ios_purple_w)
              primaryDark(ios_purple_b) /*statusbar*/
              navigationBar(cyanea.backgroundColor)
              accent(ios_purple_w)
              accentLight(ios_purple_w)
              accentDark(ios_purple_w)
            }
            ios_pink_b -> cyanea.edit {
              primary(ios_pink_b)
              primaryLight(ios_pink_b)
              primaryDark(ios_pink_w) /*statusbar*/
              navigationBar(cyanea.backgroundColor)
              accent(ios_pink_b)
              accentLight(ios_pink_b)
              accentDark(ios_pink_b)
            }
            ios_pink_w -> cyanea.edit {
              primary(ios_pink_w)
              primaryLight(ios_pink_w)
              primaryDark(ios_pink_b) /*statusbar*/
              navigationBar(cyanea.backgroundColor)
              accent(ios_pink_w)
              accentLight(ios_pink_w)
              accentDark(ios_pink_w)
            }
          }
        } else { //lite
          when (color) {
            ios_red_b -> cyanea.edit {
              primary(ios_red_b)
              primaryLight(ios_red_b)
              primaryDark(ios_red_w) /*statusbar*/
              navigationBar(cyanea.backgroundColor)
              accent(ios_red_b)
              accentLight(ios_red_b)
              accentDark(ios_red_b)
            }
            ios_red_w -> cyanea.edit {
              primary(ios_red_w)
              primaryLight(ios_red_w)
              primaryDark(ios_red_b) /*statusbar*/
              navigationBar(cyanea.backgroundColor)
              accent(ios_red_w)
              accentLight(ios_red_w)
              accentDark(ios_red_w)
            }
            ios_orange_b -> cyanea.edit {
              primary(ios_orange_b)
              primaryLight(ios_orange_b)
              primaryDark(ios_orange_w) /*statusbar*/
              navigationBar(cyanea.backgroundColor)
              accent(ios_orange_b)
              accentLight(ios_orange_b)
              accentDark(ios_orange_b)
            }
            ios_orange_w -> cyanea.edit {
              primary(ios_orange_w)
              primaryLight(ios_orange_w)
              primaryDark(ios_orange_b) /*statusbar*/
              navigationBar(cyanea.backgroundColor)
              accent(ios_orange_w)
              accentLight(ios_orange_w)
              accentDark(ios_orange_w)
            }
            ios_yellow_b -> cyanea.edit {
              primary(ios_yellow_b)
              primaryLight(ios_yellow_b)
              primaryDark(ios_yellow_w) /*statusbar*/
              navigationBar(cyanea.backgroundColor)
              accent(ios_yellow_b)
              accentLight(ios_yellow_b)
              accentDark(ios_yellow_b)
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
        ColorPrinaryPalettesDialogController().showDialog(router)
      }
    }
  }
}
