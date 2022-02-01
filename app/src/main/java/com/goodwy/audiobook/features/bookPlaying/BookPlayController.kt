package com.goodwy.audiobook.features.bookPlaying

import android.R.attr
import android.content.Context
import android.content.res.Resources
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.media.AudioManager
import android.os.Bundle
import android.view.GestureDetector
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.goodwy.audiobook.R
import com.goodwy.audiobook.common.pref.PrefKeys
import com.goodwy.audiobook.databinding.BookPlayBinding
import com.goodwy.audiobook.features.ViewBindingController
import com.goodwy.audiobook.features.audio.LoudnessDialog
import com.goodwy.audiobook.features.bookPlaying.selectchapter.SelectChapterDialog
import com.goodwy.audiobook.features.bookmarks.BookmarkController
import com.goodwy.audiobook.features.settings.SettingsController
import com.goodwy.audiobook.features.settings.dialogs.PlaybackSpeedDialogController
import com.goodwy.audiobook.injection.appComponent
import com.goodwy.audiobook.misc.CircleOutlineProvider
import com.goodwy.audiobook.misc.conductor.asTransaction
import com.goodwy.audiobook.misc.conductor.clearAfterDestroyView
import com.goodwy.audiobook.misc.conductor.context
import com.goodwy.audiobook.misc.formatTime
import com.goodwy.audiobook.misc.formatTimeSeconds
import com.goodwy.audiobook.misc.getUUID
import com.goodwy.audiobook.misc.putUUID
import com.goodwy.audiobook.playback.player.Equalizer
import com.goodwy.audiobook.uitools.PlayPauseDrawableSetter
import com.google.android.material.slider.Slider
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import de.paulwoitaschek.flowpref.Pref
import kotlinx.android.synthetic.main.book_overview_row_list.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject
import javax.inject.Named
import kotlin.time.Duration
import kotlin.time.minutes
import kotlin.time.seconds
import androidx.core.content.ContextCompat
import com.goodwy.audiobook.covercolorextractor.CoverColorExtractor
import com.goodwy.audiobook.misc.coverFile
import com.goodwy.audiobook.misc.digits
import com.goodwy.audiobook.misc.getColoredDrawableWithColor
import com.goodwy.audiobook.misc.hours
import com.goodwy.audiobook.misc.minutes
import android.R.attr.bitmap

import android.graphics.drawable.BitmapDrawable
import com.goodwy.audiobook.injection.App
import com.goodwy.audiobook.uitools.BlurFactory
import android.R.attr.bitmap
import android.graphics.Color
import android.os.Build
import android.text.TextUtils
import com.goodwy.audiobook.uitools.TextIconDrawable
import java.text.DecimalFormat


private const val NI_BOOK_ID = "niBookId"

/**
 * Base class for book playing interaction.
 */
class BookPlayController(bundle: Bundle) : ViewBindingController<BookPlayBinding>(bundle, BookPlayBinding::inflate) {

  constructor(bookId: UUID) : this(Bundle().apply { putUUID(NI_BOOK_ID, bookId) })

  @Inject
  lateinit var equalizer: Equalizer
  @Inject
  lateinit var viewModel: BookPlayViewModel
  @Inject
  lateinit var coverColorExtractor: CoverColorExtractor

  @field:[Inject Named(PrefKeys.SHOW_SLIDER_VOLUME)]
  lateinit var showSliderVolumePref: Pref<Boolean>

  @field:[Inject Named(PrefKeys.SEEK_TIME)]
  lateinit var seekTimePref: Pref<Int>

  @field:[Inject Named(PrefKeys.SEEK_TIME_REWIND)]
  lateinit var seekTimeRewindPref: Pref<Int>

  @field:[Inject Named(PrefKeys.COVER_RADIUS)]
  lateinit var coverRadiusPref: Pref<Int>

  @field:[Inject Named(PrefKeys.COVER_ELEVATION)]
  lateinit var coverElevationPref: Pref<Int>

  @field:[Inject Named(PrefKeys.PLAY_BUTTON_STYLE)]
  lateinit var playButtonStylePref: Pref<Int>

  @field:[Inject Named(PrefKeys.ICON_MODE)]
  lateinit var iconModePref: Pref<Boolean>

  @field:[Inject Named(PrefKeys.PLAYER_BACKGROUND)]
  lateinit var playerBackgroundPref: Pref<Int>

  @field:[Inject Named(PrefKeys.PADDING)]
  lateinit var paddingPref: Pref<String>

