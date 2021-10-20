package com.goodwy.audiobooklite.features.contribute

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.use
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import com.goodwy.audiobooklite.R
import com.goodwy.audiobooklite.databinding.MergeContributeRowLifebuoyBinding
import com.goodwy.audiobooklite.misc.dpToPxRounded
import com.goodwy.audiobooklite.misc.layoutInflater
import com.goodwy.audiobooklite.uitools.drawableFromAttr

class LifebuoyContributeView : ConstraintLayout {

  private val binding = MergeContributeRowLifebuoyBinding.inflate(layoutInflater(), this)

  constructor(context: Context) : super(context)
  constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    context.theme.obtainStyledAttributes(attrs, R.styleable.DarkContributeView, 0, 0).use {
      binding.contributeLifebuoyTitle.text = it.getText(R.styleable.DarkContributeView_dcv_title)
      binding.contributeLifebuoyDescription.text = it.getText(R.styleable.DarkContributeView_dcv_description)
      binding.contributeLifebuoyDescription.isVisible = binding.contributeLifebuoyDescription.text?.isNotBlank() == true
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
}
