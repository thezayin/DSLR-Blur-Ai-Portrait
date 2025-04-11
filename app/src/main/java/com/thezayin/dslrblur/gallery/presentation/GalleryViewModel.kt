package com.thezayin.dslrblur.gallery.presentation

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thezayin.dslrblur.common.analytics.analytics.Analytics
import com.thezayin.dslrblur.common.analytics.events.AnalyticsEvent
import com.thezayin.dslrblur.framework.ads.admob.domain.repository.InterstitialAdManager
import com.thezayin.dslrblur.framework.ads.admob.domain.repository.RewardedAdManager
import com.thezayin.dslrblur.gallery.domain.usecase.AlbumsUseCase
import com.thezayin.dslrblur.gallery.domain.usecase.ImagesUseCase
import com.thezayin.dslrblur.gallery.presentation.event.GalleryEvent
import com.thezayin.dslrblur.gallery.presentation.state.GalleryState
import com.thezayin.framework.remote.RemoteConfig
import com.thezayin.framework.session.SessionManager
import com.thezayin.framework.utils.Response
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GalleryViewModel(
    private val albumsUseCase: AlbumsUseCase,
    private val imagesUseCase: ImagesUseCase,
    private val sessionManager: SessionManager,
    val interstitialAdManager: InterstitialAdManager,
    val rewardedAdManager: RewardedAdManager,
    val analytics: Analytics,
    val remoteConfig: RemoteConfig
) : ViewModel() {

    private val _state = MutableStateFlow(GalleryState())
    val state: StateFlow<GalleryState> = _state.asStateFlow()

    fun sendEvent(event: GalleryEvent) {
        viewModelScope.launch {
            when (event) {
                is GalleryEvent.LoadAlbums -> loadAlbums()
                is GalleryEvent.SelectAlbum -> selectAlbum(event.albumId)
                is GalleryEvent.SelectImage -> selectImage(event.imageUri)
                is GalleryEvent.ResetImageSelection -> resetImageSelection()
            }
        }
    }

    private suspend fun loadAlbums() {
        _state.update { it.copy(isLoadingAlbums = true, errorMessage = null) }
        albumsUseCase().collect { response ->
            when (response) {
                is Response.Loading -> {
                    _state.update { it.copy(isLoadingAlbums = true) }
                }

                is Response.Success -> {
                    analytics.logEvent(AnalyticsEvent.LoadAlbumsEvent("Load"))
                    _state.update { currentState ->
                        currentState.copy(
                            isLoadingAlbums = false,
                            albums = response.data,
                            selectedAlbum = response.data.firstOrNull()
                        )
                    }
                    response.data.firstOrNull()?.let { firstAlbum ->
                        sendEvent(GalleryEvent.SelectAlbum(firstAlbum.id))
                    }
                }

                is Response.Error -> {
                    _state.update { currentState ->
                        currentState.copy(
                            isLoadingAlbums = false,
                            errorMessage = response.e
                        )
                    }
                }
            }
        }
    }

    private suspend fun selectAlbum(albumId: String) {
        _state.update { it.copy(isLoadingImages = true, errorMessage = null) }
        imagesUseCase(albumId).collect { response ->
            when (response) {
                is Response.Loading -> {
                    _state.update { it.copy(isLoadingImages = true) }
                }

                is Response.Success -> {
                    analytics.logEvent(AnalyticsEvent.SelectAlbumEvent("Select"))
                    _state.update { currentState ->
                        currentState.copy(
                            isLoadingImages = false,
                            images = response.data,
                            selectedAlbum = currentState.albums.find { it.id == albumId }
                        )
                    }
                }

                is Response.Error -> {
                    _state.update { currentState ->
                        currentState.copy(
                            isLoadingImages = false,
                            errorMessage = response.e
                        )
                    }
                }
            }
        }
    }

    private fun selectImage(uri: Uri) {
        viewModelScope.launch {
            analytics.logEvent(AnalyticsEvent.SelectImageEvent("Select"))
            sessionManager.setBaseImage(uri)
            _state.update { it.copy(isImageSelected = true) }
        }
    }

    private fun resetImageSelection() {
        viewModelScope.launch {
            analytics.logEvent(AnalyticsEvent.ResetImageSelectionEvent("Reset"))
            _state.update { it.copy(isImageSelected = false) }
        }
    }
}
