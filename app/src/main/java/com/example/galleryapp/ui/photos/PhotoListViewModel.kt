package com.example.galleryapp.ui.photos

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.galleryapp.data.IGalleryRepository
import com.example.galleryapp.model.Photo
import com.example.galleryapp.model.PhotoFolder
import com.example.galleryapp.ui.adapter.GalleryAdapter
import com.example.galleryapp.util.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PhotoListViewModel(
    private val context: Application,
    private val repository: IGalleryRepository
) :
    ViewModel() {

    val imagesObs = SingleLiveEvent<List<GalleryAdapter.GalleryObject>>()

    private var folderName: String? = null

    private var listOfFolderPhotos: List<PhotoFolder>? = null

    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    fun loadImages(folderName: String?) {
        this.folderName = folderName
        scope.launch {
            listOfFolderPhotos = repository.listOfImages(context = context)
            val photos =
                listOfFolderPhotos?.firstOrNull { it.folderName == folderName }?.photos.orEmpty()
            val galleryObjects = mapToGalleryObject(photos)
            imagesObs.postValue(galleryObjects)
        }
    }

    private fun mapToGalleryObject(photos: List<Photo>): List<GalleryAdapter.GalleryObject> {
        val items = arrayListOf<GalleryAdapter.GalleryObject>()
        photos.groupBy { it.dateAdded }.toSortedMap(compareByDescending { it }).forEach {
            items.add(GalleryAdapter.GalleryObject.DateItem(it.key))
            items.addAll(it.value.map { photo ->
                GalleryAdapter.GalleryObject.PhotoItem(
                    photo.url,
                    photo.folderName
                )
            })
        }
        return items
    }
}