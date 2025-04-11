package com.thezayin.dslrblur.gallery.presentation

import android.app.Activity
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
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
import com.thezayin.dslrblur.common.analytics.events.AnalyticsEvent
import com.thezayin.dslrblur.gallery.presentation.component.GalleryPermissionHandler
import com.thezayin.dslrblur.gallery.presentation.component.GalleryScreenContent
import com.thezayin.dslrblur.gallery.presentation.event.GalleryEvent
import com.thezayin.framework.extension.createImageFile
import org.koin.compose.koinInject
import java.io.File

@Composable
fun GalleryScreen(
    vm: GalleryViewModel = koinInject(),
    onNavigateToNextScreen: () -> Unit = {},
    navigateBack: () -> Unit = {},
) {
    val state by vm.state.collectAsState()
    val context = LocalContext.current
    val activity = LocalActivity.current as Activity
    val adManager = vm.interstitialAdManager
    vm.analytics.logEvent(AnalyticsEvent.ScreenViewEvent("GalleryScreen"))

    LaunchedEffect(Unit) {
        adManager.loadAd(activity)  // Load the ad once
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
        adManager.showAd(
            activity = activity,
            showAd = vm.remoteConfig.adConfigs.interstitialAdOnBack,
            adImpression = {},
            onNext = {
                navigateBack()
            }
        )
    }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            adManager.showAd(
                activity = activity,
                showAd = vm.remoteConfig.adConfigs.interstitialOnImageSelected,
                adImpression = {},
                onNext = {
                    vm.sendEvent(GalleryEvent.SelectImage(photoUri))
                }
            )

        } else {
            Toast.makeText(context, "Picture not taken or canceled", Toast.LENGTH_SHORT).show()
        }
    }

    GalleryPermissionHandler(
        onPermissionGranted = {
            LaunchedEffect(Unit) { vm.sendEvent(GalleryEvent.LoadAlbums) }
            GalleryScreenContent(
                onImageSelected = { uri: Uri ->
                    adManager.showAd(
                        activity = activity,
                        showAd = vm.remoteConfig.adConfigs.interstitialOnImageSelected,
                        adImpression = {},
                        onNext = {
                            vm.sendEvent(GalleryEvent.SelectImage(uri))
                        }
                    )
                },
                onAlbumSelected = { albumId: String ->
                    adManager.showAd(
                        activity = activity,
                        showAd = vm.remoteConfig.adConfigs.interstitialOnAlbumSelected,
                        adImpression = {},
                        onNext = {
                            vm.sendEvent(GalleryEvent.SelectAlbum(albumId))
                        }
                    )
                },
                onCameraClick = {
                    adManager.showAd(
                        activity = activity,
                        showAd = vm.remoteConfig.adConfigs.interstitialOnCameraClick,
                        adImpression = {},
                        onNext = {
                            takePictureLauncher.launch(photoUri)
                        }
                    )
                },
                state = state,
                onBackClick = {
                    adManager.showAd(
                        activity = activity,
                        showAd = vm.remoteConfig.adConfigs.interstitialAdOnBack,
                        adImpression = {},
                        onNext = {
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
            vm.sendEvent(GalleryEvent.ResetImageSelection)
        }
    }
}