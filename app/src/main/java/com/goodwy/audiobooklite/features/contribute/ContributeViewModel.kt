package com.goodwy.audiobooklite.features.contribute

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri
import de.paulwoitaschek.flowpref.Pref
import com.goodwy.audiobooklite.common.pref.PrefKeys
import com.goodwy.audiobooklite.misc.DARK_THEME_SETTABLE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import javax.inject.Named

class ContributeViewModel
@Inject constructor(
  private val context: Context,
  @Named(PrefKeys.DARK_THEME)
  private val useDarkTheme: Pref<Boolean>,
  @Named(PrefKeys.SHOW_RATING)
  private val showRatingPref: Pref<Boolean>
) {

  fun viewState(): Flow<ContributeViewState> {
    return combine(
      useDarkTheme.flow,
      showRatingPref.flow
    ) { useDarkTheme, showRatingPref ->
      ContributeViewState(
        useDarkTheme = useDarkTheme,
        showDarkThemePref = DARK_THEME_SETTABLE,
        showRatingPref = showRatingPref
      )
    }
  }

  fun toggleDarkTheme() {
    useDarkTheme.value = !useDarkTheme.value
  }

  // Rate - >
  fun rateIntent() {
    toggleShowRating()
    showRating()
  }

  fun toggleShowRating() {
    showRatingPref.value = !showRatingPref.value
  }

  fun showRating() {
    /*val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.goodwy.audiobooklite"))
      .addFlags(
        Intent.FLAG_ACTIVITY_NO_HISTORY
          or Intent.FLAG_ACTIVITY_NEW_DOCUMENT
          or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)

    try {
      startActivityExternal(intent)
    } catch (e: ActivityNotFoundException) {
      val url = "http://play.google.com/store/apps/details?id=com.goodwy.audiobooklite"
      startActivityExternal(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }*/
    val PLAYBOOKS_GP_URL = "https://play.google.com/store/apps/details?id=com.goodwy.audiobooklite".toUri()
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = PLAYBOOKS_GP_URL
    startActivity(intent)
  }

  private fun startActivity(intent: Intent) {
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
  }

  private fun startActivityExternal(intent: Intent) {
    if (intent.resolveActivity(context.packageManager) != null) {
      startActivity(intent)
    } else {
      startActivity(Intent.createChooser(intent, null))
    }
  }
  // <- Rate
}
