package com.thezayin.dslrblur.feature.setting.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.thezayin.dslrblur.R

/**
 * Composable function that defines the content layout for the settings screen.
 *
 * @param onBackClick Callback function to handle the back button click action.
 */
@Composable
fun SettingScreenContent(
    onBackClick: () -> Unit,
) {

    Scaffold(
        modifier = Modifier
            .statusBarsPadding()  // Add padding for status bars
            .navigationBarsPadding(),  // Add padding for navigation bars
        containerColor = colorResource(id = R.color.black),  // Set the background color
        topBar = {
            // Top bar component with back button and screen title
            TopBar(
                modifier = Modifier,
                screenTitle = "Settings",  // Updated to "Settings" for better readability
                onBackClick = {
                    onBackClick()  // Trigger back action
                },
            )
        }
    ) { paddingValues ->
        // Main content of the screen
        Column(
            modifier = Modifier
                .padding(paddingValues)  // Apply padding from Scaffold
                .padding(top = 60.dp)  // Additional top padding
                .fillMaxSize()  // Fill the available screen space
                .verticalScroll(rememberScrollState())  // Enable vertical scrolling
        ) {
            // List of settings options (replace with your actual content)
            SettingOptionsList()
        }
    }
}