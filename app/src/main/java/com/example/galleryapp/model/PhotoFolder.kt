package com.example.galleryapp.model

data class PhotoFolder(
    val folderName: String?,
    val photos: ArrayList<Photo> = arrayListOf()
)