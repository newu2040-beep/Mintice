package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String = "",
    val eventType: String = "Personal", // Birthday, Festival, Anniversary, Meeting, Personal, Workout, Work, Travel, Reminder, Custom
    val category: String = "Personal",
    val startDateTime: Long, // Epoch milliseconds
    val endDateTime: Long, // Epoch milliseconds
    val timezone: String = "UTC",
    val isAllDay: Boolean = false,
    val locationName: String = "",
    val locationLatitude: Double? = null,
    val locationLongitude: Double? = null,
    val colorHex: String = "#D96B82",
    val iconName: String = "cake",
    val coverImageUri: String? = null,
    val reminderEnabled: Boolean = false,
    val reminderMinutesBefore: Int = 60, // 0 = at time, 15, 60, 1440 = 1 day before
    val recurrenceRule: String = "NONE", // NONE, DAILY, WEEKLY, MONTHLY, YEARLY
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val isCompleted: Boolean = false,
    val isArchived: Boolean = false,
    val birthYear: Int? = null // For birthdays, to calculate age
)
