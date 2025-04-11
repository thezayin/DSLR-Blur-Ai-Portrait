package com.thezayin.dslrblur.common.analytics.analytics

import android.annotation.SuppressLint
import com.google.firebase.analytics.FirebaseAnalytics
import com.thezayin.dslrblur.common.analytics.events.AnalyticsEvent

/**
 * Implementation of `Analytics` which logs events
 * to a Firebase backend.
 */
class AnalyticsImpl(
    private val analytics: FirebaseAnalytics,
) : Analytics {

    @SuppressLint("BinaryOperationInTimber")
    override fun logEvent(event: AnalyticsEvent) {
        event.event?.let { eventName ->
            analytics.logEvent(eventName, event.args)
        }
    }
}