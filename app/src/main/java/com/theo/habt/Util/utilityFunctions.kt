package com.theo.habt.Util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


fun convertLongToLocalDate(longDate : Long): String? {
    // Convert the Long back to a LocalDate object
    val date = LocalDate.ofEpochDay(longDate)
    val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
    val formattedDate = date.format(formatter)
    return formattedDate
}

fun getCurrentDateInLong(): Long {
    return LocalDate.now(ZoneId.systemDefault()).toEpochDay()
}


fun isNotificationsEnabled(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    } else {
        NotificationManagerCompat.from(context).areNotificationsEnabled()
    }
}
