package com.theo.habt.dataLayer.localDb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.theo.habt.Util.getCurrentDateInLong
import com.theo.habt.dataLayer.constants.HabitWithCompletions
import com.theo.habt.dataLayer.constants.HabitWithStatus
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

    @Transaction // Important: Ensures the query is run atomically
    @Query("SELECT * FROM habits")
    fun getHabitsWithCompletions(): Flow<List<HabitWithCompletions?>?>



    @Query("""
        SELECT 
            habits.*, 
            EXISTS (
                SELECT 1 FROM habit_completions 
                WHERE habit_id = habits.id AND completion_date = :date
            ) as isCompleted
        FROM habits
    """)
    suspend fun getHabitsWithStatusForDate(date: Long): List<HabitWithStatus>


    @Query("""
        SELECT
            habits.*,
            EXISTS (
                SELECT 1 FROM habit_completions
                WHERE habit_id = habits.id AND completion_date = :date
            ) as isCompleted
        FROM habits
    """)
     fun getHabitsWithStatusForDateFlow(date: Long): Flow<List<HabitWithStatus>>

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


@Dao
interface NextHabitScheduleDAO{

    @Insert
    suspend fun insertNextHabitSchedule(habitSchedule: NextHabitSchedule)

    @Query("UPDATE next_Habit_schedule Set date = :date WHERE habit_id = :habitID ")
    suspend fun updateNextHabitSchedule(date : Long,habitID : Int )

    @Delete
    suspend fun deleteNextHabitSchedule(habitSchedule: NextHabitSchedule)

    @Query("Select * FROM next_Habit_schedule WHERE habit_id = :habitId")
    suspend fun getNextHabitsScheduleById(habitId :Int): NextHabitSchedule

    @Query("SELECT * FROM next_Habit_schedule ")
    fun getAllNextHabitSchedule(): Flow<List<NextHabitSchedule>>

}