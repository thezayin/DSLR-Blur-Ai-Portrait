package com.thezayin.dslrblur.gallery.domain.usecase

import com.thezayin.dslrblur.gallery.domain.model.Album
import com.thezayin.dslrblur.gallery.domain.repository.GetAlbumsRepository
import com.thezayin.framework.utils.Response
import kotlinx.coroutines.flow.Flow

interface AlbumsUseCase {
    suspend operator fun invoke(): Flow<Response<List<Album>>>
}

class AlbumsUseCaseImpl(private val repository: GetAlbumsRepository) : AlbumsUseCase {
    override suspend fun invoke(): Flow<Response<List<Album>>> = repository.getAlbums()
}
