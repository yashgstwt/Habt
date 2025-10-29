package com.theo.habt.dataLayer.typeConverters

import androidx.room.TypeConverter
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class TimeConverter {
    // This formatter is optional but good practice for consistency
    private val formatter = DateTimeFormatter.ISO_LOCAL_TIME

    @TypeConverter
    fun fromLocalTime(time: LocalTime?): String? {
        // Convert LocalTime? to String?
        return time?.format(formatter)
    }

    @TypeConverter
    fun toLocalTime(value: String?): LocalTime? {
        // Convert String? back to LocalTime?
        return value?.let {
            LocalTime.parse(it, formatter)
        }
    }
}