package com.goodwy.audiobook.features.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat.ACTION_FAST_FORWARD
import android.support.v4.media.session.PlaybackStateCompat.ACTION_PLAY_PAUSE
import android.support.v4.media.session.PlaybackStateCompat.ACTION_REWIND
import android.view.View
import android.view.WindowManager
import android.widget.RemoteViews
import androidx.core.content.ContextCompat.getColor
import androidx.media.session.MediaButtonReceiver.buildMediaButtonPendingIntent
import com.squareup.picasso.Picasso
import dagger.Reusable
import de.paulwoitaschek.flowpref.Pref
import com.goodwy.audiobook.R
import com.goodwy.audiobook.common.CoverReplacement
import com.goodwy.audiobook.common.ImageHelper
import com.goodwy.audiobook.common.MAX_IMAGE_SIZE
import com.goodwy.audiobook.common.pref.PrefKeys
import com.goodwy.audiobook.data.Book
import com.goodwy.audiobook.data.repo.BookRepository
import com.goodwy.audiobook.features.MainActivity
import com.goodwy.audiobook.misc.coverFile
import com.goodwy.audiobook.misc.dpToPxRounded
import com.goodwy.audiobook.playback.playstate.PlayStateManager
import com.goodwy.audiobook.uitools.RoundedTransformation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import java.util.UUID
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Provider

