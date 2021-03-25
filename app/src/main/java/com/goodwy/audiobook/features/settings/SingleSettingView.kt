package com.goodwy.audiobook.features.settings

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.use
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import com.goodwy.audiobook.R
import com.goodwy.audiobook.databinding.MergeSettingRowSingleBinding
import com.goodwy.audiobook.misc.dpToPxRounded
import com.goodwy.audiobook.misc.layoutInflater
import com.goodwy.audiobook.uitools.drawableFromAttr
import kotlinx.android.synthetic.main.merge_setting_row_single.view.*

class SingleSettingView : ConstraintLayout {

  private val binding = MergeSettingRowSingleBinding.inflate(layoutInflater(), this)

  constructor(context: Context) : super(context)
  constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    context.theme.obtainStyledAttributes(attrs, R.styleable.TripleSettingView, 0, 0).use {
      binding.title.text = it.getText(R.styleable.TripleSettingView_tsv_title)
      binding.value.text = it.getText(R.styleable.TripleSettingView_tsv_value)
      binding.value.isVisible = binding.value.text?.isNotBlank() == true
    }
  }

  init {
    foreground = context.drawableFromAttr(R.attr.selectableItemBackground)
    val padding = context.dpToPxRounded(8F)
    updatePadding(top = padding, bottom = padding)
  }

  fun setValue(text: String?) {
    value.isVisible = text != null
    value.text = text
  }

  fun onCheckedChanged(listener: () -> Unit) {
    setOnClickListener { listener() }
  }
}
