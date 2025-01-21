package com.thezayin.framework.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AdUnits(
    @SerialName("appOpenAd") val appOpenAd: String = "",
    @SerialName("interstitialAdOnBack") val interstitialAdOnBack: String = "",
    @SerialName("appOpenOnSplash") val appOpenOnSplash: String = "",
    @SerialName("interstitialOnOnboardingComplete") val interstitialOnOnboardingComplete: String = "",
    @SerialName("interstitialAdOnSettingClick") val interstitialAdOnSettingClick: String = "",
    @SerialName("rewardedAdOnEditClick") val rewardedAdOnEditClick: String = "",
    @SerialName("interstitialOnImageSelected") val interstitialOnImageSelected: String = "",
    @SerialName("interstitialOnAlbumSelected") val interstitialOnAlbumSelected: String = "",
    @SerialName("interstitialOnCameraClick") val interstitialOnCameraClick: String = "",
    @SerialName("rewardedAdOnImageSave") val rewardedAdOnImageSave: String = "",
    @SerialName("interstitialOnAdjustBlurIntensity") val interstitialOnAdjustBlurIntensity: String = "",
    @SerialName("interstitialOnAdjustSmoothing") val interstitialOnAdjustSmoothing: String = "",
    @SerialName("interstitialOnDismissSaveSuccess") val interstitialOnDismissSaveSuccess: String = ""
)

val defaultAdUnits = """
{
  "appOpenAd": "ca-app-pub-3940256099942544/9257395921",
  "appOpenOnSplash": "ca-app-pub-3940256099942544/9257395921",
  "interstitialAdOnBack": "ca-app-pub-3940256099942544/1033173712",
  "interstitialOnOnboardingComplete": "ca-app-pub-3940256099942544/1033173712",
  "interstitialAdOnSettingClick": "ca-app-pub-3940256099942544/1033173712",
  "rewardedAdOnEditClick": "ca-app-pub-3940256099942544/5224354917",
  "interstitialOnImageSelected": "ca-app-pub-3940256099942544/1033173712",
  "interstitialOnAlbumSelected": "ca-app-pub-3940256099942544/1033173712",
  "interstitialOnCameraClick": "ca-app-pub-3940256099942544/1033173712",
  "rewardedAdOnImageSave": "ca-app-pub-3940256099942544/5224354917",
  "interstitialOnAdjustBlurIntensity": "ca-app-pub-3940256099942544/1033173712",
  "interstitialOnAdjustSmoothing": "ca-app-pub-3940256099942544/1033173712",
  "interstitialOnDismissSaveSuccess": "ca-app-pub-3940256099942544/1033173712"
}
""".trimIndent()