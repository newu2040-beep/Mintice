package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.*

fun getCategoryColors(iconName: String): Pair<Color, Color> {
    return when (iconName.lowercase()) {
        "cake", "birthday" -> Pair(CategoryBirthdayContainer, CategoryBirthdayTint)
        "festival", "lantern", "sun" -> Pair(CategoryFestivalContainer, CategoryFestivalTint)
        "heart", "anniversary" -> Pair(CategoryAnniversaryContainer, CategoryAnniversaryTint)
        "fitness", "gym" -> Pair(CategoryHealthContainer, CategoryHealthTint)
        "work", "meeting" -> Pair(CategoryWorkContainer, CategoryWorkTint)
        "flight", "travel" -> Pair(CategoryTravelContainer, CategoryTravelTint)
        "bell", "reminder" -> Pair(CategoryReminderContainer, CategoryReminderTint)
        "camera", "photo" -> Pair(Color(0xFFF3EDF7), Color(0xFF865EA3))
        "coffee" -> Pair(Color(0xFFF9EFE9), Color(0xFFA56847))
        "note" -> Pair(Color(0xFFFEF8E7), Color(0xFFD49A32))
        else -> Pair(CategoryPersonalContainer, CategoryPersonalTint)
    }
}

fun getCategoryVector(iconName: String): ImageVector {
    return when (iconName.lowercase()) {
        "cake", "birthday" -> Icons.Default.Cake
        "festival", "lantern", "sun" -> Icons.Default.WbSunny
        "heart", "anniversary" -> Icons.Default.Favorite
        "fitness", "gym" -> Icons.Default.FitnessCenter
        "work", "meeting" -> Icons.Default.Work
        "flight", "travel" -> Icons.Default.Flight
        "bell", "reminder" -> Icons.Default.Notifications
        "camera", "photo" -> Icons.Default.PhotoCamera
        "coffee" -> Icons.Default.LocalCafe
        "note" -> Icons.Default.Description
        else -> Icons.Default.AutoAwesome
    }
}

@Composable
fun CategoryIcon(
    iconName: String,
    modifier: Modifier = Modifier,
    size: Dp = 44.dp,
    iconSize: Dp = 22.dp,
    cornerRadius: Dp = 14.dp,
    customTint: Color? = null,
    customContainer: Color? = null
) {
    val (defaultContainer, defaultTint) = getCategoryColors(iconName)
    val containerColor = customContainer ?: defaultContainer
    val tintColor = customTint ?: defaultTint
    val vector = getCategoryVector(iconName)

    Box(
        modifier = modifier
            .size(size)
            .background(containerColor, RoundedCornerShape(cornerRadius)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = vector,
            contentDescription = iconName,
            tint = tintColor,
            modifier = Modifier.size(iconSize)
        )
    }
}
