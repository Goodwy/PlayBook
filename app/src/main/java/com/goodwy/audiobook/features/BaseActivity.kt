package com.goodwy.audiobook.features

import android.content.Intent
import android.content.res.Configuration
import android.view.ContextThemeWrapper
import androidx.appcompat.app.AppCompatActivity
import com.goodwy.audiobook.features.externalStorageMissing.NoExternalStorageActivity
import com.goodwy.audiobook.misc.storageMounted
import com.goodwy.audiobook.playback.session.PlaybackService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Locale

/**
 * Base class for all Activities which checks in onResume, if the storage
 * is mounted. Shuts down service if not.
 */
abstract class BaseActivity : AppCompatActivity() {

  //language ->
  companion object {
    public var dLocale: Locale? = null
  }

  init {
    updateConfig(this)
  }

  fun updateConfig(wrapper: ContextThemeWrapper) {
    if(dLocale==Locale("") ) // Do nothing if dLocale is null
      return

    Locale.setDefault(dLocale)
    val configuration = Configuration()
    configuration.setLocale(dLocale)
    wrapper.applyOverrideConfiguration(configuration)
  }
  //<- language

  override fun onResume() {
    super.onResume()

    GlobalScope.launch(Dispatchers.Main) {
      if (!storageMounted()) {
        val serviceIntent = Intent(this@BaseActivity, PlaybackService::class.java)
        stopService(serviceIntent)

        startActivity(
          Intent(this@BaseActivity, NoExternalStorageActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
          }
        )
        return@launch
      }
    }
  }
}
