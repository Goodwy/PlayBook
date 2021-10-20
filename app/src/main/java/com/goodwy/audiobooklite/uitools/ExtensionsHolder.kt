package com.goodwy.audiobooklite.uitools

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.goodwy.audiobooklite.misc.layoutInflater
import kotlinx.android.extensions.LayoutContainer

open class ExtensionsHolder(itemView: View) : RecyclerView.ViewHolder(itemView), LayoutContainer {

  constructor(parent: ViewGroup, layoutRes: Int) : this(
    parent.layoutInflater().inflate(
      layoutRes,
      parent,
      false
    )
  )

  final override val containerView: View? get() = itemView
}
