package com.thezayin.dslrblur.gallery.domain.repository

import com.thezayin.dslrblur.gallery.domain.model.Image
import com.thezayin.framework.utils.Response
import kotlinx.coroutines.flow.Flow

interface GetImagesRepository {
    fun getImages(albumId: String): Flow<Response<List<Image>>>
}
