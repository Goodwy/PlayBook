package com.goodwy.audiobook.features

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.goodwy.audiobook.features.externalStorageMissing.NoExternalStorageActivity
import com.goodwy.audiobook.misc.storageMounted
import com.goodwy.audiobook.playback.session.PlaybackService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Base class for all Activities which checks in onResume, if the storage
 * is mounted. Shuts down service if not.
 */
abstract class BaseActivity : AppCompatActivity() {

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
