package com.thezayin.dslrblur.framework.ads.admob.domain.event

interface AdEventCallbacks {
    fun onAdImpression() {}
    fun onAdFailed(error: String) {}
    fun onAdClicked() {}
    fun onAdDismissed() {}
    fun onNextAction()
}