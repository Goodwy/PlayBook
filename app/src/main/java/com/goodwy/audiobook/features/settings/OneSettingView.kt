package com.goodwy.audiobook.features.settings

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.res.use
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import com.goodwy.audiobook.R
import com.goodwy.audiobook.misc.dpToPxRounded
import com.goodwy.audiobook.uitools.drawableFromAttr
import kotlinx.android.synthetic.main.merge_setting_row_double.view.*

class OneSettingView : LinearLayout {

  constructor(context: Context) : super(context)
  constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    context.theme.obtainStyledAttributes(attrs, R.styleable.OneSettingView, 0, 0).use {
      title.text = it.getText(R.styleable.OneSettingView_osv_title)
     // description.text = it.getText(R.styleable.OneSettingView_osv_description)
    }
  }

  init {
    foreground = context.drawableFromAttr(R.attr.selectableItemBackground)
    gravity = Gravity.CENTER_VERTICAL
    orientation = VERTICAL
    val padding = context.dpToPxRounded(8F)
    updatePadding(top = padding, bottom = padding)

    View.inflate(context, R.layout.merge_setting_row_one, this)
  }

  fun setDescription(text: String?) {
    description.isVisible = text != null
    description.text = text
  }
}
