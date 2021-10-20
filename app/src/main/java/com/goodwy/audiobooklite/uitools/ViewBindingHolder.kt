package com.goodwy.audiobooklite.uitools

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.goodwy.audiobooklite.features.InflateBinding

abstract class ViewBindingHolder<B : ViewBinding>(val binding: B) : RecyclerView.ViewHolder(binding.root) {

  constructor(parent: ViewGroup, inflateBinding: InflateBinding<B>) : this(
    inflateBinding(LayoutInflater.from(parent.context), parent, false)
  )
}
