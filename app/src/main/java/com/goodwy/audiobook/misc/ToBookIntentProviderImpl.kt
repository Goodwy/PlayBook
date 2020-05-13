package com.goodwy.audiobook.misc

import android.content.Context
import android.content.Intent
import com.goodwy.audiobook.features.MainActivity
import com.goodwy.audiobook.playback.notification.ToBookIntentProvider
import java.util.UUID
import javax.inject.Inject

class ToBookIntentProviderImpl
@Inject constructor(private val context: Context) : ToBookIntentProvider {

  override fun goToBookIntent(id: UUID): Intent {
    return MainActivity.goToBookIntent(context, id)
  }
}
