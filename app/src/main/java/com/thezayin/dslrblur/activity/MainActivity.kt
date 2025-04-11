package com.thezayin.dslrblur.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.thezayin.dslrblur.common.analytics.analytics.Analytics
import com.thezayin.dslrblur.navigation.NavHost
import com.thezayin.dslrblur.ui.theme.DSLRBlurTheme
import com.thezayin.framework.ads.functions.appOpenAd
import com.thezayin.framework.remote.RemoteConfig
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val remoteConfig: RemoteConfig by inject()
    private val analytics: Analytics by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DSLRBlurTheme {
                val navController = rememberNavController()
                NavHost(navController = navController)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        this.appOpenAd(
            analytics = analytics,
            showLoading = {},
            hideLoading = {},
            callback = {},
            adUnitId = remoteConfig.adUnits.appOpenAd,
            showAd = remoteConfig.adConfigs.appOpenAd
        )
    }
}