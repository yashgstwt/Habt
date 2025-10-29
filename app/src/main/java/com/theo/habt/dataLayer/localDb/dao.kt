package com.theo.habt.dataLayer.localDb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface HabitDAO {

    @Query("Select * from habits")
    suspend fun getAllHabits() : List<Habit>

    @Insert
    fun insertHabit(habit : Habit)

    @Delete
    fun deleteHabit(habit: Habit)

}

@Dao
interface HabitCompletionDAO {

    @Query("SELECT * FROM habit_completions WHERE habit_id = :habitId AND completion_date BETWEEN :startDate AND :endDate ORDER BY completion_date ASC")
    suspend fun getHabitCompletionsForDateRange(habitId: Int, startDate: Long, endDate: Long): List<HabitCompletion>

    @Insert
    suspend fun insertHabitCompletion(habitCompletion: HabitCompletion)

    @Delete
    suspend fun deleteHabitCompletion(habitCompletion: HabitCompletion)

}