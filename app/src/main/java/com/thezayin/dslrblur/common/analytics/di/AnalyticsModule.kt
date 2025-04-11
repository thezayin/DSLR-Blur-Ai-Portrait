package com.thezayin.dslrblur.common.analytics.di

import com.google.firebase.analytics.FirebaseAnalytics
import com.thezayin.dslrblur.common.analytics.analytics.Analytics
import com.thezayin.dslrblur.common.analytics.analytics.AnalyticsImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val analyticsModule = module {
    single { FirebaseAnalytics.getInstance(get()) }
    factoryOf(::AnalyticsImpl) bind Analytics::class
}