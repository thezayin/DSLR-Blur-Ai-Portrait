package com.thezayin.framework.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AdConfigs(
    @SerialName("appOpenAd") val appOpenAd: Boolean = true,
    @SerialName("interstitialAdOnBack") val interstitialAdOnBack: Boolean = true,
    @SerialName("appOpenOnSplash") val appOpenOnSplash: Boolean = true,
    @SerialName("interstitialOnOnboardingComplete") val interstitialOnOnboardingComplete: Boolean = true,
    @SerialName("interstitialAdOnSettingClick") val interstitialAdOnSettingClick: Boolean = true,
    @SerialName("rewardedAdOnEditClick") val rewardedAdOnEditClick: Boolean = true,
    @SerialName("interstitialOnImageSelected") val interstitialOnImageSelected: Boolean = true,
    @SerialName("interstitialOnAlbumSelected") val interstitialOnAlbumSelected: Boolean = true,
    @SerialName("interstitialOnCameraClick") val interstitialOnCameraClick: Boolean = true,
    @SerialName("rewardedAdOnImageSave") val rewardedAdOnImageSave: Boolean = true,
    @SerialName("interstitialOnAdjustBlurIntensity") val interstitialOnAdjustBlurIntensity: Boolean = true,
    @SerialName("interstitialOnAdjustSmoothing") val interstitialOnAdjustSmoothing: Boolean = true,
    @SerialName("interstitialOnDismissSaveSuccess") val interstitialOnDismissSaveSuccess: Boolean = true,
)

val defaultAdConfigs = """
  {
  "appOpenAd": false,
  "appOpenOnSplash": true,
  "interstitialAdOnBack": false,
  "interstitialOnOnboardingComplete": false,
  "interstitialAdOnSettingClick": false,
  "rewardedAdOnEditClick": false,
  "interstitialOnImageSelected": false,
  "interstitialOnAlbumSelected": false,
  "interstitialOnCameraClick": false,
  "rewardedAdOnImageSave": false,
  "interstitialOnAdjustBlurIntensity": false,
  "interstitialOnAdjustSmoothing": false,
  "interstitialOnDismissSaveSuccess": false
}
""".trimIndent()