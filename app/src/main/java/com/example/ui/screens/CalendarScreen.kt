package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CategoryIcon
import com.example.ui.components.EventCard
import com.example.ui.components.getCategoryColors
import com.example.ui.theme.LocalCompactMode
import com.example.ui.viewmodel.MinticeViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun CalendarScreen(
    viewModel: MinticeViewModel,
    modifier: Modifier = Modifier
) {
    val allEvents by viewModel.allEvents.collectAsState()
    val selectedDateMillis by viewModel.selectedCalendarDate.collectAsState()
    val eventsForSelectedDate by viewModel.eventsForSelectedDate.collectAsState()
    val isCompact = LocalCompactMode.current

    // Calendar month currently navigated in view
    var calendarMonthCal by remember {
        mutableStateOf(Calendar.getInstance().apply { timeInMillis = selectedDateMillis })
    }

    val selectedCal = Calendar.getInstance().apply { timeInMillis = selectedDateMillis }
    val todayCal = Calendar.getInstance()

    val selectedDateHeader = if (isSameDay(selectedCal, todayCal)) {
        "Today"
    } else {
        val df = SimpleDateFormat("EEEE, d MMM", Locale.getDefault())
        df.format(Date(selectedDateMillis))
    }

    val monthYearFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
    val monthYearText = monthYearFormat.format(calendarMonthCal.time)

    val cardRadius = if (isCompact) 16.dp else 22.dp
    val cardPadding = if (isCompact) 12.dp else 16.dp
    val sectionSpacing = if (isCompact) 10.dp else 18.dp

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = if (isCompact) PaddingValues(horizontal = 14.dp, vertical = 10.dp) else PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(sectionSpacing)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(if (isCompact) 22.dp else 26.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Calendar",
                        style = if (isCompact) MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                                else MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = (-0.5).sp
                                ),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { viewModel.openSearch() },
                        modifier = Modifier.size(if (isCompact) 34.dp else 38.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(
                        onClick = {
                            // Jump to today
                            val nowCal = Calendar.getInstance()
                            calendarMonthCal = Calendar.getInstance()
                            viewModel.setSelectedCalendarDate(nowCal.timeInMillis)
                        },
                        modifier = Modifier.size(if (isCompact) 34.dp else 38.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = "Today / Filter",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Calendar Card (Month View)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(cardRadius))
                    .background(MaterialTheme.colorScheme.surface)
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(cardRadius))
                    .padding(cardPadding)
            ) {
                // Month Navigation Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = monthYearText,
                        style = if (isCompact) MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                else MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = {
                                val next = Calendar.getInstance().apply {
                                    timeInMillis = calendarMonthCal.timeInMillis
                                    add(Calendar.MONTH, -1)
                                }
                                calendarMonthCal = next
                            },
                            modifier = Modifier.size(if (isCompact) 28.dp else 32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                contentDescription = "Previous Month",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        IconButton(
                            onClick = {
                                val next = Calendar.getInstance().apply {
                                    timeInMillis = calendarMonthCal.timeInMillis
                                    add(Calendar.MONTH, 1)
                                }
                                calendarMonthCal = next
                            },
                            modifier = Modifier.size(if (isCompact) 28.dp else 32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = "Next Month",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(if (isCompact) 8.dp else 14.dp))

                // Days of Week Row (Mon, Tue, Wed, Thu, Fri, Sat, Sun)
                val dayHeaders = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                Row(modifier = Modifier.fillMaxWidth()) {
                    for (header in dayHeaders) {
                        Text(
                            text = header,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            fontSize = if (isCompact) 11.sp else 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(if (isCompact) 6.dp else 10.dp))

                // Month Grid Days
                val daysInGrid = generateMonthGrid(calendarMonthCal)
                val weeks = daysInGrid.chunked(7)

                for (week in weeks) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = if (isCompact) 1.dp else 3.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        for (cell in week) {
                            val isSelected = isSameDay(cell.dateCal, selectedCal)
                            val isCurrentMonth = cell.isCurrentMonth
                            val hasEvents = allEvents.any { isSameDay(it.startDateTime, cell.dateCal) }

                            // Category dot color if any event
                            val eventForDay = allEvents.firstOrNull { isSameDay(it.startDateTime, cell.dateCal) }
                            val dotColor = if (eventForDay != null) {
                                getCategoryColors(eventForDay.category).second
                            } else MaterialTheme.colorScheme.primary

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1.0f)
                                    .padding(if (isCompact) 1.dp else 2.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
                                    )
                                    .clickable {
                                        viewModel.setSelectedCalendarDate(cell.dateCal.timeInMillis)
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = cell.dateCal.get(Calendar.DAY_OF_MONTH).toString(),
                                        fontSize = if (isCompact) 11.sp else 13.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = when {
                                            isSelected -> MaterialTheme.colorScheme.onPrimary
                                            isCurrentMonth -> MaterialTheme.colorScheme.onSurface
                                            else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.35f)
                                        }
                                    )

                                    if (hasEvents) {
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Box(
                                            modifier = Modifier
                                                .size(if (isCompact) 3.dp else 4.dp)
                                                .clip(CircleShape)
                                                .background(if (isSelected) MaterialTheme.colorScheme.onPrimary else dotColor)
                                        )
                                    } else {
                                        Spacer(modifier = Modifier.height(if (isCompact) 4.dp else 6.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Selected Date Header + Events Count Badge
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedDateHeader,
                    style = if (isCompact) MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            else MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${eventsForSelectedDate.size} events",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        // Events for Selected Date
        if (eventsForSelectedDate.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(cardRadius))
                        .background(MaterialTheme.colorScheme.surface)
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(cardRadius))
                        .padding(if (isCompact) 18.dp else 28.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CategoryIcon(
                            iconName = "cake",
                            size = if (isCompact) 38.dp else 46.dp,
                            iconSize = if (isCompact) 18.dp else 22.dp,
                            cornerRadius = if (isCompact) 12.dp else 14.dp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "No events on this day",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Tap + below to add an event, birthday, or reminder.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        } else {
            items(eventsForSelectedDate, key = { it.id }) { event ->
                EventCard(
                    event = event,
                    onClick = { viewModel.openEventDetail(event) },
                    showCompleteToggle = true,
                    onToggleComplete = { isDone ->
                        viewModel.toggleEventComplete(event, isDone)
                    }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

private data class CalendarGridCell(
    val dateCal: Calendar,
    val isCurrentMonth: Boolean
)

private fun generateMonthGrid(calendarMonthCal: Calendar): List<CalendarGridCell> {
    val result = mutableListOf<CalendarGridCell>()

    val cal = Calendar.getInstance().apply {
        timeInMillis = calendarMonthCal.timeInMillis
        set(Calendar.DAY_OF_MONTH, 1)
    }

    val currentMonth = cal.get(Calendar.MONTH)

    // In Europe/ISO: Monday is 1, Sunday is 7
    val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
    val startDayOffset = if (dayOfWeek == Calendar.SUNDAY) 6 else dayOfWeek - Calendar.MONDAY

    cal.add(Calendar.DAY_OF_MONTH, -startDayOffset)

    for (i in 0 until 42) { // 6 weeks * 7 days
        val cellCal = Calendar.getInstance().apply { timeInMillis = cal.timeInMillis }
        result.add(
            CalendarGridCell(
                dateCal = cellCal,
                isCurrentMonth = cellCal.get(Calendar.MONTH) == currentMonth
            )
        )
        cal.add(Calendar.DAY_OF_MONTH, 1)
    }

    return result
}

private fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
}

private fun isSameDay(timeMillis1: Long, cal2: Calendar): Boolean {
    val cal1 = Calendar.getInstance().apply { timeInMillis = timeMillis1 }
    return isSameDay(cal1, cal2)
}
