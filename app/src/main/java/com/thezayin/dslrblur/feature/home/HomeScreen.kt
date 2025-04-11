package com.thezayin.dslrblur.feature.home

import android.app.Activity
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.thezayin.dslrblur.common.analytics.events.AnalyticsEvent
import com.thezayin.dslrblur.feature.home.component.HomeScreenContent
import org.koin.compose.koinInject

@Composable
fun HomeScreen(
    onSettingScreenClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    vm: HomeViewModel = koinInject()
) {
    vm.analytics.logEvent(AnalyticsEvent.ScreenViewEvent("HomeScreen"))
    val activity = LocalActivity.current as Activity
    val interstitialAdManager = vm.interstitialAdManager
    val rewardedAdManager = vm.rewardedAdManager

    LaunchedEffect(Unit) {
        interstitialAdManager.loadAd(activity)
        rewardedAdManager.loadAd(activity)
    }

    BackHandler {
        vm.analytics.logEvent(AnalyticsEvent.BackPressedEvent("HomeScreen"))
        interstitialAdManager.showAd(
            activity = activity,
            showAd = vm.remoteConfig.adConfigs.interstitialAdOnBack,
            adImpression = {},
            onNext = {
                activity.finish()
            }
        )
    }
    HomeScreenContent(
        onSettingScreenClick = {
            onSettingScreenClick()
        },
        onEditClick = {
            vm.analytics.logEvent(AnalyticsEvent.EditButtonClickedEvent("HomeScreen"))
            rewardedAdManager.showAd(
                activity = activity,
                showAd = vm.remoteConfig.adConfigs.rewardedAdOnEditClick,
                onNext = { onEditClick() }
            )
        })
}