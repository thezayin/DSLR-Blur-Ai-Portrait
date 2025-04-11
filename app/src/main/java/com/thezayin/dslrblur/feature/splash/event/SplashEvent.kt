package com.thezayin.dslrblur.feature.splash.event

sealed class SplashEvent {
    data object LoadSplash : SplashEvent()
    data object CheckInternet : SplashEvent()
    data object NavigateToOnboarding : SplashEvent()
    data object NavigateToHome : SplashEvent()
}
