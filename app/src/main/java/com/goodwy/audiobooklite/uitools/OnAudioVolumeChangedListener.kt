package com.goodwy.audiobooklite.uitools

interface OnAudioVolumeChangedListener {
  fun onAudioVolumeChanged(currentVolume: Int, maxVolume: Int)
}
