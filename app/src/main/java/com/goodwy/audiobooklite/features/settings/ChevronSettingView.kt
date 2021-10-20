package com.goodwy.audiobooklite.features.settings

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.use
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import com.goodwy.audiobooklite.R
import com.goodwy.audiobooklite.databinding.MergeSettingRowChevronBinding
import com.goodwy.audiobooklite.misc.dpToPxRounded
import com.goodwy.audiobooklite.misc.layoutInflater
import com.goodwy.audiobooklite.uitools.drawableFromAttr
import kotlinx.android.synthetic.main.merge_setting_row_double.view.*

class ChevronSettingView : ConstraintLayout {

  private val binding = MergeSettingRowChevronBinding.inflate(layoutInflater(), this)

  constructor(context: Context) : super(context)
  constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    context.theme.obtainStyledAttributes(attrs, R.styleable.DoubleSettingView, 0, 0).use {
      binding.title.text = it.getText(R.styleable.DoubleSettingView_dsv_title)
      binding.description.text = it.getText(R.styleable.DoubleSettingView_dsv_description)
      binding.description.isVisible = binding.description.text?.isNotBlank() == true
    }
  }

  init {
    foreground = context.drawableFromAttr(R.attr.selectableItemBackground)
    val padding = context.dpToPxRounded(8F)
    updatePadding(top = padding, bottom = padding)
  }

  fun setDescription(text: String?) {
    description.isVisible = text != null
    description.text = text
  }

  fun onCheckedChanged(listener: () -> Unit) {
    setOnClickListener { listener() }
  }
}
