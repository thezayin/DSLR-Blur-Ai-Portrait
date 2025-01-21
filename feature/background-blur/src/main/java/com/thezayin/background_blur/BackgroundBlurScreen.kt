package com.thezayin.background_blur

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.thezayin.analytics.events.AnalyticsEvent
import com.thezayin.background_blur.component.BlurScreenContent
import com.thezayin.background_blur.event.BackgroundBlurEvent
import com.thezayin.components.AdLoadingDialog
import com.thezayin.framework.ads.functions.interstitialAd
import com.thezayin.framework.ads.functions.rewardedAd
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
    val showLoadingAd = remember { mutableStateOf(false) }
    val activity = LocalContext.current as Activity
    if (showLoadingAd.value) {
        AdLoadingDialog()
    }

    vm.analytics.logEvent(AnalyticsEvent.ScreenViewEvent("BackgroundBlurScreen"))

    BackHandler {
        activity.interstitialAd(
            analytics = vm.analytics,
            showAd = vm.remoteConfig.adConfigs.interstitialAdOnBack,
            adUnitId = vm.remoteConfig.adUnits.interstitialAdOnBack,
            showLoading = { showLoadingAd.value = true },
            hideLoading = { showLoadingAd.value = false },
            callback = {
                onBack()
            }
        )
    }

    BlurScreenContent(
        onBack = {
            activity.interstitialAd(
                analytics = vm.analytics,
                showAd = vm.remoteConfig.adConfigs.interstitialAdOnBack,
                adUnitId = vm.remoteConfig.adUnits.interstitialAdOnBack,
                showLoading = { showLoadingAd.value = true },
                hideLoading = { showLoadingAd.value = false },
                callback = {
                    onBack()
                }
            )
        },
        state = state,
        onDownloadClick = {
            vm.analytics.logEvent(AnalyticsEvent.DownloadButtonClickedEvent("BackgroundBlurScreen"))
            activity.rewardedAd(
                analytics = vm.analytics,
                showAd = vm.remoteConfig.adConfigs.rewardedAdOnImageSave,
                adUnitId = vm.remoteConfig.adUnits.rewardedAdOnImageSave,
                showLoading = { showLoadingAd.value = true },
                hideLoading = { showLoadingAd.value = false },
                callback = {
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
            activity.interstitialAd(
                analytics = vm.analytics,
                showAd = vm.remoteConfig.adConfigs.interstitialOnAdjustBlurIntensity,
                adUnitId = vm.remoteConfig.adUnits.interstitialOnAdjustBlurIntensity,
                showLoading = { showLoadingAd.value = true },
                hideLoading = { showLoadingAd.value = false },
                callback = {
                    vm.handleEvent(BackgroundBlurEvent.UpdateBlurIntensity(value))
                }
            )
        },
        adjustSmoothing = { value ->
            vm.analytics.logEvent(AnalyticsEvent.SmoothingSliderChangedEvent(value))
            activity.interstitialAd(
                analytics = vm.analytics,
                showAd = vm.remoteConfig.adConfigs.interstitialOnAdjustSmoothing,
                adUnitId = vm.remoteConfig.adUnits.interstitialOnAdjustSmoothing,
                showLoading = { showLoadingAd.value = true },
                hideLoading = { showLoadingAd.value = false },
                callback = {
                    vm.handleEvent(BackgroundBlurEvent.UpdateEdgeSmoothness(value))
                }
            )
        },
        dismissSaveSuccess = {
            vm.analytics.logEvent(AnalyticsEvent.DismissSaveSuccessEvent("BackgroundBlurScreen"))
            activity.interstitialAd(
                analytics = vm.analytics,
                showAd = vm.remoteConfig.adConfigs.interstitialOnDismissSaveSuccess,
                adUnitId = vm.remoteConfig.adUnits.interstitialOnOnboardingComplete,
                showLoading = { showLoadingAd.value = true },
                hideLoading = { showLoadingAd.value = false },
                callback = {
                    vm.handleEvent(BackgroundBlurEvent.HideSaveSuccess)
                }
            )
        }
    )
}
