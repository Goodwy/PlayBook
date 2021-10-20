package com.goodwy.audiobooklite.injection

import android.content.Context
import com.google.android.exoplayer2.SimpleExoPlayer
import dagger.Module
import dagger.Provides
import com.goodwy.audiobooklite.playback.OnlyAudioRenderersFactory

@Module
object PlaybackModule {

  @Provides
  @JvmStatic
  fun exoPlayer(context: Context, onlyAudioRenderersFactory: OnlyAudioRenderersFactory): SimpleExoPlayer {
    return SimpleExoPlayer.Builder(context, onlyAudioRenderersFactory)
      .build()
  }
}
