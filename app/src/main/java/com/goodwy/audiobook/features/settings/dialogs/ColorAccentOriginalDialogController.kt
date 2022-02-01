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

class ColorAccentOriginalDialogController : DialogController(),
  BaseCyaneaActivity {

  @field:[Inject Named(PrefKeys.PRO)]
  lateinit var isProPref: Pref<Boolean>

  private val colors = intArrayOf(white, backgroundLight, grey, grey2, dark2, dark, backgroundDark,
    black, Vintage2, Meetup, Oblivion, Waaark, Vibrant, Amazon, Bing, Flax2, Desert2,
    Tasty, Rio, Weber, Periscope, Obsidian, blue, Vitamin, Philips, Inkpot, Twitch, Fresh2)

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
          }
        } else { //lite
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
