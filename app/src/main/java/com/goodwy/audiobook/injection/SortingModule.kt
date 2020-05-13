package com.goodwy.audiobook.injection

import dagger.MapKey
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import de.paulwoitaschek.flowpref.Pref
import de.paulwoitaschek.flowpref.android.AndroidPreferences
import de.paulwoitaschek.flowpref.android.enum
import com.goodwy.audiobook.data.BookComparator
import com.goodwy.audiobook.features.bookOverview.list.header.BookOverviewCategory
import javax.inject.Singleton

@MapKey
annotation class BookOverviewCategoryKey(val value: BookOverviewCategory)

@Module
object SortingModule {

  @JvmStatic
  @Provides
  @Singleton
  @IntoMap
  @BookOverviewCategoryKey(BookOverviewCategory.CURRENT)
  fun currentComparatorPref(prefs: AndroidPreferences): Pref<BookComparator> {
    return prefs.enum(BookOverviewCategory.CURRENT.name, BookComparator.BY_NAME)
  }

  @JvmStatic
  @Provides
  @Singleton
  @IntoMap
  @BookOverviewCategoryKey(BookOverviewCategory.NOT_STARTED)
  fun notStartedComparatorPref(prefs: AndroidPreferences): Pref<BookComparator> {
    return prefs.enum(BookOverviewCategory.NOT_STARTED.name, BookComparator.BY_NAME, BookComparator::class.java)
  }

  @JvmStatic
  @Provides
  @Singleton
  @IntoMap
  @BookOverviewCategoryKey(BookOverviewCategory.FINISHED)
  fun finishedComparatorPref(prefs: AndroidPreferences): Pref<BookComparator> {
    return prefs.enum(BookOverviewCategory.FINISHED.name, BookComparator.BY_NAME, BookComparator::class.java)
  }
}
