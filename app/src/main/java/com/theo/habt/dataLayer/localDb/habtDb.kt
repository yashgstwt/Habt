package com.theo.habt.dataLayer.localDb

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.theo.habt.dataLayer.typeConverters.ColorConverter
import com.theo.habt.dataLayer.typeConverters.TimeConverter

@Database(entities = [Habit::class,HabitCompletion::class], version = 1)
@TypeConverters(ColorConverter::class , TimeConverter::class)
abstract class HabtDb() : RoomDatabase(){
    abstract fun habitDao() : HabitDAO

    abstract fun HabitCompletionDAO() : HabitCompletionDAO
}