package com.example.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ReminderNotificationReceiver : BroadcastReceiver() {
    companion object {
        const val CHANNEL_ID = "mintice_events_channel"
        const val CHANNEL_NAME = "Event Reminders"

        fun showNotification(
            context: Context,
            eventId: Long,
            title: String,
            location: String = "",
            eventTime: Long = 0L,
            category: String = "Event",
            customBody: String? = null
        ) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager ?: return

            createNotificationChannel(context, notificationManager)

            val timeStr = if (eventTime > 0) {
                SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(eventTime))
            } else ""

            val contentText = customBody ?: buildString {
                if (timeStr.isNotEmpty()) append("At $timeStr")
                if (location.isNotBlank()) {
                    if (isNotEmpty()) append(" • ")
                    append(location)
                }
                if (isEmpty()) append("You have an upcoming $category reminder")
            }

            val tapIntent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra("OPEN_EVENT_ID", eventId)
            }

            val pendingIntent = PendingIntent.getActivity(
                context,
                (eventId.toInt()).coerceAtLeast(1),
                tapIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("✦ $title")
                .setContentText(contentText)
                .setStyle(NotificationCompat.BigTextStyle().bigText(contentText))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setCategory(NotificationCompat.CATEGORY_EVENT)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .build()

            notificationManager.notify((eventId.toInt()).coerceAtLeast(1), notification)
        }

        fun createNotificationChannel(context: Context, manager: NotificationManager) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val existing = manager.getNotificationChannel(CHANNEL_ID)
                if (existing == null) {
                    val channel = NotificationChannel(
                        CHANNEL_ID,
                        CHANNEL_NAME,
                        NotificationManager.IMPORTANCE_HIGH
                    ).apply {
                        description = "Reminders for events, birthdays, occasions, and celebrations"
                        enableVibration(true)
                        enableLights(true)
                    }
                    manager.createNotificationChannel(channel)
                }
            }
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        val eventId = intent.getLongExtra("EXTRA_EVENT_ID", 0L)
        val eventTitle = intent.getStringExtra("EXTRA_EVENT_TITLE") ?: "Upcoming Event"
        val eventLocation = intent.getStringExtra("EXTRA_EVENT_LOCATION") ?: ""
        val eventTime = intent.getLongExtra("EXTRA_EVENT_TIME", 0L)
        val category = intent.getStringExtra("EXTRA_CATEGORY") ?: "Occasion"

        showNotification(
            context = context,
            eventId = eventId,
            title = eventTitle,
            location = eventLocation,
            eventTime = eventTime,
            category = category
        )
    }
}
