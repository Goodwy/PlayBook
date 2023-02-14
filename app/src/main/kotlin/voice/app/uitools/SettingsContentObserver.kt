package voice.app.uitools

import android.content.Context
import android.database.ContentObserver
import android.media.AudioManager
import android.os.Handler
import de.paulwoitaschek.flowpref.Pref
import voice.app.injection.appComponent
import voice.common.pref.PrefKeys
import voice.logging.core.Logger
import javax.inject.Inject
import javax.inject.Named

class SettingsContentObserver (var context: Context, handler: Handler?) : ContentObserver(handler) {
  var previousVolume: Int

  @field:[Inject Named(PrefKeys.CURRENT_VOLUME)]
  lateinit var currentVolumePref: Pref<Int>

  init {
    appComponent.inject(this)
    val audio = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    previousVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC)
  }

  override fun deliverSelfNotifications(): Boolean {
    return super.deliverSelfNotifications()
  }

  override fun onChange(selfChange: Boolean) {
    super.onChange(selfChange)
    val audio = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    val currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC)
    if (previousVolume != currentVolume) {
      Logger.d("Volume has changed");
      previousVolume = currentVolume
      currentVolumePref.value = currentVolume
    }
  }
}
