package com.thezayin.dslrblur.gallery.presentation.state

import com.thezayin.dslrblur.gallery.domain.model.Album
import com.thezayin.dslrblur.gallery.domain.model.Image

data class GalleryState(
    val isLoadingAlbums: Boolean = false,
    val albums: List<Album> = emptyList(),
    val selectedAlbum: Album? = null,
    val isLoadingImages: Boolean = false,
    val images: List<Image> = emptyList(),
    val isImageSelected: Boolean = false,
    val errorMessage: String? = null
)