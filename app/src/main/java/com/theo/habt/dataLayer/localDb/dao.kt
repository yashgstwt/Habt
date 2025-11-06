package com.theo.habt.dataLayer.localDb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow


@Dao
interface HabitDAO {

    @Query("Select * from habits")
    fun getAllHabits() : Flow<List<Habit?>>

    @Insert
    suspend fun insertHabit(habit : Habit)

    @Delete
    suspend fun deleteHabit(habit: Habit)

}

@Dao
interface HabitCompletionDAO {

    @Query("SELECT * FROM habit_completions WHERE habit_id = :habitId AND completion_date BETWEEN :startDate AND :endDate ORDER BY completion_date ASC")
    fun getHabitCompletionsForDateRange(habitId: Int, startDate: Long, endDate: Long): Flow<List<HabitCompletion>>

    @Insert
    suspend fun insertHabitCompletion(habitCompletion: HabitCompletion)

    @Delete
    suspend fun deleteHabitCompletion(habitCompletion: HabitCompletion)

}