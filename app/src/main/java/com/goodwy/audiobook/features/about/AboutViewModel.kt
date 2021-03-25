package com.goodwy.audiobook.features.about

import de.paulwoitaschek.flowpref.Pref
import com.goodwy.audiobook.common.pref.PrefKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import javax.inject.Named

class AboutViewModel
@Inject constructor(
  @Named(PrefKeys.DEV_MODE)
  private val devModePref: Pref<Boolean>
) {

  fun viewState(): Flow<AboutViewState> {
    return combine(
      devModePref.flow
    ) { useDarkTheme ->
      AboutViewState(
        devModePref = devModePref
      )
    }
  }

  fun toggleDevMode() {
    devModePref.value = !devModePref.value
  }
}
