package com.goodwy.audiobook.injection

import android.app.Application
import android.content.Context
import dagger.BindsInstance
import dagger.Component
import com.goodwy.audiobook.data.repo.internals.PersistenceModule
import com.goodwy.audiobook.features.MainActivity
import com.goodwy.audiobook.features.audio.LoudnessDialog
import com.goodwy.audiobook.features.bookCategory.BookCategoryController
import com.goodwy.audiobook.features.bookOverview.BookOverviewController
import com.goodwy.audiobook.features.bookOverview.EditBookAuthorDialogController
import com.goodwy.audiobook.features.bookOverview.EditBookBottomSheetController
import com.goodwy.audiobook.features.bookOverview.EditBookTitleDialogController
import com.goodwy.audiobook.features.bookOverview.EditCoverDialogController
import com.goodwy.audiobook.features.bookOverview.list.LoadBookCover
import com.goodwy.audiobook.features.bookPlaying.BookPlayController
import com.goodwy.audiobook.features.bookPlaying.JumpToPositionDialogController
import com.goodwy.audiobook.features.bookPlaying.SeekDialogController
import com.goodwy.audiobook.features.bookPlaying.SleepTimerDialogController
import com.goodwy.audiobook.features.bookPlaying.selectchapter.SelectChapterDialog
import com.goodwy.audiobook.features.bookmarks.BookmarkPresenter
import com.goodwy.audiobook.features.folderChooser.FolderChooserPresenter
import com.goodwy.audiobook.features.folderOverview.FolderOverviewPresenter
import com.goodwy.audiobook.features.imagepicker.CoverFromInternetController
import com.goodwy.audiobook.features.contribute.ContributeController
import com.goodwy.audiobook.features.about.AboutController
import com.goodwy.audiobook.features.bookPlaying.SeekRewindDialogController
import com.goodwy.audiobook.features.prefAppearanceUI.PrefAppearanceUIController
import com.goodwy.audiobook.features.prefAppearanceUI.MiniPlayerStyleDialogController
import com.goodwy.audiobook.features.prefAppearanceUI.RewindStyleDialogController
import com.goodwy.audiobook.features.prefBeta.PrefBetaController
import com.goodwy.audiobook.features.prefSkipInterval.PrefSkipIntervalController
import com.goodwy.audiobook.features.settings.SettingsController
import com.goodwy.audiobook.features.settings.dialogs.AutoRewindDialogController
import com.goodwy.audiobook.features.settings.dialogs.PlaybackSpeedDialogController
import com.goodwy.audiobook.features.widget.BaseWidgetProvider
import com.goodwy.audiobook.misc.MediaAnalyzer
import com.goodwy.audiobook.playback.di.PlaybackComponent
import com.goodwy.audiobook.playback.playstate.PlayStateManager
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
  fun inject(target: EditBookBottomSheetController)
  fun inject(target: EditBookTitleDialogController)
  fun inject(target: EditBookAuthorDialogController)
  fun inject(target: EditCoverDialogController)
  fun inject(target: FolderChooserPresenter)
  fun inject(target: FolderOverviewPresenter)
  fun inject(target: JumpToPositionDialogController)
  fun inject(target: LoadBookCover)
  fun inject(target: LoudnessDialog)
  fun inject(target: MainActivity)
  fun inject(target: MiniPlayerStyleDialogController)
  fun inject(target: PlaybackSpeedDialogController)
  fun inject(target: PrefAppearanceUIController)
  fun inject(target: PrefBetaController)
  fun inject(target: PrefSkipIntervalController)
  fun inject(target: RewindStyleDialogController)
  fun inject(target: SeekDialogController)
  fun inject(target: SeekRewindDialogController)
  fun inject(target: SettingsController)
  fun inject(target: SelectChapterDialog)
  fun inject(target: SleepTimerDialogController)

  fun playbackComponentFactory(): PlaybackComponent.Factory

  @Component.Factory
  interface Factory {
    fun create(@BindsInstance application: Application): AppComponent
  }

  companion object {
    fun factory(): Factory = DaggerAppComponent.factory()
  }
}
