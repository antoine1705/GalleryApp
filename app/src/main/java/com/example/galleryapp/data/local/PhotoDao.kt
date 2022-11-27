package com.example.galleryapp.data.local

import androidx.room.*

@Dao
interface PhotoDao {
    @Query("SELECT * FROM PhotoFavorite")
    fun getAll(): List<PhotoFavorite>

    @Query(
        "SELECT * FROM PhotoFavorite WHERE folder_name LIKE :folderName AND " +
                "url LIKE :url LIMIT 1"
    )
    fun findPhotoFavorite(url: String, folderName: String): PhotoFavorite?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg photos: PhotoFavorite)

    @Delete
    fun delete(photos: PhotoFavorite)
}
