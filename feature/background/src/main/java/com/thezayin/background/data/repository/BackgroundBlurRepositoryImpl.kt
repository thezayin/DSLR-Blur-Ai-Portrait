package com.thezayin.background.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.thezayin.background.data.blur.BlurUtils
import com.thezayin.background.data.segmentation.SubjectSegmentationHelper
import com.thezayin.background.domain.model.BlurResult
import com.thezayin.background.domain.repository.BackgroundBlurRepository
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.filter.GPUImageGaussianBlurFilter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * Implementation of [BackgroundBlurRepository].
 * Manages the base image, applies blur and smoothing effects, and maintains processed images.
 *
 * @param context The Android context required for GPUImage operations.
 */
class BackgroundBlurRepositoryImpl(
    private val context: Context
) : BackgroundBlurRepository {

    companion object {
        private const val TAG = "RepositoryImpl"
    }

    private var baseImage: Bitmap? = null
    private var blurredImage: Bitmap? = null
    private var smoothedImage: Bitmap? = null
    private var currentSmoothnessValue: Int = 0

    private val blurCache = mutableMapOf<Int, Bitmap>()
    private val smoothnessCache = mutableMapOf<Int, Bitmap>()

    override suspend fun setBaseImage(baseImage: Bitmap) {
        withContext(Dispatchers.Default) {
            this@BackgroundBlurRepositoryImpl.baseImage =
                baseImage.copy(Bitmap.Config.ARGB_8888, true)
            blurredImage = null
            smoothedImage = null
            currentSmoothnessValue = 0
            blurCache.clear()
            smoothnessCache.clear()
            Timber.tag(TAG).d("setBaseImage: baseImage set successfully and caches cleared.")
        }
    }

    override suspend fun applyBlurToBackground(blurRadius: Int): BlurResult? =
        withContext(Dispatchers.Default) {
            try {
                val originalBitmap = baseImage ?: run {
                    Timber.tag(TAG).d("applyBlurToBackground: baseImage is null.")
                    return@withContext null
                }

                Timber.tag(TAG).d("applyBlurToBackground: Applying blur with radius $blurRadius.")

                val maskBitmap = SubjectSegmentationHelper.getSegmentationMask(originalBitmap)
                    ?: run {
                        Timber.tag(TAG)
                            .d("applyBlurToBackground: Failed to obtain segmentation mask.")
                        return@withContext null
                    }

                Timber.tag(TAG).d("applyBlurToBackground: Segmentation mask obtained successfully.")

                val blurredBackground = blurCache[blurRadius] ?: run {
                    Timber.tag(TAG)
                        .d("applyBlurToBackground: Blurred background not in cache. Applying Gaussian Blur.")

                    val gpuImage = GPUImage(context)
                    gpuImage.setImage(originalBitmap)
                    val blurFilter = GPUImageGaussianBlurFilter(blurRadius.toFloat())
                    gpuImage.setFilter(blurFilter)
                    val blurred = gpuImage.bitmapWithFilterApplied

                    if (blurred == null) {
                        Timber.tag(TAG).d("applyBlurToBackground: Failed to apply Gaussian Blur.")
                        return@withContext null
                    }

                    blurCache[blurRadius] = blurred
                    blurred
                }

                Timber.tag(TAG).d("applyBlurToBackground: Gaussian Blur applied successfully.")

                val mergedBitmap =
                    BlurUtils.mergeWithMask(originalBitmap, blurredBackground, maskBitmap)
                        ?: run {
                            Timber.tag(TAG)
                                .d("applyBlurToBackground: Failed to merge images with mask.")
                            return@withContext null
                        }

                Timber.tag(TAG).d("applyBlurToBackground: Images merged with mask successfully.")

                blurredImage = blurredBackground

                if (currentSmoothnessValue > 0) {
                    Timber.tag(TAG)
                        .d("applyBlurToBackground: Applying existing edge smoothing with smoothness $currentSmoothnessValue.")
                    val smoothed = applyEdgeSmoothing(currentSmoothnessValue)
                    smoothedImage = smoothed
                    return@withContext BlurResult(
                        blurredBitmap = blurredBackground,
                        maskBitmap = maskBitmap,
                        mergedBitmap = smoothed
                    )
                } else {
                    smoothedImage = mergedBitmap
                    return@withContext BlurResult(
                        blurredBitmap = blurredBackground,
                        maskBitmap = maskBitmap,
                        mergedBitmap = mergedBitmap
                    )
                }
            } catch (e: Exception) {
                FirebaseCrashlytics.getInstance().recordException(e)
                Timber.tag(TAG).d("applyBlurToBackground: Exception occurred - ${e.message}")
                null
            }
        }

    override suspend fun applyEdgeSmoothing(smoothness: Int): Bitmap? =
        withContext(Dispatchers.Default) {
            try {
                val originalBitmap = baseImage ?: run {
                    Timber.tag(TAG).d("applyEdgeSmoothing: baseImage is null.")
                    return@withContext null
                }

                Timber.tag(TAG)
                    .d("applyEdgeSmoothing: Applying edge smoothing with smoothness: $smoothness.")

                val maskBitmap = SubjectSegmentationHelper.getSegmentationMask(originalBitmap)
                    ?: run {
                        Timber.tag(TAG)
                            .d("applyEdgeSmoothing: Failed to obtain segmentation mask for smoothing.")
                        return@withContext null
                    }

                Timber.tag(TAG).d("applyEdgeSmoothing: Segmentation mask obtained for smoothing.")

                val blurredMask = smoothnessCache[smoothness] ?: run {
                    Timber.tag(TAG)
                        .d("applyEdgeSmoothing: Smoothed mask not in cache. Applying optimized GPU-based Gaussian blur.")

                    val blurred = BlurUtils.blurMaskWithGPUOptimized(context, maskBitmap, smoothness.toFloat())
                        ?: run {
                            Timber.tag(TAG)
                                .d("applyEdgeSmoothing: Failed to apply optimized GPU-based Gaussian blur to mask.")
                            return@withContext null
                        }

                    smoothnessCache[smoothness] = blurred
                    blurred
                }

                Timber.tag(TAG)
                    .d("applyEdgeSmoothing: Optimized GPU-based Gaussian blur applied successfully.")

                val finalBitmap = BlurUtils.mergeWithMask(originalBitmap, blurredImage!!, blurredMask)
                    ?: run {
                        Timber.tag(TAG)
                            .d("applyEdgeSmoothing: Failed to merge images with blurred mask.")
                        return@withContext null
                    }

                Timber.tag(TAG)
                    .d("applyEdgeSmoothing: Images merged with blurred mask successfully.")

                smoothedImage = finalBitmap
                currentSmoothnessValue = smoothness

                finalBitmap
            } catch (e: Exception) {
                FirebaseCrashlytics.getInstance().recordException(e)
                Timber.tag(TAG).d("applyEdgeSmoothing: Exception occurred - ${e.message}")
                null
            }
        }

    override suspend fun getProcessedImage(): Bitmap? = withContext(Dispatchers.Default) {
        smoothedImage
    }

    override suspend fun getBlurredImage(): Bitmap? = withContext(Dispatchers.Default) {
        blurredImage
    }
}
