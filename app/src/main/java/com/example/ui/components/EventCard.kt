package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.EventEntity

@Composable
fun EventCard(
    event: EventEntity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    showCompleteToggle: Boolean = false,
    onToggleComplete: ((Boolean) -> Unit)? = null
) {
    val isCompact = com.example.ui.theme.LocalCompactMode.current
    val vPadding = if (isCompact) 8.dp else 12.dp
    val hPadding = if (isCompact) 10.dp else 14.dp
    val iconSize = if (isCompact) 38.dp else 46.dp
    val cardRadius = if (isCompact) 14.dp else 18.dp

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(cardRadius))
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(cardRadius))
            .clickable(onClick = onClick)
            .padding(horizontal = hPadding, vertical = vPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon or Cover Thumbnail
        if (!event.coverImageUri.isNullOrBlank()) {
            AsyncImage(
                model = event.coverImageUri,
                contentDescription = event.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(iconSize)
                    .clip(RoundedCornerShape(if (isCompact) 10.dp else 14.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )
        } else {
            CategoryIcon(
                iconName = event.iconName.ifBlank { event.category },
                size = iconSize,
                iconSize = if (isCompact) 18.dp else 22.dp,
                cornerRadius = if (isCompact) 10.dp else 14.dp
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Center Info
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = event.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (event.isCompleted) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
                textDecoration = if (event.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(3.dp))

            val relativeTime = formatEventRelativeTime(
                event.startDateTime,
                event.endDateTime,
                event.isAllDay
            )

            Text(
                text = relativeTime,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )

            if (event.locationName.isNotBlank()) {
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        modifier = Modifier.size(11.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = event.locationName,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Right side: Pill Chip or Checkbox or Live dot
        if (showCompleteToggle && onToggleComplete != null) {
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .clip(CircleShape)
                    .background(
                        if (event.isCompleted) Color(0xFF56A678) else Color.Transparent
                    )
                    .border(
                        1.5.dp,
                        if (event.isCompleted) Color(0xFF56A678) else Color(0xFFC7BCB3),
                        CircleShape
                    )
                    .clickable { onToggleComplete(!event.isCompleted) },
                contentAlignment = Alignment.Center
            ) {
                if (event.isCompleted) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Completed",
                        tint = Color.White,
                        modifier = Modifier.size(15.dp)
                    )
                }
            }
        } else {
            // Category Pill Chip
            val (containerColor, tintColor) = getCategoryColors(event.category)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(containerColor)
                    .padding(horizontal = 9.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = event.category,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = tintColor
                )
            }

            Spacer(modifier = Modifier.width(6.dp))

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                tint = Color(0xFFC7BCB3),
                modifier = Modifier.size(13.dp)
            )
        }
    }
}
