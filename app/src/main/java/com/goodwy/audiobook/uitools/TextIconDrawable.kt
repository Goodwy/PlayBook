package com.goodwy.audiobook.uitools

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.text.TextPaint
import androidx.core.graphics.ColorUtils
import kotlin.properties.Delegates

class TextIconDrawable: Drawable() {
  private var isAlpha = 255
  private var textPaint = TextPaint().apply {
    textAlign = Paint.Align.CENTER
  }
  var text by Delegates.observable("") { _, _, _ -> invalidateSelf() }
  var textColor by Delegates.observable(Color.BLACK) { _, _, _ -> invalidateSelf() }
  var size by Delegates.observable(48f) { _, _, _ -> invalidateSelf() }

  private fun fitText(width: Int) {
    textPaint.textSize = size
    val widthAt48 = textPaint.measureText(text)
    textPaint.textSize = size / widthAt48 * width.toFloat()
  }

  override fun draw(canvas: Canvas) {
    val xPos = bounds.width() / 2f
    val yPos = bounds.height() / 2f + size / (7/2)//(bounds.height() / 2 - (textPaint.descent() + textPaint.ascent()) / 2)
    fitText(bounds.width())
    textPaint.color = ColorUtils.setAlphaComponent(textColor, isAlpha)
    canvas.drawText(text, xPos, yPos, textPaint)
  }

  override fun setAlpha(alpha: Int) {
    this.alpha = isAlpha
  }

  override fun setColorFilter(colorFilter: ColorFilter?) {
    textPaint.colorFilter = colorFilter
  }

  override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

}

/* Usage
val drawable = TextIconDrawable().apply {
    text = "Hello, world!"
    textColor = Color.BLACK
}
requireView().findViewById<ImageView>(R.id.imageView).setImageDrawable(drawable)
 */
