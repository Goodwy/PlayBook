package com.goodwy.audiobook.features.prefAppearanceUIPlayer

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import de.paulwoitaschek.flowpref.Pref
import com.goodwy.audiobook.R
import com.goodwy.audiobook.common.pref.PrefKeys
import com.goodwy.audiobook.databinding.DialogRadiusChooserBinding
import com.goodwy.audiobook.injection.appComponent
import com.goodwy.audiobook.misc.DialogController
import com.goodwy.audiobook.misc.conductor.context
import com.goodwy.audiobook.misc.onProgressChanged
import javax.inject.Inject
import javax.inject.Named

class CoverSettingsDialogController : DialogController() {

  @field:[Inject Named(PrefKeys.COVER_RADIUS)]
  lateinit var coverRadiusPref: Pref<Int>

  @field:[Inject Named(PrefKeys.COVER_ELEVATION)]
  lateinit var coverElevationPref: Pref<Int>

  @SuppressLint("InflateParams")
  override fun onCreateDialog(savedViewState: Bundle?): Dialog {
    appComponent.inject(this)

    val binding = DialogRadiusChooserBinding.inflate(activity!!.layoutInflater)

    val oldRadius = coverRadiusPref.value
    binding.seekBarRadius.max = (MAX_RADIUS - MIN_RADIUS) * FACTOR_RADIUS
    binding.seekBarRadius.progress = (oldRadius - MIN_RADIUS) * FACTOR_RADIUS
    binding.seekBarRadius.onProgressChanged(initialNotification = true) {
      val progress = it / FACTOR_RADIUS
      val radiusSummary = activity!!.resources.getString(R.string.pref_cover_corner_radius_title) + " " + progress.toString()
      binding.textViewRadius.text = radiusSummary
      binding.coverCard.radius = progress.toFloat() * 2
    }

    val oldElevation = coverElevationPref.value
    binding.seekBarElevation.max = (MAX_ELEVATION - MIN_ELEVATION) * FACTOR_ELEVATION
    binding.seekBarElevation.progress = (oldElevation - MIN_ELEVATION) * FACTOR_ELEVATION
    binding.seekBarElevation.onProgressChanged(initialNotification = true) {
      val progress = it / FACTOR_ELEVATION
      val elevationSummary = activity!!.resources.getString(R.string.pref_cover_shadow_title) + " " + progress.toString()
      binding.textViewElevation.text = elevationSummary
      binding.coverCard.cardElevation = progress.toFloat()
    }
    //shadow color
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
      binding.coverCard.outlineAmbientShadowColor = ContextCompat.getColor(context, R.color.shadow_elevation)
      binding.coverCard.outlineSpotShadowColor = ContextCompat.getColor(context, R.color.shadow_elevation)
    }

    return MaterialDialog(activity!!).apply {
      title(R.string.pref_cover_settings_title)
	  cornerRadius(4f)
      customView(view = binding.root, scrollable = true)
      positiveButton(R.string.dialog_confirm) {
        val newRadius = binding.seekBarRadius.progress / FACTOR_RADIUS + MIN_RADIUS
        coverRadiusPref.value = newRadius
        val newElevation = binding.seekBarElevation.progress / FACTOR_ELEVATION + MIN_ELEVATION
        coverElevationPref.value = newElevation
      }
      negativeButton(R.string.dialog_cancel)
      neutralButton(R.string.pref_default_theme_title) {
        coverRadiusPref.value = 8
        coverElevationPref.value = 80
      }
    }
  }

  companion object {
    private const val MIN_RADIUS = 0
    private const val MAX_RADIUS = 100
    private const val FACTOR_RADIUS = 10
    private const val MIN_ELEVATION = 0
    private const val MAX_ELEVATION = 100
    private const val FACTOR_ELEVATION = 10
  }
}
