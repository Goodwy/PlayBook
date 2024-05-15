package voice.app.injection

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.datastore.core.DataStore
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import de.paulwoitaschek.flowpref.Pref
import de.paulwoitaschek.flowpref.android.AndroidPreferences
import de.paulwoitaschek.flowpref.android.boolean
import de.paulwoitaschek.flowpref.android.enum
import de.paulwoitaschek.flowpref.android.int
import de.paulwoitaschek.flowpref.android.string
import de.paulwoitaschek.flowpref.android.stringSet
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.builtins.serializer
import voice.app.BuildConfig
import voice.app.serialization.UriSerializer
import voice.bookOverview.BookMigrationExplanationQualifier
import voice.bookOverview.BookMigrationExplanationShown
import voice.common.AppScope
import voice.common.BookId
import voice.common.constants.*
import voice.common.grid.GridMode
import voice.common.pref.AuthorAudiobookFolders
import voice.common.pref.CurrentBook
import voice.common.pref.OnboardingCompleted
import voice.common.pref.PrefKeys
import voice.common.pref.RootAudiobookFolders
import voice.common.pref.SingleFileAudiobookFolders
import voice.common.pref.SingleFolderAudiobookFolders
import voice.datastore.VoiceDataStoreFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@ContributesTo(AppScope::class)
object PrefsModule {

  @Provides
  @Singleton
  fun provideSharedPreferences(context: Context): SharedPreferences {
    return context.getSharedPreferences("${BuildConfig.APPLICATION_ID}_preferences", Context.MODE_PRIVATE)
  }

  @Provides
  @Singleton
  fun prefs(sharedPreferences: SharedPreferences): AndroidPreferences {
    return AndroidPreferences(sharedPreferences)
  }

  @Provides
  @Singleton
  @Named(PrefKeys.DARK_THEME)
  fun darkThemePref(prefs: AndroidPreferences): Pref<Boolean> {
    return prefs.boolean(PrefKeys.DARK_THEME, false)
  }

  @Provides
  @Singleton
  @Named(PrefKeys.AUTO_REWIND_AMOUNT)
  fun provideAutoRewindAmountPreference(prefs: AndroidPreferences): Pref<Int> {
    return prefs.int(PrefKeys.AUTO_REWIND_AMOUNT, 2)
  }

  @Provides
  @Singleton
  @Named(PrefKeys.SEEK_TIME)
  fun provideSeekTimePreference(prefs: AndroidPreferences): Pref<Int> {
    return prefs.int(PrefKeys.SEEK_TIME, 30)
  }

  @Provides
  @Singleton
  @Named(PrefKeys.SLEEP_TIME)
  fun provideSleepTimePreference(prefs: AndroidPreferences): Pref<Int> {
    return prefs.int(PrefKeys.SLEEP_TIME, 15)
  }

  @Provides
  @Singleton
  @Named(PrefKeys.SINGLE_BOOK_FOLDERS)
  fun provideSingleBookFoldersPreference(prefs: AndroidPreferences): Pref<Set<String>> {
    return prefs.stringSet(PrefKeys.SINGLE_BOOK_FOLDERS, emptySet())
  }

  @Provides
  @Singleton
  @Named(PrefKeys.COLLECTION_BOOK_FOLDERS)
  fun provideCollectionFoldersPreference(prefs: AndroidPreferences): Pref<Set<String>> {
    return prefs.stringSet(PrefKeys.COLLECTION_BOOK_FOLDERS, emptySet())
  }

  @Provides
  @Singleton
  @Named(PrefKeys.GRID_MODE)
  fun gridViewPref(prefs: AndroidPreferences): Pref<GridMode> {
    return prefs.enum(PrefKeys.GRID_MODE, GridMode.FOLLOW_DEVICE)
  }

  @Provides
  @Singleton
  @OnboardingCompleted
  fun onboardingCompleted(factory: VoiceDataStoreFactory): DataStore<Boolean> {
    return factory.boolean("onboardingCompleted", defaultValue = false)
  }

  @Provides
  @Singleton
  @RootAudiobookFolders
  fun audiobookFolders(factory: VoiceDataStoreFactory): DataStore<List<Uri>> {
    return factory.createUriList("audiobookFolders")
  }

  @Provides
  @Singleton
  @SingleFolderAudiobookFolders
  fun singleFolderAudiobookFolders(factory: VoiceDataStoreFactory): DataStore<List<Uri>> {
    return factory.createUriList("SingleFolderAudiobookFolders")
  }

  @Provides
  @Singleton
  @SingleFileAudiobookFolders
  fun singleFileAudiobookFolders(factory: VoiceDataStoreFactory): DataStore<List<Uri>> {
    return factory.createUriList("SingleFileAudiobookFolders")
  }

  @Provides
  @Singleton
  @AuthorAudiobookFolders
  fun authorAudiobookFolders(factory: VoiceDataStoreFactory): DataStore<List<Uri>> {
    return factory.createUriList("AuthorAudiobookFolders")
  }

