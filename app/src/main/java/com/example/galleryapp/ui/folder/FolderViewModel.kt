package com.example.galleryapp.ui.folder

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.galleryapp.data.IGalleryRepository
import com.example.galleryapp.model.PhotoFolder
import com.example.galleryapp.util.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class FolderViewModel(
    private val context: Application,
    private val repository: IGalleryRepository
) :
    ViewModel() {

    val imagesObs = SingleLiveEvent<List<PhotoFolder>>()

    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    fun loadImages() {
        scope.launch {
            val listOfImages = repository.listOfImages(context = context)
            imagesObs.postValue(listOfImages)
        }
    }
}