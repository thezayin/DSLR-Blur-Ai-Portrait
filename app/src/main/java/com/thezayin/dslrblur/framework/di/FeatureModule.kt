package com.thezayin.dslrblur.framework.di

import com.thezayin.dslrblur.framework.ads.admob.data.repository.AppOpenAdManagerImpl
import com.thezayin.dslrblur.framework.ads.admob.data.repository.InterstitialAdManagerImpl
import com.thezayin.dslrblur.framework.ads.admob.data.repository.RewardedAdManagerImpl
import com.thezayin.dslrblur.framework.ads.admob.domain.repository.AppOpenAdManager
import com.thezayin.dslrblur.framework.ads.admob.domain.repository.InterstitialAdManager
import com.thezayin.dslrblur.framework.ads.admob.domain.repository.RewardedAdManager
import com.thezayin.framework.pref.PreferencesManager
import com.thezayin.framework.remote.RemoteConfig
import com.thezayin.framework.session.SessionManager
import com.thezayin.framework.session.SessionManagerImpl
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val featureModule = module {
    single<InterstitialAdManager> { InterstitialAdManagerImpl() }
    single<AppOpenAdManager> { AppOpenAdManagerImpl() }
    single<RewardedAdManager> { RewardedAdManagerImpl() }
    single<SessionManager> { SessionManagerImpl() }
    single { Json { ignoreUnknownKeys = true } }
    single { RemoteConfig(get()) }
    single { PreferencesManager(get()) }
}