  private val bookId = bundle.getUUID(NI_BOOK_ID)
  private var coverLoaded = false

  private var sleepTimerItem: MenuItem by clearAfterDestroyView()
  private var timeLapseItem: MenuItem by clearAfterDestroyView()
  private var skipSilenceItem: MenuItem by clearAfterDestroyView()
  private var showChapterNumbers: MenuItem by clearAfterDestroyView()
  private var repeatModeItem: MenuItem by clearAfterDestroyView()

  private var playPauseDrawableSetter by clearAfterDestroyView<PlayPauseDrawableSetter>()

  private val seekTime: Duration get() = seekTimePref.value.seconds
  private val seekTimeRewind: Duration get() = seekTimeRewindPref.value.seconds

  init {
    appComponent.inject(this)
    this.viewModel.bookId = bookId
  }

  override fun BookPlayBinding.onBindingCreated() {
    coverLoaded = false
    playPauseDrawableSetter = PlayPauseDrawableSetter(play)
    setupClicks()
    setupSlider()
    setupSliderVolume()
    setupToolbar()
    play.apply {
      outlineProvider = CircleOutlineProvider()
      clipToOutline = true
    }
    rewind.apply {
      outlineProvider = CircleOutlineProvider()
      clipToOutline = true
    }
    fastForward.apply {
      outlineProvider = CircleOutlineProvider()
      clipToOutline = true
    }
    rewindTime.apply {
      outlineProvider = CircleOutlineProvider()
      clipToOutline = true
    }
    fastForwardTime.apply {
      outlineProvider = CircleOutlineProvider()
      clipToOutline = true
    }
    contentList.apply {
      outlineProvider = CircleOutlineProvider()
      clipToOutline = true
    }
  }

  override fun BookPlayBinding.onAttach() {
    lifecycleScope.launch {
      this@BookPlayController.viewModel.viewState().collect {
        this@onAttach.render(it)
      }
    }
    lifecycleScope.launch {
      this@BookPlayController.viewModel.viewEffects.collect {
        handleViewEffect(it)
      }
    }
    //padding for Edge-to-edge
    lifecycleScope.launch {
      paddingPref.flow.collect {
        val top = paddingPref.value.substringBefore(';').toInt()
        val bottom = paddingPref.value.substringAfter(';').substringBefore(';').toInt()
        val left = paddingPref.value.substringBeforeLast(';').substringAfterLast(';').toInt()
        val right = paddingPref.value.substringAfterLast(';').toInt()
        root.setPadding(left, top, right, bottom)
      }
    }
  }

  private fun handleViewEffect(effect: BookPlayViewEffect) {
    when (effect) {
      BookPlayViewEffect.BookmarkAdded -> {
        Snackbar.make(view!!, R.string.bookmark_added, Snackbar.LENGTH_SHORT)
          .show()
      }
      BookPlayViewEffect.ShowSleepTimeDialog -> {
        openSleepTimeDialog()
      }
    }
  }

