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

class ColorAccentIosDialogController : DialogController(),
  BaseCyaneaActivity {

  val grey: Int = android.graphics.Color.parseColor("#FFE0E0E0")
  val dark: Int = android.graphics.Color.parseColor("#FF272f35")

  val ios_red_b : Int = android.graphics.Color.parseColor("#ff453a")
  val ios_red_w : Int = android.graphics.Color.parseColor("#ff3b30")
  val ios_orange_b : Int = android.graphics.Color.parseColor("#ff9f0a")
  val ios_orange_w : Int = android.graphics.Color.parseColor("#ff9500")
  val ios_yellow_b : Int = android.graphics.Color.parseColor("#ffd60a")
  val ios_yellow_w : Int = android.graphics.Color.parseColor("#ffcc00")
  val ios_green_b : Int = android.graphics.Color.parseColor("#32d74b")
  val ios_green_w : Int = android.graphics.Color.parseColor("#34c759")
  val ios_teal_b : Int = android.graphics.Color.parseColor("#64d2ff")
  val ios_teal_w : Int = android.graphics.Color.parseColor("#5ac8fa")
  val ios_blue_b : Int = android.graphics.Color.parseColor("#0a84ff")
  val ios_blue_w : Int = android.graphics.Color.parseColor("#007aff")
  val ios_indigo_b : Int = android.graphics.Color.parseColor("#5e5ce6")
  val ios_indigo_w : Int = android.graphics.Color.parseColor("#5856d6")
  val ios_purple_b : Int = android.graphics.Color.parseColor("#bf5af2")
  val ios_purple_w : Int = android.graphics.Color.parseColor("#af52de")
  val ios_pink_b : Int = android.graphics.Color.parseColor("#ff375f")
  val ios_pink_w : Int = android.graphics.Color.parseColor("#ff2d55")

  val colors = intArrayOf(ios_red_b, ios_red_w, ios_orange_b,
    ios_orange_w, ios_yellow_b, ios_yellow_w, ios_green_b, ios_green_w, ios_teal_b,
    ios_teal_w, ios_blue_b, ios_blue_w, ios_indigo_b, ios_indigo_w, ios_purple_b,
    ios_purple_w, ios_pink_b, ios_pink_w)


  override fun onCreateDialog(savedViewState: Bundle?): Dialog {
    return MaterialDialog(activity!!).apply {
      cornerRadius(4f)
      title(R.string.pref_statusbar_color_title)
      colorChooser(colors, initialSelection = cyanea.primaryDark) { dialog, color ->
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
        ColorAccentPalettesDialogController().showDialog(router)
      }
    }
  }
}
