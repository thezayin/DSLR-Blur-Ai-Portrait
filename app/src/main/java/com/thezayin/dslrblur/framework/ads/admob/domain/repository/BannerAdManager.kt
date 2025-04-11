package com.thezayin.dslrblur.framework.ads.admob.domain.repository

import android.app.Activity

interface BannerAdManager {
    fun loadAd(adId: String, activity: Activity)
    fun showAd(activity: Activity, showAd: Boolean)
}
