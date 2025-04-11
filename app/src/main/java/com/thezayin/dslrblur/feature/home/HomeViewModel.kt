package com.thezayin.dslrblur.feature.home

import androidx.lifecycle.ViewModel
import com.thezayin.dslrblur.common.analytics.analytics.Analytics
import com.thezayin.dslrblur.framework.ads.admob.domain.repository.InterstitialAdManager
import com.thezayin.dslrblur.framework.ads.admob.domain.repository.RewardedAdManager
import com.thezayin.framework.remote.RemoteConfig

class HomeViewModel(
    val analytics: Analytics,
    val remoteConfig: RemoteConfig,
    val interstitialAdManager: InterstitialAdManager,
    val rewardedAdManager: RewardedAdManager
):ViewModel() {
}