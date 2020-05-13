package com.goodwy.audiobook.features.folderOverview

import com.goodwy.audiobook.common.comparator.NaturalOrderComparator
import java.io.File

data class FolderModel(val folder: String, val isCollection: Boolean) : Comparable<FolderModel> {
  override fun compareTo(other: FolderModel): Int {
    val isCollectionCompare = other.isCollection.compareTo(isCollection)
    if (isCollectionCompare != 0) return isCollectionCompare

    return NaturalOrderComparator.fileComparator.compare(File(folder), File(other.folder))
  }
}
