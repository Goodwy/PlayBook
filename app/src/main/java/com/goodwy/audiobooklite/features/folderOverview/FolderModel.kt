package com.goodwy.audiobooklite.features.folderOverview

import com.goodwy.audiobooklite.common.comparator.NaturalOrderComparator
import com.goodwy.audiobooklite.features.folderChooser.FolderChooserActivity
import java.io.File

data class FolderModel(val folder: String, val isCollection: FolderChooserActivity.OperationMode) : Comparable<FolderModel> {
  override fun compareTo(other: FolderModel): Int {
    val isCollectionCompare = other.isCollection.compareTo(isCollection)
    if (isCollectionCompare != 0) return isCollectionCompare

    return NaturalOrderComparator.fileComparator.compare(File(folder), File(other.folder))
  }
}
