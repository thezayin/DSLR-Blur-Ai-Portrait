package com.thezayin.dslrblur.feature.splash

import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import com.thezayin.dslrblur.R
import com.thezayin.dslrblur.feature.splash.component.BottomText
import com.thezayin.dslrblur.feature.splash.component.ImageHeader
import com.thezayin.dslrblur.feature.splash.component.NoInternetComposable
import com.thezayin.dslrblur.feature.splash.event.SplashEvent
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
    val activity = LocalActivity.current as Activity
    val adManager = viewModel.adManager

    if (!viewModel.isAdLoaded.value!!) {
        viewModel.setAdLoaded(true)
        adManager.loadAd(
            activity = activity
        )
    }

    LaunchedEffect(state) {
        adManager.showAd(
            activity = activity,
            showAd = viewModel.remoteConfig.adConfigs.appOpenOnSplash,
            onNext = {
                if (state.navigateToOnboarding) {
                    navigateToOnboarding()
                } else if (state.navigateToHome) {
                    navigateToHome()
                }
            },
            adImpression = {}
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
        }) { paddingValues ->
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
                    }, modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    }
}