  @Provides
  @Singleton
  @CurrentBook
  fun currentBook(factory: VoiceDataStoreFactory): DataStore<BookId?> {
    return factory.create(
      serializer = BookId.serializer().nullable,
      fileName = "currentBook",
      defaultValue = null,
    )
  }

  @Provides
  @Singleton
  @BookMigrationExplanationQualifier
  fun bookMigrationExplanationShown(factory: VoiceDataStoreFactory): BookMigrationExplanationShown {
    return factory.create(Boolean.serializer(), false, "bookMigrationExplanationShown2")
  }

  //PlayBook
  @Provides
  @Singleton
  @Named(PrefKeys.THEME)
  fun themePref(prefs: AndroidPreferences): Pref<Int> {
    return prefs.int(PrefKeys.THEME, THEME_LIGHT)
  }

  @Provides
  @Singleton
  @Named(PrefKeys.COLOR_THEME)
  fun colorThemePreference(prefs: AndroidPreferences): Pref<Int> {
    return prefs.int(PrefKeys.COLOR_THEME, Color(red = 76, green = 134, blue = 203).toArgb())
  }

  @Provides
  @Singleton
  @Named(PrefKeys.THEME_WIDGET)
  fun themeWidgetPref(prefs: AndroidPreferences): Pref<Int> {
    return prefs.int(PrefKeys.THEME_WIDGET, THEME_LIGHT)
  }

  @Provides
  @Singleton
  @Named(PrefKeys.SEEK_TIME_REWIND)
  fun provideSeekTimeRewindPreference(prefs: AndroidPreferences): Pref<Int> {
    return prefs.int(PrefKeys.SEEK_TIME_REWIND, 20)
  }

  @Provides
  @Singleton
  @Named(PrefKeys.SKIP_BUTTON_STYLE)
  fun skipButtonStylePref(prefs: AndroidPreferences): Pref<Int> {
    return prefs.int(PrefKeys.SKIP_BUTTON_STYLE, SKIP_BUTTON_ROUND)
  }

  @Provides
  @Singleton
  @Named(PrefKeys.PLAY_BUTTON_STYLE)
  fun playButtonStylePref(prefs: AndroidPreferences): Pref<Int> {
    return prefs.int(PrefKeys.PLAY_BUTTON_STYLE, PLAY_BUTTON_ROUND_AND_SQUARE)
  }

  @Provides
  @Singleton
  @Named(PrefKeys.MINI_PLAYER_STYLE)
  fun miniPlayerStylePref(prefs: AndroidPreferences): Pref<Int> {
    return prefs.int(PrefKeys.MINI_PLAYER_STYLE, MINI_PLAYER_PLAYER)
  }

  @Provides
  @Singleton
  @Named(PrefKeys.CURRENT_VOLUME)
  fun currentVolumePreference(prefs: AndroidPreferences): Pref<Int> {
    return prefs.int(PrefKeys.CURRENT_VOLUME, 0)
  }

  @Provides
  @Singleton
  @Named(PrefKeys.SHOW_SLIDER_VOLUME)
  fun showSliderVolumePref(prefs: AndroidPreferences): Pref<Boolean> {
    return prefs.boolean(PrefKeys.SHOW_SLIDER_VOLUME, true)
  }

  @Provides
  @Singleton
  @Named(PrefKeys.TRANSPARENT_NAVIGATION)
  fun useTransparentNavigationPref(prefs: AndroidPreferences): Pref<Boolean> {
    return prefs.boolean(PrefKeys.TRANSPARENT_NAVIGATION, true)
  }

  @Provides
  @Singleton
  @Named(PrefKeys.PADDING)
  fun paddingPref(prefs: AndroidPreferences): Pref<String> {
    return prefs.string(PrefKeys.PADDING, "0;0;0;0") //padding in Dp for edge to edge
  }

  @Provides
  @Singleton
  @Named(PrefKeys.PLAYER_BACKGROUND)
  fun playerBackgroundPref(prefs: AndroidPreferences): Pref<Int> {
    return prefs.int(PrefKeys.PLAYER_BACKGROUND, PLAYER_BACKGROUND_THEME)
  }

  @Provides
  @Singleton
  @Named(PrefKeys.COVER_RADIUS)
  fun coverRadiusPreference(prefs: AndroidPreferences): Pref<Int> {
    return prefs.int(PrefKeys.COVER_RADIUS, 12)
  }

  @Provides
  @Singleton
  @Named(PrefKeys.COVER_ELEVATION)
  fun coverElevationPreference(prefs: AndroidPreferences): Pref<Int> {
    return prefs.int(PrefKeys.COVER_ELEVATION, 16)
  }

  @Provides
  @Singleton
  @Named(PrefKeys.REPEAT_MODE)
  fun repeatModePreference(prefs: AndroidPreferences): Pref<Int> {
    return prefs.int(PrefKeys.REPEAT_MODE, REPEAT_OFF)
  }

  @Provides
  @Singleton
  @Named(PrefKeys.PRO)
  fun isProPref(prefs: AndroidPreferences): Pref<Boolean> {
    return prefs.boolean(PrefKeys.PRO, false)
  }

