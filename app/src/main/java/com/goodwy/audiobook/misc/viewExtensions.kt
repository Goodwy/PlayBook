package com.goodwy.audiobook.misc

import android.annotation.SuppressLint
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.SeekBar
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun SeekBar.onProgressChanged(
  initialNotification: Boolean = false,
  progressChanged: (Int) -> Unit
) {
  val listener = object : SeekBar.OnSeekBarChangeListener {
    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
      progressChanged(progress)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
    }
  }

  setOnSeekBarChangeListener(listener)
  if (initialNotification) listener.onProgressChanged(this, progress, false)
}

fun SeekBar.progressChangedStream(): Flow<Int> {
  return callbackFlow {
    onProgressChanged(initialNotification = true) { position ->
      offer(position)
    }
    awaitClose {
      setOnSeekBarChangeListener(null)
    }
  }
}

@SuppressLint("ClickableViewAccessibility")
inline fun <T : Adapter> AdapterView<T>.itemSelections(crossinline listener: (Int) -> Unit) {
  // add an onTouchListener to check if it's really user input
  var isUserSelection = false
  setOnTouchListener { _, _ ->
    isUserSelection = true
    false
  }
  onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
      // only fire the listener if it was user input
      if (isUserSelection) {
        isUserSelection = false
        listener(position)
      }
    }
  }
}

/** if the recyclerview is computing layout, post the action. else just execute it */
inline fun RecyclerView.postedIfComputingLayout(crossinline action: () -> Unit) {
  if (!isComputingLayout) action()
  else post { action() }
}
