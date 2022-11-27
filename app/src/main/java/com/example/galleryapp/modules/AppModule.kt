package com.example.galleryapp.modules

import androidx.room.Room
import com.example.galleryapp.data.GalleryRepository
import com.example.galleryapp.data.IGalleryRepository
import com.example.galleryapp.data.local.AppDatabase
import com.example.galleryapp.ui.detail.PhotoViewModel
import com.example.galleryapp.ui.favorite.FavoritePhotoListViewModel
import com.example.galleryapp.ui.folder.FolderViewModel
import com.example.galleryapp.ui.photos.PhotoListViewModel
import com.example.galleryapp.ui.video.VideoListViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module


val appModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java, "database-gallery"
        ).build()
    }

    single {
        val database = get<AppDatabase>()
        database.photoDao()
    }

    single<IGalleryRepository> {
        GalleryRepository(get())
    }
}

val viewModelModule = module {
    single {
        FolderViewModel(get(), get())
    }
    single {
        PhotoListViewModel(get(), get())
    }
    single {
        PhotoViewModel(get())
    }
    single {
        FavoritePhotoListViewModel(get())
    }
    single {
        VideoListViewModel(get(), get())
    }
}