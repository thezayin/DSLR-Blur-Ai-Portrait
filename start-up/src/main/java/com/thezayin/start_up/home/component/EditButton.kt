package com.thezayin.start_up.home.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Preview
@Composable
fun EditButton(
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .height(120.sdp)
            .padding(15.sdp),
        shape = RoundedCornerShape(10.sdp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(com.thezayin.values.R.color.woodsmoke)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    onClick()
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = colorResource(com.thezayin.values.R.color.white),
                modifier = Modifier.size(30.sdp)
            )
            Text(
                text = "Edit",
                fontSize = 14.ssp,
                fontFamily = FontFamily(Font(com.thezayin.values.R.font.gilroy_bold)),
                color = colorResource(com.thezayin.values.R.color.white)
            )
        }
    }
}