package com.goodwy.audiobooklite.injection

import android.app.Application
import android.content.Context
import dagger.BindsInstance
import dagger.Component
import com.goodwy.audiobooklite.data.repo.internals.PersistenceModule
import com.goodwy.audiobooklite.features.MainActivity
import com.goodwy.audiobooklite.features.audio.LoudnessDialog
import com.goodwy.audiobooklite.features.bookCategory.BookCategoryController
import com.goodwy.audiobooklite.features.bookOverview.BookOverviewController
import com.goodwy.audiobooklite.features.bookOverview.EditBookAuthorDialogController
import com.goodwy.audiobooklite.features.bookOverview.EditBookBottomSheetController
import com.goodwy.audiobooklite.features.bookOverview.EditBookTitleDialogController
import com.goodwy.audiobooklite.features.bookOverview.EditCoverDialogController
import com.goodwy.audiobooklite.features.bookOverview.list.LoadBookCover
import com.goodwy.audiobooklite.features.bookPlaying.BookPlayController
import com.goodwy.audiobooklite.features.bookPlaying.JumpToPositionDialogController
import com.goodwy.audiobooklite.features.bookPlaying.SeekDialogController
import com.goodwy.audiobooklite.features.bookPlaying.SleepTimerDialogController
import com.goodwy.audiobooklite.features.bookPlaying.selectchapter.SelectChapterDialog
import com.goodwy.audiobooklite.features.bookmarks.BookmarkPresenter
import com.goodwy.audiobooklite.features.folderChooser.FolderChooserPresenter
import com.goodwy.audiobooklite.features.folderOverview.FolderOverviewPresenter
import com.goodwy.audiobooklite.features.imagepicker.CoverFromInternetController
import com.goodwy.audiobooklite.features.contribute.ContributeController
import com.goodwy.audiobooklite.features.about.AboutController
import com.goodwy.audiobooklite.features.bookOverview.list.BookOverviewHolder
import com.goodwy.audiobooklite.features.bookPlaying.SeekRewindDialogController
import com.goodwy.audiobooklite.features.bookPlaying.SleepTimerListDialogController
import com.goodwy.audiobooklite.features.prefAppearanceUI.PrefAppearanceUIController
import com.goodwy.audiobooklite.features.prefAppearanceUI.MiniPlayerStyleDialogController
import com.goodwy.audiobooklite.features.prefAppearanceUIPlayer.CoverSettingsDialogController
import com.goodwy.audiobooklite.features.prefAppearanceUIPlayer.PlayStyleDialogController
import com.goodwy.audiobooklite.features.prefAppearanceUIPlayer.RewindStyleDialogController
import com.goodwy.audiobooklite.features.prefAppearanceUIPlayer.PrefAppearanceUIPlayerController
import com.goodwy.audiobooklite.features.prefBeta.PrefBetaController
import com.goodwy.audiobooklite.features.prefSkipInterval.PrefSkipIntervalController
import com.goodwy.audiobooklite.features.settings.SettingsController
import com.goodwy.audiobooklite.features.settings.dialogs.AutoRewindDialogController
import com.goodwy.audiobooklite.features.settings.dialogs.PlaybackSpeedDialogController
import com.goodwy.audiobooklite.features.widget.BaseWidgetProvider
import com.goodwy.audiobooklite.misc.MediaAnalyzer
import com.goodwy.audiobooklite.playback.di.PlaybackComponent
import com.goodwy.audiobooklite.playback.playstate.PlayStateManager
import javax.inject.Singleton

/**
 * Base component that is the entry point for injection.
 */
@Singleton
@Component(
  modules = [
    AndroidModule::class,
    PrefsModule::class,
    PersistenceModule::class,
    PlaybackModule::class,
    SortingModule::class
  ]
)
interface AppComponent {

  val bookmarkPresenter: BookmarkPresenter
  val context: Context
  val playStateManager: PlayStateManager
  val ma: MediaAnalyzer

  fun inject(target: AboutController)
  fun inject(target: App)
  fun inject(target: AutoRewindDialogController)
  fun inject(target: BaseWidgetProvider)
  fun inject(target: BookCategoryController)
  fun inject(target: BookOverviewController)
  fun inject(target: BookPlayController)
  fun inject(target: ContributeController)
  fun inject(target: CoverFromInternetController)
  fun inject(target: CoverSettingsDialogController)
  fun inject(target: EditBookBottomSheetController)
  fun inject(target: EditBookTitleDialogController)
  fun inject(target: EditBookAuthorDialogController)
  fun inject(target: EditCoverDialogController)
  fun inject(target: FolderChooserPresenter)
  fun inject(target: FolderOverviewPresenter)
  fun inject(target: BookOverviewHolder)
  fun inject(target: JumpToPositionDialogController)
  fun inject(target: LoadBookCover)
  fun inject(target: LoudnessDialog)
  fun inject(target: MainActivity)
  fun inject(target: MiniPlayerStyleDialogController)
  fun inject(target: PlaybackSpeedDialogController)
  fun inject(target: PlayStyleDialogController)
  fun inject(target: PrefAppearanceUIController)
  fun inject(target: PrefAppearanceUIPlayerController)
  fun inject(target: PrefBetaController)
  fun inject(target: PrefSkipIntervalController)
  fun inject(target: RewindStyleDialogController)
  fun inject(target: SeekDialogController)
  fun inject(target: SeekRewindDialogController)
  fun inject(target: SettingsController)
  fun inject(target: SelectChapterDialog)
  fun inject(target: SleepTimerDialogController)
  fun inject(target: SleepTimerListDialogController)

  fun playbackComponentFactory(): PlaybackComponent.Factory

  @Component.Factory
  interface Factory {
    fun create(@BindsInstance application: Application): AppComponent
  }

  companion object {
    fun factory(): Factory = DaggerAppComponent.factory()
  }
}
