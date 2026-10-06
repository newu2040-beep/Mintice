package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun QuickActionsRow(
    onAddEventClick: () -> Unit,
    onCalendarClick: () -> Unit,
    onRemindersClick: () -> Unit,
    onQuickNoteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        QuickActionItem(
            icon = Icons.Default.Add,
            label = "Add Event",
            iconBgColor = Color(0xFF4A7BD4),
            iconTint = Color.White,
            onClick = onAddEventClick,
            modifier = Modifier.weight(1f)
        )
        QuickActionItem(
            icon = Icons.Default.CalendarToday,
            label = "Calendar",
            iconBgColor = Color(0xFF56A678),
            iconTint = Color.White,
            onClick = onCalendarClick,
            modifier = Modifier.weight(1f)
        )
        QuickActionItem(
            icon = Icons.Default.Notifications,
            label = "Reminders",
            iconBgColor = Color(0xFFE27C5A),
            iconTint = Color.White,
            onClick = onRemindersClick,
            modifier = Modifier.weight(1f)
        )
        QuickActionItem(
            icon = Icons.Default.Description,
            label = "Quick Note",
            iconBgColor = Color(0xFFDF9E33),
            iconTint = Color.White,
            onClick = onQuickNoteClick,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun QuickActionItem(
    icon: ImageVector,
    label: String,
    iconBgColor: Color,
    iconTint: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isCompact = com.example.ui.theme.LocalCompactMode.current
    val vPad = if (isCompact) 8.dp else 14.dp
    val circleSize = if (isCompact) 30.dp else 36.dp
    val iconSize = if (isCompact) 16.dp else 20.dp
    val cardRadius = if (isCompact) 12.dp else 16.dp

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(cardRadius))
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(cardRadius))
            .clickable(onClick = onClick)
            .padding(vertical = vPad, horizontal = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(circleSize)
                .background(iconBgColor.copy(alpha = 0.14f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconBgColor,
                modifier = Modifier.size(iconSize)
            )
        }
        Spacer(modifier = Modifier.height(if (isCompact) 4.dp else 8.dp))
        Text(
            text = label,
            fontSize = if (isCompact) 10.sp else 11.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}
