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

class ColorPrimaryOriginalDialogController : DialogController(),
  BaseCyaneaActivity {

  @field:[Inject Named(PrefKeys.PRO)]
  lateinit var isProPref: Pref<Boolean>

  private val colors = intArrayOf(Vintage2, Meetup, Oblivion, Waaark, Vibrant, Amazon, Bing, Flax2, Desert2,
    Tasty, Rio, Weber, Periscope, Obsidian, blue, Vitamin, Philips, Inkpot, Twitch, Fresh2)

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
          }
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
          }
        }
        } else { //lite
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
