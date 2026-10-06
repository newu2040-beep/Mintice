package com.example.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.data.local.MinticeDatabase
import com.example.service.AlarmScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (action == Intent.ACTION_BOOT_COMPLETED ||
            action == Intent.ACTION_MY_PACKAGE_REPLACED ||
            action == Intent.ACTION_TIMEZONE_CHANGED ||
            action == Intent.ACTION_TIME_CHANGED
        ) {
            val scheduler = AlarmScheduler(context)
            val database = MinticeDatabase.getInstance(context)

            CoroutineScope(Dispatchers.IO).launch {
                val pendingEvents = database.eventDao().getPendingReminderEvents(System.currentTimeMillis())
                for (event in pendingEvents) {
                    scheduler.scheduleReminder(event)
                }
            }
        }
    }
}
