package com.thezayin.start_up.splash

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import com.thezayin.analytics.events.AnalyticsEvent
import com.thezayin.components.AdLoadingDialog
import com.thezayin.framework.ads.functions.appOpenAd
import com.thezayin.start_up.splash.component.BottomText
import com.thezayin.start_up.splash.component.ImageHeader
import com.thezayin.start_up.splash.component.NoInternetComposable
import com.thezayin.start_up.splash.event.SplashEvent
import com.thezayin.dslrblur.R
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun SplashScreen(
    navigateToHome: () -> Unit,
    navigateToOnboarding: () -> Unit
) {
    val viewModel: SplashViewModel = koinInject()
    val state by viewModel.state.collectAsState()
    val scope = rememberCoroutineScope()
    val activity = LocalContext.current as Activity
    val showLoadingAd = remember { mutableStateOf(false) }


    viewModel.analytics.logEvent(AnalyticsEvent.ScreenViewEvent("SplashScreen"))

    if (state.navigateToNextScreen) {
        activity.appOpenAd(
            analytics = viewModel.analytics,
            showLoading = { showLoadingAd.value = true },
            hideLoading = { showLoadingAd.value = false },
            callback = {
                if (state.isFirstTime) {
                    navigateToOnboarding()
                } else {
                    navigateToHome()
                }
            },
            adUnitId = viewModel.remoteConfig.adUnits.appOpenOnSplash,
            showAd = viewModel.remoteConfig.adConfigs.appOpenOnSplash
        )
    }

    Scaffold(
        modifier = Modifier.navigationBarsPadding(),
        containerColor = colorResource(R.color.black),
        bottomBar = {
            if (!state.isConnected && state.error != null) {
                BottomText(modifier = Modifier, text = R.string.no_internet_error)
            } else if (state.isLoading) {
                BottomText(modifier = Modifier, text = state.currentSplashText)
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (state.isLoading) {
                ImageHeader(modifier = Modifier.align(Alignment.Center))
            }

            if (!state.isConnected && state.error != null) {
                NoInternetComposable(
                    onRetry = {
                        scope.launch {
                            viewModel.sendEvent(SplashEvent.CheckInternet)
                        }
                    },
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    }
}
