package com.goodwy.audiobooklite.misc

import android.content.Context
import android.content.Intent
import com.goodwy.audiobooklite.features.MainActivity
import com.goodwy.audiobooklite.playback.notification.ToBookIntentProvider
import java.util.UUID
import javax.inject.Inject

class ToBookIntentProviderImpl
@Inject constructor(private val context: Context) : ToBookIntentProvider {

  override fun goToBookIntent(id: UUID): Intent {
    return MainActivity.goToBookIntent(context, id)
  }
}
