package com.thezayin.presentation

import android.app.Activity
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import com.thezayin.analytics.events.AnalyticsEvent
import com.thezayin.components.AdLoadingDialog
import com.thezayin.framework.ads.functions.interstitialAd
import com.thezayin.framework.extension.createImageFile
import com.thezayin.presentation.component.GalleryPermissionHandler
import com.thezayin.presentation.component.GalleryScreenContent
import com.thezayin.presentation.event.GalleryEvent
import org.koin.compose.koinInject
import java.io.File

@Composable
fun GalleryScreen(
    viewModel: GalleryViewModel = koinInject(),
    onNavigateToNextScreen: () -> Unit = {},
    navigateBack: () -> Unit = {},
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val activity = LocalContext.current as Activity
    viewModel.analytics.logEvent(AnalyticsEvent.ScreenViewEvent("GalleryScreen"))
    val showLoadingAd = remember { mutableStateOf(false) }
    if (showLoadingAd.value) {
        AdLoadingDialog()
    }
    val photoFile: File by remember {
        mutableStateOf(createImageFile(context))
    }
    val photoUri: Uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        photoFile
    )

    BackHandler {
        activity.interstitialAd(
            analytics = viewModel.analytics,
            showAd = viewModel.remoteConfig.adConfigs.interstitialAdOnBack,
            adUnitId = viewModel.remoteConfig.adUnits.interstitialAdOnBack,
            showLoading = { showLoadingAd.value = true },
            hideLoading = { showLoadingAd.value = false },
            callback = {
                navigateBack()
            }
        )
    }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            activity.interstitialAd(
                analytics = viewModel.analytics,
                showAd = viewModel.remoteConfig.adConfigs.interstitialOnImageSelected,
                adUnitId = viewModel.remoteConfig.adUnits.interstitialOnImageSelected,
                showLoading = { showLoadingAd.value = true },
                hideLoading = { showLoadingAd.value = false },
                callback = {
                    viewModel.sendEvent(GalleryEvent.SelectImage(photoUri))
                }
            )

        } else {
            Toast.makeText(context, "Picture not taken or canceled", Toast.LENGTH_SHORT).show()
        }
    }

    GalleryPermissionHandler(
        onPermissionGranted = {
            LaunchedEffect(Unit) { viewModel.sendEvent(GalleryEvent.LoadAlbums) }
            GalleryScreenContent(
                onImageSelected = { uri: Uri ->
                    activity.interstitialAd(
                        analytics = viewModel.analytics,
                        showAd = viewModel.remoteConfig.adConfigs.interstitialOnImageSelected,
                        adUnitId = viewModel.remoteConfig.adUnits.interstitialOnImageSelected,
                        showLoading = { showLoadingAd.value = true },
                        hideLoading = { showLoadingAd.value = false },
                        callback = {
                            viewModel.sendEvent(GalleryEvent.SelectImage(uri))
                        }
                    )
                },
                onAlbumSelected = { albumId: String ->
                    activity.interstitialAd(
                        analytics = viewModel.analytics,
                        showAd = viewModel.remoteConfig.adConfigs.interstitialOnAlbumSelected,
                        adUnitId = viewModel.remoteConfig.adUnits.interstitialOnAlbumSelected,
                        showLoading = { showLoadingAd.value = true },
                        hideLoading = { showLoadingAd.value = false },
                        callback = {
                            viewModel.sendEvent(GalleryEvent.SelectAlbum(albumId))
                        }
                    )
                },
                onCameraClick = {
                    activity.interstitialAd(
                        analytics = viewModel.analytics,
                        showAd = viewModel.remoteConfig.adConfigs.interstitialOnCameraClick,
                        adUnitId = viewModel.remoteConfig.adUnits.interstitialOnCameraClick,
                        showLoading = { showLoadingAd.value = true },
                        hideLoading = { showLoadingAd.value = false },
                        callback = {
                            takePictureLauncher.launch(photoUri)
                        }
                    )
                },
                state = state,
                onBackClick = {
                    activity.interstitialAd(
                        analytics = viewModel.analytics,
                        showAd = viewModel.remoteConfig.adConfigs.interstitialAdOnBack,
                        adUnitId = viewModel.remoteConfig.adUnits.interstitialAdOnBack,
                        showLoading = { showLoadingAd.value = true },
                        hideLoading = { showLoadingAd.value = false },
                        callback = {
                            navigateBack()
                        }
                    )
                },
            )
        }
    )

    if (state.isImageSelected) {
        LaunchedEffect(Unit) {
            onNavigateToNextScreen()
            viewModel.sendEvent(GalleryEvent.ResetImageSelection)
        }
    }
}