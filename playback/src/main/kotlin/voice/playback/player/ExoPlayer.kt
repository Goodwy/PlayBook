package voice.playback.player

import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

internal fun Player.onAudioSessionIdChanged(action: (audioSessionId: Int?) -> Unit) {
  fun emitSessionId(id: Int) {
    action(id.takeUnless { it == C.AUDIO_SESSION_ID_UNSET })
  }
  if (this is ExoPlayer) {
    emitSessionId(audioSessionId)
  }
  addListener(
    object : Player.Listener {
      override fun onAudioSessionIdChanged(audioSessionId: Int) {
        emitSessionId(audioSessionId)
      }
    },
  )
}

internal fun Player.onPlaybackSuppressionReasonChanged(action: (reason: Int) -> Unit) {
  fun audioFocusLoss(reason: Int) {
    action(reason)
  }
  addListener(
    object : Player.Listener {
      override fun onPlaybackSuppressionReasonChanged(reason: Int) {
        super.onPlaybackSuppressionReasonChanged(reason)
        if (reason == Player.PLAYBACK_SUPPRESSION_REASON_TRANSIENT_AUDIO_FOCUS_LOSS) {
          audioFocusLoss(reason)
        }
      }
    },
  )
}
