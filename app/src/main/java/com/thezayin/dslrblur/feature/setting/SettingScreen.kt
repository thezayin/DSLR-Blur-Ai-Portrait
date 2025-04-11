package com.thezayin.dslrblur.feature.setting

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.thezayin.dslrblur.feature.setting.component.SettingScreenContent
import org.koin.compose.koinInject

/**
 * Composable function for displaying the settings screen.
 *
 * @param onBackClick Callback function to handle the back button click action.
 */
@Composable
fun SettingScreen(
    onBackClick: () -> Unit
) {
    val viewModel: SettingViewModel = koinInject()
    val activity = LocalActivity.current as Activity
    val adManager = viewModel.adManager

    LaunchedEffect(Unit) {
        adManager.loadAd(activity)
    }

    adManager.loadAd(
        activity = activity
    )

    BackHandler {
        adManager.showAd(
            activity = activity,
            showAd = viewModel.remoteConfig.adConfigs.interstitialAdOnBack,
            adImpression = { },
            onNext = { onBackClick() }
        )
    }

    SettingScreenContent(
        onBackClick = {
            adManager.showAd(
                activity = activity,
                showAd = viewModel.remoteConfig.adConfigs.interstitialAdOnBack,
                adImpression = { },
                onNext = { onBackClick() }
            )
        }
    )
}