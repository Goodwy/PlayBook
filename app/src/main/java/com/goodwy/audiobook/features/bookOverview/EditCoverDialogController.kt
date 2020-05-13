package com.goodwy.audiobook.features.bookOverview

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.core.view.isVisible
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.getActionButton
import com.afollestad.materialdialogs.customview.customView
import com.bluelinelabs.conductor.Controller
import com.squareup.picasso.Picasso
import com.goodwy.audiobook.R
import com.goodwy.audiobook.common.ImageHelper
import com.goodwy.audiobook.data.repo.BookRepository
import com.goodwy.audiobook.databinding.DialogCoverEditBinding
import com.goodwy.audiobook.injection.appComponent
import com.goodwy.audiobook.misc.DialogController
import com.goodwy.audiobook.misc.coverFile
import com.goodwy.audiobook.uitools.CropTransformation
import com.goodwy.audiobook.uitools.SimpleTarget
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject
import com.squareup.picasso.Callback as PicassoCallback

/**
 * Simple dialog to edit the cover of a book.
 */
class EditCoverDialogController(bundle: Bundle) : DialogController(bundle) {

  @Inject
  lateinit var repo: BookRepository
  @Inject
  lateinit var imageHelper: ImageHelper

  @SuppressLint("InflateParams")
  override fun onCreateDialog(savedViewState: Bundle?): Dialog {
    appComponent.inject(this)

    val picasso = Picasso.get()

    val binding = DialogCoverEditBinding.inflate(activity!!.layoutInflater)
    // init values
    val arguments = args.getParcelable<Arguments>(NI_ARGS)!!
    val book = repo.bookById(arguments.bookId)!!

    binding.coverReplacement.isVisible = true
    binding.cropOverlay.selectionOn = false
    picasso.load(arguments.coverUri)
      .into(
        binding.coverImage, object : PicassoCallback {
          override fun onError(e: Exception?) {
            dismissDialog()
          }

          override fun onSuccess() {
            binding.cropOverlay.selectionOn = true
            binding.coverReplacement.isVisible = false
          }
        }
      )

    val dialog = MaterialDialog(activity!!).apply {
      customView(view = binding.root)
      title(R.string.cover)
	  cornerRadius(16f)
      positiveButton(R.string.dialog_confirm)
    }

    // use a click listener so the dialog stays open till the image was saved
    dialog.getActionButton(WhichButton.POSITIVE).setOnClickListener {
      val r = binding.cropOverlay.selectedRect
      if (!r.isEmpty) {
        val target = object : SimpleTarget {
          override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom?) {
            GlobalScope.launch(Dispatchers.Main) {
              val coverFile = book.coverFile()
              imageHelper.saveCover(bitmap, coverFile)
              picasso.invalidate(coverFile)
              val callback = targetController as Callback
              callback.onBookCoverChanged(book.id)
              dismissDialog()
            }
          }

          override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
            dismissDialog()
          }
        }
        // picasso only holds a weak reference so we have to protect against gc
        binding.coverImage.tag = target
        picasso.load(arguments.coverUri)
          .transform(CropTransformation(binding.cropOverlay, binding.coverImage))
          .into(target)
      } else dismissDialog()
    }
    return dialog
  }

  interface Callback {
    fun onBookCoverChanged(bookId: UUID)
  }

  companion object {
    private const val NI_ARGS = "ni#bundle"

    operator fun <T> invoke(target: T, args: Arguments): EditCoverDialogController where T : Controller, T : Callback {
      val bundle = Bundle().apply {
        putParcelable(NI_ARGS, args)
      }
      return EditCoverDialogController(bundle).apply {
        targetController = target
      }
    }
  }

  @Parcelize
  data class Arguments(
    val coverUri: Uri,
    val bookId: UUID
  ) : Parcelable
}
