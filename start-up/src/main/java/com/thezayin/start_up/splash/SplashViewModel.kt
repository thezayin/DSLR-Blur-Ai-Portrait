package com.thezayin.start_up.splash

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.mlkit.common.MlKitException
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.segmentation.subject.SubjectSegmentation
import com.google.mlkit.vision.segmentation.subject.SubjectSegmenterOptions
import com.google.mlkit.vision.segmentation.subject.SubjectSegmenterOptions.SubjectResultOptions
import com.thezayin.analytics.analytics.analytics.Analytics
import com.thezayin.analytics.events.AnalyticsEvent
import com.thezayin.framework.pref.PreferencesManager
import com.thezayin.framework.remote.RemoteConfig
import com.thezayin.start_up.splash.event.SplashEvent
import com.thezayin.start_up.splash.state.SplashState
import com.thezayin.dslrblur.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

class SplashViewModel(
    application: Application,
     val remoteConfig: RemoteConfig,
    val analytics: Analytics,
    private val preferencesManager: PreferencesManager
) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(SplashState())
    val state: StateFlow<SplashState> = _state.asStateFlow()

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
        _state.value = _state.value.copy(
            isFirstTime = preferencesManager.isFirstTime.value
        )
        sendEvent(SplashEvent.LoadSplash)
    }

    fun sendEvent(event: SplashEvent) {
        when (event) {
            SplashEvent.LoadSplash -> handleLoadSplash()
            SplashEvent.CheckInternet -> handleCheckInternet()
            SplashEvent.NavigateNext -> handleNavigateNext()
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
                    warmUpSubjectSegmentationModel()
                    sendEvent(SplashEvent.NavigateNext)
                } else {
                    sendEvent(SplashEvent.NavigateNext)
                }
            }
        }
    }

    private fun handleNavigateNext() {
        _state.update { it.copy(navigateToNextScreen = true) }
    }

    /**
     * Check if there's internet by connecting to Google DNS quickly.
     */
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
        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            val testBitmap = loadSmallBitmapFromDrawable()
            val success = tryDownloadModel(testBitmap, attempt = 1)
            if (success) {
                analytics.logEvent(
                    AnalyticsEvent.WarmUpEvent(
                        "SubjectSegmentation model downloaded successfully.",
                        true
                    )
                )
            } else {
                analytics.logEvent(
                    AnalyticsEvent.WarmUpEvent(
                        "SubjectSegmentation model download failed.",
                        false
                    )
                )
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
                analytics.logEvent(
                    AnalyticsEvent.TryToDownloadSegEvent(
                        "Attempt $attempt: SubjectSegmentation model downloaded successfully."
                    )
                )
                true
            } catch (e: MlKitException) {
                FirebaseCrashlytics.getInstance().recordException(e)
                if (e.message?.contains("Waiting for the subject segmentation optional module") == true) {
                    if (attempt < MAX_DOWNLOAD_RETRY) {
                        delay(MODEL_DOWNLOAD_RETRY_DELAY_MS)
                        return@withContext tryDownloadModel(bitmap, attempt + 1)
                    }
                    return@withContext false
                }
                return@withContext false
            } catch (ex: Exception) {
                FirebaseCrashlytics.getInstance().recordException(ex)
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
        return BitmapFactory.decodeResource(ctx.resources, R.drawable.boarding_third_after)
    }
}
