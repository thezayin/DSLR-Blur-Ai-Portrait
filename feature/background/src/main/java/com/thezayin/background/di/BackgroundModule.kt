package com.thezayin.background.di

import android.content.Context
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.thezayin.background.data.repository.BackgroundBlurRepositoryImpl
import com.thezayin.background.domain.repository.BackgroundBlurRepository
import com.thezayin.background.domain.usecase.AdjustBlurIntensityUseCase
import com.thezayin.background.domain.usecase.ApplyBlurToBackgroundUseCase
import com.thezayin.background.domain.usecase.ApplySmoothingUseCase
import com.thezayin.background.domain.usecase.SaveBlurredImageUseCase
import com.thezayin.background.domain.usecase.SetBaseImageUseCase
import org.koin.dsl.module

val backgroundModule = module {

    factory { ApplyBlurToBackgroundUseCase(repository = get()) }
    factory { ApplySmoothingUseCase(repository = get()) }
    factory { AdjustBlurIntensityUseCase(repository = get()) }
    factory { SaveBlurredImageUseCase(context = get()) }
    single<Gson> {
        GsonBuilder()
            .setLenient()
            .create()
    }

    single<Context> { get<android.app.Application>() }
    single { SetBaseImageUseCase(get()) }
    single<FirebaseRemoteConfig> {
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(com.thezayin.values.R.xml.remote_config_defaults)
        remoteConfig
    }

    single<BackgroundBlurRepository> {
        BackgroundBlurRepositoryImpl(get())
    }
}
