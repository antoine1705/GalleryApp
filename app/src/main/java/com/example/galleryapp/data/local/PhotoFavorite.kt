package com.example.galleryapp.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PhotoFavorite(
    @ColumnInfo(name = "folder_name") val folderName: String?,
    @PrimaryKey @ColumnInfo(name = "url") val url: String
)
