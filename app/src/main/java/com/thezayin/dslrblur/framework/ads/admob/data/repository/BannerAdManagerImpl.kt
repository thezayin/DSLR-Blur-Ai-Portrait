package com.thezayin.dslrblur.framework.ads.admob.data.repository

import android.app.Activity
import android.util.Log
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.thezayin.dslrblur.framework.ads.admob.domain.repository.BannerAdManager
import timber.log.Timber

class BannerAdManagerImpl : BannerAdManager {
    private var bannerAdView: AdView? = null
    override fun loadAd(adId: String, activity: Activity) {
        if (bannerAdView == null) {
            bannerAdView = AdView(activity)
            bannerAdView?.adUnitId = adId

            val adRequest = AdRequest.Builder().build()
            bannerAdView?.loadAd(adRequest)
        }
    }

    override fun showAd(activity: Activity, showAd: Boolean) {
        if (showAd) {
            bannerAdView?.let { adView ->
                adView.adListener = object : com.google.android.gms.ads.AdListener() {
                    override fun onAdLoaded() {
                        Timber.tag("AdLoaded").d("Banner ad loaded successfully")
                    }

                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        super.onAdFailedToLoad(adError)
                        Timber.tag("AdError").d("Banner ad failed to load: ${adError.message}")
                    }

                    override fun onAdOpened() {
                    }

                    override fun onAdClosed() {
                    }
                }

                activity.findViewById<android.view.ViewGroup>(android.R.id.content)?.addView(adView)
            }
        }
    }
}
