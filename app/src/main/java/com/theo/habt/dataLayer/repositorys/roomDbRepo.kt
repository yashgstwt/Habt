package com.theo.habt.dataLayer.repositorys

import android.database.sqlite.SQLiteConstraintException
import com.theo.habt.dataLayer.localDb.Habit
import com.theo.habt.dataLayer.localDb.HabitCompletion
import com.theo.habt.dataLayer.localDb.HabtDb
import javax.inject.Inject

interface RoomDbRepoInter{

    suspend fun getAllHabits() : List<Habit>

    suspend fun insertHabit(habit : Habit): Result<Unit>

    suspend fun deleteHabit(habit: Habit)

    suspend fun getHabitCompletionsForDateRange(habitId: Int, startDate: Long, endDate: Long): List<HabitCompletion>

    suspend fun insertHabitCompletion(habitCompletion: HabitCompletion)

    suspend fun deleteHabitCompletion(habitCompletion: HabitCompletion)
}


class RoomDbRepo @Inject constructor( private val roomDatabase: HabtDb) : RoomDbRepoInter{

    override suspend fun getAllHabits(): List<Habit> {
        return  roomDatabase.habitDao().getAllHabits()
    }

    override suspend fun insertHabit(habit: Habit): Result<Unit> {

        return runCatching {
            try {
                roomDatabase.habitDao().insertHabit(habit)
            }catch (e : SQLiteConstraintException){
                if (e.message?.contains("UNIQUE constraint failed", ignoreCase = true) == true) {
                    // Translate the specific DB error into our domain-specific error
                  throw  RepositoryError.RoomErrors.NameAlreadyExist
                } else {
                   throw RepositoryError.RoomErrors.Unknown
                }

            }
        }
    }

    override suspend fun deleteHabit(habit: Habit) {
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