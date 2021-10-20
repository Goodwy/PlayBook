package com.goodwy.audiobooklite.features.settings

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.use
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import com.goodwy.audiobooklite.R
import com.goodwy.audiobooklite.databinding.MergeSettingRowSwitchNotEnabledBinding
import com.goodwy.audiobooklite.misc.dpToPxRounded
import com.goodwy.audiobooklite.misc.layoutInflater
import com.goodwy.audiobooklite.uitools.drawableFromAttr

class SwitchNotEnabledSettingView : ConstraintLayout {

  private val binding = MergeSettingRowSwitchNotEnabledBinding.inflate(layoutInflater(), this)

  constructor(context: Context) : super(context)
  constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    context.theme.obtainStyledAttributes(attrs, R.styleable.SwitchSettingView, 0, 0).use {
      binding.switchTitle.text = it.getText(R.styleable.SwitchSettingView_ssv_title)
      binding.switchDescription.text = it.getText(R.styleable.SwitchSettingView_ssv_description)
      binding.switchDescription.isVisible = binding.switchDescription.text?.isNotBlank() == true
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

  fun setChecked(checked: Boolean) {
    binding.switchSetting.isChecked = checked
  }
}
