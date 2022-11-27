package com.example.galleryapp.ui.detail

import androidx.lifecycle.ViewModel
import com.example.galleryapp.data.IGalleryRepository
import com.example.galleryapp.util.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PhotoViewModel(private val repository: IGalleryRepository) :
    ViewModel() {

    val photoFavoriteObs = SingleLiveEvent<Boolean>()

    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    fun isFavorite(photoUrl: String, folderName: String) {
        scope.launch {
            val isFavorite = repository.isFavorite(photoUrl, folderName)
            photoFavoriteObs.postValue(isFavorite)
        }
    }

    fun addFavorite(photoUrl: String, folderName: String, isAdd: Boolean) {
        scope.launch {
            repository.addFavorite(photoUrl, folderName, isAdd)
        }
    }
}