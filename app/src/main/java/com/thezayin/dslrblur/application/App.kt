package com.thezayin.dslrblur.application

import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.google.firebase.FirebaseApp
import com.thezayin.analytics.di.analyticsModule
import com.thezayin.background.di.backgroundModule
import com.thezayin.background_blur.di.blurModule
import com.thezayin.framework.di.featureModule
import com.thezayin.framework.notification.NotificationUtil
import com.thezayin.presentation.di.galleryModule
import com.thezayin.start_up.di.startUpModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        NotificationUtil.createNotificationChannel(this)
        MobileAds.initialize(this)
        FirebaseApp.initializeApp(this)
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@App)
            modules(
                blurModule,
                featureModule,
                analyticsModule,
                galleryModule,
                startUpModule,
                backgroundModule,
            )
        }
    }
}