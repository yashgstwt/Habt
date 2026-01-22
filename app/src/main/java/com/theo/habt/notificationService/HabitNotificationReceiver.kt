package com.theo.habt.notificationService

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.theo.habt.R

class HabitNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val habitName = intent.getStringExtra("HABIT_NAME") ?: "your habit"
        val hour = intent.getIntExtra("HOUR", 12)
        val min = intent.getIntExtra("MIN", 0)

        Log.d("notificationMsg", "Alarm received for $habitName")

        showNotification(
            context,
            "Time for $habitName",
            "Don't break your streak!"
        )

        // Reschedule for next day
        HabitScheduler(context, habitName)
            .scheduleDailyNotification(hour, min)
    }

    private fun showNotification(
        context: Context,
        title: String,
        message: String
    ) {
        val channelId = "habit_channel"
        val manager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Habit Reminders",
                NotificationManager.IMPORTANCE_HIGH
            )
            manager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.android) // use your icon
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        manager.notify(System.currentTimeMillis().toInt(), notification)
    }
}
