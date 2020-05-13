package com.goodwy.audiobook.features

import android.app.Activity
import android.content.Intent
import com.bluelinelabs.conductor.Controller
import com.goodwy.audiobook.features.bookOverview.EditCoverDialogController
import java.util.UUID
import javax.inject.Inject

private const val REQUEST_CODE = 7

class GalleryPicker
@Inject constructor() {

  private var pickForBookId: UUID? = null

  fun pick(bookId: UUID, controller: Controller) {
    pickForBookId = bookId
    val intent = Intent(Intent.ACTION_PICK)
      .setType("image/*")
    controller.startActivityForResult(intent, REQUEST_CODE)
  }

  fun parse(requestCode: Int, resultCode: Int, data: Intent?): EditCoverDialogController.Arguments? {
    if (requestCode != REQUEST_CODE) {
      return null
    }
    return if (resultCode == Activity.RESULT_OK) {
      val imageUri = data?.data
      val bookId = pickForBookId
      if (imageUri == null || bookId == null) {
        null
      } else {
        EditCoverDialogController.Arguments(imageUri, bookId)
      }
    } else {
      null
    }
  }
}
