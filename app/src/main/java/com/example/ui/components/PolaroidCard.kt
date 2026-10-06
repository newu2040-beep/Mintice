package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.theme.ScriptAccentStyle
import com.example.ui.theme.WashiTapeBeige
import com.example.ui.theme.WashiTapePink

@Composable
fun PolaroidCard(
    photoUri: String?,
    modifier: Modifier = Modifier,
    caption: String? = null,
    rotationDegrees: Float = 0f,
    showTape: Boolean = true,
    tapeColor: Color = WashiTapeBeige,
    imageAspectRatio: Float = 1.0f,
    outerPadding: Dp = 6.dp,
    bottomChinHeight: Dp = 16.dp,
    onClick: (() -> Unit)? = null
) {
    val clickModifier = if (onClick != null) {
        Modifier.clickable(onClick = onClick)
    } else Modifier

    Box(
        modifier = modifier
            .rotate(rotationDegrees)
            .then(clickModifier),
        contentAlignment = Alignment.TopCenter
    ) {
        // Main Polaroid Paper Card
        Column(
            modifier = Modifier
                .shadow(elevation = 3.dp, shape = RoundedCornerShape(8.dp), ambientColor = Color(0x1F000000))
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                .border(0.5.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(8.dp))
                .padding(outerPadding)
        ) {
            // Photo Container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(imageAspectRatio)
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                if (!photoUri.isNullOrBlank()) {
                    AsyncImage(
                        model = photoUri,
                        contentDescription = caption ?: "Photo Memory",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.matchParentSize()
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = "No photo",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier.height(28.dp)
                    )
                }
            }

            // Bottom Chin with optional caption
            if (!caption.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = caption,
                    style = ScriptAccentStyle.copy(fontSize = 11.sp),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 2.dp, vertical = 2.dp)
                )
            } else {
                Spacer(modifier = Modifier.height(bottomChinHeight))
            }
        }

        // Decorative Washi Tape at Top
        if (showTape) {
            Box(
                modifier = Modifier
                    .offset(y = (-5).dp)
                    .width(36.dp)
                    .height(10.dp)
                    .rotate(if (rotationDegrees != 0f) -1.5f else 2f)
                    .background(tapeColor, RoundedCornerShape(2.dp))
                    .border(0.5.dp, Color(0x33000000), RoundedCornerShape(2.dp))
            )
        }
    }
}
