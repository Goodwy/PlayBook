package com.goodwy.audiobooklite.prefs

import kotlinx.coroutines.flow.Flow
import kotlin.properties.ReadWriteProperty

abstract class Pref<T> : ReadWriteProperty<Any, T> {

  @Suppress("LeakingThis")
  var value: T by this

  abstract val flow: Flow<T>
}
