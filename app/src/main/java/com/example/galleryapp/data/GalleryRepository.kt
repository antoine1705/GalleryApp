package com.example.galleryapp.data

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.example.galleryapp.data.local.PhotoDao
import com.example.galleryapp.data.local.PhotoFavorite
import com.example.galleryapp.model.Photo
import com.example.galleryapp.model.PhotoFolder
import com.example.galleryapp.util.toMonth
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface IGalleryRepository {
    suspend fun listOfImages(context: Context): List<PhotoFolder>
    suspend fun listOfVideos(context: Context): ArrayList<String>
    suspend fun listOfFavoritePhoto(): List<Photo>
    suspend fun addFavorite(photoUrl: String, folderName: String, isAdd: Boolean)
    suspend fun isFavorite(photoUrl: String, folderName: String): Boolean
}

class GalleryRepository(
    private val photoDao: PhotoDao,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : IGalleryRepository {

    override suspend fun listOfImages(context: Context): List<PhotoFolder> {
        return withContext(defaultDispatcher) {
            val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            var absolutePathOfImage: String
            val protection =
                arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.DATE_ADDED)

            val orderBy = MediaStore.Images.Media.DATE_TAKEN
            val cursor = context.contentResolver.query(uri, protection, null, null, "$orderBy DESC")
            val columnsIndexData = cursor?.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)

            val listFolderName = hashMapOf<String, ArrayList<Photo>>()
            val columnIndexFolderName =
                cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
            val columnIndexDateAdded =
                cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)

            while (cursor?.moveToNext() == true) {
                absolutePathOfImage = cursor.getString(columnsIndexData ?: 0)
                val absolutePathOfFolderName = cursor.getString(columnIndexFolderName ?: 0)
                val dateAdded = cursor.getString(columnIndexDateAdded ?: 0).toMonth()
                if (listFolderName.containsKey(absolutePathOfFolderName)) {
                    listFolderName[absolutePathOfFolderName]!!.add(
                        Photo(
                            url = absolutePathOfImage,
                            folderName = absolutePathOfFolderName,
                            dateAdded = dateAdded
                        )
                    )
                } else {
                    listFolderName[absolutePathOfFolderName] = arrayListOf()
                }
            }
            val listPhotoFolders = arrayListOf<PhotoFolder>()
            listFolderName.forEach {
                listPhotoFolders.add(PhotoFolder(folderName = it.key, photos = it.value))
            }
            listPhotoFolders
        }
    }

    override suspend fun listOfVideos(context: Context): ArrayList<String> {
        return withContext(defaultDispatcher) {
            val uri: Uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            var absolutePathOfVideo: String
            val protection =
                arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Video.Media.BUCKET_DISPLAY_NAME)

            val orderBy = MediaStore.Video.Media.DATE_TAKEN
            val cursor = context.contentResolver.query(uri, protection, null, null, "$orderBy DESC")
            val columnsIndexData = cursor?.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)

            val listVideos = arrayListOf<String>()
            while (cursor?.moveToNext() == true) {
                absolutePathOfVideo = cursor.getString(columnsIndexData ?: 0)
                listVideos.add(absolutePathOfVideo)
            }
            listVideos
        }
    }

    override suspend fun listOfFavoritePhoto(): List<Photo> = withContext(defaultDispatcher) {
        photoDao.getAll().map { Photo(it.url, it.folderName.orEmpty()) }
    }

    override suspend fun addFavorite(photoUrl: String, folderName: String, isAdd: Boolean) =
        withContext(defaultDispatcher) {
            if (isAdd) {
                photoDao.insertAll(PhotoFavorite(folderName = folderName, url = photoUrl))
            } else {
                photoDao.delete(PhotoFavorite(folderName, photoUrl))
            }
        }

    override suspend fun isFavorite(photoUrl: String, folderName: String): Boolean =
        withContext(defaultDispatcher) {
            photoDao.findPhotoFavorite(photoUrl, folderName) != null
        }
}


