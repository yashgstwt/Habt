package com.theo.habt.notificationService

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.core.net.toUri
import com.theo.habt.constants.Constant
import java.util.Calendar
import java.util.TimeZone

class HabitScheduler(
    private val context: Context,
    private val habitName: String
) {

    private val alarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun scheduleDailyNotification(hour: Int, min: Int) {

        val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Kolkata")).apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, min)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            // If time already passed today → schedule tomorrow
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        Log.d("notificationMsg", "Scheduled for: ${calendar.time}")

        val intent = Intent(context, HabitNotificationReceiver::class.java).apply {
            putExtra("HABIT_NAME", habitName)
            putExtra("HOUR", hour)
            putExtra("MIN", min)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            Constant.NOTIFACTION_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                !alarmManager.canScheduleExactAlarms()
            ) {
                val permissionIntent =
                    Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                        data = "package:${context.packageName}".toUri()
                    }
                context.startActivity(permissionIntent)
                return
            }

            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )

        } catch (e: Exception) {
            Log.e("notificationMsg", "Alarm error", e)
        }
    }

    fun cancelNotification() {
        val intent = Intent(context, HabitNotificationReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            Constant.NOTIFACTION_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.cancel(pendingIntent)
    }
}
