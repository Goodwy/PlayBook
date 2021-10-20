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
import java.util.Timer
import kotlin.concurrent.timerTask

class ColorAccentOriginalDialogController : DialogController(),
  BaseCyaneaActivity {

  private val white: Int = android.graphics.Color.parseColor("#FFFFFFFF")
  private val backgroundLight: Int = android.graphics.Color.parseColor("#FFfafafa")
  private val grey: Int = android.graphics.Color.parseColor("#FFE0E0E0")
  private val grey2: Int = android.graphics.Color.parseColor("#c6c6c7")
  private val dark2: Int = android.graphics.Color.parseColor("#394248")
  private val dark: Int = android.graphics.Color.parseColor("#FF272f35")
  private val backgroundDark: Int = android.graphics.Color.parseColor("#1e2225")
  private val black: Int = android.graphics.Color.parseColor("#FF000000")

  private val Vintage2: Int = android.graphics.Color.parseColor("#FF8C2D2D")
  private val Meetup: Int = android.graphics.Color.parseColor("#FFED1744")
  private val Oblivion: Int = android.graphics.Color.parseColor("#FFD25252")
  private val Waaark: Int = android.graphics.Color.parseColor("#FFF76C6C")
  private val Vibrant: Int = android.graphics.Color.parseColor("#FFEC691E")
  private val Amazon: Int = android.graphics.Color.parseColor("#FFFF9900")
  private val Bing: Int = android.graphics.Color.parseColor("#FFFFB900")
  private val Flax2: Int = android.graphics.Color.parseColor("#FFECBC1B")
  private val Desert2: Int = android.graphics.Color.parseColor("#FF90A04A")
  private val Tasty: Int = android.graphics.Color.parseColor("#FF437356")
  private val Rio: Int = android.graphics.Color.parseColor("#FF0B4239")
  private val Weber: Int = android.graphics.Color.parseColor("#FF729F98")
  private val Periscope: Int = android.graphics.Color.parseColor("#FF40A4C4")
  private val Obsidian: Int = android.graphics.Color.parseColor("#FF678CB1")
  private val blue: Int = android.graphics.Color.parseColor("#FF4C86CB")
  private val Vitamin: Int = android.graphics.Color.parseColor("#FF0359AE")
  private val Philips: Int = android.graphics.Color.parseColor("#FF2F435E")
  private val Inkpot: Int = android.graphics.Color.parseColor("#FF6068B2")
  private val Twitch: Int = android.graphics.Color.parseColor("#FF7E57C2")
  private val Fresh2: Int = android.graphics.Color.parseColor("#FF8A36BB")

  private val colors = intArrayOf(white, backgroundLight, grey, grey2, dark2, dark, backgroundDark, black, Vintage2, Meetup, Oblivion, Waaark, Vibrant, Amazon, Bing, Flax2, Desert2,
    Tasty, Rio, Weber, Periscope, Obsidian, blue, Vitamin, Philips, Inkpot, Twitch, Fresh2)


  @SuppressLint("CheckResult")
  override fun onCreateDialog(savedViewState: Bundle?): Dialog {
    return MaterialDialog(activity!!).apply {
      cornerRadius(4f)
      title(R.string.pref_statusbar_color_title)
      colorChooser(colors, initialSelection = cyanea.primaryDark) { _, color ->
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
          }/*
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
        ColorAccentPalettesDialogController().showDialog(router)
      }
    }
  }
}
