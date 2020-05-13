package com.goodwy.audiobook.features

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.bluelinelabs.conductor.Controller
import com.goodwy.audiobook.misc.conductor.ControllerLifecycleOwner
import com.goodwy.audiobook.misc.conductor.LifecycleScopeProperty

abstract class BaseController(args: Bundle = Bundle()) : Controller(args), LifecycleOwner {

  @Suppress("LeakingThis")
  private val lifecycleOwner = ControllerLifecycleOwner(this)

  final override fun getLifecycle(): Lifecycle = lifecycleOwner.lifecycle

  val lifecycleScope by LifecycleScopeProperty(lifecycle)
}
