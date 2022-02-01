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

class ColorPrimaryMaterialDialogController : DialogController(),
  BaseCyaneaActivity {

  @field:[Inject Named(PrefKeys.PRO)]
  lateinit var isProPref: Pref<Boolean>

  private val colors = intArrayOf(md_red_500, md_pink_500, md_purple_500,
    md_deep_purple_500, md_indigo_500, md_blue_500, md_light_blue_500, md_cyan_500, md_teal_500,
    md_green_500, md_light_green_500, md_lime_500, md_yellow_500, md_amber_500, md_orange_500,
    md_deep_orange_500, md_brown_500, md_grey_500, md_blue_grey_500)

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
        }
        } else { //lite
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
