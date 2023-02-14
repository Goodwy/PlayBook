package voice.playbackScreen

import android.app.Application
import android.media.AudioManager
import androidx.core.content.getSystemService
import javax.inject.Inject

class AudioVolume
@Inject constructor(
  private val context: Application,
) {
  private val audioManager = context.getSystemService<AudioManager>()

  fun volumeChangeTo(volume: Int) {
    audioManager!!.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0)
  }

  fun volumeMax(): Int {
    return audioManager!!.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
  }

  fun volumeCurrent(): Int {
    return audioManager!!.getStreamVolume(AudioManager.STREAM_MUSIC)
  }
}
