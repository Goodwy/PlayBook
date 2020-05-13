package com.goodwy.audiobook.features.folderChooser

import android.view.ViewGroup
import com.goodwy.audiobook.R
import com.goodwy.audiobook.uitools.ExtensionsHolder
import kotlinx.android.synthetic.main.activity_folder_chooser_adapter_row_layout.*
import java.io.File

class Holder(
  parent: ViewGroup,
  private val mode: FolderChooserActivity.OperationMode,
  private val listener: (selected: File) -> Unit
) : ExtensionsHolder(parent, R.layout.activity_folder_chooser_adapter_row_layout) {

  private var boundFile: File? = null

  init {
    itemView.setOnClickListener {
      boundFile?.let(listener)
    }
  }

  fun bind(selectedFile: File) {
    boundFile = selectedFile
    val isDirectory = selectedFile.isDirectory

    text.text = selectedFile.name

    // if its not a collection its also fine to pick a file
    if (mode == FolderChooserActivity.OperationMode.COLLECTION_BOOK) {
      text.isEnabled = isDirectory
    }

    val context = itemView.context
    val drawableRes = if (isDirectory) R.drawable.ic_folder else R.drawable.ic_album
    icon.setImageResource(drawableRes)
    icon.contentDescription = context.getString(
        if (isDirectory) {
          R.string.content_is_folder
        } else R.string.content_is_file
    )
  }
}
