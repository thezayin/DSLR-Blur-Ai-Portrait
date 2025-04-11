package com.thezayin.dslrblur.feature.onboarding

import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.thezayin.dslrblur.common.analytics.events.AnalyticsEvent
import com.thezayin.dslrblur.feature.onboarding.actions.OnboardingActions
import com.thezayin.dslrblur.feature.onboarding.components.OnboardingContent
import org.koin.compose.koinInject

@Composable
fun OnboardingScreen(
    navigateToHome: () -> Unit,
    vm: OnboardingViewModel = koinInject()
) {
    val state = vm.state.collectAsState()
    val activity = LocalActivity.current as Activity
    val adManager = vm.adManager
    adManager.loadAd(
        activity = activity
    )

    if (state.value.isOnboardingCompleted) {
        adManager.showAd(
            activity = activity,
            showAd = vm.remoteConfig.adConfigs.interstitialOnOnboardingComplete,
            adImpression = {},
            onNext = {
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