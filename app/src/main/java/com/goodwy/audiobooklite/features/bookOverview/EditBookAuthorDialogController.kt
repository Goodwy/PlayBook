package com.goodwy.audiobooklite.features.bookOverview

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.text.InputType
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.goodwy.audiobooklite.R
import com.goodwy.audiobooklite.data.Book
import com.goodwy.audiobooklite.data.repo.BookRepository
import com.goodwy.audiobooklite.injection.appComponent
import com.goodwy.audiobooklite.misc.DialogController
import com.goodwy.audiobooklite.misc.getUUID
import com.goodwy.audiobooklite.misc.putUUID
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val NI_PRESET_AUTHOR = "niPresetAuthor"
private const val NI_BOOK_ID = "niBookId"

class EditBookAuthorDialogController(args: Bundle) : DialogController(args) {

  constructor(book: Book) : this(Bundle().apply {
    putString(NI_PRESET_AUTHOR, book.author)
    putUUID(NI_BOOK_ID, book.id)
  })

  @Inject
  lateinit var repo: BookRepository

  override fun onCreateDialog(savedViewState: Bundle?): Dialog {
    appComponent.inject(this)

    val presetAuthor = args.getString(NI_PRESET_AUTHOR)
    val bookId = args.getUUID(NI_BOOK_ID)

    return MaterialDialog(activity!!).apply {
      title(R.string.edit_book_author)
	  cornerRadius(4f)
      val inputType = InputType.TYPE_CLASS_TEXT or
          InputType.TYPE_TEXT_FLAG_CAP_SENTENCES or InputType.TYPE_TEXT_FLAG_AUTO_CORRECT
      input(
        inputType = inputType,
        hintRes = R.string.change_book_author,
        prefill = presetAuthor
      ) { _, text ->
        val newText = text.toString()
        if (newText != presetAuthor) {
          GlobalScope.launch {
            repo.updateBookAuthor(bookId, newText)
          }
        }
        positiveButton(R.string.dialog_confirm)
      }
      negativeButton(R.string.dialog_cancel)
    }
  }
}
