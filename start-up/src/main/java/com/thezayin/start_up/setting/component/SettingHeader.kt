package com.thezayin.start_up.setting.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thezayin.dslrblur.R

@Preview
@Composable
fun SettingHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp) // Added horizontal padding for better layout
            .padding(top = 24.dp), // Adjusted top padding for better spacing
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.size(100.dp),
            shape = RoundedCornerShape(50.dp), // Adjusted shape for a perfect circle
            onClick = { /* Add click action if needed */ }
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_main),
                modifier = Modifier.fillMaxSize(),
                contentDescription = "Logo"
            )
        }
        Text(
            text = stringResource(id = R.string.app_name),
            fontSize = 20.sp,
            fontFamily = FontFamily(Font(R.font.gilroy_regular)),
            color = colorResource(id = R.color.white),
            modifier = Modifier.padding(top = 16.dp) // Adjusted padding for better spacing
        )
    }
}