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
import java.util.Timer
import kotlin.concurrent.timerTask

class ColorAccentOriginalDialogController : DialogController(),
  BaseCyaneaActivity {

  val white: Int = android.graphics.Color.parseColor("#FFFFFFFF")
  val backgroundLight: Int = android.graphics.Color.parseColor("#FFfafafa")
  val grey: Int = android.graphics.Color.parseColor("#FFE0E0E0")
  val grey2: Int = android.graphics.Color.parseColor("#c6c6c7")
  val dark2: Int = android.graphics.Color.parseColor("#394248")
  val dark: Int = android.graphics.Color.parseColor("#FF272f35")
  val backgroundDark: Int = android.graphics.Color.parseColor("#1e2225")
  val black: Int = android.graphics.Color.parseColor("#FF000000")

  val Vintage2: Int = android.graphics.Color.parseColor("#FF8C2D2D")
  val Meetup: Int = android.graphics.Color.parseColor("#FFED1744")
  val Oblivion: Int = android.graphics.Color.parseColor("#FFD25252")
  val Waaark: Int = android.graphics.Color.parseColor("#FFF76C6C")
  val Vibrant: Int = android.graphics.Color.parseColor("#FFEC691E")
  val Amazon: Int = android.graphics.Color.parseColor("#FFFF9900")
  val Bing: Int = android.graphics.Color.parseColor("#FFFFB900")
  val Flax2: Int = android.graphics.Color.parseColor("#FFECBC1B")
  val Desert2: Int = android.graphics.Color.parseColor("#FF90A04A")
  val Tasty: Int = android.graphics.Color.parseColor("#FF437356")
  val Rio: Int = android.graphics.Color.parseColor("#FF0B4239")
  val Weber: Int = android.graphics.Color.parseColor("#FF729F98")
  val Periscope: Int = android.graphics.Color.parseColor("#FF40A4C4")
  val Obsidian: Int = android.graphics.Color.parseColor("#FF678CB1")
  val blue: Int = android.graphics.Color.parseColor("#FF4C86CB")
  val Vitamin: Int = android.graphics.Color.parseColor("#FF0359AE")
  val Philips: Int = android.graphics.Color.parseColor("#FF2F435E")
  val Inkpot: Int = android.graphics.Color.parseColor("#FF6068B2")
  val Twitch: Int = android.graphics.Color.parseColor("#FF7E57C2")
  val Fresh2: Int = android.graphics.Color.parseColor("#FF8A36BB")

  val colors = intArrayOf(white, backgroundLight, grey, grey2, dark2, dark, backgroundDark, black, Vintage2, Meetup, Oblivion, Waaark, Vibrant, Amazon, Bing, Flax2, Desert2,
    Tasty, Rio, Weber, Periscope, Obsidian, blue, Vitamin, Philips, Inkpot, Twitch, Fresh2)


  override fun onCreateDialog(savedViewState: Bundle?): Dialog {
    return MaterialDialog(activity!!).apply {
      cornerRadius(4f)
      title(R.string.pref_statusbar_color_title)
      colorChooser(colors, initialSelection = cyanea.primaryDark) { dialog, color ->
        when (color) {
          white -> cyanea.edit {
            primaryDark(white) /*statusbar*/
          }
          backgroundLight -> cyanea.edit {
            primaryDark(backgroundLight) /*statusbar*/
          }
          grey -> cyanea.edit {
            primaryDark(grey) /*statusbar*/
          }
          grey2 -> cyanea.edit {
            primaryDark(grey2) /*statusbar*/
          }
          dark2 -> cyanea.edit {
            primaryDark(dark2) /*statusbar*/
          }
          dark -> cyanea.edit {
            primaryDark(dark) /*statusbar*/
          }
          backgroundDark -> cyanea.edit {
            primaryDark(backgroundDark) /*statusbar*/
          }
          black -> cyanea.edit {
            primaryDark(black) /*statusbar*/
          }
          Vintage2 -> cyanea.edit {
            primaryDark(Vintage2) /*statusbar*/
          }
          Meetup -> cyanea.edit {
            primaryDark(Meetup) /*statusbar*/
          }
          Oblivion -> cyanea.edit {
            primaryDark(Oblivion) /*statusbar*/
          }
          Waaark -> cyanea.edit {
            primaryDark(Waaark) /*statusbar*/
          }
          Vibrant -> cyanea.edit {
            primaryDark(Vibrant) /*statusbar*/
          }
          Amazon -> cyanea.edit {
            primaryDark(Amazon) /*statusbar*/
          }
          Bing -> cyanea.edit {
            primaryDark(Bing) /*statusbar*/
          }
          Flax2 -> cyanea.edit {
            primaryDark(Flax2) /*statusbar*/
          }
          Desert2 -> cyanea.edit {
            primaryDark(Desert2) /*statusbar*/
          }
          Tasty -> cyanea.edit {
            primaryDark(Tasty) /*statusbar*/
          }
          Rio -> cyanea.edit {
            primaryDark(Rio) /*statusbar*/
          }
          Weber -> cyanea.edit {
            primaryDark(Weber) /*statusbar*/
          }
          Periscope -> cyanea.edit {
            primaryDark(Periscope) /*statusbar*/
          }
          Obsidian -> cyanea.edit {
            primaryDark(Obsidian) /*statusbar*/
          }
          blue -> cyanea.edit {
            primaryDark(blue) /*statusbar*/
          }
          Vitamin -> cyanea.edit {
            primaryDark(Vitamin) /*statusbar*/
          }
          Philips -> cyanea.edit {
            primaryDark(Philips) /*statusbar*/
          }
          Inkpot -> cyanea.edit {
            primaryDark(Inkpot) /*statusbar*/
          }
          Twitch -> cyanea.edit {
            primaryDark(Twitch) /*statusbar*/
          }
          Fresh2 -> cyanea.edit {
            primaryDark(Fresh2) /*statusbar*/
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