  private suspend fun BookPlayBinding.render(viewState: BookPlayViewState) {
    Timber.d("render $viewState")
    val density = context.resources.displayMetrics.density

/*    toolbar.title = viewState.title*/ /*Имя книги в заголовке плеера*/
    val size_18dp = (18 * density + 0.5f).toFloat()
    val timeLapseIcon = TextIconDrawable().apply {
      text = DecimalFormat("0.0x").format(viewState.timeLapse)
      textColor = ContextCompat.getColor(context, R.color.colorIcon)
      size = size_18dp
    }
    timeLapseItem.setIcon(timeLapseIcon)

    currentBookText.text = viewState.bookName
    currentBookText.ellipsize = TextUtils.TruncateAt.MARQUEE
    currentBookText.isSelected = true
    currentBookText.isSingleLine = true
    currentChapterText.text = viewState.chapterName
    currentChapterContainer.isVisible = viewState.chapterName != null
    contentList.isInvisible = true /* = contentsButtonMode.value*/
    previous.isVisible = viewState.showPreviousNextButtons
    next.isVisible = viewState.showPreviousNextButtons
    playedTime.text = formatTime(viewState.playedTime.toLongMilliseconds(), viewState.duration.toLongMilliseconds())
    maxTime.text = formatTime(viewState.duration.toLongMilliseconds(), viewState.duration.toLongMilliseconds())
    remainingTime.isVisible = viewState.chapterName != null
    remainingTime.text = context.getString(R.string.left, formatTimeMinutes(viewState.remainingTimeInMs))
    // Bug fixes:
    // java.lang.IllegalStateException
    //com.google.android.material.slider.Slider.validateValues
    if (viewState.playedTime.inMilliseconds.toFloat() > viewState.duration.inMilliseconds.toFloat()) {
      slider.valueTo = viewState.playedTime.inMilliseconds.toFloat()
    } else {
      slider.valueTo = viewState.duration.inMilliseconds.toFloat()
    }
    if (!slider.isPressed) {
      slider.value = viewState.playedTime.inMilliseconds.toFloat()
    }

    val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
    val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
    volumeUp.isVisible = showSliderVolumePref.value
    volumeDown.isVisible = showSliderVolumePref.value
    sliderVolume.isVisible = showSliderVolumePref.value
    sliderVolume.valueTo = maxVolume.toFloat()
    if (!sliderVolume.isPressed) {
      sliderVolume.value = viewState.currentVolumePref.toFloat()//currentVolume.toFloat()
    }

    skipSilenceItem.isChecked = viewState.skipSilence
    showChapterNumbers.isChecked = viewState.showChapterNumbers
    playPauseDrawableSetter.setPlaying(viewState.playing)
    showLeftSleepTime(this, viewState.sleepTime)
    showRepeatMode(viewState.repeatModePref)

    rewind.isVisible = viewState.rewindButtonStylePref == 1
    fastForward.isVisible = viewState.rewindButtonStylePref == 1
    rewindTime.isVisible = viewState.rewindButtonStylePref == 2
    fastForwardTime.isVisible = viewState.rewindButtonStylePref == 2
    seekRewindTimeText.isVisible = viewState.rewindButtonStylePref == 2
    fastForwardTimeText.isVisible = viewState.rewindButtonStylePref == 2

    seekRewindTimeText.text = formatTimeSeconds(seekTimeRewind.toLongMilliseconds())
    fastForwardTimeText.text = formatTimeSeconds(seekTime.toLongMilliseconds())

    val coverFile = viewState.cover.file()
    if (!coverLoaded) {
      coverLoaded = true
      //cover.transitionName = viewState.cover.coverTransitionName() //анимация обложки
      currentBookText.transitionName = viewState.cover.coverTransitionName()
     // cover.clipToOutline = true
     // cover.outlineProvider = RoundRectOutlineProvider(context.dpToPx(8F)) // TODO RADIUS COVER
        val placeholder = viewState.cover.placeholder(activity!!)
        if (coverFile == null) {
          Picasso.get().cancelRequest(cover)
          cover.setImageDrawable(placeholder)
        } else {
          Picasso.get()
            .load(coverFile)
            .placeholder(placeholder)
            //.transform(RoundedTransformation(8, 20))
            .into(cover)
      }

      /*val displayMetrics = context.resources.displayMetrics.heightPixels / 3
      val width_330dp = (330 * density + 0.5f).toInt()
      val layoutParams = coverCard.layoutParams
     // layoutParams?.height = width_330dp
     // layoutParams?.width = width_330dp //ViewGroup.LayoutParams.MATCH_PARENT
      coverCard.layoutParams = layoutParams*/
      coverCard.radius = coverRadiusPref.value.toFloat() * 2 //16F
      coverCard.cardElevation = coverElevationPref.value.toFloat() //128F
      coverCard.maxCardElevation = 128F
      //shadow color
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        coverCard.outlineAmbientShadowColor = ContextCompat.getColor(context, R.color.shadow_elevation)
        coverCard.outlineSpotShadowColor = ContextCompat.getColor(context, R.color.shadow_elevation)
      }
    }

    // background
    //root.setPadding(paddingLeftPref.value, paddingTopPref.value, paddingRightPref.value, paddingBottomPref.value) //padding for Edge-to-edge
    var drawable = resources!!.getColoredDrawableWithColor(R.drawable.book_play_backgroung, ContextCompat.getColor(context, R.color.primary), 80)
    if (playerBackgroundPref.value == 1) {
        val extractedColor = coverColorExtractor.extract(coverFile!!)
        val color = extractedColor ?: ContextCompat.getColor(context, R.color.primary)
        drawable = resources!!.getColoredDrawableWithColor(R.drawable.book_play_backgroung, color, 80)
        //activity!!.window.navigationBarColor = color
        //activity!!.window.statusBarColor = color
      } else if (playerBackgroundPref.value == 2) {
        val coverFileBlur = BlurFactory.fileToBlurBitmap(coverFile!!, context)
        if (coverFileBlur == null) {
          drawable = resources!!.getColoredDrawableWithColor(R.drawable.book_play_backgroung, ContextCompat.getColor(context, R.color.primary), 80)
        } else {
          drawable = BitmapDrawable(resources, coverFileBlur)
          drawable.alpha = 60
        }
      } else {
      drawable = resources!!.getColoredDrawableWithColor(R.drawable.book_play_backgroung, ContextCompat.getColor(context, R.color.primary), 80)
    }
    bookPlayRoot.background = drawable

