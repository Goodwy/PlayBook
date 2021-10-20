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

class ColorPrimaryOriginalDialogController : DialogController(),
  BaseCyaneaActivity {

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

  //statusbar
  private val Vintage2Dark: Int = android.graphics.Color.parseColor("#FF772626")
  private val MeetupDark: Int = android.graphics.Color.parseColor("#FFC91339")
  private val OblivionDark: Int = android.graphics.Color.parseColor("#FFB24545")
  private val WaaarkDark: Int = android.graphics.Color.parseColor("#FFD15B5B")
  private val VibrantDark: Int = android.graphics.Color.parseColor("#FFC85919")
  private val AmazonDark: Int = android.graphics.Color.parseColor("#FFD88200")
  private val BingDark: Int = android.graphics.Color.parseColor("#FFD89D00")
  private val Flax2Dark: Int = android.graphics.Color.parseColor("#FFC89F16")
  private val Desert2Dark: Int = android.graphics.Color.parseColor("#FF7A883E")
  private val TastyDark: Int = android.graphics.Color.parseColor("#FF386149")
  private val RioDark: Int = android.graphics.Color.parseColor("#FF093830")
  private val WeberDark: Int = android.graphics.Color.parseColor("#FF608781")
  private val PeriscopeDark: Int = android.graphics.Color.parseColor("#FF368BA6")
  private val ObsidianDark: Int = android.graphics.Color.parseColor("#FF577796")
  private val blueDark: Int = android.graphics.Color.parseColor("#FF4D6CA2")
  private val VitaminDark: Int = android.graphics.Color.parseColor("#FF024B93")
  private val PhilipsDark: Int = android.graphics.Color.parseColor("#FF27384F")
  private val InkpotDark: Int = android.graphics.Color.parseColor("#FF515897")
  private val TwitchDark: Int = android.graphics.Color.parseColor("#FF6B49A4")
  private val Fresh2Dark: Int = android.graphics.Color.parseColor("#FF752D9E")

  private val colors = intArrayOf(Vintage2, Meetup, Oblivion, Waaark, Vibrant, Amazon, Bing, Flax2, Desert2,
    Tasty, Rio, Weber, Periscope, Obsidian, blue, Vitamin, Philips, Inkpot, Twitch, Fresh2)


  @SuppressLint("CheckResult")
  override fun onCreateDialog(savedViewState: Bundle?): Dialog {
    return MaterialDialog(activity!!).apply {
      cornerRadius(4f)
      title(R.string.pref_primary_color_title)
      colorChooser(colors, initialSelection = cyanea.primary) { _, color ->
        when (color) {
          Vintage2 -> cyanea.edit {
            primary(Vintage2)
            primaryLight(Vintage2)
            primaryDark(Vintage2Dark) /*statusbar*/
            navigationBar(cyanea.backgroundColor)
            accent(Vintage2)
            accentLight(Vintage2)
            accentDark(Vintage2)
          }
          Meetup -> cyanea.edit {
            primary(Meetup)
            primaryLight(Meetup)
            primaryDark(MeetupDark) /*statusbar*/
            navigationBar(cyanea.backgroundColor)
            accent(Meetup)
            accentLight(Meetup)
            accentDark(Meetup)
          }
          Oblivion -> cyanea.edit {
            primary(Oblivion)
            primaryLight(Oblivion)
            primaryDark(OblivionDark) /*statusbar*/
            navigationBar(cyanea.backgroundColor)
            accent(Oblivion)
            accentLight(Oblivion)
            accentDark(Oblivion)
          }
          Waaark -> cyanea.edit {
            primary(Waaark)
            primaryLight(Waaark)
            primaryDark(WaaarkDark) /*statusbar*/
            navigationBar(cyanea.backgroundColor)
            accent(Waaark)
            accentLight(Waaark)
            accentDark(Waaark)
          }
          Vibrant -> cyanea.edit {
            primary(Vibrant)
            primaryLight(Vibrant)
            primaryDark(VibrantDark) /*statusbar*/
            navigationBar(cyanea.backgroundColor)
            accent(Vibrant)
            accentLight(Vibrant)
            accentDark(Vibrant)
          }/*
          Amazon -> cyanea.edit {
            primary(Amazon)
            primaryLight(Amazon)
            primaryDark(AmazonDark) /*statusbar*/
            navigationBar(cyanea.backgroundColor)
            accent(Amazon)
            accentLight(Amazon)
            accentDark(Amazon)
          }
          Bing -> cyanea.edit {
            primary(Bing)
            primaryLight(Bing)
            primaryDark(BingDark) /*statusbar*/
            navigationBar(cyanea.backgroundColor)
            accent(Bing)
            accentLight(Bing)
            accentDark(Bing)
          }
          Flax2 -> cyanea.edit {
            primary(Flax2)
            primaryLight(Flax2)
            primaryDark(Flax2Dark) /*statusbar*/
            navigationBar(cyanea.backgroundColor)
            accent(Flax2)
            accentLight(Flax2)
            accentDark(Flax2)
          }
          Desert2 -> cyanea.edit {
            primary(Desert2)
            primaryLight(Desert2)
            primaryDark(Desert2Dark) /*statusbar*/
            navigationBar(cyanea.backgroundColor)
            accent(Desert2)
            accentLight(Desert2)
            accentDark(Desert2)
          }
          Tasty -> cyanea.edit {
            primary(Tasty)
            primaryLight(Tasty)
            primaryDark(TastyDark) /*statusbar*/
            navigationBar(cyanea.backgroundColor)
            accent(Tasty)
            accentLight(Tasty)
            accentDark(Tasty)
          }
          Rio -> cyanea.edit {
            primary(Rio)
            primaryLight(Rio)
            primaryDark(RioDark) /*statusbar*/
            navigationBar(cyanea.backgroundColor)
            accent(Rio)
            accentLight(Rio)
            accentDark(Rio)
          }
          Weber -> cyanea.edit {
            primary(Weber)
            primaryLight(Weber)
            primaryDark(WeberDark) /*statusbar*/
            navigationBar(cyanea.backgroundColor)
            accent(Weber)
            accentLight(Weber)
            accentDark(Weber)
          }
          Periscope -> cyanea.edit {
            primary(Periscope)
            primaryLight(Periscope)
            primaryDark(PeriscopeDark) /*statusbar*/
            navigationBar(cyanea.backgroundColor)
            accent(Periscope)
            accentLight(Periscope)
            accentDark(Periscope)
          }
          Obsidian -> cyanea.edit {
            primary(Obsidian)
            primaryLight(Obsidian)
            primaryDark(ObsidianDark) /*statusbar*/
            navigationBar(cyanea.backgroundColor)
            accent(Obsidian)
            accentLight(Obsidian)
            accentDark(Obsidian)
          }
          blue -> cyanea.edit {
            primary(blue)
            primaryLight(blue)
            primaryDark(blueDark) /*statusbar*/
            navigationBar(cyanea.backgroundColor)
            accent(blue)
            accentLight(blue)
            accentDark(blue)
          }
          Vitamin -> cyanea.edit {
            primary(Vitamin)
            primaryLight(Vitamin)
            primaryDark(VitaminDark) /*statusbar*/
            navigationBar(cyanea.backgroundColor)
            accent(Vitamin)
            accentLight(Vitamin)
            accentDark(Vitamin)
          }
          Philips -> cyanea.edit {
            primary(Philips)
            primaryLight(Philips)
            primaryDark(PhilipsDark) /*statusbar*/
            navigationBar(cyanea.backgroundColor)
            accent(Philips)
            accentLight(Philips)
            accentDark(Philips)
          }
          Inkpot -> cyanea.edit {
            primary(Inkpot)
            primaryLight(Inkpot)
            primaryDark(InkpotDark) /*statusbar*/
            navigationBar(cyanea.backgroundColor)
            accent(Inkpot)
            accentLight(Inkpot)
            accentDark(Inkpot)
          }
          Twitch -> cyanea.edit {
            primary(Twitch)
            primaryLight(Twitch)
            primaryDark(TwitchDark) /*statusbar*/
            navigationBar(cyanea.backgroundColor)
            accent(Twitch)
            accentLight(Twitch)
            accentDark(Twitch)
          }
          Fresh2 -> cyanea.edit {
            primary(Fresh2)
            primaryLight(Fresh2)
            primaryDark(Fresh2Dark) /*statusbar*/
            navigationBar(cyanea.backgroundColor)
            accent(Fresh2)
            accentLight(Fresh2)
            accentDark(Fresh2)
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
