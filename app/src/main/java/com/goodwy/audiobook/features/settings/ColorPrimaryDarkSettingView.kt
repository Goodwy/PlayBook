package com.goodwy.audiobook.features.settings

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.use
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import com.goodwy.audiobook.R
import com.goodwy.audiobook.databinding.MergeSettingRowColorPrimaryDarkBinding
import com.goodwy.audiobook.misc.dpToPxRounded
import com.goodwy.audiobook.misc.layoutInflater
import com.goodwy.audiobook.uitools.drawableFromAttr

class ColorPrimaryDarkSettingView : ConstraintLayout {

  private val binding = MergeSettingRowColorPrimaryDarkBinding.inflate(layoutInflater(), this)

  constructor(context: Context) : super(context)
  constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    context.theme.obtainStyledAttributes(attrs, R.styleable.ColorPrimaryDarkSettingView, 0, 0).use {
      binding.colorPrimaryDarkTitle.text = it.getText(R.styleable.ColorPrimaryDarkSettingView_cpdv_title)
      binding.colorPrimaryDarkDescription.text = it.getText(R.styleable.ColorPrimaryDarkSettingView_cpdv_description)
      binding.colorPrimaryDarkDescription.isVisible = binding.colorPrimaryDarkDescription.text?.isNotBlank() == true
    }
  }

  init {
    foreground = context.drawableFromAttr(R.attr.selectableItemBackground)
    val padding = context.dpToPxRounded(8F)
    updatePadding(top = padding, bottom = padding)
  }

  fun onCheckedChanged(listener: () -> Unit) {
    setOnClickListener { listener() }
  }
/*
  fun setChecked(checked: Boolean) {
    binding.colorPrimaryDarkSetting.isChecked = checked
  }*/
}
