package com.theo.habt.dataLayer.typeConverters

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.room.TypeConverter
import java.time.LocalDate

class LocalDateConverter {

    @TypeConverter
    fun fromLocalDate(localDate: LocalDate) : Long{
        return localDate.toEpochDay()
    }

    @TypeConverter
    fun toLocalDate(epochDay :Long): LocalDate{
        return LocalDate.ofEpochDay(epochDay)
    }

}

