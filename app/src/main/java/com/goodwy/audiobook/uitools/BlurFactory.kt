package com.goodwy.audiobook.uitools

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


/**
 * Copyright (C) 2021  Goodwy
 * Copyright (C) 2018  Felix Nüsse
 * Created on 13.10.18 - 16:25
 *
 * Edited by: Felix Nüsse felix.nuesse(at)t-online.de
 *
 *
 * This program is released under the GPLv3 license
 *
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */
//https://www.ssaurel.com/blog/create-a-blur-effect-on-android-with-renderscript/
object BlurFactory {
  private const val BITMAP_SCALE = 0.6f
  private const val BLUR_RADIUS = 15f

  fun blurBitmap(file: File, context: Context): File {
    val filePath = file.path
    val image = BitmapFactory.decodeFile(filePath)
    val width = Math.round(image.width * BITMAP_SCALE)
    val height = Math.round(image.height * BITMAP_SCALE)
    val inputBitmap = Bitmap.createScaledBitmap(image, width, height, false)
    val outputBitmap = Bitmap.createBitmap(inputBitmap)
    val rs = RenderScript.create(context)
    val intrinsicBlur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
    val tmpIn = Allocation.createFromBitmap(rs, inputBitmap)
    val tmpOut = Allocation.createFromBitmap(rs, outputBitmap)
    intrinsicBlur.setRadius(BLUR_RADIUS)
    intrinsicBlur.setInput(tmpIn)
    intrinsicBlur.forEach(tmpOut)
    tmpOut.copyTo(outputBitmap)
    return bitmapToFile(makeBigBitmap(outputBitmap, inputBitmap), context)
  }

  fun fileToBlurBitmap(file: File, context: Context): Bitmap? {
    if (!file.canRead()) {
      return null
    }
    val filePath = file.path
    val image = BitmapFactory.decodeFile(filePath)
    val width = Math.round(image.width * BITMAP_SCALE)
    val height = Math.round(image.height * BITMAP_SCALE)
    val inputBitmap = Bitmap.createScaledBitmap(image, width, height, false)
    val outputBitmap = Bitmap.createBitmap(inputBitmap)
    val rs = RenderScript.create(context)
    val intrinsicBlur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
    val tmpIn = Allocation.createFromBitmap(rs, inputBitmap)
    val tmpOut = Allocation.createFromBitmap(rs, outputBitmap)
    intrinsicBlur.setRadius(BLUR_RADIUS)
    intrinsicBlur.setInput(tmpIn)
    intrinsicBlur.forEach(tmpOut)
    tmpOut.copyTo(outputBitmap)
    return outputBitmap
  }

  private fun makeBigBitmap(outer: Bitmap, inner: Bitmap): Bitmap {
    val wideBmp: Bitmap
    val wideBmpCanvas: Canvas
    var src: Rect
    var dest: Rect

    // assume all of the src bitmaps are the same height & width
    wideBmp = Bitmap.createBitmap(outer.width, outer.height, outer.config)
    wideBmpCanvas = Canvas(wideBmp)
    src = Rect(0, 0, outer.width, outer.height)
    dest = Rect(src)
    dest.offset(0, 0)
    wideBmpCanvas.drawBitmap(outer, src, dest, null)

    // Bitmap inner = Bitmap.createScaledBitmap(innerB, (int)(innerB.getWidth()*0.9), (int)(innerB.getHeight()*0.9), false);
    val scale = 6
    val tempBmp = Bitmap.createScaledBitmap(inner, inner.width - inner.width / scale, inner.height - inner.height / scale, false)
    src = Rect(0, 0, tempBmp.width, tempBmp.height)
    dest = Rect(src)
    //dest.offset((inner.getHeight()/scale)/2, (inner.getHeight()/scale))/2;
    dest.offset(inner.height / scale / 2, inner.height / scale / 2)
    wideBmpCanvas.drawBitmap(tempBmp, src, dest, null)
    return wideBmp
  }

  private fun bitmapToFile(b: Bitmap, context: Context): File {
    val filesDir = context.filesDir
    val f = File(filesDir, "cover.png")
    val bos = ByteArrayOutputStream()
    b.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos)
    val bitmapdata = bos.toByteArray()
    try {
      val fos = FileOutputStream(f)
      fos.write(bitmapdata)
      fos.flush()
      fos.close()
    } catch (e: IOException) {
      e.printStackTrace()
    }
    return f
  }
}
