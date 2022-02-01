package com.goodwy.audiobook.features.bookPlaying

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.text.format.DateUtils
import android.widget.FrameLayout
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.goodwy.audiobook.R
import com.goodwy.audiobook.common.pref.PrefKeys
import com.goodwy.audiobook.data.repo.BookRepository
import com.goodwy.audiobook.data.repo.BookmarkRepo
import com.goodwy.audiobook.databinding.DialogSleepListBinding
import com.goodwy.audiobook.databinding.DialogSleepModernBinding
import com.goodwy.audiobook.injection.appComponent
import com.goodwy.audiobook.misc.DialogController
import com.goodwy.audiobook.misc.conductor.context
import com.goodwy.audiobook.misc.getUUID
import com.goodwy.audiobook.misc.putUUID
import com.goodwy.audiobook.playback.ShakeDetector
import com.goodwy.audiobook.playback.SleepTimer
import de.paulwoitaschek.flowpref.Pref
import kotlinx.android.synthetic.main.dialog_sleep_modern.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.util.UUID
import javax.inject.Inject
import javax.inject.Named

private const val NI_BOOK_ID = "ni#bookId"
private const val SI_MINUTES = "si#time"

/**
 * Simple dialog for activating the sleep timer
 */
class SleepTimerListDialogController(bundle: Bundle) : DialogController(bundle) {

  constructor(bookId: UUID) : this(Bundle().apply {
    putUUID(NI_BOOK_ID, bookId)
  })

  @Inject
  lateinit var bookmarkRepo: BookmarkRepo

  @Inject
  lateinit var sleepTimer: SleepTimer

  @Inject
  lateinit var repo: BookRepository

  @Inject
  lateinit var shakeDetector: ShakeDetector

  @field:[Inject Named(PrefKeys.SLEEP_TIME)]
  lateinit var sleepTimePref: Pref<Int>

  @field:[Inject Named(PrefKeys.SLEEP_TIMER_CURRENT_CHAPTER)]
  lateinit var sleepTimeCurrentChapterPref: Pref<Int>

  @field:[Inject Named(PrefKeys.SHAKE_TO_RESET)]
  lateinit var shakeToResetPref: Pref<Boolean>

  private var selectedMinutes = 0

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putInt(SI_MINUTES, selectedMinutes)
  }

  override fun onCreateDialog(savedViewState: Bundle?): Dialog {
    appComponent.inject(this)

    val binding = DialogSleepListBinding.inflate(activity!!.layoutInflater)

    /*fun updateTimeState() {
      binding.time.text = context.getString(R.string.min, selectedMinutes.toString())

      if (selectedMinutes > 0) binding.fab.show()
      else binding.fab.hide()
    }*/

    @SuppressLint("SetTextI18n")
    fun appendNumber(number: Int) {
		val newNumber = selectedMinutes * 10 + number
		if (newNumber > 999) return
		selectedMinutes = newNumber
		//updateTimeState()
	}

   // selectedMinutes = savedViewState?.getInt(SI_MINUTES) ?: sleepTimePref.value
   // updateTimeState()

    val bookId = args.getUUID(NI_BOOK_ID)
    val book = repo.bookById(bookId)!!
    val title = book.content.currentChapter.name
    val currentChapter = book.content.currentChapterIndex
    val date = DateUtils.formatDateTime(
      context,
      System.currentTimeMillis(),
      DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_NUMERIC_DATE
    )

    if (book.content.nextChapter == null)  {binding.endOfCurrentChapter.isGone = true}
    binding.x5.text = context.getString(R.string.min, "5")
    binding.x10.text = context.getString(R.string.min, "10")
    binding.x20.text = context.getString(R.string.min, "20")
    binding.x30.text = context.getString(R.string.min, "30")
    binding.x45.text = context.getString(R.string.min, "45")
    binding.x60.text = context.getString(R.string.min, "60")

    fun startSleepTimerEndOfCurrentChapter() {
      sleepTimeCurrentChapterPref.value = currentChapter
      GlobalScope.launch(Dispatchers.IO) {
        bookmarkRepo.addBookmarkAtBookPosition(
          book = book,
          setBySleepTimer = true,
          title = date + ", " + title
        )
      }
      sleepTimer.setActive(true, false)
      dismissDialog()
    }
    fun startSleepTimer(time: Int) {
      sleepTimePref.value = time
      GlobalScope.launch(Dispatchers.IO) {
        bookmarkRepo.addBookmarkAtBookPosition(
          book = book,
          setBySleepTimer = true,
          title = date + ", " + title
        )
      }
      sleepTimer.setActive(true, true)
      dismissDialog()
    }
    binding.shakeToResetSwitch.setOnClickListener {
      shakeToResetPref.value = binding.shakeToResetSwitch.isChecked
    }
    /*binding.toggleButton.chip_off.setOnClickListener {
      shakeToResetPref.value = false
      binding.toggleButton.chip_off.alpha = 1.0f
      binding.toggleButton.chip_on.alpha = 0.1f
      //binding.toggleButton.chip_off.setChipBackgroundColorResource(R.color.primary)
      //binding.toggleButton.chip_on.setChipBackgroundColorResource(R.color.cyanea_primary_reference)
    }
    binding.toggleButton.chip_on.setOnClickListener {
      shakeToResetPref.value = true
      binding.toggleButton.chip_on.alpha = 1.0f
      binding.toggleButton.chip_off.alpha = 0.1f
      //binding.toggleButton.chip_on.setChipBackgroundColorResource(R.color.primary)
      //binding.toggleButton.chip_off.setChipBackgroundColorResource(R.color.cyanea_primary_reference)
    }*/
    binding.x5.setOnClickListener {
      startSleepTimer(5)
    }
    binding.x10.setOnClickListener {
      startSleepTimer(10)
    }
    binding.x20.setOnClickListener {
      startSleepTimer(20)
    }
    binding.x30.setOnClickListener {
      startSleepTimer(30)
    }
    binding.x45.setOnClickListener {
      startSleepTimer(45)
    }
    binding.x60.setOnClickListener {
      startSleepTimer(60)
    }
    binding.endOfCurrentChapter.setOnClickListener {
      startSleepTimerEndOfCurrentChapter()
    }
    binding.custom.setOnClickListener {
      dismissDialog()
      SleepTimerDialogController(bookId)
        .showDialog(router)
    }
    binding.cancel.setOnClickListener {
      dismissDialog()
    }

    // setup shake to reset setting
    binding.shakeToResetSwitch.isChecked = shakeToResetPref.value
    val shakeSupported = shakeDetector.shakeSupported()
    if (!shakeSupported) {
      binding.shakeToResetSwitch.isVisible = false
      //binding.shakeToResetSwitchText.isVisible = false
      //binding.toggleButton.isVisible = false
      binding.note.isVisible = false
    }
    /*if (shakeToResetPref.value) {
      binding.toggleButton.chip_on.alpha = 1.0f
      binding.toggleButton.chip_off.alpha = 0.1f
    } else {
      binding.toggleButton.chip_on.alpha = 0.1f
      binding.toggleButton.chip_off.alpha = 1.0f
    }*/

    return BottomSheetDialog(context).apply {
      setContentView(binding.root)
      // hide the background so the fab looks overlapping
      setOnShowListener {
        val parentView = binding.root.parent as View
        parentView.background = null
        val coordinator = findViewById<FrameLayout>(R.id.design_bottom_sheet)!!
        val behavior = BottomSheetBehavior.from(coordinator)
       // behavior.peekHeight = binding.time.bottom
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
      }
    }
  }
}
