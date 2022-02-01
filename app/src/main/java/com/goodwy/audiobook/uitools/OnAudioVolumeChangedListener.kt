package com.goodwy.audiobook.uitools

interface OnAudioVolumeChangedListener {
  fun onAudioVolumeChanged(currentVolume: Int, maxVolume: Int)
}
