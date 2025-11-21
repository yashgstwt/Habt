package com.theo.habt.dataLayer.repositorys

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import android.util.StateSet
import androidx.sqlite.SQLiteException
import com.theo.habt.dataLayer.constants.HabitWithCompletions
import com.theo.habt.dataLayer.localDb.Habit
import com.theo.habt.dataLayer.localDb.HabitCompletion
import com.theo.habt.dataLayer.localDb.HabtDb
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

interface RoomDbRepoInter{

    suspend fun getAllHabits() : Result<Flow<List<Habit?>>>

    suspend fun insertHabit(habit : Habit): Result<Unit>

    suspend fun deleteHabit(habit: Habit) : Result<Unit>

    suspend fun getHabitCompletionsForDateRange(habitId: Int, startDate: Long, endDate: Long):Result< Flow<List<HabitCompletion>>>

    suspend fun insertHabitCompletion(habitCompletion: HabitCompletion) :Result<Unit>

    suspend fun deleteHabitCompletion(habitCompletion: HabitCompletion):Result<Unit>

    suspend fun getHabitWithCompletions() : Result<Flow<List<HabitWithCompletions?>?>>
}


class RoomDbRepo @Inject constructor( private val roomDatabase: HabtDb) : RoomDbRepoInter{

    override suspend fun getAllHabits(): Result<Flow<List<Habit?>>> {
        return runCatching {
            try {
                roomDatabase.habitDao().getAllHabits()

            } catch (e: SQLiteException) {

                Log.e("RoomDbRepo", "Database error while getting all habits", e)

                throw RepositoryError.RoomErrors.FetchingFailed("Unexpected error while fetching habits ")
            }
        }
    }

    override suspend fun insertHabit(habit: Habit): Result<Unit> {

        return runCatching {
            try {
                roomDatabase.habitDao().insertHabit(habit)
            }catch (e : SQLiteConstraintException){
                Log.e("RoomDbRepo", "Database error while inserting  habits", e)

                if (e.message?.contains("UNIQUE constraint failed", ignoreCase = true) == true) {
                    // Translate the specific DB error into our domain-specific error
                  throw  RepositoryError.RoomErrors.NameAlreadyExist
                } else {
                   throw RepositoryError.RoomErrors.Unknown
                }
            }
        }
    }

    override suspend fun deleteHabit(habit: Habit) : Result<Unit> {
        return runCatching {

            try{
                roomDatabase.habitDao().deleteHabit(habit)

            }catch (e :Exception ){

            }

        }
    }

    override suspend fun getHabitCompletionsForDateRange(
        habitId: Int,
        startDate: Long,
        endDate: Long
    ): Result<Flow<List<HabitCompletion>>> {
        return runCatching {

            try {
                roomDatabase.HabitCompletionDAO().getHabitCompletionsForDateRange(habitId,startDate,endDate)
            }catch (e : SQLiteException){
                Log.e("Habit Error", e.toString())
                throw RepositoryError.RoomErrors.FetchingFailed()
            }
        }
    }

    override suspend fun insertHabitCompletion(habitCompletion: HabitCompletion) : Result<Unit> {
        return runCatching {
            roomDatabase.HabitCompletionDAO().insertHabitCompletion(habitCompletion)
        }

    }

    override suspend fun deleteHabitCompletion(habitCompletion: HabitCompletion): Result<Unit> {
        return runCatching {
            roomDatabase.HabitCompletionDAO().deleteHabitCompletion(habitCompletion)

        }
    }

    override suspend fun getHabitWithCompletions(): Result<Flow<List<HabitWithCompletions?>?>> {
        return runCatching {
            roomDatabase.habitDao().getHabitsWithCompletions()
        }
    }

}