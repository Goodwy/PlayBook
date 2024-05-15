package voice.common.pref

import javax.inject.Qualifier

object PrefKeys {

  const val AUTO_REWIND_AMOUNT = "AUTO_REWIND"
  const val SEEK_TIME = "SEEK_TIME"
  const val SLEEP_TIME = "SLEEP_TIME"
  const val SINGLE_BOOK_FOLDERS = "singleBookFolders"
  const val COLLECTION_BOOK_FOLDERS = "folders"
  const val DARK_THEME = "darkTheme"
  const val GRID_MODE = "gridView"

  //PlayBooks
  const val THEME = "THEME"
  const val COLOR_THEME = "COLOR_THEME"
  const val THEME_WIDGET = "THEME_WIDGET"
  const val SEEK_TIME_REWIND = "SEEK_TIME_REWIND"
  const val CURRENT_VOLUME = "CURRENT_VOLUME"
  const val SHOW_SLIDER_VOLUME = "SHOW_SLIDER_VOLUME"
  const val SKIP_BUTTON_STYLE = "SKIP_BUTTON_STYLE"
  const val PLAY_BUTTON_STYLE = "PLAY_BUTTON_STYLE"
  const val MINI_PLAYER_STYLE = "MINI_PLAYER_STYLE"
  const val PADDING = "PADDING"
  const val TRANSPARENT_NAVIGATION = "TRANSPARENT_NAVIGATION"
  const val PLAYER_BACKGROUND = "PLAYER_BACKGROUND"
  const val COVER_RADIUS = "COVER_RADIUS"
  const val COVER_ELEVATION = "COVER_ELEVATION"
  const val REPEAT_MODE = "REPEAT_MODE"
  const val PRO = "PRO"
  const val PRO_SUBS = "PRO_SUBS"
  const val PRO_RUSTORE = "PRO_RUSTORE"
  const val PRICES = "PRICES"
  const val PRICES_SUBS = "PRICES_SUBS"
  const val PURCHASED_LIST = "PURCHASED_LIST"
  const val PURCHASED_SUBS_LIST = "PURCHASED_SUBS_LIST"
  const val USE_GOOGLE_PLAY = "USE_GOOGLE_PLAY"
  const val IS_PLAY_STORE_INSTALLED = "IS_PLAY_STORE_INSTALLED"
  const val IS_RU_STORE_INSTALLED = "IS_RU_STORE_INSTALLED"
  const val PRICES_RUSTORE = "PRICES_RUSTORE"
  const val PURCHASED_LIST_RUSTORE = "PURCHASED_LIST_RUSTORE"
  const val SORTING = "SORTING"
  const val SORTING_BOOKMARK = "SORTING_BOOKMARK"
  const val USE_GESTURES = "USE_GESTURES"
  const val USE_HAPTIC_FEEDBACK = "USE_HAPTIC_FEEDBACK"
  const val SCAN_COVER_CHAPTER = "SCAN_COVER_CHAPTER"
  const val USE_MENU_ICONS = "USE_MENU_ICONS"

  //old
  const val SHAKE_TO_RESET = "SHAKE_TO_RESET"
  const val SHOW_RATING = "showRatingPref"
  const val DEV_MODE = "devModePref"
  const val SCREEN_ORIENTATION = "screenOrientationPref"
  const val GRID_AUTO = "gridViewAutoPref"
  const val SHOW_PROGRESS_BAR = "showProgressBarPref"
  const val SHOW_DIVIDER = "showDividerPref"
  const val USE_ENGLISH = "useEnglishPref"
}

@Qualifier
annotation class OnboardingCompleted

@Qualifier
annotation class RootAudiobookFolders

@Qualifier
annotation class SingleFolderAudiobookFolders

@Qualifier
annotation class SingleFileAudiobookFolders

@Qualifier
annotation class AuthorAudiobookFolders

@Qualifier
annotation class CurrentBook
