package com.goodwy.audiobooklite.features.settings.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.color.colorChooser
import com.goodwy.audiobooklite.R
import com.goodwy.audiobooklite.features.contribute.ContributeController
import com.goodwy.audiobooklite.misc.DialogController
import com.goodwy.audiobooklite.misc.conductor.asTransaction
import com.jaredrummler.cyanea.Cyanea
import com.jaredrummler.cyanea.app.BaseCyaneaActivity

class ColorPrimaryMaterialDialogController : DialogController(),
  BaseCyaneaActivity {

  private val backgroundLight: Int = android.graphics.Color.parseColor("#FFFAFAFA")
  private val backgroundDark: Int = android.graphics.Color.parseColor("#FF1E2225")
  private val grey: Int = android.graphics.Color.parseColor("#FFE0E0E0")
  private val dark: Int = android.graphics.Color.parseColor("#FF272f35")
  private val blue: Int = android.graphics.Color.parseColor("#FF4C86CB")

  private val md_red_500 : Int = android.graphics.Color.parseColor("#f44336")
  private val md_pink_500 : Int = android.graphics.Color.parseColor("#e91e63")
  private val md_purple_500 : Int = android.graphics.Color.parseColor("#9c27b0")
  private val md_deep_purple_500 : Int = android.graphics.Color.parseColor("#673ab7")
  private val md_indigo_500 : Int = android.graphics.Color.parseColor("#3f51b5")
  private val md_blue_500 : Int = android.graphics.Color.parseColor("#2196f3")
  private val md_light_blue_500 : Int = android.graphics.Color.parseColor("#03a9f4")
  private val md_cyan_500 : Int = android.graphics.Color.parseColor("#00bcd4")
  private val md_teal_500 : Int = android.graphics.Color.parseColor("#009688")
  private val md_green_500 : Int = android.graphics.Color.parseColor("#4caf50")
  private val md_light_green_500 : Int = android.graphics.Color.parseColor("#8bc34a")
  private val md_lime_500 : Int = android.graphics.Color.parseColor("#cddc39")
  private val md_yellow_500 : Int = android.graphics.Color.parseColor("#ffeb3b")
  private val md_amber_500 : Int = android.graphics.Color.parseColor("#ffc107")
  private val md_orange_500 : Int = android.graphics.Color.parseColor("#ff9800")
  private val md_deep_orange_500 : Int = android.graphics.Color.parseColor("#ff5722")
  private val md_brown_500 : Int = android.graphics.Color.parseColor("#795548")
  private val md_grey_500 : Int = android.graphics.Color.parseColor("#9e9e9e")
  private val md_blue_grey_500 : Int = android.graphics.Color.parseColor("#607d8b")

  private val md_red_700 : Int = android.graphics.Color.parseColor("#D32F2F")
  private val md_pink_700 : Int = android.graphics.Color.parseColor("#C2185B")
  private val md_purple_700 : Int = android.graphics.Color.parseColor("#7B1FA2")
  private val md_deep_purple_700 : Int = android.graphics.Color.parseColor("#512DA8")
  private val md_indigo_700 : Int = android.graphics.Color.parseColor("#303F9F")
  private val md_blue_700 : Int = android.graphics.Color.parseColor("#1976D2")
  private val md_light_blue_700 : Int = android.graphics.Color.parseColor("#0288D1")
  private val md_cyan_700 : Int = android.graphics.Color.parseColor("#0097A7")
  private val md_teal_700 : Int = android.graphics.Color.parseColor("#00796B")
  private val md_green_700 : Int = android.graphics.Color.parseColor("#388E3C")
  private val md_light_green_700 : Int = android.graphics.Color.parseColor("#689F38")
  private val md_lime_700 : Int = android.graphics.Color.parseColor("#AFB42B")
  private val md_yellow_700 : Int = android.graphics.Color.parseColor("#FBC02D")
  private val md_amber_700 : Int = android.graphics.Color.parseColor("#FFA000")
  private val md_orange_700 : Int = android.graphics.Color.parseColor("#F57C00")
  private val md_deep_orange_700 : Int = android.graphics.Color.parseColor("#E64A19")
  private val md_brown_700 : Int = android.graphics.Color.parseColor("#5D4037")
  private val md_grey_700 : Int = android.graphics.Color.parseColor("#616161")
  private val md_blue_grey_700 : Int = android.graphics.Color.parseColor("#455A64")

  private val colors = intArrayOf(md_red_500, md_pink_500, md_purple_500,
    md_deep_purple_500, md_indigo_500, md_blue_500, md_light_blue_500, md_cyan_500, md_teal_500,
    md_green_500, md_light_green_500, md_lime_500, md_yellow_500, md_amber_500, md_orange_500,
    md_deep_orange_500, md_brown_500, md_grey_500, md_blue_grey_500)


  @SuppressLint("CheckResult")
  override fun onCreateDialog(savedViewState: Bundle?): Dialog {
    return MaterialDialog(activity!!).apply {
      cornerRadius(4f)
      title(R.string.pref_primary_color_title)
      colorChooser(colors, initialSelection = cyanea.primary) { _, color ->
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
          }/*
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
          }*/
          //todo Lite
          else -> {
            val text = (R.string.only_the_first_5_colors_available)
            val duration = Toast.LENGTH_LONG
            Toast.makeText(applicationContext, text, duration).show()
            val transaction = ContributeController().asTransaction()
            router.pushController(transaction)
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
