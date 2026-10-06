package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class MinticeTab {
    HOME,
    CALENDAR,
    MEMORIES,
    MORE
}

@Composable
fun MinticeBottomBar(
    currentTab: MinticeTab,
    onTabSelected: (MinticeTab) -> Unit,
    onFabClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isCompact = com.example.ui.theme.LocalCompactMode.current
    val barHeight = if (isCompact) 52.dp else 64.dp
    val fabSize = if (isCompact) 46.dp else 54.dp
    val fabOffset = if (isCompact) (-14).dp else (-18).dp

    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        contentAlignment = Alignment.BottomCenter
    ) {
        // Bottom Bar Surface
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(barHeight)
                .shadow(elevation = 8.dp, ambientColor = Color(0x1F000000)),
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Home
                NavBarItem(
                    label = "Home",
                    icon = if (currentTab == MinticeTab.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                    isSelected = currentTab == MinticeTab.HOME,
                    onClick = { onTabSelected(MinticeTab.HOME) },
                    modifier = Modifier.weight(1f)
                )

                // Calendar
                NavBarItem(
                    label = "Calendar",
                    icon = if (currentTab == MinticeTab.CALENDAR) Icons.Filled.CalendarMonth else Icons.Outlined.CalendarMonth,
                    isSelected = currentTab == MinticeTab.CALENDAR,
                    onClick = { onTabSelected(MinticeTab.CALENDAR) },
                    modifier = Modifier.weight(1f)
                )

                // Spacer for the center circular FAB
                Spacer(modifier = Modifier.weight(1f))

                // Memories
                NavBarItem(
                    label = "Memories",
                    icon = if (currentTab == MinticeTab.MEMORIES) Icons.Filled.PhotoLibrary else Icons.Outlined.PhotoLibrary,
                    isSelected = currentTab == MinticeTab.MEMORIES,
                    onClick = { onTabSelected(MinticeTab.MEMORIES) },
                    modifier = Modifier.weight(1f)
                )

                // More
                NavBarItem(
                    label = "More",
                    icon = if (currentTab == MinticeTab.MORE) Icons.Filled.MoreHoriz else Icons.Outlined.MoreHoriz,
                    isSelected = currentTab == MinticeTab.MORE,
                    onClick = { onTabSelected(MinticeTab.MORE) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Center Floating Action Button (+)
        Box(
            modifier = Modifier
                .offset(y = fabOffset)
                .size(fabSize)
                .shadow(elevation = 6.dp, shape = CircleShape, ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.35f))
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onFabClick
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Create Event",
                tint = Color.White,
                modifier = Modifier.size(if (isCompact) 22.dp else 28.dp)
            )
        }
    }
}

@Composable
private fun NavBarItem(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isCompact = com.example.ui.theme.LocalCompactMode.current
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.65f)
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) MaterialTheme.colorScheme.primary else unselectedColor,
            modifier = Modifier.size(if (isCompact) 19.dp else 22.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = if (isCompact) 10.sp else 11.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            color = if (isSelected) MaterialTheme.colorScheme.primary else unselectedColor
        )
    }
}
