package com.thezayin.dslrblur.common.analytics.analytics

import com.thezayin.dslrblur.common.analytics.events.AnalyticsEvent

interface Analytics {
    fun logEvent(event: AnalyticsEvent)
}