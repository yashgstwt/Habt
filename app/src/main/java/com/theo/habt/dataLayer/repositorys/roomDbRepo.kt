package com.theo.habt.dataLayer.repositorys

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import androidx.sqlite.SQLiteException
import com.theo.habt.Util.Response
import com.theo.habt.Util.getCurrentDateInLong
import com.theo.habt.dataLayer.constants.HabitWithCompletions
import com.theo.habt.dataLayer.constants.HabitWithStatus
import com.theo.habt.dataLayer.localDb.Habit
import com.theo.habt.dataLayer.localDb.HabitCompletion
import com.theo.habt.dataLayer.localDb.HabtDb
import com.theo.habt.dataLayer.localDb.NextHabitSchedule
import com.theo.habt.dataLayer.localDb.NextHabitScheduleDAO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface RoomDbRepoInter  {

    suspend fun getAllHabits() : Result<Flow<List<Habit?>>>
    suspend fun getAllHabitsForWidgetWithStatus(date :Long ) : List<HabitWithStatus>
     fun getAllHabitsForWidgetWithStatusFlow(date :Long ) : Flow<List<HabitWithStatus>>

    suspend fun getHabitWithCompletionsInRange(
        startDate: Long,
        endDate: Long
    ): Result<Flow<List<HabitWithCompletions>>>
    suspend fun insertHabit(habit : Habit): Result<Unit>

    suspend fun deleteHabit(habit: Habit) : Result<Unit>

    suspend fun getHabitCompletionsForDateRange(habitId: Int, startDate: Long, endDate: Long):Result< Flow<List<HabitCompletion>>>

    suspend fun insertHabitCompletion(habitCompletion: HabitCompletion) :Result<Unit>

    suspend fun deleteHabitCompletion(habitCompletion: HabitCompletion):Result<Unit>

    suspend fun getHabitWithCompletions() : Result<Flow<List<HabitWithCompletions?>?>>

    suspend fun insertNextHabitSchedule(habitSchedule: NextHabitSchedule)

    suspend fun updateNextHabitSchedule(date: Long, habitID: Int)

    suspend fun deleteNextHabitSchedule(habitSchedule: NextHabitSchedule)

    suspend fun getNextHabitsScheduleById(habitId: Int): Flow<Response>

    suspend fun getAllNextHabitSchedule(): Flow<Response>

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

    // In RoomDbRepo.kt

    override suspend fun getHabitWithCompletionsInRange(
        startDate: Long,
        endDate: Long
    ): Result<Flow<List<HabitWithCompletions>>> {
        return runCatching {

            roomDatabase.habitDao().getHabitsWithCompletionsInRange(startDate, endDate).map { flatList ->
                flatList.groupBy { it.habit }.map { (habit, joins) ->
                    HabitWithCompletions(
                        habit = habit,
                        // Filter out nulls (where the LEFT JOIN found no completion for that date)
                        completions = joins.mapNotNull { it.completion }
                    )
                }
            }
        }
    }



    override suspend fun getAllHabitsForWidgetWithStatus(date: Long): List<HabitWithStatus> {

        return roomDatabase.habitDao().getHabitsWithStatusForDate(date)
    }

    override  fun getAllHabitsForWidgetWithStatusFlow(date: Long): Flow<List<HabitWithStatus>> {
        return roomDatabase.habitDao().getHabitsWithStatusForDateFlow(date)
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
                Log.e("RoomError", e.toString())
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

    override suspend fun insertNextHabitSchedule(habitSchedule: NextHabitSchedule) {
        roomDatabase.NextHabitScheduleDAO().insertNextHabitSchedule(habitSchedule)
    }

    override suspend fun updateNextHabitSchedule(date: Long, habitID: Int) {
        roomDatabase.NextHabitScheduleDAO().updateNextHabitSchedule(date, habitID)
    }

    override suspend fun deleteNextHabitSchedule(habitSchedule: NextHabitSchedule) {
    }

    override suspend fun getNextHabitsScheduleById(habitId: Int): Flow<Response> {
        return flow {
            emit(Response.Loading)
            try {
                emit(Response.Success(roomDatabase.NextHabitScheduleDAO().getNextHabitsScheduleById(habitId)))
            }catch (e: Exception ){
                emit(Response.Error("Failed to fetch the data"))
            }
        }
    }

    override suspend fun getAllNextHabitSchedule(): Flow<Response> {
        return flow {
                emit(Response.Loading)
            try {
                emit(Response.Success(roomDatabase.NextHabitScheduleDAO().getAllNextHabitSchedule()))
            }catch (e : Exception){
                emit(Response.Error("Failed to load data"))
            }
        }
    }
}