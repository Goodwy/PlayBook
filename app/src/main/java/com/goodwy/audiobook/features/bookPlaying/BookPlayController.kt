package com.goodwy.audiobook.features.bookPlaying

import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import android.view.GestureDetector
import android.view.KeyEvent
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject
import javax.inject.Named
import kotlin.time.Duration
import kotlin.time.seconds


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

  @field:[Inject Named(PrefKeys.CONTENTS_BUTTON_MODE)]
  lateinit var contentsButtonMode: Pref<Boolean>

  @field:[Inject Named(PrefKeys.SHOW_SLIDER_VOLUME)]
  lateinit var showSliderVolumePref: Pref<Boolean>

  @field:[Inject Named(PrefKeys.SEEK_TIME)]
  lateinit var seekTimePref: Pref<Int>

  @field:[Inject Named(PrefKeys.SEEK_TIME_REWIND)]
  lateinit var seekTimeRewindPref: Pref<Int>

  @field:[Inject Named(PrefKeys.ICON_MODE)]
  lateinit var iconModePref: Pref<Boolean>

  private val bookId = bundle.getUUID(NI_BOOK_ID)
  private var coverLoaded = false

  private var sleepTimerItem: MenuItem by clearAfterDestroyView()
  private var skipSilenceItem: MenuItem by clearAfterDestroyView()
  private var showChapterNumbers: MenuItem by clearAfterDestroyView()

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

  private fun BookPlayBinding.render(viewState: BookPlayViewState) {
    Timber.d("render $viewState")
/*    toolbar.title = viewState.title*/ /*Имя книги в заголовке плеера*/
    currentBookText.text = viewState.bookName
    currentChapterText.text = viewState.chapterName
    currentChapterContainer.isVisible = viewState.chapterName != null
    contentList.isInvisible = true /* = contentsButtonMode.value*/
    previous.isVisible = viewState.showPreviousNextButtons
    next.isVisible = viewState.showPreviousNextButtons
    playedTime.text = formatTime(viewState.playedTime.toLongMilliseconds(), viewState.duration.toLongMilliseconds())
    maxTime.text = formatTime(viewState.duration.toLongMilliseconds(), viewState.duration.toLongMilliseconds())
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
    bottomContainer.isVisible = !showSliderVolumePref.value
    sliderVolume.valueTo = maxVolume.toFloat()
    if (!slider.isPressed) {
      sliderVolume.value = currentVolume.toFloat()
    }
    skipSilenceItem.isChecked = viewState.skipSilence
    showChapterNumbers.isChecked = viewState.showChapterNumbers
    playPauseDrawableSetter.setPlaying(viewState.playing)
    showLeftSleepTime(this, viewState.sleepTime)

    rewind.isVisible = viewState.rewindButtonStylePref == 1
    fastForward.isVisible = viewState.rewindButtonStylePref == 1
    rewindTime.isVisible = viewState.rewindButtonStylePref == 2
    fastForwardTime.isVisible = viewState.rewindButtonStylePref == 2
    seekRewindTimeText.isVisible = viewState.rewindButtonStylePref == 2
    fastForwardTimeText.isVisible = viewState.rewindButtonStylePref == 2

    seekRewindTimeText.text = formatTimeSeconds(seekTimeRewind.toLongMilliseconds())
    fastForwardTimeText.text = formatTimeSeconds(seekTime.toLongMilliseconds())

    if (!coverLoaded) {
      coverLoaded = true
      cover.transitionName = viewState.cover.coverTransitionName()
        val coverFile = viewState.cover.file()
        val placeholder = viewState.cover.placeholder(activity!!)
        if (coverFile == null) {
          Picasso.get().cancelRequest(cover)
          cover.setImageDrawable(placeholder)
        } else {
          Picasso.get().load(coverFile).placeholder(placeholder).into(cover)
      }
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
    binding.sliderVolume.addOnChangeListener { sliderVolume, value, fromUser ->
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

  fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
    when (keyCode) {
      KeyEvent.KEYCODE_VOLUME_UP -> {
        event.startTracking()
        return true
      }
      KeyEvent.KEYCODE_VOLUME_DOWN -> {
        Toast.makeText(applicationContext, "Нажата кнопка громкости", Toast.LENGTH_SHORT)
          .show()
        return false
      }
    }
    return onKeyDown(keyCode, event)
  }

  private fun setupToolbar() {
    val menu = binding.toolbar.menu

    sleepTimerItem = menu.findItem(R.id.action_sleep)
    val equalizerItem = menu.findItem(R.id.action_equalizer)
    equalizerItem.isVisible = equalizer.exists

    skipSilenceItem = menu.findItem(R.id.action_skip_silence)
    showChapterNumbers = menu.findItem(R.id.show_chapter_numbers)

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
    binding.timerCountdownView.text = formatTime(duration.toLongMilliseconds(), duration.toLongMilliseconds())
    binding.timerCountdownView.isVisible = active
  }

  private fun openSleepTimeDialog() {
    SleepTimerDialogController(bookId)
      .showDialog(router)
  }
}
