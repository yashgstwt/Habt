package com.theo.habt.notificationService

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.provider.Settings
import android.util.Log
import com.theo.habt.constants.Constant
import androidx.core.net.toUri

class HabitScheduler(private val context : Context) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    private val intent = Intent(context, HabitNotificationReceiver::class.java)


    fun scheduleDailyNotification(hour : Int , min :Int , timeInMs : Long = System.currentTimeMillis()){


        Log.d("notificationMsg", "passed hour : $hour , min : $min , $timeInMs")

        val time = Calendar.getInstance().apply {
            timeInMillis = timeInMs
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, min)
            set(Calendar.SECOND, 0)
            Log.d("notificationMsg", "scheduleDaily notification ${this.time}")
        }

        val nextDay = time.apply {
            add(Calendar.DAY_OF_YEAR, 1)
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, min)
            set(Calendar.SECOND, 0)
        }
        val intentWithExtras = intent.apply {
            putExtra("HOUR",hour)
            putExtra("MIN", min)
            putExtra("timeInMs", nextDay)

        }


        val pendingIntent = PendingIntent.getBroadcast(
            context,
            Constant.NOTIFACTION_CODE,
            intentWithExtras,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )





        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                Log.d("notificationMsg", "scheduleDaily notification ${alarmManager.canScheduleExactAlarms()}")
                if (!alarmManager.canScheduleExactAlarms()) {
                    val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                        data = "package:${context.packageName}".toUri()
                    }
                    context.startActivity(intent)
                }else{
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        time.timeInMillis,
                        pendingIntent
                    )
                }
            } else {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    time.timeInMillis,
                    pendingIntent
                )
                Log.d("notificationMsg", "reached : setAlarm()")

            }
        }catch (e : Exception){
            Log.e("notification" , e.toString())
        }
    }

    fun cancelNotification() {

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            Constant.NOTIFACTION_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

}