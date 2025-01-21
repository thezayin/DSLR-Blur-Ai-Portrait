package com.thezayin.start_up.home

import android.app.Activity
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.thezayin.analytics.events.AnalyticsEvent
import com.thezayin.components.AdLoadingDialog
import com.thezayin.framework.ads.functions.interstitialAd
import com.thezayin.framework.ads.functions.rewardedAd
import com.thezayin.start_up.home.component.HomeScreenContent
import org.koin.compose.koinInject

@Composable
fun HomeScreen(
    onSettingScreenClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    vm: HomeViewModel = koinInject()
) {
    Log.d("HomeScreen", "HomeScreen")
    vm.analytics.logEvent(AnalyticsEvent.ScreenViewEvent("HomeScreen"))
    val activity = LocalContext.current as Activity
    val showLoadingAd = remember { mutableStateOf(false) }
    if (showLoadingAd.value) {
        AdLoadingDialog()
    }
    BackHandler {
        vm.analytics.logEvent(AnalyticsEvent.BackPressedEvent("HomeScreen"))
        activity.interstitialAd(
            analytics = vm.analytics,
            showAd = vm.remoteConfig.adConfigs.interstitialAdOnBack,
            adUnitId = vm.remoteConfig.adUnits.interstitialAdOnBack,
            showLoading = { showLoadingAd.value = true },
            hideLoading = { showLoadingAd.value = false },
            callback = {
                activity.finish()
            }
        )
    }
    HomeScreenContent(
        onSettingScreenClick = {
            activity.interstitialAd(
                analytics = vm.analytics,
                showAd = vm.remoteConfig.adConfigs.interstitialAdOnSettingClick,
                adUnitId = vm.remoteConfig.adUnits.interstitialAdOnSettingClick,
                showLoading = { showLoadingAd.value = true },
                hideLoading = { showLoadingAd.value = false },
                callback = { onSettingScreenClick() }
            )
        },
        onEditClick = {
            vm.analytics.logEvent(AnalyticsEvent.EditButtonClickedEvent("HomeScreen"))
            activity.rewardedAd(
                analytics = vm.analytics,
                showAd = vm.remoteConfig.adConfigs.rewardedAdOnEditClick,
                adUnitId = vm.remoteConfig.adUnits.rewardedAdOnEditClick,
                showLoading = { showLoadingAd.value = true },
                hideLoading = { showLoadingAd.value = false },
                callback = { onEditClick() }
            )
        })
}