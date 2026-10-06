package com.example.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.data.model.EventEntity
import com.example.ui.components.CategoryIcon
import com.example.ui.components.PolaroidCard
import com.example.ui.components.getCategoryColors
import com.example.ui.theme.LocalCompactMode
import com.example.ui.theme.ScriptAccentStyle
import com.example.ui.theme.WashiTapePink
import com.example.ui.viewmodel.MinticeViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun CreateEventScreen(
    viewModel: MinticeViewModel,
    eventToEdit: EventEntity?,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isCompact = LocalCompactMode.current

    // Form state
    var title by remember { mutableStateOf(eventToEdit?.title ?: "") }
    var description by remember { mutableStateOf(eventToEdit?.description ?: "") }
    var category by remember { mutableStateOf(eventToEdit?.category ?: "Birthday") }
    var iconName by remember { mutableStateOf(eventToEdit?.iconName ?: "cake") }
    var locationName by remember { mutableStateOf(eventToEdit?.locationName ?: "") }
    var coverImageUri by remember { mutableStateOf<String?>(eventToEdit?.coverImageUri) }

    var startDateTime by remember {
        mutableLongStateOf(
            eventToEdit?.startDateTime ?: (System.currentTimeMillis() + (60 * 60 * 1000L))
        )
    }
    var endDateTime by remember {
        mutableLongStateOf(
            eventToEdit?.endDateTime ?: (System.currentTimeMillis() + (3 * 60 * 60 * 1000L))
        )
    }

    var isAllDay by remember { mutableStateOf(eventToEdit?.isAllDay ?: false) }
    var reminderEnabled by remember { mutableStateOf(eventToEdit?.reminderEnabled ?: true) }
    var reminderMinutesBefore by remember { mutableIntStateOf(eventToEdit?.reminderMinutesBefore ?: 60) }
    var recurrenceRule by remember { mutableStateOf(eventToEdit?.recurrenceRule ?: "NONE") }

    // Notification permission launcher for Android 13+
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            Toast.makeText(context, "Notifications are off. Reminders won't show in the tray.", Toast.LENGTH_SHORT).show()
        }
    }

    // Photo picker launcher (Zero-permission Android standard)
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            coverImageUri = uri.toString()
        }
    }

    val availableCategories = listOf(
        Pair("Birthday", "cake"),
        Pair("Festival", "festival"),
        Pair("Anniversary", "heart"),
        Pair("Personal", "sparkle"),
        Pair("Health", "fitness"),
        Pair("Work", "work"),
        Pair("Travel", "flight"),
        Pair("Reminder", "bell")
    )

    val dateFormat = SimpleDateFormat("d MMM yyyy", Locale.getDefault())
    val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())

    val cardCornerRadius = if (isCompact) 14.dp else 18.dp
    val cardPaddingHorizontal = if (isCompact) 12.dp else 14.dp
    val cardPaddingVertical = if (isCompact) 8.dp else 10.dp
    val sectionSpacing = if (isCompact) 10.dp else 14.dp

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Bar: Close (X), Title, Save pill button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = if (isCompact) 8.dp else 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onClose,
                modifier = Modifier.size(if (isCompact) 34.dp else 38.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            Text(
                text = if (eventToEdit == null) "Create Event" else "Edit Event",
                style = if (isCompact) MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        else MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )

            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        val event = EventEntity(
                            id = eventToEdit?.id ?: 0L,
                            title = title.trim(),
                            description = description.trim(),
                            eventType = category,
                            category = category,
                            iconName = iconName,
                            colorHex = getCategoryColors(iconName).second.toString(),
                            startDateTime = startDateTime,
                            endDateTime = if (endDateTime >= startDateTime) endDateTime else startDateTime + (60 * 60 * 1000L),
                            isAllDay = isAllDay,
                            locationName = locationName.trim(),
                            coverImageUri = coverImageUri,
                            reminderEnabled = reminderEnabled,
                            reminderMinutesBefore = reminderMinutesBefore,
                            recurrenceRule = recurrenceRule
                        )
                        viewModel.saveEvent(event)
                        Toast.makeText(
                            context,
                            if (reminderEnabled) "Event saved with reminder ♡" else "Event saved ♡",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(context, "Please enter an event title", Toast.LENGTH_SHORT).show()
                    }
                },
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                contentPadding = PaddingValues(horizontal = if (isCompact) 14.dp else 18.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Save",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // Form Fields in LazyColumn
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = if (isCompact) PaddingValues(horizontal = 14.dp, vertical = 8.dp) else PaddingValues(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(sectionSpacing)
        ) {
            // Pinned Polaroid Cover Header
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    PolaroidCard(
                        photoUri = coverImageUri,
                        caption = if (coverImageUri != null) title.ifBlank { "Event Cover" } else null,
                        rotationDegrees = 0f,
                        showTape = true,
                        tapeColor = WashiTapePink,
                        imageAspectRatio = 1.3f,
                        outerPadding = if (isCompact) 6.dp else 8.dp,
                        bottomChinHeight = if (isCompact) 10.dp else 14.dp,
                        modifier = Modifier
                            .fillMaxWidth(if (isCompact) 0.65f else 0.72f)
                            .clickable {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
                            .clickable {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            }
                            .padding(horizontal = 14.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (coverImageUri == null) "+ Add cover photo" else "Change cover",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // Title Input Card
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(cardCornerRadius))
                        .background(MaterialTheme.colorScheme.surface)
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(cardCornerRadius))
                        .padding(horizontal = cardPaddingHorizontal, vertical = cardPaddingVertical),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CategoryIcon(
                        iconName = iconName,
                        size = if (isCompact) 32.dp else 38.dp,
                        iconSize = if (isCompact) 18.dp else 20.dp,
                        cornerRadius = if (isCompact) 10.dp else 12.dp
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        placeholder = { Text("Event title...", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Category Chips Row
            item {
                Column {
                    Text(
                        text = "Category",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(availableCategories) { (catName, catIcon) ->
                            val isSelected = category.equals(catName, ignoreCase = true)
                            val (contColor, tintColor) = getCategoryColors(catIcon)

                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(
                                        if (isSelected) contColor else MaterialTheme.colorScheme.surface
                                    )
                                    .border(
                                        1.dp,
                                        if (isSelected) tintColor else MaterialTheme.colorScheme.outlineVariant,
                                        RoundedCornerShape(14.dp)
                                    )
                                    .clickable {
                                        category = catName
                                        iconName = catIcon
                                    }
                                    .padding(horizontal = 12.dp, vertical = if (isCompact) 6.dp else 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CategoryIcon(
                                    iconName = catIcon,
                                    size = 22.dp,
                                    iconSize = 13.dp,
                                    cornerRadius = 7.dp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = catName,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) tintColor else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }

            // Date & Time Row
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(cardCornerRadius))
                        .background(MaterialTheme.colorScheme.surface)
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(cardCornerRadius))
                        .padding(horizontal = cardPaddingHorizontal, vertical = if (isCompact) 8.dp else 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Date picker trigger
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                val cal = Calendar.getInstance().apply { timeInMillis = startDateTime }
                                DatePickerDialog(
                                    context,
                                    { _, year, month, day ->
                                        val newCal = Calendar.getInstance().apply {
                                            timeInMillis = startDateTime
                                            set(year, month, day)
                                        }
                                        startDateTime = newCal.timeInMillis
                                        if (endDateTime < startDateTime) {
                                            endDateTime = startDateTime + (60 * 60 * 1000L)
                                        }
                                    },
                                    cal.get(Calendar.YEAR),
                                    cal.get(Calendar.MONTH),
                                    cal.get(Calendar.DAY_OF_MONTH)
                                ).show()
                            }
                            .padding(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = "Date",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = dateFormat.format(Date(startDateTime)),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    // Time picker trigger
                    if (!isAllDay) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    val cal = Calendar.getInstance().apply { timeInMillis = startDateTime }
                                    TimePickerDialog(
                                        context,
                                        { _, hourOfDay, minute ->
                                            val newCal = Calendar.getInstance().apply {
                                                timeInMillis = startDateTime
                                                set(Calendar.HOUR_OF_DAY, hourOfDay)
                                                set(Calendar.MINUTE, minute)
                                            }
                                            startDateTime = newCal.timeInMillis
                                            endDateTime = startDateTime + (2 * 60 * 60 * 1000L)
                                        },
                                        cal.get(Calendar.HOUR_OF_DAY),
                                        cal.get(Calendar.MINUTE),
                                        false
                                    ).show()
                                }
                                .padding(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = "Time",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = timeFormat.format(Date(startDateTime)),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    }
                }
            }

            // Location Input
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(cardCornerRadius))
                        .background(MaterialTheme.colorScheme.surface)
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(cardCornerRadius))
                        .padding(horizontal = cardPaddingHorizontal, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = locationName,
                        onValueChange = { locationName = it },
                        placeholder = { Text("Location (e.g. Home, Cafe, Central Park...)", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Notes Input
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(cardCornerRadius))
                        .background(MaterialTheme.colorScheme.surface)
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(cardCornerRadius))
                        .padding(horizontal = cardPaddingHorizontal, vertical = 6.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Description,
                        contentDescription = "Notes",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        placeholder = { Text("Add notes (optional)...", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        maxLines = 4,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Reminder Toggle Card
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(cardCornerRadius))
                        .background(MaterialTheme.colorScheme.surface)
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(cardCornerRadius))
                        .padding(horizontal = cardPaddingHorizontal, vertical = cardPaddingVertical)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Set reminder",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Switch(
                            checked = reminderEnabled,
                            onCheckedChange = {
                                reminderEnabled = it
                                if (it && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    val check = ContextCompat.checkSelfPermission(
                                        context,
                                        android.Manifest.permission.POST_NOTIFICATIONS
                                    )
                                    if (check != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                                        notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                                    }
                                }
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = MaterialTheme.colorScheme.primary
                            )
                        )
                    }

                    if (reminderEnabled) {
                        Spacer(modifier = Modifier.height(10.dp))
                        val reminderOptions = listOf(
                            Pair(0, "At time"),
                            Pair(15, "15m before"),
                            Pair(60, "1 hr before"),
                            Pair(1440, "1 day before")
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            for ((minutes, label) in reminderOptions) {
                                val isSelected = reminderMinutesBefore == minutes
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(
                                            if (isSelected) MaterialTheme.colorScheme.primaryContainer
                                            else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                        )
                                        .clickable { reminderMinutesBefore = minutes }
                                        .padding(vertical = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = label,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Recurrence Row
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(cardCornerRadius))
                        .background(MaterialTheme.colorScheme.surface)
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(cardCornerRadius))
                        .padding(horizontal = cardPaddingHorizontal, vertical = if (isCompact) 8.dp else 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Repeat,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Repeats",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    val rules = listOf("NONE", "DAILY", "WEEKLY", "MONTHLY", "YEARLY")
                    val displayRule = when (recurrenceRule) {
                        "DAILY" -> "Every day"
                        "WEEKLY" -> "Every week"
                        "MONTHLY" -> "Every month"
                        "YEARLY" -> "Every year"
                        else -> "Never"
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                            .clickable {
                                val currentIndex = rules.indexOf(recurrenceRule)
                                val nextIndex = (currentIndex + 1) % rules.size
                                recurrenceRule = rules[nextIndex]
                            }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = displayRule,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // Cozy script footer note
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "✦ Small moments, big happiness ♡ ✦",
                    style = ScriptAccentStyle.copy(fontSize = 15.sp),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }
}
