package com.thezayin.start_up.home

import androidx.lifecycle.ViewModel
import com.thezayin.analytics.analytics.analytics.Analytics
import com.thezayin.framework.remote.RemoteConfig

class HomeViewModel(
    val analytics: Analytics,
    val remoteConfig: RemoteConfig
):ViewModel() {
}