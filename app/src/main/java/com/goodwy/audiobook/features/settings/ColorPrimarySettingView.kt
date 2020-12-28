package com.goodwy.audiobook.features.settings

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.use
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import com.goodwy.audiobook.R
import com.goodwy.audiobook.databinding.MergeSettingRowColorPrimaryBinding
import com.goodwy.audiobook.misc.dpToPxRounded
import com.goodwy.audiobook.misc.layoutInflater
import com.goodwy.audiobook.uitools.drawableFromAttr

class ColorPrimarySettingView : ConstraintLayout {

  private val binding = MergeSettingRowColorPrimaryBinding.inflate(layoutInflater(), this)

  constructor(context: Context) : super(context)
  constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    context.theme.obtainStyledAttributes(attrs, R.styleable.ColorPrimarySettingView, 0, 0).use {
      binding.colorPrimaryTitle.text = it.getText(R.styleable.ColorPrimarySettingView_cpv_title)
      binding.colorPrimaryDescription.text = it.getText(R.styleable.ColorPrimarySettingView_cpv_description)
      binding.colorPrimaryDescription.isVisible = binding.colorPrimaryDescription.text?.isNotBlank() == true
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
    binding.colorPrimarySetting.isChecked = checked
  }*/
}
