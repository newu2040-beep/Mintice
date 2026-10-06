package com.example.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun formatEventRelativeTime(startMillis: Long, endMillis: Long, isAllDay: Boolean): String {
    val now = System.currentTimeMillis()
    val eventCal = Calendar.getInstance().apply { timeInMillis = startMillis }
    val nowCal = Calendar.getInstance().apply { timeInMillis = now }

    val isSameDay = eventCal.get(Calendar.YEAR) == nowCal.get(Calendar.YEAR) &&
            eventCal.get(Calendar.DAY_OF_YEAR) == nowCal.get(Calendar.DAY_OF_YEAR)

    nowCal.add(Calendar.DAY_OF_YEAR, 1)
    val isTomorrow = eventCal.get(Calendar.YEAR) == nowCal.get(Calendar.YEAR) &&
            eventCal.get(Calendar.DAY_OF_YEAR) == nowCal.get(Calendar.DAY_OF_YEAR)

    nowCal.add(Calendar.DAY_OF_YEAR, -2)
    val isYesterday = eventCal.get(Calendar.YEAR) == nowCal.get(Calendar.YEAR) &&
            eventCal.get(Calendar.DAY_OF_YEAR) == nowCal.get(Calendar.DAY_OF_YEAR)

    val timeFormatter = SimpleDateFormat("h:mm a", Locale.getDefault())
    val timeStr = timeFormatter.format(Date(startMillis))

    return when {
        now in startMillis..endMillis -> "LIVE NOW"
        isAllDay && isSameDay -> "Today • All day"
        isAllDay && isTomorrow -> "Tomorrow • All day"
        isAllDay -> {
            val dateStr = SimpleDateFormat("MMM d", Locale.getDefault()).format(Date(startMillis))
            "$dateStr • All day"
        }
        isSameDay -> "Today • $timeStr"
        isTomorrow -> "Tomorrow • $timeStr"
        isYesterday -> "Yesterday • $timeStr"
        else -> {
            val dateStr = SimpleDateFormat("MMM d", Locale.getDefault()).format(Date(startMillis))
            "$dateStr • $timeStr"
        }
    }
}

fun getCountdownString(startMillis: Long, endMillis: Long): String {
    val now = System.currentTimeMillis()
    if (now in startMillis..endMillis) return "LIVE NOW"
    if (now > endMillis) return "Ended"

    val diffMillis = startMillis - now
    val diffMinutes = (diffMillis / (1000 * 60)).toInt()
    val diffHours = (diffMinutes / 60)
    val diffDays = (diffHours / 24)

    return when {
        diffMinutes < 60 -> "In ${diffMinutes}m"
        diffHours < 24 -> "In ${diffHours}h ${diffMinutes % 60}m"
        diffDays == 1 -> "Tomorrow"
        diffDays < 30 -> "In $diffDays days"
        else -> "In ${diffDays / 30} months"
    }
}

@Composable
fun LiveIndicatorBadge(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Row(
        modifier = modifier
            .background(Color(0xFFEAF5EE), RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .alpha(alpha)
                .background(Color(0xFF3DA365), CircleShape)
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = "LIVE NOW",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2A7A4A)
        )
    }
}
