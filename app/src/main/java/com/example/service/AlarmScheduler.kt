package com.example.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.data.model.EventEntity
import com.example.receiver.ReminderNotificationReceiver

class AlarmScheduler(private val context: Context) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

    fun scheduleReminder(event: EventEntity) {
        if (!event.reminderEnabled || alarmManager == null) return

        val now = System.currentTimeMillis()
        val calculatedTrigger = event.startDateTime - (event.reminderMinutesBefore * 60 * 1000L)

        // If the reminder offset already passed but the event itself hasn't started yet,
        // fire a reminder immediately in 2 seconds so the user doesn't miss it!
        val triggerTime = when {
            calculatedTrigger > now -> calculatedTrigger
            event.startDateTime > now -> now + 2000L // 2 seconds from now
            else -> return // Event has already finished / passed
        }

        val intent = Intent(context, ReminderNotificationReceiver::class.java).apply {
            action = "com.example.mintice.ACTION_REMINDER"
            putExtra("EXTRA_EVENT_ID", event.id)
            putExtra("EXTRA_EVENT_TITLE", event.title)
            putExtra("EXTRA_EVENT_LOCATION", event.locationName)
            putExtra("EXTRA_EVENT_TIME", event.startDateTime)
            putExtra("EXTRA_CATEGORY", event.category)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            event.id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerTime,
                        pendingIntent
                    )
                } else {
                    alarmManager.setAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerTime,
                        pendingIntent
                    )
                }
            } else {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
                )
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
            // Graceful fallback to non-exact alarm
            try {
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
                )
            } catch (ignored: Exception) {}
        }
    }

    fun cancelReminder(eventId: Long) {
        if (alarmManager == null) return
        val intent = Intent(context, ReminderNotificationReceiver::class.java).apply {
            action = "com.example.mintice.ACTION_REMINDER"
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            eventId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    /**
     * Immediately posts a test notification to the system tray
     */
    fun sendInstantNotification(
        title: String = "Mintice Real-Time Notification",
        message: String = "Plan it. Remember it. Live it. Notifications are fully active ♡",
        eventId: Long = 9999L
    ) {
        ReminderNotificationReceiver.showNotification(
            context = context,
            eventId = eventId,
            title = title,
            location = "Mintice Organizer",
            eventTime = System.currentTimeMillis(),
            category = "Reminder",
            customBody = message
        )
    }

    /**
     * Schedules a test alarm via AlarmManager to trigger after [secondsFromNow]
     */
    fun scheduleImmediateTest(secondsFromNow: Int = 5) {
        if (alarmManager == null) {
            sendInstantNotification(title = "Mintice Test Alarm", message = "Alarm triggered immediately!")
            return
        }

        val triggerTime = System.currentTimeMillis() + (secondsFromNow * 1000L)
        val intent = Intent(context, ReminderNotificationReceiver::class.java).apply {
            action = "com.example.mintice.ACTION_REMINDER"
            putExtra("EXTRA_EVENT_ID", 8888L)
            putExtra("EXTRA_EVENT_TITLE", "Test Reminder Alarm")
            putExtra("EXTRA_EVENT_LOCATION", "Real-Time System Notification")
            putExtra("EXTRA_EVENT_TIME", triggerTime)
            putExtra("EXTRA_CATEGORY", "Reminder")
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            8888,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
                )
            } else {
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
                )
            }
        } catch (e: Exception) {
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                pendingIntent
            )
        }
    }
}
