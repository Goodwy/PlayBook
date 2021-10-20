package com.goodwy.audiobooklite.playback.di

interface PlaybackComponentFactoryProvider {

  fun factory(): PlaybackComponent.Factory
}
