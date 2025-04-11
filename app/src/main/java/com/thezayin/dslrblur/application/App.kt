package com.thezayin.dslrblur.application

import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.google.firebase.FirebaseApp
import com.thezayin.dslrblur.common.analytics.di.analyticsModule
import com.thezayin.dslrblur.feature.background.di.backgroundModule
import com.thezayin.dslrblur.feature.background_blur.di.blurModule
import com.thezayin.dslrblur.feature.di.startUpModule
import com.thezayin.dslrblur.gallery.presentation.di.galleryModule
import com.thezayin.dslrblur.framework.di.featureModule
import com.thezayin.framework.notification.NotificationUtil
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        NotificationUtil.createNotificationChannel(this)
        MobileAds.initialize(this) { initializationStatus ->
            Timber.d("AdMob Initialization: ${initializationStatus.adapterStatusMap}")
        }
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