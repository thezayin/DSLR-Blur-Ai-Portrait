package com.thezayin.dslrblur.feature.onboarding.state

import com.thezayin.dslrblur.feature.onboarding.model.OnboardingPage

data class OnboardingState(
    val currentPage: Int = 0,
    val isOnboardingCompleted: Boolean = false,
    val pages: List<OnboardingPage> = emptyList(),
    val error: String? = null
)