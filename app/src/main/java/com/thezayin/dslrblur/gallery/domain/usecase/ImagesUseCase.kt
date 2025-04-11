package com.thezayin.dslrblur.gallery.domain.usecase

import com.thezayin.dslrblur.gallery.domain.model.Image
import com.thezayin.dslrblur.gallery.domain.repository.GetImagesRepository
import com.thezayin.framework.utils.Response
import kotlinx.coroutines.flow.Flow

interface ImagesUseCase {
    suspend operator fun invoke(albumId: String): Flow<Response<List<Image>>>
}

class ImagesUseCaseImpl(private val repository: GetImagesRepository) : ImagesUseCase {
    override suspend fun invoke(albumId: String): Flow<Response<List<Image>>> =
        repository.getImages(albumId)
}
