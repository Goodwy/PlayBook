package com.goodwy.audiobook.misc

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable

fun Resources.getColoredDrawable(drawableId: Int, colorId: Int, alpha: Int = 255) = getColoredDrawableWithColor(drawableId, getColor(colorId), alpha)

@SuppressLint("UseCompatLoadingForDrawables")
fun Resources.getColoredDrawableWithColor(drawableId: Int, color: Int, alpha: Int = 255): Drawable {
  val drawable = getDrawable(drawableId)
  drawable!!.mutate().setColorFilter(color, PorterDuff.Mode.SRC_IN)
  drawable!!.mutate().alpha = alpha
  return drawable
}
