package com.thezayin.dslrblur.feature.di

import com.thezayin.dslrblur.feature.home.HomeViewModel
import com.thezayin.dslrblur.feature.onboarding.OnboardingViewModel
import com.thezayin.dslrblur.feature.setting.SettingViewModel
import com.thezayin.dslrblur.feature.splash.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val startUpModule = module {
    viewModelOf(::SplashViewModel)
    viewModelOf(::OnboardingViewModel)
    viewModelOf(::SettingViewModel)
    viewModelOf(::HomeViewModel)
}