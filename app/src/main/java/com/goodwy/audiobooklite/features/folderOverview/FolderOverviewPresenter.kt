package com.goodwy.audiobooklite.features.folderOverview

import de.paulwoitaschek.flowpref.Pref
import com.goodwy.audiobooklite.common.pref.PrefKeys
import com.goodwy.audiobooklite.features.folderChooser.FolderChooserActivity
import com.goodwy.audiobooklite.injection.appComponent
import com.goodwy.audiobooklite.mvp.Presenter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

/**
 * The presenter for [FolderOverviewController]
 */
class FolderOverviewPresenter : Presenter<FolderOverviewController>() {

  init {
    appComponent.inject(this)
  }

  @field:[Inject Named(PrefKeys.SINGLE_BOOK_FOLDERS)]
  lateinit var singleBookFolderPref: Pref<Set<String>>
  @field:[Inject Named(PrefKeys.COLLECTION_BOOK_FOLDERS)]
  lateinit var collectionBookFolderPref: Pref<Set<String>>
  @field:[Inject Named(PrefKeys.LIBRARY_BOOK_FOLDERS)]
  lateinit var libraryBookFolderPref: Pref<Set<String>>

  override fun onAttach(view: FolderOverviewController) {
    val collectionFolderStream = collectionBookFolderPref.flow
      .map { set -> set.map { FolderModel(it, FolderChooserActivity.OperationMode.COLLECTION_BOOK) } }
    val singleFolderStream = singleBookFolderPref.flow
      .map { set -> set.map { FolderModel(it, FolderChooserActivity.OperationMode.SINGLE_BOOK) } }
    val libraryFolderStream = libraryBookFolderPref.flow
      .map { set -> set.map { FolderModel(it, FolderChooserActivity.OperationMode.LIBRARY_BOOK) } }

    onAttachScope.launch {
      combine(collectionFolderStream, singleFolderStream, libraryFolderStream) { t1, t2, t3 -> t1 + t2 + t3}
        .collect { view.newData(it) }
    }
  }

  /** removes a selected folder **/
  fun removeFolder(folder: FolderModel) {
    scope.launch {
      val folders = collectionBookFolderPref.flow.first().toMutableSet()
      val removed = folders.remove(folder.folder)
      if (removed) collectionBookFolderPref.value = folders
    }

    scope.launch {
      val folders = singleBookFolderPref.flow.first().toMutableSet()
      val removed = folders.remove(folder.folder)
      if (removed) singleBookFolderPref.value = folders
    }

    scope.launch {
      val folders = libraryBookFolderPref.flow.first().toMutableSet()
      val removed = folders.remove(folder.folder)
      if (removed) libraryBookFolderPref.value = folders
    }
  }
}
