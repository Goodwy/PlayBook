package com.goodwy.audiobook.features.settings.dialogs

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.color.colorChooser
import com.goodwy.audiobook.R
import com.goodwy.audiobook.features.contribute.ContributeController
import com.goodwy.audiobook.misc.DialogController
import com.goodwy.audiobook.misc.conductor.asTransaction
import com.jaredrummler.cyanea.Cyanea
import com.jaredrummler.cyanea.app.BaseCyaneaActivity

class ColorPrimaryMaterialDialogController : DialogController(),
  BaseCyaneaActivity {

  val backgroundLight: Int = android.graphics.Color.parseColor("#FFFAFAFA")
  val backgroundDark: Int = android.graphics.Color.parseColor("#FF1E2225")
  val grey: Int = android.graphics.Color.parseColor("#FFE0E0E0")
  val dark: Int = android.graphics.Color.parseColor("#FF272f35")
  val blue: Int = android.graphics.Color.parseColor("#FF4C86CB")

  val md_red_500 : Int = android.graphics.Color.parseColor("#f44336")
  val md_pink_500 : Int = android.graphics.Color.parseColor("#e91e63")
  val md_purple_500 : Int = android.graphics.Color.parseColor("#9c27b0")
  val md_deep_purple_500 : Int = android.graphics.Color.parseColor("#673ab7")
  val md_indigo_500 : Int = android.graphics.Color.parseColor("#3f51b5")
  val md_blue_500 : Int = android.graphics.Color.parseColor("#2196f3")
  val md_light_blue_500 : Int = android.graphics.Color.parseColor("#03a9f4")
  val md_cyan_500 : Int = android.graphics.Color.parseColor("#00bcd4")
  val md_teal_500 : Int = android.graphics.Color.parseColor("#009688")
  val md_green_500 : Int = android.graphics.Color.parseColor("#4caf50")
  val md_light_green_500 : Int = android.graphics.Color.parseColor("#8bc34a")
  val md_lime_500 : Int = android.graphics.Color.parseColor("#cddc39")
  val md_yellow_500 : Int = android.graphics.Color.parseColor("#ffeb3b")
  val md_amber_500 : Int = android.graphics.Color.parseColor("#ffc107")
  val md_orange_500 : Int = android.graphics.Color.parseColor("#ff9800")
  val md_deep_orange_500 : Int = android.graphics.Color.parseColor("#ff5722")
  val md_brown_500 : Int = android.graphics.Color.parseColor("#795548")
  val md_grey_500 : Int = android.graphics.Color.parseColor("#9e9e9e")
  val md_blue_grey_500 : Int = android.graphics.Color.parseColor("#607d8b")

  val md_red_700 : Int = android.graphics.Color.parseColor("#D32F2F")
  val md_pink_700 : Int = android.graphics.Color.parseColor("#C2185B")
  val md_purple_700 : Int = android.graphics.Color.parseColor("#7B1FA2")
  val md_deep_purple_700 : Int = android.graphics.Color.parseColor("#512DA8")
  val md_indigo_700 : Int = android.graphics.Color.parseColor("#303F9F")
  val md_blue_700 : Int = android.graphics.Color.parseColor("#1976D2")
  val md_light_blue_700 : Int = android.graphics.Color.parseColor("#0288D1")
  val md_cyan_700 : Int = android.graphics.Color.parseColor("#0097A7")
  val md_teal_700 : Int = android.graphics.Color.parseColor("#00796B")
  val md_green_700 : Int = android.graphics.Color.parseColor("#388E3C")
  val md_light_green_700 : Int = android.graphics.Color.parseColor("#689F38")
  val md_lime_700 : Int = android.graphics.Color.parseColor("#AFB42B")
  val md_yellow_700 : Int = android.graphics.Color.parseColor("#FBC02D")
  val md_amber_700 : Int = android.graphics.Color.parseColor("#FFA000")
  val md_orange_700 : Int = android.graphics.Color.parseColor("#F57C00")
  val md_deep_orange_700 : Int = android.graphics.Color.parseColor("#E64A19")
  val md_brown_700 : Int = android.graphics.Color.parseColor("#5D4037")
  val md_grey_700 : Int = android.graphics.Color.parseColor("#616161")
  val md_blue_grey_700 : Int = android.graphics.Color.parseColor("#455A64")

  val colors = intArrayOf(md_red_500, md_pink_500, md_purple_500,
    md_deep_purple_500, md_indigo_500, md_blue_500, md_light_blue_500, md_cyan_500, md_teal_500,
    md_green_500, md_light_green_500, md_lime_500, md_yellow_500, md_amber_500, md_orange_500,
    md_deep_orange_500, md_brown_500, md_grey_500, md_blue_grey_500)


  override fun onCreateDialog(savedViewState: Bundle?): Dialog {
    return MaterialDialog(activity!!).apply {
      cornerRadius(4f)
      title(R.string.pref_primary_color_title)
      colorChooser(colors, initialSelection = cyanea.primary) { dialog, color ->
        when (color) {
          md_red_500 -> cyanea.edit {
            primary(md_red_500)
            primaryLight(md_red_500)
            primaryDark(md_red_700) /*statusbar*/
            navigationBar(cyanea.backgroundColor)
            accent(md_red_500)
            accentLight(md_red_500)
            accentDark(md_red_500)
          }
          md_pink_500 -> cyanea.edit {
            primary(md_pink_500)
            primaryLight(md_pink_500)
            primaryDark(md_pink_700) /*statusbar*/
            navigationBar(cyanea.backgroundColor)
            accent(md_pink_500)
            accentLight(md_pink_500)
            accentDark(md_pink_500)
          }
          md_purple_500 -> cyanea.edit {
            primary(md_purple_500)
            primaryLight(md_purple_500)
            primaryDark(md_purple_700) /*statusbar*/
            navigationBar(cyanea.backgroundColor)
            accent(md_purple_500)
            accentLight(md_purple_500)
            accentDark(md_purple_500)
          }
          md_deep_purple_500 -> cyanea.edit {
            primary(md_deep_purple_500)
            primaryLight(md_deep_purple_500)
            primaryDark(md_deep_purple_700) /*statusbar*/
            navigationBar(cyanea.backgroundColor)
            accent(md_deep_purple_500)
            accentLight(md_deep_purple_500)
            accentDark(md_deep_purple_500)
          }
          md_indigo_500 -> cyanea.edit {
            primary(md_indigo_500)
            primaryLight(md_indigo_500)
            primaryDark(md_indigo_700) /*statusbar*/
            navigationBar(cyanea.backgroundColor)
            accent(md_indigo_500)
            accentLight(md_indigo_500)
            accentDark(md_indigo_500)
          }
          md_blue_500 -> cyanea.edit {
            primary(md_blue_500)
            primaryLight(md_blue_500)
            primaryDark(md_blue_700) /*statusbar*/
            navigationBar(cyanea.backgroundColor)
            accent(md_blue_500)
            accentLight(md_blue_500)
            accentDark(md_blue_500)
          }
          md_light_blue_500 -> cyanea.edit {
            primary(md_light_blue_500)
            primaryLight(md_light_blue_500)
            primaryDark(md_light_blue_700) /*statusbar*/
            navigationBar(cyanea.backgroundColor)
            accent(md_light_blue_500)
            accentLight(md_light_blue_500)
            accentDark(md_light_blue_500)
          }
          md_cyan_500 -> cyanea.edit {
            primary(md_cyan_500)
            primaryLight(md_cyan_500)
            primaryDark(md_cyan_700) /*statusbar*/
            navigationBar(cyanea.backgroundColor)
            accent(md_cyan_500)
            accentLight(md_cyan_500)
            accentDark(md_cyan_500)
          }
          md_teal_500 -> cyanea.edit {
            primary(md_teal_500)
            primaryLight(md_teal_500)
            primaryDark(md_teal_700) /*statusbar*/
            navigationBar(cyanea.backgroundColor)
            accent(md_teal_500)
            accentLight(md_teal_500)
            accentDark(md_teal_500)
          }
          md_green_500 -> cyanea.edit {
            primary(md_green_500)
            primaryLight(md_green_500)
            primaryDark(md_green_700) /*statusbar*/
            navigationBar(cyanea.backgroundColor)
            accent(md_green_500)
            accentLight(md_green_500)
            accentDark(md_green_500)
          }
          md_light_green_500 -> cyanea.edit {
            primary(md_light_green_500)
            primaryLight(md_light_green_500)
            primaryDark(md_light_green_700) /*statusbar*/
            navigationBar(cyanea.backgroundColor)
            accent(md_light_green_500)
            accentLight(md_light_green_500)
            accentDark(md_light_green_500)
          }
          md_lime_500 -> cyanea.edit {
            primary(md_lime_500)
            primaryLight(md_lime_500)
            primaryDark(md_lime_700) /*statusbar*/
            navigationBar(cyanea.backgroundColor)
            accent(md_lime_500)
            accentLight(md_lime_500)
            accentDark(md_lime_500)
          }
          md_yellow_500 -> cyanea.edit {
            primary(md_yellow_500)
            primaryLight(md_yellow_500)
            primaryDark(md_yellow_700) /*statusbar*/
            navigationBar(cyanea.backgroundColor)
            accent(md_yellow_500)
            accentLight(md_yellow_500)
            accentDark(md_yellow_500)
          }
          md_amber_500 -> cyanea.edit {
            primary(md_amber_500)
            primaryLight(md_amber_500)
            primaryDark(md_amber_700) /*statusbar*/
            navigationBar(cyanea.backgroundColor)
            accent(md_amber_500)
            accentLight(md_amber_500)
            accentDark(md_amber_500)
          }
          md_orange_500 -> cyanea.edit {
            primary(md_orange_500)
            primaryLight(md_orange_500)
            primaryDark(md_orange_700) /*statusbar*/
            navigationBar(cyanea.backgroundColor)
            accent(md_orange_500)
            accentLight(md_orange_500)
            accentDark(md_orange_500)
          }
          md_deep_orange_500 -> cyanea.edit {
            primary(md_deep_orange_500)
            primaryLight(md_deep_orange_500)
            primaryDark(md_deep_orange_700) /*statusbar*/
            navigationBar(cyanea.backgroundColor)
            accent(md_deep_orange_500)
            accentLight(md_deep_orange_500)
            accentDark(md_deep_orange_500)
          }
          md_brown_500 -> cyanea.edit {
            primary(md_brown_500)
            primaryLight(md_brown_500)
            primaryDark(md_brown_700) /*statusbar*/
            navigationBar(cyanea.backgroundColor)
            accent(md_brown_500)
            accentLight(md_brown_500)
            accentDark(md_brown_500)
          }
          md_grey_500 -> cyanea.edit {
            primary(md_grey_500)
            primaryLight(md_grey_500)
            primaryDark(md_grey_700) /*statusbar*/
            navigationBar(cyanea.backgroundColor)
            accent(md_grey_500)
            accentLight(md_grey_500)
            accentDark(md_grey_500)
          }
          md_blue_grey_500 -> cyanea.edit {
            primary(md_blue_grey_500)
            primaryLight(md_blue_grey_500)
            primaryDark(md_blue_grey_700) /*statusbar*/
            navigationBar(cyanea.backgroundColor)
            accent(md_blue_grey_500)
            accentLight(md_blue_grey_500)
            accentDark(md_blue_grey_500)
          }
          //todo Lite
          /*else -> {
            val text = (R.string.only_the_first_5_colors_available)
            val duration = Toast.LENGTH_LONG
            Toast.makeText(applicationContext, text, duration).show()
            val transaction = ContributeController().asTransaction()
            router.pushController(transaction)
          }*/
        }
      }
      positiveButton(R.string.dialog_ok) {
        //Handler().postDelayed({
          activity!!.recreate()
        //}, 1000)
      }
      negativeButton(R.string.dialog_cancel) { dialog ->
      }
      neutralButton(R.string.palettes) { dialog ->
        ColorPrinaryPalettesDialogController().showDialog(router)
      }
    }
  }
}
