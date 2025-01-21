package com.thezayin.start_up.onboarding

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thezayin.analytics.analytics.analytics.Analytics
import com.thezayin.framework.pref.PreferencesManager
import com.thezayin.framework.remote.RemoteConfig
import com.thezayin.start_up.onboarding.actions.OnboardingActions
import com.thezayin.start_up.onboarding.model.OnboardingPage
import com.thezayin.start_up.onboarding.state.OnboardingState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OnboardingViewModel(
    application: Application,
    val analytics: Analytics,
    val remoteConfig: RemoteConfig,
    private val preferencesManager: PreferencesManager
) : ViewModel() {
    private val _state = MutableStateFlow(
        OnboardingState(
            pages = listOf(
                OnboardingPage(
                    beforeImageRes = com.thezayin.values.R.drawable.boarding_first_before,
                    afterImageRes = com.thezayin.values.R.drawable.boarding_first_after,
                    title = application.getString(com.thezayin.values.R.string.blur_natural),
                    subtitle = application.getString(com.thezayin.values.R.string.blur_natural_desc)
                ),
                OnboardingPage(
                    beforeImageRes = com.thezayin.values.R.drawable.boarding_second_before,
                    afterImageRes = com.thezayin.values.R.drawable.boarding_second_after,
                    title = application.getString(com.thezayin.values.R.string.blur_intensity),
                    subtitle = application.getString(com.thezayin.values.R.string.blur_intensity_desc)
                ),
                OnboardingPage(
                    beforeImageRes = com.thezayin.values.R.drawable.boarding_third_before,
                    afterImageRes = com.thezayin.values.R.drawable.boarding_third_after,
                    title = application.getString(com.thezayin.values.R.string.smooth_edges),
                    subtitle = application.getString(com.thezayin.values.R.string.smooth_edges_desc)
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