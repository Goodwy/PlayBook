package com.goodwy.audiobooklite.features.folderOverview

import android.view.ViewGroup
import com.goodwy.audiobooklite.R
import com.goodwy.audiobooklite.features.folderChooser.FolderChooserActivity
import com.goodwy.audiobooklite.uitools.ExtensionsHolder
import kotlinx.android.synthetic.main.activity_folder_overview_row_layout.*

class FolderOverviewHolder(
  parent: ViewGroup,
  itemClicked: (position: Int) -> Unit
) : ExtensionsHolder(parent, R.layout.activity_folder_overview_row_layout) {

  private val context = parent.context

  init {
    remove.setOnClickListener {
      if (adapterPosition != -1) {
        itemClicked(adapterPosition)
      }
    }
  }

  fun bind(model: FolderModel) {
    // set text
    textView.text = model.folder

    // set correct image
    val drawableId =
      if (model.isCollection == FolderChooserActivity.OperationMode.COLLECTION_BOOK) R.drawable.folder_multiple else if (model.isCollection == FolderChooserActivity.OperationMode.SINGLE_BOOK) R.drawable.ic_folder else R.drawable.folder_libraries
    icon.setImageResource(drawableId)

    // set content description
    val contentDescriptionId =
      if (model.isCollection == FolderChooserActivity.OperationMode.COLLECTION_BOOK) R.string.folder_add_collection else if (model.isCollection == FolderChooserActivity.OperationMode.SINGLE_BOOK) R.string.folder_add_single_book else R.string.folder_add_library_book
    val contentDescription = itemView.context.getString(contentDescriptionId)
    icon.contentDescription = contentDescription
  }
}
