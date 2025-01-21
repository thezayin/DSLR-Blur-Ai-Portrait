package com.thezayin.start_up.di

import com.thezayin.start_up.home.HomeViewModel
import com.thezayin.start_up.onboarding.OnboardingViewModel
import com.thezayin.start_up.setting.SettingViewModel
import com.thezayin.start_up.splash.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val startUpModule = module {
    viewModelOf(::SplashViewModel)
    viewModelOf(::OnboardingViewModel)
    viewModelOf(::SettingViewModel)
    viewModelOf(::HomeViewModel)
}