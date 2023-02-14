package voice.sleepTimer

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.squareup.anvil.annotations.ContributesTo
import voice.common.AppScope
import voice.common.BookId
import voice.common.conductor.DialogController
import voice.common.rootComponentAs
import voice.data.getBookId
import voice.data.putBookId
import voice.sleepTimer.databinding.DialogSleepListBinding
import javax.inject.Inject

private const val NI_BOOK_ID = "ni#bookId"

class SleepTimerListDialogController(bundle: Bundle) : DialogController(bundle) {

  constructor(bookId: BookId) : this(
    Bundle().apply {
      putBookId(NI_BOOK_ID, bookId)
    },
  )

  @Inject
  lateinit var viewModel: SleepTimerDialogViewModel

  init {
    rootComponentAs<Component>().inject(this)
  }

  override fun onCreateDialog(savedViewState: Bundle?): Dialog {
    val binding = DialogSleepListBinding.inflate(activity!!.layoutInflater)

    //if (book.content.nextChapter == null)  {binding.endOfCurrentChapter.isGone = true}
    binding.x5.text = activity!!.getString(R.string.min, "5")
    binding.x10.text = activity!!.getString(R.string.min, "10")
    binding.x20.text = activity!!.getString(R.string.min, "20")
    binding.x30.text = activity!!.getString(R.string.min, "30")
    binding.x45.text = activity!!.getString(R.string.min, "45")
    binding.x60.text = activity!!.getString(R.string.min, "60")

    binding.x5.setOnClickListener {
      viewModel.onTimeClicked(5)
      startSleepTimer()
    }
    binding.x10.setOnClickListener {
      viewModel.onTimeClicked(10)
      startSleepTimer()
    }
    binding.x20.setOnClickListener {
      viewModel.onTimeClicked(20)
      startSleepTimer()
    }
    binding.x30.setOnClickListener {
      viewModel.onTimeClicked(30)
      startSleepTimer()
    }
    binding.x45.setOnClickListener {
      viewModel.onTimeClicked(45)
      startSleepTimer()
    }
    binding.x60.setOnClickListener {
      viewModel.onTimeClicked(60)
      startSleepTimer()
    }
    binding.custom.setOnClickListener {
      dismissDialog()
      SleepTimerDialogController(args.getBookId(NI_BOOK_ID)!!)
        .showDialog(router)
    }
    binding.endOfCurrentChapter.setOnClickListener {
      viewModel.onConfirmEocButtonClicked(args.getBookId(NI_BOOK_ID)!!)
      dismissDialog()
    }
    binding.cancel.setOnClickListener {
      dismissDialog()
    }

    return BottomSheetDialog(activity!!).apply {
      setContentView(binding.root)
      // hide the background so the fab appears overlapping
      setOnShowListener {
        val parentView = binding.root.parent as View
        parentView.background = null
        val coordinator = findViewById<FrameLayout>(R.id.design_bottom_sheet)!!
        val behavior = BottomSheetBehavior.from(coordinator)
        //behavior.peekHeight = binding.time.bottom
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
      }
    }
  }

  private fun startSleepTimer() {
    viewModel.onConfirmButtonClicked(args.getBookId(NI_BOOK_ID)!!)
    dismissDialog()
  }

  @ContributesTo(AppScope::class)
  interface Component {
    fun inject(target: SleepTimerListDialogController)
  }
}
