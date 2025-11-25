package com.theo.habt.Util

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


fun convertLongToLocalDate(longDate : Long): String? {
    // Convert the Long back to a LocalDate object
    val date = LocalDate.ofEpochDay(longDate)
    val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
    val formattedDate = date.format(formatter)
    return formattedDate
}