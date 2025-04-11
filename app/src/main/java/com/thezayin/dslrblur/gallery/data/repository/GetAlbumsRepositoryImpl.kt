package com.thezayin.dslrblur.gallery.data.repository

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.thezayin.dslrblur.gallery.data.network.MediaStoreUtil
import com.thezayin.dslrblur.gallery.domain.model.Album
import com.thezayin.dslrblur.gallery.domain.repository.GetAlbumsRepository
import com.thezayin.framework.utils.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetAlbumsRepositoryImpl(private val mediaStoreUtil: MediaStoreUtil) : GetAlbumsRepository {
    override fun getAlbums(): Flow<Response<List<Album>>> = flow {
        emit(Response.Loading)
        try {
            val albums = mediaStoreUtil.fetchAlbums()
            emit(Response.Success(albums))
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
            emit(Response.Error(e.localizedMessage ?: "Failed to load albums"))
        }
    }
}
