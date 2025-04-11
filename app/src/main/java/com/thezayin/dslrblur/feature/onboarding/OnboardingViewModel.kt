package com.thezayin.dslrblur.feature.onboarding

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thezayin.dslrblur.R
import com.thezayin.dslrblur.common.analytics.analytics.Analytics
import com.thezayin.dslrblur.feature.onboarding.actions.OnboardingActions
import com.thezayin.dslrblur.feature.onboarding.model.OnboardingPage
import com.thezayin.dslrblur.feature.onboarding.state.OnboardingState
import com.thezayin.dslrblur.framework.ads.admob.domain.repository.InterstitialAdManager
import com.thezayin.framework.pref.PreferencesManager
import com.thezayin.framework.remote.RemoteConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OnboardingViewModel(
    application: Application,
    val analytics: Analytics,
    val remoteConfig: RemoteConfig,
    val adManager: InterstitialAdManager,
    private val preferencesManager: PreferencesManager,
) : ViewModel() {
    private val _state = MutableStateFlow(
        OnboardingState(
            pages = listOf(
                OnboardingPage(
                    beforeImageRes = R.drawable.boarding_first_before,
                    afterImageRes = R.drawable.boarding_first_after,
                    title = application.getString(R.string.blur_natural),
                    subtitle = application.getString(R.string.blur_natural_desc)
                ),
                OnboardingPage(
                    beforeImageRes = R.drawable.boarding_second_before,
                    afterImageRes = R.drawable.boarding_second_after,
                    title = application.getString(R.string.blur_intensity),
                    subtitle = application.getString(R.string.blur_intensity_desc)
                ),
                OnboardingPage(
                    beforeImageRes = R.drawable.boarding_third_before,
                    afterImageRes = R.drawable.boarding_third_after,
                    title = application.getString(R.string.smooth_edges),
                    subtitle = application.getString(R.string.smooth_edges_desc)
                ),
            )
        )
    )
    val state: StateFlow<OnboardingState> = _state

    fun onAction(action: OnboardingActions) {
        viewModelScope.launch {
            when (action) {
                is OnboardingActions.NextPage -> {
                    _state.update { currentState ->
                        val newPage =
                            (currentState.currentPage + 1).coerceAtMost(currentState.pages.size - 1)
                        currentState.copy(currentPage = newPage)
                    }
                }

                is OnboardingActions.CompleteOnboarding -> {
                    _state.update { currentState ->
                        currentState.copy(isOnboardingCompleted = true)
                    }
                    preferencesManager.setOnboardingCompleted()
                }

                is OnboardingActions.ShowError -> {
                    _state.update { currentState ->
                        currentState.copy(error = action.errorMessage)
                    }
                }
            }
        }
    }
}