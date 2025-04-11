package com.thezayin.dslrblur.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.thezayin.dslrblur.feature.background_blur.BackgroundBlurScreen
import com.thezayin.dslrblur.gallery.presentation.GalleryScreen
import com.thezayin.dslrblur.feature.home.HomeScreen
import com.thezayin.dslrblur.feature.onboarding.OnboardingScreen
import com.thezayin.dslrblur.feature.setting.SettingScreen
import com.thezayin.dslrblur.feature.splash.SplashScreen

@Composable
fun NavHost(navController: NavHostController) {
    androidx.navigation.compose.NavHost(
        navController = navController, startDestination = SplashScreenNav
    ) {
        composable<SplashScreenNav> {
            SplashScreen(
                navigateToHome = { navController.navigate(HomeScreenNav) },
                navigateToOnboarding = { navController.navigate(OnBoardingScreenNav) })
        }

        composable<SettingScreenNav> {
            SettingScreen(
                onBackClick = { navController.navigateUp() }
            )
        }

        composable<HomeScreenNav> {
            HomeScreen(
                onSettingScreenClick = { navController.navigate(SettingScreenNav) },
                onEditClick = { navController.navigate(GalleryScreenNav) }
            )
        }

        composable<BackgroundBlurScreenNav> {
            BackgroundBlurScreen(onBack = { navController.popBackStack() })
        }

        composable<GalleryScreenNav> {
            GalleryScreen(navigateBack = { navController.navigateUp() },
                onNavigateToNextScreen = { navController.navigate(BackgroundBlurScreenNav) })
        }

        composable<OnBoardingScreenNav> {
            OnboardingScreen(navigateToHome = { navController.navigate(HomeScreenNav) })
        }
    }
}