package com.thezayin.dslrblur.gallery.data.repository

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.thezayin.dslrblur.gallery.data.network.MediaStoreUtil
import com.thezayin.dslrblur.gallery.domain.model.Image
import com.thezayin.dslrblur.gallery.domain.repository.GetImagesRepository
import com.thezayin.framework.utils.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetImagesRepositoryImpl(private val mediaStoreUtil: MediaStoreUtil) : GetImagesRepository {
    override fun getImages(albumId: String): Flow<Response<List<Image>>> = flow {
        emit(Response.Loading)
        try {
            val images = mediaStoreUtil.fetchImages(albumId)
            emit(Response.Success(images))
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
            emit(Response.Error(e.localizedMessage ?: "Failed to load images"))
        }
    }
}