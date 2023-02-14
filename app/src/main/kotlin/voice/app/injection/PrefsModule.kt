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
import voice.app.serialization.SerializableDataStoreFactory
import voice.app.serialization.UriSerializer
import voice.bookOverview.BookMigrationExplanationQualifier
import voice.bookOverview.BookMigrationExplanationShown
import voice.common.AppScope
import voice.common.BookId
import voice.common.grid.GridMode
import voice.common.pref.CurrentBook
import voice.common.pref.PrefKeys
import voice.common.pref.RootAudiobookFolders
import voice.common.pref.SingleFileAudiobookFolders
import voice.common.pref.SingleFolderAudiobookFolders
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
  @Named(PrefKeys.RESUME_ON_REPLUG)
  fun provideResumeOnReplugPreference(prefs: AndroidPreferences): Pref<Boolean> {
    return prefs.boolean(PrefKeys.RESUME_ON_REPLUG, true)
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
    return prefs.int(PrefKeys.SLEEP_TIME, 20)
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
  @RootAudiobookFolders
  fun audiobookFolders(factory: SerializableDataStoreFactory): DataStore<List<Uri>> {
    return factory.createUriList("audiobookFolders")
  }

  @Provides
  @Singleton
  @SingleFolderAudiobookFolders
  fun singleFolderAudiobookFolders(factory: SerializableDataStoreFactory): DataStore<List<Uri>> {
    return factory.createUriList("SingleFolderAudiobookFolders")
  }

  @Provides
  @Singleton
  @SingleFileAudiobookFolders
  fun singleFileAudiobookFolders(factory: SerializableDataStoreFactory): DataStore<List<Uri>> {
    return factory.createUriList("SingleFileAudiobookFolders")
  }

  @Provides
  @Singleton
  @CurrentBook
  fun currentBook(factory: SerializableDataStoreFactory): DataStore<BookId?> {
    return factory.create(
      serializer = BookId.serializer().nullable,
      fileName = "currentBook",
      defaultValue = null,
    )
  }

  @Provides
  @Singleton
  @BookMigrationExplanationQualifier
  fun bookMigrationExplanationShown(factory: SerializableDataStoreFactory): BookMigrationExplanationShown {
    return factory.create(Boolean.serializer(), false, "bookMigrationExplanationShown2")
  }

  //PlayBook
  @Provides
  @Singleton
  @Named(PrefKeys.THEME)
  fun themePref(prefs: AndroidPreferences): Pref<Int> {
    return prefs.int(PrefKeys.THEME, 0) //0-light, 1-dark, 2-system
  }

  @Provides
  @Singleton
  @Named(PrefKeys.COLOR_THEME)
  fun colorThemePreference(prefs: AndroidPreferences): Pref<Int> {
    return prefs.int(PrefKeys.COLOR_THEME, Color(red = 76, green = 134, blue = 203).toArgb())
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
    return prefs.int(PrefKeys.SKIP_BUTTON_STYLE, 1) // 0-classic, 1-round
  }

  @Provides
  @Singleton
  @Named(PrefKeys.PLAY_BUTTON_STYLE)
  fun playButtonStylePref(prefs: AndroidPreferences): Pref<Int> {
    return prefs.int(PrefKeys.PLAY_BUTTON_STYLE, 1) //0-classic, 1-round, 2-square
  }

  @Provides
  @Singleton
  @Named(PrefKeys.MINI_PLAYER_STYLE)
  fun miniPlayerStylePref(prefs: AndroidPreferences): Pref<Int> {
    return prefs.int(PrefKeys.MINI_PLAYER_STYLE, 0) //0-mini player, 1-round button, 2-square button
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
    return prefs.int(PrefKeys.PLAYER_BACKGROUND, 0) //0-app background, 1-blur cover
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
    return prefs.int(PrefKeys.REPEAT_MODE, 0) //0-off, 1-repeat one, 2-repeat all
  }

  @Provides
  @Singleton
  @Named(PrefKeys.PRO)
  fun isProPref(prefs: AndroidPreferences): Pref<Boolean> {
    return prefs.boolean(PrefKeys.PRO, false)
  }

  @Provides
  @Singleton
  @Named(PrefKeys.PRICES)
  fun pricesPref(prefs: AndroidPreferences): Pref<String> {
    return prefs.string(PrefKeys.PRICES, "0,0,0")
  }
}

private fun SerializableDataStoreFactory.createUriList(
  name: String,
): DataStore<List<Uri>> = create(
  serializer = ListSerializer(UriSerializer),
  fileName = name,
  defaultValue = emptyList(),
)
