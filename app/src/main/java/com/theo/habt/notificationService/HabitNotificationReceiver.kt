package com.theo.habt.notificationService

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit

class HabitNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // 1. Show the Notification
        showNotification(context, "Time to read!", "Don't break your streak.")

        //2.Reschedule the next notification for the next day
        val scheduler = HabitScheduler(context)
        val hour =  intent.getIntExtra("HOUR",12)
        val min = intent.getIntExtra("MIN", 0)
        val timeInMs = intent.getLongExtra("timeInMs", System.currentTimeMillis())
        scheduler.scheduleDailyNotification(hour,min, timeInMs) .also {
            Log.d("notificationMsg",  " : rescheduled for next day " + Date(timeInMs).toString() + " reschedule in millis : ${timeInMs}" )
        }
    }

    private fun showNotification(context: Context, title: String, message: String) {
        val channelId = "habit_channel"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create Channel (Required for Android 8+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Habit Reminders",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
            Log.d("notificationMsg", "rached : in noification channel() of broadcast reciver ")

        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm) // Replace with your app icon
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }
}