package com.thezayin.background.data.segmentation

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.segmentation.subject.Subject
import com.google.mlkit.vision.segmentation.subject.SubjectSegmentation
import com.google.mlkit.vision.segmentation.subject.SubjectSegmentationResult
import com.google.mlkit.vision.segmentation.subject.SubjectSegmenterOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * Helper object that uses ML Kit's Subject Segmentation API to remove backgrounds.
 */
object SubjectSegmentationHelper {

    private const val TAG = "SubjectSegmentationHelper"

    private val client = SubjectSegmentation.getClient(
        SubjectSegmenterOptions.Builder()
            .enableMultipleSubjects(
                SubjectSegmenterOptions.SubjectResultOptions.Builder()
                    .enableConfidenceMask()
                    .build()
            )
            .build()
    )

    /**
     * Generates a unified segmentation mask for the given bitmap using confidence masks.
     *
     * @param originalBitmap The original image Bitmap.
     * @return The segmentation mask Bitmap where the subject is opaque and the background is transparent.
     */
    suspend fun getSegmentationMask(originalBitmap: Bitmap): Bitmap? =
        withContext(Dispatchers.Default) {
            try {
                Timber.tag(TAG).d("Starting segmentation mask generation.")
                val inputImage = InputImage.fromBitmap(originalBitmap, 0)
                val segmentationResult: SubjectSegmentationResult =
                    client.process(inputImage).await()
                val maskBitmap = createUnifiedMask(
                    originalBitmap.width,
                    originalBitmap.height,
                    segmentationResult.subjects
                )

                Timber.tag(TAG).d("Segmentation mask generated successfully.")
                maskBitmap
            } catch (e: Exception) {
                FirebaseCrashlytics.getInstance().recordException(e)
                Timber.tag(TAG).e("Error generating segmentation mask: ${e.message}")
                null
            }
        }

    /**
     * Creates a unified mask bitmap from all detected subjects' confidence masks.
     *
     * @param width The width of the original image.
     * @param height The height of the original image.
     * @param subjects The list of detected subjects.
     * @return The unified segmentation mask Bitmap.
     */
    private fun createUnifiedMask(width: Int, height: Int, subjects: List<Subject>): Bitmap {
        Timber.tag(TAG).d("Creating unified mask from ${subjects.size} subjects.")
        val maskBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(maskBitmap)
        val paint = Paint().apply {
            color = Color.WHITE
            style = Paint.Style.FILL
            isAntiAlias = true
        }

        for (subject in subjects) {
            val confidenceMask = subject.confidenceMask
            if (confidenceMask == null) {
                Timber.tag(TAG).e("Subject confidence mask is null.")
                continue
            }

            try {
                val subjectMaskBitmap = convertConfidenceMaskToBitmap(
                    confidenceMask,
                    subject.width,
                    subject.height
                )
                canvas.drawBitmap(
                    subjectMaskBitmap,
                    subject.startX.toFloat(),
                    subject.startY.toFloat(),
                    paint
                )
                Timber.tag(TAG).d("Subject mask merged successfully.")
            } catch (e: Exception) {
                FirebaseCrashlytics.getInstance().recordException(e)
                Timber.tag(TAG).e("Error merging subject mask: ${e.message}")
            }
        }
        return maskBitmap
    }


    /**
     * Converts a confidence mask FloatBuffer to a grayscale Bitmap.
     *
     * @param mask The confidence mask FloatBuffer.
     * @param maskWidth The width of the subject's mask.
     * @param maskHeight The height of the subject's mask.
     * @return The grayscale confidence bitmap.
     */
    private fun convertConfidenceMaskToBitmap(
        mask: java.nio.FloatBuffer,
        maskWidth: Int,
        maskHeight: Int
    ): Bitmap {
        try {
            mask.rewind()
            Timber.tag(TAG)
                .d("convertConfidenceMaskToBitmap: Buffer capacity: ${mask.capacity()}, Remaining: ${mask.remaining()}")

            val expectedFloats = maskWidth * maskHeight
            if (mask.remaining() < expectedFloats) {
                Timber.tag(TAG)
                    .e("convertConfidenceMaskToBitmap: Not enough floats in buffer. Expected: $expectedFloats, Available: ${mask.remaining()}")
                throw IllegalStateException("Insufficient floats in confidence mask buffer.")
            }

            val confidencePixels = FloatArray(expectedFloats)
            mask.get(confidencePixels)

            val bitmap = Bitmap.createBitmap(maskWidth, maskHeight, Bitmap.Config.ARGB_8888)
            val pixels = IntArray(maskWidth * maskHeight)

            for (i in confidencePixels.indices) {
                val confidence = confidencePixels[i]
                val alpha = (confidence * 255).toInt().coerceIn(0, 255)
                pixels[i] = Color.argb(alpha, 255, 255, 255)
            }

            bitmap.setPixels(pixels, 0, maskWidth, 0, 0, maskWidth, maskHeight)
            return bitmap
        } catch (e: Exception) {
            Timber.tag(TAG).e("convertConfidenceMaskToBitmap: Exception - ${e.message}")
            throw e
        }
    }
}