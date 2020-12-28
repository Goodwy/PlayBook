package com.goodwy.audiobook.features.settings.dialogs

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.color.colorChooser
import com.goodwy.audiobook.R
import com.goodwy.audiobook.misc.DialogController
import com.jaredrummler.cyanea.Cyanea
import com.jaredrummler.cyanea.app.BaseCyaneaActivity
import java.util.Timer
import kotlin.concurrent.timerTask

class ColorAccentDialogController : DialogController(),
  BaseCyaneaActivity {

  val grey: Int = android.graphics.Color.parseColor("#FFE0E0E0")
  val dark: Int = android.graphics.Color.parseColor("#FF272f35")
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
  val colors = intArrayOf(md_red_500, md_pink_500, md_purple_500,
    md_deep_purple_500, md_indigo_500, md_blue_500, md_light_blue_500, md_cyan_500, md_teal_500,
    md_green_500, md_light_green_500, md_lime_500, md_yellow_500, md_amber_500, md_orange_500,
    md_deep_orange_500, md_brown_500, md_grey_500, md_blue_grey_500)


  override fun onCreateDialog(savedViewState: Bundle?): Dialog {
    return MaterialDialog(activity!!).apply {
      cornerRadius(4f)
      title(R.string.pref_statusbar_color_title)
      colorChooser(colors) { dialog, color ->
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
      }
      positiveButton(R.string.dialog_ок) {
        Handler().postDelayed({
          activity!!.recreate()
        }, 1000)
      }
      negativeButton(R.string.dialog_cancel) { dialog ->
      }
      neutralButton(R.string.pref_default_theme_title) { dialog ->
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
