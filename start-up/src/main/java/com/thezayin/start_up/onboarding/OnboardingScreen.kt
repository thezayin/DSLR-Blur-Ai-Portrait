package com.thezayin.start_up.onboarding

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.thezayin.analytics.events.AnalyticsEvent
import com.thezayin.components.AdLoadingDialog
import com.thezayin.framework.ads.functions.interstitialAd
import com.thezayin.start_up.onboarding.actions.OnboardingActions
import com.thezayin.start_up.onboarding.components.OnboardingContent
import org.koin.compose.koinInject

@Composable
fun OnboardingScreen(
    navigateToHome: () -> Unit,
    vm: OnboardingViewModel = koinInject()
) {
    val state = vm.state.collectAsState()
    val activity = LocalContext.current as Activity
    val showLoadingAd = remember { mutableStateOf(false) }
    if (showLoadingAd.value) {
        AdLoadingDialog()
    }
    if (state.value.isOnboardingCompleted) {
        activity.interstitialAd(
            analytics = vm.analytics,
            showAd = vm.remoteConfig.adConfigs.interstitialOnOnboardingComplete,
            adUnitId = vm.remoteConfig.adUnits.interstitialOnOnboardingComplete,
            showLoading = { showLoadingAd.value = true },
            hideLoading = { showLoadingAd.value = false },
            callback = {
                navigateToHome()
            }
        )
        return
    }
    vm.analytics.logEvent(AnalyticsEvent.ScreenViewEvent("OnboardingScreen"))
    OnboardingContent(
        onboardPages = state.value.pages,
        currentPage = state.value.currentPage,
        onNextClicked = {
            if (state.value.currentPage < state.value.pages.size - 1) {
                vm.onAction(OnboardingActions.NextPage)
            } else {
                vm.analytics.logEvent(AnalyticsEvent.OnboardingCompletedEvent())
                vm.onAction(OnboardingActions.CompleteOnboarding)
            }
        }
    )
}