package com.thezayin.dslrblur.framework.ads.admob.domain.repository

import android.app.Activity

interface AppOpenAdManager {
    fun loadAd(activity: Activity)
    fun showAd(activity: Activity, showAd: Boolean, adImpression: () -> Unit, onNext: () -> Unit)
}
