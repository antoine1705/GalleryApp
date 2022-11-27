package com.example.galleryapp.ui.favorite

import androidx.lifecycle.ViewModel
import com.example.galleryapp.data.IGalleryRepository
import com.example.galleryapp.model.Photo
import com.example.galleryapp.ui.adapter.GalleryAdapter
import com.example.galleryapp.util.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class FavoritePhotoListViewModel(
    private val repository: IGalleryRepository
) :
    ViewModel() {

    val photosObs = SingleLiveEvent<List<GalleryAdapter.GalleryObject.PhotoItem>>()

    private var listOfFavoritePhoto = listOf<Photo>()

    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    fun loadFavoritePhotos() {
        scope.launch {
            listOfFavoritePhoto = repository.listOfFavoritePhoto()
            photosObs.postValue(listOfFavoritePhoto.map {
                GalleryAdapter.GalleryObject.PhotoItem(
                    it.url,
                    it.folderName
                )
            })
        }
    }
}