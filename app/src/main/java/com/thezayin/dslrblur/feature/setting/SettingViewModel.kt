package com.thezayin.dslrblur.feature.setting

import androidx.lifecycle.ViewModel
import com.thezayin.dslrblur.common.analytics.analytics.Analytics
import com.thezayin.dslrblur.framework.ads.admob.domain.repository.InterstitialAdManager
import com.thezayin.framework.remote.RemoteConfig

/**
 * ViewModel for handling settings-related logic, including managing Google Ads and Remote Config.
 *
 * @param remoteConfig Manages remote configurations to dynamically control settings and features.
 */
class SettingViewModel(
    val remoteConfig: RemoteConfig,
    val analytics: Analytics,
    val adManager: InterstitialAdManager
) : ViewModel()