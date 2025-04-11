package com.thezayin.background_blur.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.thezayin.dslrblur.R
import ir.kaaveh.sdpcompose.sdp

/**
 * Composable displaying a slider for adjustment with a "Done" button and applying effects
 * only when the user lifts their finger.
 *
 * @param label The label indicating what is being adjusted.
 * @param initialValue The initial value of the adjustment.
 * @param valueRange The range of values for the slider.
 * @param onValueChangeFinished Callback when the slider value is finalized.
 * @param onDone Callback when the "Done" button is clicked.
 * @param modifier Modifier for styling.
 */
@Composable
fun AdjustmentSlider(
    label: String,
    initialValue: Int,
    valueRange: ClosedFloatingPointRange<Float>,
    onValueChangeFinished: (Int) -> Unit,
    onDone: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentValue by remember { mutableStateOf(initialValue.toFloat()) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "$label: ${currentValue.toInt()}",
            color = colorResource(R.color.white),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Slider(
            value = currentValue,
            onValueChange = { currentValue = it },
            onValueChangeFinished = { onValueChangeFinished(currentValue.toInt()) },
            valueRange = valueRange,
            colors = SliderDefaults.colors(
                thumbColor = colorResource(R.color.white),
                activeTrackColor = colorResource(R.color.white),
                inactiveTrackColor = colorResource(R.color.dusty_grey)
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.sdp))
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.button_color)
            ),
            onClick = onDone,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = stringResource(R.string.done), color = colorResource(R.color.white))
        }
    }
}
