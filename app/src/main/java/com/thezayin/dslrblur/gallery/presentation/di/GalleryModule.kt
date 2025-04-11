package com.thezayin.dslrblur.gallery.presentation.di

import com.thezayin.dslrblur.gallery.data.network.MediaStoreUtil
import com.thezayin.dslrblur.gallery.data.repository.GetAlbumsRepositoryImpl
import com.thezayin.dslrblur.gallery.data.repository.GetImagesRepositoryImpl
import com.thezayin.dslrblur.gallery.domain.repository.GetAlbumsRepository
import com.thezayin.dslrblur.gallery.domain.repository.GetImagesRepository
import com.thezayin.dslrblur.gallery.domain.usecase.AlbumsUseCase
import com.thezayin.dslrblur.gallery.domain.usecase.AlbumsUseCaseImpl
import com.thezayin.dslrblur.gallery.domain.usecase.ImagesUseCase
import com.thezayin.dslrblur.gallery.domain.usecase.ImagesUseCaseImpl
import com.thezayin.dslrblur.gallery.presentation.GalleryViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val galleryModule = module {
    single { MediaStoreUtil(get()) }

    single<GetAlbumsRepository> { GetAlbumsRepositoryImpl(get()) }
    single<GetImagesRepository> { GetImagesRepositoryImpl(get()) }
    single<AlbumsUseCase> { AlbumsUseCaseImpl(get()) }
    single<ImagesUseCase> { ImagesUseCaseImpl(get()) }

    viewModelOf(::GalleryViewModel)
}