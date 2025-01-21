package com.thezayin.start_up.home.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun HomeScreenContent(
    onSettingScreenClick: () -> Unit = {},
    onEditClick: () -> Unit = {}
) {
    Scaffold(
        modifier = Modifier,
        containerColor = colorResource(com.thezayin.values.R.color.black),
        topBar = { HomeTopBar(onSettingScreenClick = onSettingScreenClick) },
        bottomBar = {
            EditButton(
                onClick = onEditClick
            )
        },
        content = { paddingValues ->
            Column {
                IntroImage(
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    )
}