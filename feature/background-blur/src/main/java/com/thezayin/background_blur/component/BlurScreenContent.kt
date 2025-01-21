package com.thezayin.background_blur.component

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.thezayin.background_blur.event.AdjustmentMode
import com.thezayin.background_blur.state.BackgroundBlurState
import com.thezayin.values.R

/**
 * Composable representing the main content of the Background Blur screen.
 *
 * @param state The current state of the UI.
 * @param onBack Callback when the back button is pressed.
 * @param onDownloadClick Callback when the download button is clicked.
 * @param adjustBlurIntensity Callback to adjust blur intensity.
 * @param adjustSmoothing Callback to adjust edge smoothness.
 */
@Composable
fun BlurScreenContent(
    state: BackgroundBlurState,
    onBack: () -> Unit,
    onDownloadClick: () -> Unit,
    adjustBlurIntensity: (Int) -> Unit,
    adjustSmoothing: (Int) -> Unit,
    dismissSaveSuccess: () -> Unit,
) {

    if (state.showSaveSuccess) {
        BlurSaveSuccessDialog(
            onDismiss = dismissSaveSuccess,
            imageUri = Uri.parse("file://${state.displayBitmap}"), // Provide correct Uri
            context = LocalContext.current
        )
    }

    val isDownloadEnabled =
        !state.isLoading && state.displayBitmap != null && state.errorMessage == null

    var adjustmentMode by remember { mutableStateOf<AdjustmentMode>(AdjustmentMode.None) }

    Scaffold(
        containerColor = colorResource(R.color.black),
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            BlurTopBar(
                onBackClicked = { onBack() },
                onDownloadClicked = onDownloadClick,
                isDownloadEnabled = isDownloadEnabled
            )
        },
        bottomBar = {
            if (!state.isLoading && state.displayBitmap != null) {
                when (adjustmentMode) {
                    AdjustmentMode.None -> {
                        AdjustmentIcons(
                            onAdjustSmoothness = {
                                adjustmentMode = AdjustmentMode.AdjustSmoothness
                            },
                            onAdjustBlurIntensity = {
                                adjustmentMode = AdjustmentMode.AdjustBlurIntensity
                            }
                        )
                    }

                    AdjustmentMode.AdjustSmoothness -> {
                        AdjustmentSlider(
                            label = stringResource(R.string.edge_smoothness_label),
                            initialValue = state.currentSmoothness,
                            valueRange = 0f..100f,
                            onValueChangeFinished = { value ->
                                adjustSmoothing(value)
                            },
                            onDone = { adjustmentMode = AdjustmentMode.None }
                        )
                    }

                    AdjustmentMode.AdjustBlurIntensity -> {
                        AdjustmentSlider(
                            label = stringResource(R.string.blur_intensity_label),
                            initialValue = state.currentBlurRadius,
                            valueRange = 0f..25f,
                            onValueChangeFinished = { value ->
                                adjustBlurIntensity(value)
                            },
                            onDone = { adjustmentMode = AdjustmentMode.None }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            if (!state.isLoading && state.displayBitmap != null && state.errorMessage == null) {
                var showOriginal by remember { mutableStateOf(false) }
                Image(
                    bitmap = if (showOriginal) state.originalBitmap?.asImageBitmap()
                        ?: state.displayBitmap.asImageBitmap() else state.displayBitmap.asImageBitmap(),
                    contentDescription = stringResource(id = R.string.content_description_processed_image),
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = androidx.compose.ui.layout.ContentScale.Fit
                )

                BlurCompareIcon(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    onPressStart = {
                        showOriginal = true
                    },
                    onPressEnd = {
                        showOriginal = false
                    }
                )
            }
            if (state.isLoading) {
                BlurringDialog(
                    modifier = Modifier,
                    statusText = state.statusText
                )
            }

            if (state.errorMessage != null) {
                Text(
                    text = state.errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                )
            }
        }
    }
}
