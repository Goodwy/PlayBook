package com.goodwy.audiobook.features.bookOverview

import android.app.Dialog
import android.os.Bundle
import android.text.InputType
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.goodwy.audiobook.R
import com.goodwy.audiobook.data.Book
import com.goodwy.audiobook.data.repo.BookRepository
import com.goodwy.audiobook.injection.appComponent
import com.goodwy.audiobook.misc.DialogController
import com.goodwy.audiobook.misc.getUUID
import com.goodwy.audiobook.misc.putUUID
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val NI_PRESET_NAME = "niPresetName"
private const val NI_BOOK_ID = "niBookId"

class EditBookTitleDialogController(args: Bundle) : DialogController(args) {

  constructor(book: Book) : this(Bundle().apply {
    putString(NI_PRESET_NAME, book.name)
    putUUID(NI_BOOK_ID, book.id)
  })

  @Inject
  lateinit var repo: BookRepository

  override fun onCreateDialog(savedViewState: Bundle?): Dialog {
    appComponent.inject(this)

    val presetName = args.getString(NI_PRESET_NAME)
    val bookId = args.getUUID(NI_BOOK_ID)

    return MaterialDialog(activity!!).apply {
      title(R.string.edit_book_title)
	  cornerRadius(4f)
      val inputType = InputType.TYPE_CLASS_TEXT or
          InputType.TYPE_TEXT_FLAG_CAP_SENTENCES or InputType.TYPE_TEXT_FLAG_AUTO_CORRECT
      input(
        inputType = inputType,
        hintRes = R.string.change_book_name,
        prefill = presetName
      ) { _, text ->
        val newText = text.toString()
        if (newText != presetName) {
          GlobalScope.launch {
            repo.updateBookName(bookId, newText)
          }
        }
        positiveButton(R.string.dialog_confirm)
      }
      negativeButton(R.string.dialog_cancel)
    }
  }
}