@Reusable
class WidgetUpdater @Inject constructor(
  private val context: Context,
  private val repo: BookRepository,
  @Named(PrefKeys.CURRENT_BOOK)
  private val currentBookIdPref: Pref<UUID>,
  @Named(PrefKeys.DARK_THEME)
  private val useDarkTheme: Pref<Boolean>,
  private val imageHelper: ImageHelper,
  private val playStateManager: PlayStateManager,
  private val windowManager: Provider<WindowManager>
) {

  private val appWidgetManager = AppWidgetManager.getInstance(context)

  fun update() {
    GlobalScope.launch(Dispatchers.IO) {
      val book = repo.bookById(currentBookIdPref.value)
      Timber.i("update with book ${book?.name}")
      val componentName = ComponentName(this@WidgetUpdater.context, BaseWidgetProvider::class.java)
      val ids = appWidgetManager.getAppWidgetIds(componentName)

      for (widgetId in ids) {
        updateWidgetForId(book, widgetId)
      }
    }
  }

  private suspend fun updateWidgetForId(book: Book?, widgetId: Int) {
    if (book != null) {
      initWidgetForPresentBook(widgetId, book)
    } else {
      initWidgetForAbsentBook(widgetId)
    }
  }

  private suspend fun initWidgetForPresentBook(widgetId: Int, book: Book) {
    val opts = appWidgetManager.getAppWidgetOptions(widgetId)
    val useWidth = widgetWidth(opts)
    val useHeight = widgetHeight(opts)

    val remoteViews = RemoteViews(context.packageName, R.layout.widget)
    initElements(remoteViews = remoteViews, book = book, coverSize = useHeight)

    if (useWidth > 0 && useHeight > 0) {
      setVisibilities(remoteViews, useWidth, useHeight, book.content.chapters.size == 1)
    }
    appWidgetManager.updateAppWidget(widgetId, remoteViews)
  }

  private fun widgetWidth(opts: Bundle): Int {
    val key = if (isPortrait) {
      AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH
    } else AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH
    val dp = opts.getInt(key)
    return context.dpToPxRounded(dp.toFloat())
  }

  private fun widgetHeight(opts: Bundle): Int {
    val key = if (isPortrait) {
      AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT
    } else AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT
    val dp = opts.getInt(key)
    return context.dpToPxRounded(dp.toFloat())
  }

  private fun initWidgetForAbsentBook(widgetId: Int) {
    val remoteViews = RemoteViews(context.packageName, R.layout.widget)
    // directly going back to bookChoose
    val wholeWidgetClickI = Intent(context, MainActivity::class.java)
    wholeWidgetClickI.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
    val wholeWidgetClickPI = PendingIntent.getActivity(
      context, System.currentTimeMillis().toInt(),
      wholeWidgetClickI, PendingIntent.FLAG_UPDATE_CURRENT
    )
    val coverReplacement = CoverReplacement("V", context)
    val cover = imageHelper.drawableToBitmap(
      coverReplacement,
      imageHelper.smallerScreenSize,
      imageHelper.smallerScreenSize
    )
    remoteViews.setImageViewBitmap(R.id.imageView, cover)
    remoteViews.setOnClickPendingIntent(R.id.wholeWidget, wholeWidgetClickPI)
    appWidgetManager.updateAppWidget(widgetId, remoteViews)
  }

  private val isPortrait: Boolean
    get() {
      val orientation = context.resources.configuration.orientation
      val window = windowManager.get()
      val display = window.defaultDisplay

      @Suppress("DEPRECATION")
      val displayWidth = display.width
      @Suppress("DEPRECATION")
      val displayHeight = display.height

      return orientation != Configuration.ORIENTATION_LANDSCAPE &&
        (orientation == Configuration.ORIENTATION_PORTRAIT ||
          displayWidth == displayHeight || displayWidth < displayHeight)
    }

  private suspend fun initElements(remoteViews: RemoteViews, book: Book, coverSize: Int) {
    val playPausePI = buildMediaButtonPendingIntent(context, ACTION_PLAY_PAUSE)
    remoteViews.setOnClickPendingIntent(R.id.playPause, playPausePI)

    val fastForwardPI = buildMediaButtonPendingIntent(context, ACTION_FAST_FORWARD)
    remoteViews.setOnClickPendingIntent(R.id.fastForward, fastForwardPI)

    val rewindPI = buildMediaButtonPendingIntent(context, ACTION_REWIND)
    remoteViews.setOnClickPendingIntent(R.id.rewind, rewindPI)

    val play = if (useDarkTheme.value) R.drawable.ic_play_white_36dp else R.drawable.ic_play_black_36dp
    val pause = if (useDarkTheme.value) R.drawable.ic_pause_white_36dp else R.drawable.ic_pause_black_36dp
    val playIcon = if (playStateManager.playState == PlayStateManager.PlayState.Playing) {
      pause
    } else play
    remoteViews.setImageViewResource(R.id.playPause, playIcon)

    // if we have any book, init the views and have a click on the whole widget start BookPlay.
    // if we have no book, simply have a click on the whole widget start BookChoose.
    remoteViews.setTextViewText(R.id.title, book.name)
    val name = book.content.currentChapter.name

    remoteViews.setTextViewText(R.id.summary, name)

    val wholeWidgetClickI = MainActivity.goToBookIntent(context, book.id)
    val wholeWidgetClickPI = PendingIntent.getActivity(
      context,
      System.currentTimeMillis().toInt(),
      wholeWidgetClickI,
      PendingIntent.FLAG_UPDATE_CURRENT
    )

    val coverFile = book.coverFile()
    var cover = if (coverFile.canRead() && coverFile.length() < MAX_IMAGE_SIZE) {
      val sizeForPicasso = coverSize.takeIf { it > 0 }
        ?: context.dpToPxRounded(56F)
      try {
        Picasso.get()
          .load(coverFile)
          .resize(sizeForPicasso, sizeForPicasso)
          .transform(RoundedTransformation(16, 0))
          .get()
      } catch (e: IOException) {
        Timber.e(e)
        null
      }
    } else {
      null
    }

    if (cover == null) {
      val coverReplacement = CoverReplacement(book.name, context)
      cover = imageHelper.drawableToBitmap(
        coverReplacement,
        imageHelper.smallerScreenSize,
        imageHelper.smallerScreenSize
      )
    }

    // изменение цвета виджета
    remoteViews.setImageViewBitmap(R.id.imageView, cover)
    remoteViews.setOnClickPendingIntent(R.id.wholeWidget, wholeWidgetClickPI)
    val bg = if (useDarkTheme.value) R.drawable.widget_background else R.drawable.widget_white_background
    remoteViews.setInt(R.id.wholeWidget, "setBackgroundResource", bg)

    val rewindIcon = if (useDarkTheme.value) R.drawable.ic_rewind_white_36dp else R.drawable.ic_rewind_black_36dp
    remoteViews.setImageViewResource(R.id.rewind, rewindIcon)

    val fastForwardIcon = if (useDarkTheme.value) R.drawable.ic_fast_forward_white_36dp else R.drawable.ic_fast_forward_black_36dp
    remoteViews.setImageViewResource(R.id.fastForward, fastForwardIcon)

    val textColor = if (useDarkTheme.value) R.color.copy_primary_text_default_material_dark else R.color.copy_primary_text_default_material_light
    remoteViews.setTextColor(R.id.title, getColor(context, textColor))
    remoteViews.setTextColor(R.id.summary, getColor(context, textColor))
  }

  private fun setVisibilities(
    remoteViews: RemoteViews,
    width: Int,
    height: Int,
    singleChapter: Boolean
  ) {
    setHorizontalVisibility(remoteViews, width, height)
    setVerticalVisibility(remoteViews, height, singleChapter)
  }

  private fun setHorizontalVisibility(remoteViews: RemoteViews, widgetWidth: Int, coverSize: Int) {
    val singleButtonSize = context.dpToPxRounded(8F + 36F + 8F)
    // widget height because cover is square
    var summarizedItemWidth = 3 * singleButtonSize + coverSize

    // set all views visible
    remoteViews.setViewVisibility(R.id.imageView, View.VISIBLE)
    remoteViews.setViewVisibility(R.id.rewind, View.VISIBLE)
    remoteViews.setViewVisibility(R.id.fastForward, View.VISIBLE)

    // hide cover if we need space
    if (summarizedItemWidth > widgetWidth) {
      remoteViews.setViewVisibility(R.id.imageView, View.GONE)
      summarizedItemWidth -= coverSize
    }

    // hide fast forward if we need space
    if (summarizedItemWidth > widgetWidth) {
      remoteViews.setViewVisibility(R.id.fastForward, View.GONE)
      summarizedItemWidth -= singleButtonSize
    }

    // hide rewind if we need space
    if (summarizedItemWidth > widgetWidth) {
      remoteViews.setViewVisibility(R.id.rewind, View.GONE)
    }
  }

  private fun setVerticalVisibility(
    remoteViews: RemoteViews,
    widgetHeight: Int,
    singleChapter: Boolean
  ) {
    val buttonSize = context.dpToPxRounded(8F + 36F + 8F)
    val titleSize = context.resources.getDimensionPixelSize(R.dimen.list_text_primary_size)
    val summarySize = context.resources.getDimensionPixelSize(R.dimen.list_text_secondary_size)

    var summarizedItemsHeight = buttonSize + titleSize + summarySize

    // first setting all views visible
    remoteViews.setViewVisibility(R.id.summary, View.VISIBLE)
    remoteViews.setViewVisibility(R.id.title, View.VISIBLE)

    // when we are in a single chapter or we are to high, hide summary
    if (singleChapter || widgetHeight < summarizedItemsHeight) {
      remoteViews.setViewVisibility(R.id.summary, View.GONE)
      summarizedItemsHeight -= summarySize
    }

    // if we ar still to high, hide title
    if (summarizedItemsHeight > widgetHeight) {
      remoteViews.setViewVisibility(R.id.title, View.GONE)
    }
  }
}
