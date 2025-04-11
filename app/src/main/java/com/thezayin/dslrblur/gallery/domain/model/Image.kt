package com.thezayin.dslrblur.gallery.domain.model

import android.net.Uri

data class Image(
    val id: String,
    val uri: Uri,
    val title: String
)