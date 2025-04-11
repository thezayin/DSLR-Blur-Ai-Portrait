package com.thezayin.dslrblur.feature.home.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import ir.kaaveh.sdpcompose.sdp

@Composable
fun HomeTopBar(
    modifier: Modifier = Modifier,
    onSettingScreenClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .padding(horizontal = 10.sdp, vertical = 15.sdp)
            .fillMaxWidth()
            .statusBarsPadding(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            modifier = Modifier
                .size(20.sdp)
                .clickable {
                    onSettingScreenClick()
                },
            imageVector = Icons.Default.Menu,
            tint = colorResource(com.thezayin.dslrblur.R.color.white),
            contentDescription = null
        )
        Spacer(modifier = Modifier)
    }
}