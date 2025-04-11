package com.thezayin.dslrblur.feature.splash

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.mlkit.common.MlKitException
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.segmentation.subject.SubjectSegmentation
import com.google.mlkit.vision.segmentation.subject.SubjectSegmenterOptions
import com.google.mlkit.vision.segmentation.subject.SubjectSegmenterOptions.SubjectResultOptions
import com.thezayin.dslrblur.R
import com.thezayin.dslrblur.common.analytics.analytics.Analytics
import com.thezayin.dslrblur.common.analytics.events.AnalyticsEvent
import com.thezayin.dslrblur.feature.splash.event.SplashEvent
import com.thezayin.dslrblur.feature.splash.state.SplashState
import com.thezayin.dslrblur.framework.ads.admob.domain.repository.AppOpenAdManager
import com.thezayin.framework.pref.PreferencesManager
import com.thezayin.framework.remote.RemoteConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

class SplashViewModel(
    application: Application,
    val analytics: Analytics,
    val remoteConfig: RemoteConfig,
    private val preferencesManager: PreferencesManager,
    val adManager: AppOpenAdManager
) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(SplashState())
    val state: StateFlow<SplashState> = _state.asStateFlow()

    private val _isAdLoaded = MutableStateFlow(false)
    val isAdLoaded: LiveData<Boolean> = _isAdLoaded.asLiveData()

    fun setAdLoaded(loaded: Boolean) {
        _isAdLoaded.value = loaded
    }

    private val subjectSegmenterClient = SubjectSegmentation.getClient(
        SubjectSegmenterOptions.Builder()
            .enableMultipleSubjects(
                SubjectResultOptions.Builder().enableConfidenceMask().build()
            )
            .build()
    )

    companion object {
        private const val MODEL_DOWNLOAD_RETRY_DELAY_MS = 2000L
        private const val MAX_DOWNLOAD_RETRY = 3
    }

    init {
        warmUpSubjectSegmentationModel()
        _state.value = _state.value.copy(
            isFirstTime = preferencesManager.isFirstTime.value
        )
        sendEvent(SplashEvent.LoadSplash)
    }

    fun sendEvent(event: SplashEvent) {
        when (event) {
            SplashEvent.LoadSplash -> handleLoadSplash()
            SplashEvent.CheckInternet -> handleCheckInternet()
            SplashEvent.NavigateToOnboarding -> navigateToOnboarding()
            SplashEvent.NavigateToHome -> navigateToHome()
        }
    }

    private fun handleLoadSplash() {
        viewModelScope.launch {
            val totalTime = 5000L
            val interval = totalTime / _state.value.splashTexts.size

            for (i in _state.value.splashTexts.indices) {
                _state.update {
                    it.copy(
                        currentSplashText = it.splashTexts[i],
                        currentSplashIndex = i
                    )
                }
                delay(interval)
            }
            sendEvent(SplashEvent.CheckInternet)
        }
    }

    private fun handleCheckInternet() {
        viewModelScope.launch {
            val isConnected = checkInternetConnection()
            _state.update { it.copy(isConnected = isConnected, isLoading = false) }

            if (!isConnected) {
                analytics.logEvent(
                    AnalyticsEvent.NoInternetEvent(
                        "No internet connection."
                    )
                )
                _state.update { it.copy(error = "No internet connection.") }
            } else {
                if (_state.value.isFirstTime) {
                    sendEvent(SplashEvent.NavigateToOnboarding)
                } else {
                    sendEvent(SplashEvent.NavigateToHome)
                }
            }
        }
    }

    private fun navigateToOnboarding() {
        _state.update { it.copy(navigateToOnboarding = true) }
    }

    private fun navigateToHome() {
        _state.update { it.copy(navigateToHome = true) }
    }

    private suspend fun checkInternetConnection(): Boolean = withContext(Dispatchers.IO) {
        try {
            val timeoutMs = 1500
            Socket().use { socket ->
                socket.connect(InetSocketAddress("8.8.8.8", 53), timeoutMs)
            }
            true
        } catch (e: IOException) {
            false
        }
    }

    /**
     * Warm up SubjectSegmenter by processing a small asset from drawable,
     * triggering the on-demand model download if not present.
     */
    private fun warmUpSubjectSegmentationModel() {
        // Show loading while we attempt to download the model
        _state.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            // Load a small image from drawable
            val testBitmap = loadSmallBitmapFromDrawable()

            val success = tryDownloadModel(testBitmap, attempt = 1)
            if (success) {
                Timber.d("Model download success")
                sendEvent(SplashEvent.NavigateToHome)
            } else {
                // Failure => show an error
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Model download failed. Please retry."
                    )
                }
            }
        }
    }


    /**
     * Recursively attempt to process [bitmap] so ML Kit downloads the module.
     * If "Waiting for the subject segmentation optional module" => wait + retry
     */
    private suspend fun tryDownloadModel(bitmap: Bitmap, attempt: Int): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val inputImage = InputImage.fromBitmap(bitmap, 0)
                subjectSegmenterClient.process(inputImage).await()
                // If success => model is downloaded
                true
            } catch (e: MlKitException) {
                Timber.tag("tryDownloadModel").e(e, "Model download failed")
                if (e.message?.contains("Waiting for the subject segmentation optional module") == true) {
                    // Wait & retry if attempts remain
                    if (attempt < MAX_DOWNLOAD_RETRY) {
                        delay(MODEL_DOWNLOAD_RETRY_DELAY_MS)
                        return@withContext tryDownloadModel(bitmap, attempt + 1)
                    }
                    return@withContext false
                }
                // Some other error
                return@withContext false
            } catch (ex: Exception) {
                // Unknown error
                return@withContext false
            }
        }
    }

    /**
     * Loads a small image from drawable to “warm up” SubjectSegmentation.
     * E.g. a 64×64 or smaller image stored in res/drawable.
     */
    private fun loadSmallBitmapFromDrawable(): Bitmap {
        val ctx = getApplication<Application>().applicationContext
        return BitmapFactory.decodeResource(ctx.resources, R.drawable.bg_intro)
    }
}
