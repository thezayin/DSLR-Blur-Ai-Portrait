package com.thezayin.dslrblur.feature.background_blur

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.thezayin.dslrblur.common.analytics.events.AnalyticsEvent
import com.thezayin.dslrblur.feature.background_blur.component.BlurScreenContent
import com.thezayin.dslrblur.feature.background_blur.event.BackgroundBlurEvent
import org.koin.compose.koinInject

/**
 * Composable that represents the Background Blur screen, integrating permission handling.
 *
 * @param onBack Callback invoked when the back button is pressed.
 * @param vm The ViewModel instance for Background Blur.
 */
@Composable
fun BackgroundBlurScreen(
    onBack: () -> Unit,
    vm: BackgroundBlurViewModel = koinInject()
) {
    val state by vm.state.collectAsState()
    val activity = LocalActivity.current as Activity
    val interstitialAdManager = vm.interstitialAdManager
    val rewardedAdManager = vm.rewardedAdManager

    LaunchedEffect(Unit) {
        interstitialAdManager.loadAd(activity)
        rewardedAdManager.loadAd(activity)
    }

    vm.analytics.logEvent(AnalyticsEvent.ScreenViewEvent("BackgroundBlurScreen"))

    BackHandler {
        interstitialAdManager.showAd(
            activity = activity,
            adImpression = {},
            showAd = vm.remoteConfig.adConfigs.interstitialAdOnBack,
            onNext = {
                onBack()
            }
        )
    }

    BlurScreenContent(
        onBack = {
            interstitialAdManager.showAd(
                activity = activity,
                adImpression = {},
                showAd = vm.remoteConfig.adConfigs.interstitialAdOnBack,
                onNext = {
                    onBack()
                }
            )
        },
        state = state,
        onDownloadClick = {
            vm.analytics.logEvent(AnalyticsEvent.DownloadButtonClickedEvent("BackgroundBlurScreen"))
            rewardedAdManager.showAd(
                activity = activity,
                showAd = vm.remoteConfig.adConfigs.rewardedAdOnImageSave,
                onNext = {
                    vm.handleEvent(
                        BackgroundBlurEvent.SaveImage(
                            filename = "blurred_image_${System.currentTimeMillis()}",
                            asPng = false
                        )
                    )
                }
            )
        },
        adjustBlurIntensity = { value ->
            vm.analytics.logEvent(AnalyticsEvent.BlurIntensitySliderChangedEvent(value))
            vm.handleEvent(BackgroundBlurEvent.UpdateBlurIntensity(value))
        },
        adjustSmoothing = { value ->
            vm.analytics.logEvent(AnalyticsEvent.SmoothingSliderChangedEvent(value))
            vm.handleEvent(BackgroundBlurEvent.UpdateEdgeSmoothness(value))
        },
        dismissSaveSuccess = {
            vm.analytics.logEvent(AnalyticsEvent.DismissSaveSuccessEvent("BackgroundBlurScreen"))
            interstitialAdManager.showAd(
                activity = activity,
                adImpression = {},
                showAd = vm.remoteConfig.adConfigs.interstitialOnDismissSaveSuccess,
                onNext = {
                    vm.handleEvent(BackgroundBlurEvent.HideSaveSuccess)
                }
            )
        }
    )
}