    if (playButtonStylePref.value == 2) {
      play.setBackgroundResource(R.drawable.play_button)
      play.setColorFilter(ContextCompat.getColor(context, R.color.white))
      //DrawableCompat.setTint(play.getDrawable(), ContextCompat.getColor(context, R.color.white))
      play.elevation = 16f
      /*val width_83dp = (83 * density + 0.5f).toInt()
      val layoutParams = play.layoutParams
      layoutParams?.height = width_83dp
      layoutParams?.width = width_83dp //ViewGroup.LayoutParams.MATCH_PARENT
      play.layoutParams = layoutParams*/
      val width_16dp = (16 * density + 0.5f).toInt()
      play.setPadding(width_16dp,width_16dp,width_16dp,width_16dp)
    } else {
      play.setBackgroundColor(getResources()!!.getColor(android.R.color.transparent))
      play.setColorFilter(ContextCompat.getColor(context, R.color.colorIcon))
      //DrawableCompat.setTint(play.getDrawable(), ContextCompat.getColor(context, R.color.colorIcon))
      play.elevation = 0f
     /* val width_100dp = (100 * density + 0.5f).toInt()
      val layoutParams = play.layoutParams as ViewGroup.MarginLayoutParams
      layoutParams?.height = width_100dp
      layoutParams?.width = width_100dp //ViewGroup.LayoutParams.MATCH_PARENT
      play.layoutParams = layoutParams*/
      val width_4dp = (4 * density + 0.5f).toInt()
      play.setPadding(width_4dp,width_4dp,width_4dp,width_4dp)
    }
  }

  private fun setupClicks() {
    binding.play.setOnClickListener { this.viewModel.playPause() }
    binding.rewind.setOnClickListener { this.viewModel.rewind() }
    binding.fastForward.setOnClickListener { this.viewModel.fastForward() }
    binding.rewindTime.setOnClickListener { this.viewModel.rewind() }
    binding.fastForwardTime.setOnClickListener { this.viewModel.fastForward() }
    binding.playedTime.setOnClickListener { launchJumpToPositionDialog() }
    binding.previous.setOnClickListener { this.viewModel.previous() }
    binding.next.setOnClickListener { this.viewModel.next() }
    binding.currentChapterContainer.setOnClickListener {
      SelectChapterDialog(bookId).showDialog(router)
    }
    binding.contentList.setOnClickListener {
      SelectChapterDialog(bookId).showDialog(router)
    }

    val detector = GestureDetectorCompat(activity, object : GestureDetector.SimpleOnGestureListener() {
      override fun onDoubleTap(e: MotionEvent?): Boolean {
        viewModel.playPause()
        return true
      }
    })
    binding.cover.isClickable = true
    @Suppress("ClickableViewAccessibility")
    binding.cover.setOnTouchListener { _, event ->
      detector.onTouchEvent(event)
    }
  }

  private fun setupSlider() {
    binding.slider.setLabelFormatter {
      formatTime(it.toLong(), binding.slider.valueTo.toLong())
    }
    binding.slider.addOnChangeListener { slider, value, fromUser ->
      if (isAttached && !fromUser) {
        binding.playedTime.text = formatTime(value.toLong(), slider.valueTo.toLong())
      }
    }
    binding.slider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
      override fun onStartTrackingTouch(slider: Slider) {
      }

      override fun onStopTrackingTouch(slider: Slider) {
        val progress = slider.value.toLong()
        this@BookPlayController.viewModel.seekTo(progress)
      }
    })
  }

  private fun setupSliderVolume() {
    val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
    val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
   /* binding.sliderVolume.setLabelFormatter {
      audioManager.getStreamVolume(AudioManager.STREAM_MUSIC).toString()
    }*/
    // действие при изменение значения
    binding.sliderVolume.addOnChangeListener { sliderVolume, _, _ ->
      audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, sliderVolume.value.toInt(), 0)
    }
    // действие при прикосновении и остановке
    binding.sliderVolume.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
      override fun onStartTrackingTouch(sliderVolume: Slider) {
      }

      override fun onStopTrackingTouch(sliderVolume: Slider) {
        // audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, sliderVolume.value.toInt(), 0)
      }
    })
  }

  private fun setupToolbar() {
    val menu = binding.toolbar.menu

    sleepTimerItem = menu.findItem(R.id.action_sleep)
    timeLapseItem = menu.findItem(R.id.action_time_lapse)

    val equalizerItem = menu.findItem(R.id.action_equalizer)
    equalizerItem.isVisible = equalizer.exists

    skipSilenceItem = menu.findItem(R.id.action_skip_silence)
    showChapterNumbers = menu.findItem(R.id.show_chapter_numbers)
    repeatModeItem = menu.findItem(R.id.repeat_mode)

    binding.toolbar.findViewById<View>(R.id.action_bookmark)
      .setOnLongClickListener {
        this.viewModel.addBookmark()
        true
      }

    binding.toolbar.setNavigationOnClickListener { router.popController(this) }
    binding.toolbar.setOnMenuItemClickListener {
      when (it.itemId) {
        R.id.action_settings -> {
          val transaction = SettingsController().asTransaction()
          router.pushController(transaction)
          true
        }
        R.id.action_time_change -> {
          launchJumpToPositionDialog()
          true
        }
        R.id.action_sleep -> {
          this.viewModel.toggleSleepTimer()
          true
        }
        R.id.action_time_lapse -> {
          PlaybackSpeedDialogController().showDialog(router)
          true
        }
        R.id.action_bookmark -> {
          val bookmarkController = BookmarkController(bookId)
            .asTransaction()
          router.pushController(bookmarkController)
          true
        }
        R.id.action_equalizer -> {
          equalizer.launch(activity!!)
          true
        }
        R.id.action_skip_silence -> {
          this.viewModel.toggleSkipSilence()
          true
        }
        R.id.show_chapter_numbers -> {
          this.viewModel.toggleShowChapterNumbers()
          true
        }
        R.id.loudness -> {
          LoudnessDialog(bookId).showDialog(router)
          true
        }
        R.id.repeat_mode -> {
          this.viewModel.toggleRepeat()
          true
        }
        else -> false
      }
    }
  }

  private fun launchJumpToPositionDialog() {
    JumpToPositionDialogController().showDialog(router)
  }

  private fun showLeftSleepTime(binding: BookPlayBinding, duration: Duration) {
    val active = duration > Duration.ZERO
    sleepTimerItem.setIcon(if (active) R.drawable.alarm_off else R.drawable.alarm)
    //binding.timerCountdownView.text = formatTime(duration.toLongMilliseconds(), duration.toLongMilliseconds())
    binding.timerCountdownView.text = if (duration == 1000.minutes) {
      context.getString(R.string.end_of_current_chapter)
    } else formatTime(duration.toLongMilliseconds(), duration.toLongMilliseconds())
    binding.timerCountdownView.isVisible = active
  }

  private fun showRepeatMode(repeatMode: Int) {
    repeatModeItem.setIcon(if (repeatMode==1) R.drawable.ic_repeat_one else if (repeatMode==2) R.drawable.ic_repeat_all else R.drawable.ic_repeat_off)
  }

  private fun openSleepTimeDialog() {
    //SleepTimerDialogController(bookId)
    SleepTimerListDialogController(bookId)
      .showDialog(router)
  }

  fun formatTimeMinutes(timeMs: Long, durationMs: Long = 0): String {
    val m = timeMs.minutes()
    val largerTime = maxOf(timeMs, durationMs)
    val hourDigits = largerTime.hours().digits()
    val hour = context.getString(R.string.hours_letter)
    val min = context.getString(R.string.minutes_letter)
    return if (hourDigits > 0) {
      val h = timeMs.hours()
      "%1\$0${hourDigits}d %2\$s %3\$02d %4\$s".format(h, hour, m, min)
    } else {
      val minuteDigits = largerTime.minutes().digits()
      val pattern = if (minuteDigits == 0) {
        "%1\$d %2\$s"
      } else {
        "%1\$0${minuteDigits}d %2\$s"
      }
      pattern.format(m, min)
    }
  }
}
