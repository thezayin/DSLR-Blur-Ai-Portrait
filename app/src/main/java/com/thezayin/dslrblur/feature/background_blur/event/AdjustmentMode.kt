package com.thezayin.dslrblur.feature.background_blur.event

sealed class AdjustmentMode {
    data object None : AdjustmentMode()
    data object AdjustSmoothness : AdjustmentMode()
    data object AdjustBlurIntensity : AdjustmentMode()
}