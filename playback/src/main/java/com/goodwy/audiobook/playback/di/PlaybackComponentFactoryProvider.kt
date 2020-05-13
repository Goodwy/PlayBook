package com.goodwy.audiobook.playback.di

interface PlaybackComponentFactoryProvider {

  fun factory(): PlaybackComponent.Factory
}
