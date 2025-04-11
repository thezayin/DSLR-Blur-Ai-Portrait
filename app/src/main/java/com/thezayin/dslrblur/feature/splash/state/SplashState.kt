package com.thezayin.dslrblur.feature.splash.state

import  com.thezayin.dslrblur.R

data class SplashState(
    val isConnected: Boolean = false,
    val isLoading: Boolean = true,
    val error: String? = null,
    val currentSplashText: Int = R.string.loading_settings,
    val splashTexts: List<Int> = listOf(
        R.string.loading_settings, R.string.finishing_up, R.string.almost_there
    ),
    val currentSplashIndex: Int = 0,
    val isFirstTime: Boolean = true,
    val navigateToOnboarding: Boolean = false,
    val navigateToHome: Boolean = false,
)