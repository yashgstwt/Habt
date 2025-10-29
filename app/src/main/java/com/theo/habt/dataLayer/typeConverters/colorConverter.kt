package com.theo.habt.dataLayer.typeConverters

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.room.TypeConverter

class ColorConverter {

    @TypeConverter
    fun fromColor(color: Color): Int {
        // Convert the Compose Color object to an ARGB integer
        return color.toArgb()
    }

    @TypeConverter
    fun toColor(argb: Int): Color {
        // Convert the ARGB integer back to a Compose Color object
        return Color(argb)
    }
}