package com.example.galleryapp.ui.video

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.galleryapp.data.IGalleryRepository
import com.example.galleryapp.model.Photo
import com.example.galleryapp.ui.adapter.GalleryAdapter
import com.example.galleryapp.util.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class VideoListViewModel(
    private val context: Application,
    private val repository: IGalleryRepository
) :
    ViewModel() {

    val imagesObs = SingleLiveEvent<List<GalleryAdapter.GalleryObject>>()

    private var folderName: String? = null

    private var listOfFolderImages: ArrayList<String>? = null

    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    fun loadImages(folderName: String?) {
        this.folderName = folderName
        scope.launch {
            listOfFolderImages = repository.listOfVideos(context = context)
            imagesObs.postValue(listOfFolderImages?.map {
                GalleryAdapter.GalleryObject.PhotoItem(url = it, "")
            }.orEmpty())
        }
    }
}