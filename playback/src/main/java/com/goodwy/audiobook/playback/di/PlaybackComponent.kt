package com.goodwy.audiobook.playback.di

import dagger.BindsInstance
import dagger.Subcomponent
import com.goodwy.audiobook.playback.session.PlaybackService

@Subcomponent(modules = [PlaybackServiceModule::class])
@PerService
interface PlaybackComponent {

  fun inject(target: PlaybackService)

  @Subcomponent.Factory
  interface Factory {

    fun create(@BindsInstance playbackService: PlaybackService): PlaybackComponent
  }
}
