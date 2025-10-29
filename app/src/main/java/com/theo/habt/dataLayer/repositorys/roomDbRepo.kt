package com.theo.habt.dataLayer.repositorys

import com.theo.habt.dataLayer.localDb.Habit
import com.theo.habt.dataLayer.localDb.HabitCompletion
import com.theo.habt.dataLayer.localDb.HabtDb
import javax.inject.Inject

interface RoomDbRepoInter{

    suspend fun getAllHabits() : List<Habit>

    fun insertHabit(habit : Habit)

    fun deleteHabit(habit: Habit)

    suspend fun getHabitCompletionsForDateRange(habitId: Int, startDate: Long, endDate: Long): List<HabitCompletion>

    suspend fun insertHabitCompletion(habitCompletion: HabitCompletion)

    suspend fun deleteHabitCompletion(habitCompletion: HabitCompletion)
}


class RoomDbRepo @Inject constructor( private val roomDatabase: HabtDb) : RoomDbRepoInter{

    override suspend fun getAllHabits(): List<Habit> {
        return  roomDatabase.habitDao().getAllHabits()
    }

    override fun insertHabit(habit: Habit) {
        roomDatabase.habitDao().insertHabit(habit)
    }

    override fun deleteHabit(habit: Habit) {
        roomDatabase.habitDao().deleteHabit(habit)
    }

    override suspend fun getHabitCompletionsForDateRange(
        habitId: Int,
        startDate: Long,
        endDate: Long
    ): List<HabitCompletion> {
        return roomDatabase.HabitCompletionDAO().getHabitCompletionsForDateRange(habitId,startDate,endDate)
    }

    override suspend fun insertHabitCompletion(habitCompletion: HabitCompletion) {
        return roomDatabase.HabitCompletionDAO().insertHabitCompletion(habitCompletion)
    }

    override suspend fun deleteHabitCompletion(habitCompletion: HabitCompletion) {
        return roomDatabase.HabitCompletionDAO().deleteHabitCompletion(habitCompletion)
    }

}