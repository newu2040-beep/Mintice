package com.example

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.example.data.local.MinticeDatabase
import com.example.data.repository.MinticeRepository
import com.example.data.repository.UserPreferencesRepository
import com.example.receiver.ReminderNotificationReceiver
import com.example.service.AlarmScheduler

class MinticeApplication : Application() {
    lateinit var database: MinticeDatabase
        private set

    lateinit var repository: MinticeRepository
        private set

    lateinit var preferencesRepository: UserPreferencesRepository
        private set

    lateinit var alarmScheduler: AlarmScheduler
        private set

    override fun onCreate() {
        super.onCreate()
        database = MinticeDatabase.getInstance(this)
        repository = MinticeRepository(
            database.eventDao(),
            database.memoryDao(),
            database.categoryDao()
        )
        preferencesRepository = UserPreferencesRepository(this)
        alarmScheduler = AlarmScheduler(this)

        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                ReminderNotificationReceiver.CHANNEL_ID,
                ReminderNotificationReceiver.CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Mintice Event Reminders & Notifications"
                enableVibration(true)
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }
}
