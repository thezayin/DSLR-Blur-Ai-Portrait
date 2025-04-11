package com.thezayin.background_blur.component

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.thezayin.dslrblur.R
import ir.kaaveh.sdpcompose.sdp

/**
 * Bottom sheet dialog shown after a successful save operation.
 *
 * @param onDismiss Callback to dismiss the bottom sheet.
 * @param imageUri The Uri of the saved image.
 * @param context The Android context to handle sharing.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlurSaveSuccessDialog(
    onDismiss: () -> Unit,
    imageUri: Uri,
    context: Context
) {
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = colorResource(R.color.force_woodsmoke),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Top Cross Button
            IconButton(
                onClick = { onDismiss() },
                modifier = Modifier.align(Alignment.Start)
            ) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Close",
                    tint = Color.White
                )
            }

            // Success Icon and Message
            Icon(
                painter = painterResource(R.drawable.ic_success),
                contentDescription = "Save Successful",
                modifier = Modifier.size(54.sdp),
                tint = colorResource(R.color.button_color)
            )
            Text(
                text = "Saved Successfully!",
                style = MaterialTheme.typography.titleMedium,
                color = colorResource(R.color.white)
            )
            Spacer(modifier = Modifier.height(16.sdp))
            Text(
                text = "Share to:",
                style = MaterialTheme.typography.titleMedium,
                color = colorResource(R.color.white)
            )
//            // Social Media Sharing Icons
//            Row(
//                horizontalArrangement = Arrangement.spacedBy(16.sdp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                SharingIcon(context, "Facebook", R.drawable.ic_facebook, imageUri)
//                SharingIcon(context, "WhatsApp", R.drawable.ic_whatsapp, imageUri)
//                SharingIcon(context, "Instagram", R.drawable.ic_instagram, imageUri)
//                SharingIcon(context, "Snapchat", R.drawable.ic_snapchat, imageUri)
//                SharingIcon(context, "More", R.drawable.ic_more, imageUri)
//            }
            Spacer(modifier = Modifier.height(26.sdp))
        }
    }
}

/**
 * Composable for individual sharing icons.
 *
 * @param context The Android context to handle sharing.
 * @param platform The platform name (e.g., Facebook, Instagram).
 * @param iconRes The resource ID of the platform's icon.
 * @param imageUri The Uri of the saved image to share.
 */
@Composable
fun SharingIcon(
    context: Context,
    platform: String,
    iconRes: Int,
    imageUri: Uri
) {
    val message = "Check out this amazing app I used to edit this photo!"
    Image(
        painter = painterResource(id = iconRes),
        contentDescription = platform,
        contentScale = ContentScale.Inside,
        modifier = Modifier
            .size(25.sdp)
            .clickable { shareImage(context, platform, imageUri, message) }
    )
}


/**
 * Function to share the image via the specified platform with a message.
 *
 * @param context The Android context.
 * @param platform The platform name (e.g., Facebook, Instagram).
 * @param imageUri The Uri of the saved image to share.
 * @param message The message to advertise your app.
 */
fun shareImage(context: Context, platform: String, imageUri: Uri, message: String) {
    val intent = Intent(Intent.ACTION_SEND)
    intent.setType("image/*");
    intent.putExtra(Intent.EXTRA_STREAM, imageUri)
    intent.putExtra(Intent.EXTRA_TEXT, message)
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // Ensure apps can access the file
    when (platform) {
        "Facebook" -> intent.setPackage("com.facebook.katana")
        "WhatsApp" -> intent.setPackage("com.whatsapp")
        "Instagram" -> intent.setPackage("com.instagram.android")
        "Snapchat" -> intent.setPackage("com.snapchat.android")
        "More" -> {
            // Open the default share menu
            try {
                context.startActivity(Intent.createChooser(intent, "Share Image via"))
            } catch (e: Exception) {
                FirebaseCrashlytics.getInstance().recordException(e)
            }
            return
        }
    }

    try {
        context.startActivity(Intent.createChooser(intent, "share_via"))
    } catch (e: Exception) {
        FirebaseCrashlytics.getInstance().recordException(e)
    }
}

