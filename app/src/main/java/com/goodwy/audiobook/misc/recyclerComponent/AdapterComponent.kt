package com.goodwy.audiobook.misc.recyclerComponent

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlin.reflect.KClass

/**
 * A component for the [CompositeListAdapter].
 */
abstract class AdapterComponent<in Model : Any, VH : RecyclerView.ViewHolder>(
  private val modelClazz: KClass<in Model>
) {

  open val viewType: Int = modelClazz.hashCode()

  open fun onBindViewHolder(model: Model, holder: VH) {}

  abstract fun onCreateViewHolder(parent: ViewGroup): VH

  open fun isForViewType(model: Any): Boolean = model::class == modelClazz
}
