package com.thezayin.start_up.setting.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay

@Composable
fun SettingOptionsList() {
    // State to manage visibility of each component
    val headerVisible = remember { mutableStateOf(false) }
    val otherListVisible = remember { mutableStateOf(false) }
    val legalListVisible = remember { mutableStateOf(false) }

    // LaunchedEffect to handle sequential animation
    LaunchedEffect(Unit) {
        // Delay and show header
        delay(300) // Adjust delay as needed
        headerVisible.value = true

        // Delay and show other list
        delay(300) // Adjust delay as needed
        otherListVisible.value = true

        // Delay and show legal list
        delay(300) // Adjust delay as needed
        legalListVisible.value = true
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Animated visibility for header
        AnimatedVisibility(
            visible = headerVisible.value,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(
                    durationMillis = 1000, // Adjust duration as needed
                    easing = LinearOutSlowInEasing
                )
            ),
        ) {
            SettingHeader()
        }

        // Animated visibility for other list
        AnimatedVisibility(
            visible = otherListVisible.value,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(
                    durationMillis = 1000, // Adjust duration as needed
                    easing = LinearOutSlowInEasing
                )
            ),
        ) {
            OtherListComponent()
        }

        // Animated visibility for legal list
        AnimatedVisibility(
            visible = legalListVisible.value,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(
                    durationMillis = 1000, // Adjust duration as needed
                    easing = LinearOutSlowInEasing
                )
            ),
        ) {
            LegalListComponent()
        }
    }
}