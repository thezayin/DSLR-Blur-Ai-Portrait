package com.thezayin.dslrblur.common.analytics.events

import android.os.Bundle
import com.thezayin.dslrblur.common.analytics.utils.AnalyticsConstant.AD_REVENUE
import com.thezayin.dslrblur.common.analytics.utils.AnalyticsConstant.APP_OPEN_AD
import com.thezayin.dslrblur.common.analytics.utils.AnalyticsConstant.INTERSTITIAL_AD
import com.thezayin.dslrblur.common.analytics.utils.AnalyticsConstant.NATIVE_AD
import com.thezayin.dslrblur.common.analytics.utils.AnalyticsConstant.SCREEN_VIEW
import com.thezayin.dslrblur.common.analytics.utils.AnalyticsConstant.SETTINGS_CONTACT_US
import com.thezayin.dslrblur.common.analytics.utils.AnalyticsConstant.SETTINGS_FEEDBACK
import com.thezayin.dslrblur.common.analytics.utils.AnalyticsConstant.SETTINGS_MORE_APPS
import com.thezayin.dslrblur.common.analytics.utils.AnalyticsConstant.SETTINGS_PRIVACY_POLICY
import com.thezayin.dslrblur.common.analytics.utils.AnalyticsConstant.SETTINGS_RATE_US
import com.thezayin.dslrblur.common.analytics.utils.AnalyticsConstant.SETTINGS_TERMS_CONDITION

sealed class AnalyticsEvent(
    val event: String? = null,
    val args: Bundle?
) {

    class NoInternetEvent(
        status: String
    ) : AnalyticsEvent(
        "NoInternetEvent",
        Bundle().apply {
            putString("status", status)
        }
    )

    class WarmUpEvent(
        status: String,
        success: Boolean
    ) : AnalyticsEvent(
        "WarmUpSegEvent",
        Bundle().apply {
            putString("status", status)
            putBoolean("success", success)
        }
    )

    class TryToDownloadSegEvent(
        status: String,
    ) : AnalyticsEvent(
        "TryToDownloadSegEvent",
        Bundle().apply {
            putString("status", status)
        }
    )

    class OnboardingCompletedEvent : AnalyticsEvent(
        "OnboardingCompletedEvent",
        Bundle().apply {
            putString("status", "Completed")
        }
    )

    class BackPressedEvent(
        status: String
    ) : AnalyticsEvent(
        "BackClickEvent",
        Bundle().apply {
            putString("status", status)
        }
    )

    class EditButtonClickedEvent(
        status: String
    ) : AnalyticsEvent(
        "EditButtonClickedEvent",
        Bundle().apply {
            putString("status", status)
        }
    )

    class ResetImageSelectionEvent(
        status: String
    ) : AnalyticsEvent(
        "ResetImageSelectionEvent",
        Bundle().apply {
            putString("status", status)
        }
    )

    class SelectImageEvent(
        status: String
    ) : AnalyticsEvent(
        "SelectImageEvent",
        Bundle().apply {
            putString("status", status)
        }
    )

    class SelectAlbumEvent(
        status: String
    ) : AnalyticsEvent(
        "SelectAlbumEvent",
        Bundle().apply {
            putString("status", status)
        }
    )

    class LoadAlbumsEvent(
        status: String
    ) : AnalyticsEvent(
        "LoadAlbumsEvent",
        Bundle().apply {
            putString("status", status)
        }
    )

    class DismissSaveSuccessEvent(
        screenName: String
    ) : AnalyticsEvent(
        "DismissSaveSuccessEvent",
        Bundle().apply {
            putString("screen_name", screenName)
        }
    )

    class DownloadButtonClickedEvent(
        screenName: String
    ) : AnalyticsEvent(
        "DownloadButtonClickedEvent",
        Bundle().apply {
            putString("screen_name", screenName)
        }
    )

    class BlurIntensitySliderChangedEvent(
        value: Int
    ) : AnalyticsEvent(
        "BlurIntensitySliderChangedEvent",
        Bundle().apply {
            putInt("value", value)
        }
    )

    class SmoothingSliderChangedEvent(
        value: Int
    ) : AnalyticsEvent(
        "SmoothingSliderChangedEvent",
        Bundle().apply {
            putInt("value", value)
        }
    )

    class AppOpenAdEvent(
        status: String
    ) : AnalyticsEvent(
        APP_OPEN_AD,
        Bundle().apply {
            putString("status", status)
        }
    )

    class InterstitialAdEvent(
        status: String
    ) : AnalyticsEvent(
        INTERSTITIAL_AD,
        Bundle().apply {
            putString("status", status)
        }
    )

    class AdInitializationFailed(
        event: String,
        reason: String
    ) : AnalyticsEvent(
        event,
        Bundle().apply {
            putString("reason", reason)
        }
    )

    class AdPaidEvent(
        event: String,
        provider: String,
        value: String
    ) : AnalyticsEvent(
        AD_REVENUE,
        Bundle().apply {
            putString("event", event)
            putString("provider", provider)
            putString("price", value)
        }
    )

    class NativeAdEvent(
        status: String
    ) : AnalyticsEvent(
        NATIVE_AD,
        Bundle().apply {
            putString("status", status)
        }
    )

    class ScreenViewEvent(
        status: String
    ) : AnalyticsEvent(
        SCREEN_VIEW,
        Bundle().apply {
            putString("status", status)
        }
    )


    class AdImpressionEvent(event: String, provider: String) : AnalyticsEvent(
        event,
        Bundle().apply {
            putString("ad_provider", provider)
        }
    )

    class SettingsRateUs(
        status: String
    ) : AnalyticsEvent(
        SETTINGS_RATE_US,
        Bundle().apply {
            putString("status", status)
        }
    )

    class SettingsFeedback(
        status: String
    ) : AnalyticsEvent(
        SETTINGS_FEEDBACK,
        Bundle().apply {
            putString("status", status)
        }
    )

    class SettingsContactUs(
        status: String
    ) : AnalyticsEvent(
        SETTINGS_CONTACT_US,
        Bundle().apply {
            putString("status", status)
        }
    )

    class SettingsTermsConditions(
        status: String
    ) : AnalyticsEvent(
        SETTINGS_TERMS_CONDITION,
        Bundle().apply {
            putString("status", status)
        }
    )

    class SettingsPrivacyPolicy(
        status: String
    ) : AnalyticsEvent(
        SETTINGS_PRIVACY_POLICY,
        Bundle().apply {
            putString("status", status)
        }
    )

    class SettingMoreApps(
        status: String
    ) : AnalyticsEvent(
        SETTINGS_MORE_APPS,
        Bundle().apply {
            putString("status", status)
        }
    )
}