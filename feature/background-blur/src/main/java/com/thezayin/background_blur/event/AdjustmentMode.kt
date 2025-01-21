package com.thezayin.background_blur.event

sealed class AdjustmentMode {
    object None : AdjustmentMode()
    object AdjustSmoothness : AdjustmentMode()
    object AdjustBlurIntensity : AdjustmentMode()
}