  @Provides
  @Singleton
  @Named(PrefKeys.PRO_SUBS)
  fun isProSubsPref(prefs: AndroidPreferences): Pref<Boolean> {
    return prefs.boolean(PrefKeys.PRO_SUBS, false)
  }

  @Provides
  @Singleton
  @Named(PrefKeys.PRO_RUSTORE)
  fun isProRuPref(prefs: AndroidPreferences): Pref<Boolean> {
    return prefs.boolean(PrefKeys.PRO_RUSTORE, false)
  }

  @Provides
  @Singleton
  @Named(PrefKeys.PRICES)
  fun pricesPref(prefs: AndroidPreferences): Pref<String> {
    return prefs.string(PrefKeys.PRICES, "0;0;0")
  }

  @Provides
  @Singleton
  @Named(PrefKeys.PRICES_SUBS)
  fun pricesSubsPref(prefs: AndroidPreferences): Pref<String> {
    return prefs.string(PrefKeys.PRICES_SUBS, "0;0;0;0;0;0")
  }

  @Provides
  @Singleton
  @Named(PrefKeys.PURCHASED_LIST)
  fun purchasedList(prefs: AndroidPreferences): Pref<String> {
    return prefs.string(PrefKeys.PURCHASED_LIST, "0;0;0")
  }

  @Provides
  @Singleton
  @Named(PrefKeys.PURCHASED_SUBS_LIST)
  fun purchasedSubsList(prefs: AndroidPreferences): Pref<String> {
    return prefs.string(PrefKeys.PURCHASED_SUBS_LIST, "0;0;0;0;0;0")
  }

  @Provides
  @Singleton
  @Named(PrefKeys.USE_GOOGLE_PLAY)
  fun useGooglePlay(prefs: AndroidPreferences): Pref<Boolean> {
    return prefs.boolean(PrefKeys.USE_GOOGLE_PLAY, true)
  }

  @Provides
  @Singleton
  @Named(PrefKeys.IS_PLAY_STORE_INSTALLED)
  fun isPlayStoreInstalledPref(prefs: AndroidPreferences): Pref<Boolean> {
    return prefs.boolean(PrefKeys.IS_PLAY_STORE_INSTALLED, false)
  }

  @Provides
  @Singleton
  @Named(PrefKeys.IS_RU_STORE_INSTALLED)
  fun isRuStoreInstalledPref(prefs: AndroidPreferences): Pref<Boolean> {
    return prefs.boolean(PrefKeys.IS_RU_STORE_INSTALLED, false)
  }

  @Provides
  @Singleton
  @Named(PrefKeys.PRICES_RUSTORE)
  fun pricesRustorePref(prefs: AndroidPreferences): Pref<String> {
    return prefs.string(PrefKeys.PRICES_RUSTORE, "0;0;0;0;0;0;0;0;0")
  }

  @Provides
  @Singleton
  @Named(PrefKeys.PURCHASED_LIST_RUSTORE)
  fun purchasedListRustore(prefs: AndroidPreferences): Pref<String> {
    return prefs.string(PrefKeys.PURCHASED_LIST_RUSTORE, "0;0;0;0;0;0;0;0;0")
  }

  @Provides
  @Singleton
  @Named(PrefKeys.SORTING)
  fun sortingPref(prefs: AndroidPreferences): Pref<Int> {
    return prefs.int(PrefKeys.SORTING, SORTING_CLASSIC)
  }

  @Provides
  @Singleton
  @Named(PrefKeys.SORTING_BOOKMARK)
  fun sortingBookmarkPref(prefs: AndroidPreferences): Pref<Int> {
    return prefs.int(PrefKeys.SORTING_BOOKMARK, SORTING_BOOKMARK_LAST)
  }

  @Provides
  @Singleton
  @Named(PrefKeys.USE_GESTURES)
  fun useGestures(prefs: AndroidPreferences): Pref<Boolean> {
    return prefs.boolean(PrefKeys.USE_GESTURES, true)
  }

  @Provides
  @Singleton
  @Named(PrefKeys.USE_HAPTIC_FEEDBACK)
  fun useHapticFeedback(prefs: AndroidPreferences): Pref<Boolean> {
    return prefs.boolean(PrefKeys.USE_HAPTIC_FEEDBACK, false)
  }

  @Provides
  @Singleton
  @Named(PrefKeys.SCAN_COVER_CHAPTER)
  fun scanCoverChapter(prefs: AndroidPreferences): Pref<Boolean> {
    return prefs.boolean(PrefKeys.SCAN_COVER_CHAPTER, true)
  }

  @Provides
  @Singleton
  @Named(PrefKeys.USE_MENU_ICONS)
  fun useMenuIconsPref(prefs: AndroidPreferences): Pref<Boolean> {
    return prefs.boolean(PrefKeys.USE_MENU_ICONS, false)
  }
}

private fun VoiceDataStoreFactory.createUriList(name: String): DataStore<List<Uri>> = create(
  serializer = ListSerializer(UriSerializer),
  fileName = name,
  defaultValue = emptyList(),
)
