package com.example.ui.screens

import android.app.DatePickerDialog
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.EventEntity
import com.example.ui.components.CategoryIcon
import com.example.ui.components.EventCard
import com.example.ui.components.MinticeTab
import com.example.ui.components.NextEventBanner
import com.example.ui.components.PolaroidCard
import com.example.ui.components.QuickActionsRow
import com.example.ui.theme.ScriptAccentStyle
import com.example.ui.theme.WashiTapePink
import com.example.ui.viewmodel.MinticeViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
    viewModel: MinticeViewModel,
    onNavigateTab: (MinticeTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentTime by viewModel.currentTimeMillis.collectAsState()
    val nextEvent by viewModel.nextEvent.collectAsState()
    val upcomingEvents by viewModel.upcomingEvents.collectAsState()
    val allMemories by viewModel.allMemories.collectAsState()
    val latestMemory = allMemories.firstOrNull()

    // Current date and dynamic greeting driven by real-time clock
    val now = Calendar.getInstance().apply { timeInMillis = currentTime }
    val hour = now.get(Calendar.HOUR_OF_DAY)
    val greeting = when (hour) {
        in 5..11 -> "Good morning,"
        in 12..16 -> "Good afternoon,"
        in 17..21 -> "Good evening,"
        else -> "Good night,"
    }

    val dateFormat = SimpleDateFormat("EEEE, d MMM", Locale.getDefault())
    val currentDateStr = dateFormat.format(Date(currentTime))

    val isCompact = com.example.ui.theme.LocalCompactMode.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = if (isCompact) PaddingValues(horizontal = 12.dp, vertical = 10.dp) else PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(if (isCompact) 12.dp else 20.dp)
    ) {
        // Top Header: Mintice Logo + Brand Text + Avatar/Search
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Mintice Symbol",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Mintice",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-0.5).sp
                        ),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { viewModel.openSearch() },
                        modifier = Modifier.size(38.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f))
                            .clickable { onNavigateTab(MinticeTab.MORE) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile & Settings",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        }

        // Date dropdown + Greeting row with pinned Polaroid
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    // Date picker trigger button
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                val cal = Calendar.getInstance()
                                DatePickerDialog(
                                    context,
                                    { _, year, month, dayOfMonth ->
                                        val selectedCal = Calendar.getInstance().apply {
                                            set(year, month, dayOfMonth)
                                        }
                                        viewModel.setSelectedCalendarDate(selectedCal.timeInMillis)
                                        onNavigateTab(MinticeTab.CALENDAR)
                                    },
                                    cal.get(Calendar.YEAR),
                                    cal.get(Calendar.MONTH),
                                    cal.get(Calendar.DAY_OF_MONTH)
                                ).show()
                            }
                            .padding(vertical = 2.dp)
                    ) {
                        Text(
                            text = currentDateStr,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Select Date",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = greeting,
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Make it a beautiful day ♡",
                        style = ScriptAccentStyle.copy(fontSize = 17.sp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Polaroid Memory Card on Right
                PolaroidCard(
                    photoUri = latestMemory?.photoUri,
                    caption = latestMemory?.title?.ifBlank { "Moments" } ?: "Moments",
                    rotationDegrees = 4f,
                    showTape = true,
                    tapeColor = WashiTapePink,
                    imageAspectRatio = 1.0f,
                    outerPadding = 5.dp,
                    bottomChinHeight = 12.dp,
                    modifier = Modifier.width(76.dp),
                    onClick = {
                        if (latestMemory != null) {
                            viewModel.openMemoryDetail(latestMemory)
                        } else {
                            onNavigateTab(MinticeTab.MEMORIES)
                        }
                    }
                )
            }
        }

        // Showcase "Next Event" Banner
        item {
            NextEventBanner(
                event = nextEvent,
                onCardClick = { viewModel.openEventDetail(it) },
                onEditClick = { viewModel.openCreateEvent(it) },
                onAddClick = { viewModel.openCreateEvent(null) }
            )
        }

        // Quick Actions Row (Add Event, Calendar, Reminders, Quick Note)
        item {
            QuickActionsRow(
                onAddEventClick = { viewModel.openCreateEvent(null) },
                onCalendarClick = { onNavigateTab(MinticeTab.CALENDAR) },
                onRemindersClick = {
                    // Navigate to calendar or create reminder
                    val reminderEvent = EventEntity(
                        title = "",
                        eventType = "Reminder",
                        category = "Reminder",
                        iconName = "bell",
                        startDateTime = System.currentTimeMillis() + (60 * 60 * 1000L),
                        endDateTime = System.currentTimeMillis() + (90 * 60 * 1000L),
                        reminderEnabled = true
                    )
                    viewModel.openCreateEvent(reminderEvent)
                },
                onQuickNoteClick = {
                    onNavigateTab(MinticeTab.MEMORIES)
                }
            )
        }

        // Upcoming Section Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Upcoming",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )

                if (upcomingEvents.isNotEmpty()) {
                    Text(
                        text = "See all",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .clickable { onNavigateTab(MinticeTab.CALENDAR) }
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }

        // Upcoming Events List
        if (upcomingEvents.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(18.dp))
                        .padding(if (isCompact) 18.dp else 28.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CategoryIcon(
                            iconName = "sparkle",
                            size = 50.dp,
                            iconSize = 26.dp,
                            cornerRadius = 16.dp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No moments here yet.",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Create your first event and start collecting little moments.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        } else {
            items(upcomingEvents.take(8), key = { it.id }) { event ->
                EventCard(
                    event = event,
                    onClick = { viewModel.openEventDetail(event) }
                )
            }
        }

        // Bottom space so content isn't covered by bottom nav bar
        item {
            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}
