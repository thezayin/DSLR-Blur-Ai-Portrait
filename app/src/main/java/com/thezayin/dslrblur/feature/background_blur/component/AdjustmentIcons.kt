package com.thezayin.dslrblur.feature.background_blur.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.thezayin.dslrblur.R
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

/**
 * Composable displaying two icons for adjusting smoothness and blur intensity.
 *
 * @param onAdjustSmoothness Callback when smoothness icon is clicked.
 * @param onAdjustBlurIntensity Callback when blur intensity icon is clicked.
 * @param modifier Modifier for styling.
 */
@Preview
@Composable
fun AdjustmentIcons(
    onAdjustSmoothness: () -> Unit = {},
    onAdjustBlurIntensity: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.sdp, vertical = 20.sdp)
            .navigationBarsPadding(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(onClick = onAdjustSmoothness) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_smooth),
                    contentDescription = stringResource(R.string.adjust_edge_smoothness),
                    tint = colorResource(R.color.white)
                )
            }
            Text(
                fontSize = 8.ssp,
                fontFamily = FontFamily(Font(R.font.gilroy_regular)),
                text = stringResource(R.string.smooth_edge),
                color = colorResource(R.color.white)
            )
        }

        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(onClick = onAdjustBlurIntensity) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_blur),
                    contentDescription = stringResource(R.string.adjust_blur_intensity),
                    tint = colorResource(R.color.white)
                )
            }
            Text(
                fontSize = 8.ssp,
                text = stringResource(R.string.smooth_edge),
                fontFamily = FontFamily(Font(R.font.gilroy_regular)),
                color = colorResource(R.color.white)
            )
        }
    }
}