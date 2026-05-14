package com.theo.habt.presentation.homeScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.theo.habt.Util.Response
import com.theo.habt.Util.getCurrentDateInLong
import com.theo.habt.dataLayer.localDb.Habit
import com.theo.habt.dataLayer.localDb.HabitCompletion
import com.theo.habt.dataLayer.localDb.NextHabitSchedule
import com.theo.habt.dataLayer.repositorys.RepositoryError
import com.theo.habt.dataLayer.repositorys.RoomDbRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.Calendar


data class HabitWithCompletion(
    val habit: Habit?,
    val habitCompletions: List<Pair<Int, Boolean>>
)

data class HomeUiState(
    val habits: List<Habit?> = emptyList(),
    val habitsWithCompletions: List<HabitWithCompletion?>? = emptyList(),
    val isLoading: Boolean = false,
    val habitCompletionList: List<Boolean> = emptyList(),
    val habitWithCurrDateCompletionStatus: Float = 0f
)


@HiltViewModel
class HomeViewModal @Inject constructor(private val roomDbRepo: RoomDbRepo) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 2000),
        initialValue = HomeUiState()
    )


    init {
        getHabitWithCompletions()
        getHabitCompletionCurrStatus()
    }

    fun markAsCompleted(habitCompletion: HabitCompletion, interval: Int) {
        viewModelScope.launch {
            // 1. Check if the user is allowed to enter completion today
            val canEnter = checkCanEnter(habitCompletion.habitId, interval)
            Log.d( "canEnterCheck", canEnter.toString() + "in markAsCompleted")
            if (canEnter) {
                // 2. Insert the completion record
                roomDbRepo.insertHabitCompletion(habitCompletion)

                // 3. Calculate the NEXT schedule date (Today + Interval)
                val nextDate = LocalDate.now().plusDays(interval.toLong())
                Log.d("canEnterCheck", nextDate.toString())

                // 4. Update the NextHabitSchedule table in DB
                roomDbRepo.updateNextHabitSchedule(
                    date = nextDate.toEpochDay(),
                    habitID = habitCompletion.habitId,
                )
                Log.d("canEnterCheck", "Saved: Next entry set for $nextDate")
            } else {
                Log.d("canEnterCheck", "Cannot enter: Not the scheduled day yet.")
            }
        }
    }
    suspend fun checkCanEnter(habitId: Int, interval: Int): Boolean {
        Log.d("canEnterCheck", "hiii")// 1. Get the very first emission from the flow (whatever it is: Success, Loading, or Error)
        val response = try {
            roomDbRepo.getNextHabitsScheduleById(habitId).first()
        } catch (e: Exception) {
            Log.d("canEnterCheck", "Flow collection failed: $e")
            return false
        }

        Log.d("canEnterCheck", "hiii2 - Received: $response")

        return when (response) {
            is Response.Success -> {
                Log.d("canEnterCheck", "success")

                // Ensure the date isn't null (assuming response.response is NextHabitSchedule)
                val sDate = response.response.date ?: return false
                val today = LocalDate.now()

                Log.d("canEnterCheck", "Scheduled: $sDate, Today: $today, Interval: $interval")

                when {
                    // Case 1: Today is the day
                    sDate.isEqual(today) -> true

                    // Case 2: It's in the future
                    sDate.isAfter(today) -> false

                    // Case 3: It's in the past - calculate if today lands on the cycle
                    sDate.isBefore(today) -> {
                        val daysDiff = ChronoUnit.DAYS.between(sDate, today)
                        // If interval is 0, we avoid division by zero error
                        if (interval == 0) return sDate.isBefore(today)

                        daysDiff % interval == 0L
                    }
                    else -> false
                }
            }
            is Response.Error -> {
                Log.d("canEnterCheck", "Database returned Error: ${response.message}")
                false
            }
            else -> {
                // This handles 'Loading' or other states
                Log.d("canEnterCheck", "Response was Loading or unexpected: $response")
                false
            }
        }
    }

    fun getHabitWithCompletions() {
        val today = LocalDate.now()
        val startOfYear = LocalDate.of(today.year, 1, 1)
        val endOfYear = LocalDate.of(today.year, 12, 31)

        viewModelScope.launch {
            roomDbRepo.getHabitWithCompletionsInRange(startOfYear.toEpochDay(), endOfYear.toEpochDay()).onSuccess { flow ->
                flow.collect { habitsList ->
                    val habitWithHeatList = mutableListOf<HabitWithCompletion>()

                    habitsList.forEach { habitWithComp ->
                        val habit = habitWithComp.habit
                        val interval = habit.interval

                        // Use the yearly heatmap view for all habits with interval >= 1
                        // but only show blocks up to today, as requested.
                        val heatmap = getHeatmapForYearArray(
                            completedList = habitWithComp.completions,
                            startDate = startOfYear,
                            endDate = today,
                            interval = if (interval < 1) 1 else interval,
                            creationDateSeconds = habit.creationDate
                        )

                        habitWithHeatList.add(
                            HabitWithCompletion(
                                habit = habit,
                                habitCompletions = heatmap
                            )
                        )
                    }

                    _state.update { it.copy(habitsWithCompletions = habitWithHeatList) }
                }

            }.onFailure { exception ->
                when (exception) {
                    is RepositoryError.RoomErrors.FetchingFailed -> {
                        Log.d("habitError", RepositoryError.RoomErrors.FetchingFailed().msg)
                        _state.update {
                            it.copy(isLoading = true)
                        }
                    }
                }
            }
        }
    }

    fun getHeatmapForYearArray(
        completedList: List<HabitCompletion>,
        startDate: LocalDate,
        endDate: LocalDate, // This is 'today'
        interval: Int = 1,
        creationDateSeconds: Long = 0L
    ): List<Pair<Int, Boolean>> {
        val daysList = mutableListOf<Pair<Int, Boolean>>()

        // Create a set of completed dates for fast lookup
        val completedDatesSet = completedList.mapNotNull { it.completionDate }.toSet()

        if (interval <= 1) {
            // Case for Everyday: Show all days from Jan 1st to Today
            var current = startDate
            var dayCounter = 1
            while (!current.isAfter(endDate)) {
                val isCompleted = completedDatesSet.contains(current.toEpochDay())
                daysList.add(Pair(dayCounter, isCompleted))
                current = current.plusDays(1)
                dayCounter++
            }
        } else {
            // Case for Interval: Show only scheduled days up to Today.
            val creationDate = Instant.ofEpochSecond(creationDateSeconds)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()

            // Project the anchor date to the first occurrence on or after startDate
            var runDate = creationDate
            if (runDate.isBefore(startDate)) {
                val diff = ChronoUnit.DAYS.between(runDate, startDate)
                val skip = (diff + interval - 1) / interval
                runDate = runDate.plusDays(skip * interval)
            }

            while (!runDate.isAfter(endDate)) {
                val isCompleted = completedDatesSet.contains(runDate.toEpochDay())
                val dayNum = (ChronoUnit.DAYS.between(startDate, runDate) + 1).toInt()
                daysList.add(Pair(dayNum, isCompleted))
                runDate = runDate.plusDays(interval.toLong())
            }
        }

        return daysList
    }

    fun getHabitCompletionCurrStatus() {
        viewModelScope.launch {

            roomDbRepo.getAllHabitsForWidgetWithStatusFlow(getCurrentDateInLong())
                .collect { value ->
                    var noOfCompletions = 0
                    value.forEach {
                        if (it.isCompleted) noOfCompletions++
                    }
                    val percent = (noOfCompletions.toFloat() / value.size.toFloat()) * 100

                    _state.update {
                        it.copy(habitWithCurrDateCompletionStatus = percent)
                    }
                }
        }
    }


    fun getHabits() {

        _state.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch {

            roomDbRepo.getAllHabits().onSuccess { list ->

                list.collect { habits ->
                    _state.update {
                        it.copy(habits = habits)
                    }
                }

            }.onFailure { exception ->
                when (exception) {
                    is RepositoryError.RoomErrors.FetchingFailed -> {
                        Log.d("habitError", RepositoryError.RoomErrors.FetchingFailed().msg)
                    }
                }
            }
        }
    }


    fun getHeatmapForMonthArray(
        completedList: List<HabitCompletion?>? = null,
        year: Int,
        month: Int,
        interval : Int = 1
    ): List<Pair<Int, Boolean>> {
//    ): List<List<Pair<Int, Boolean>>> {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, 1)
        var maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        val daysArray: Array<Boolean> = Array(maxDays) { false }
        completedList?.forEach { habit ->
            if (habit?.completionDate != null) {

                val completionDate = LocalDate.ofEpochDay(habit.completionDate)
                if (completionDate.year == year && completionDate.monthValue == month) {
                    val dayOfMonth = completionDate.dayOfMonth
                    daysArray[dayOfMonth - 1] = true
                }
            }
        }
        val dayRows = daysArray.mapIndexed { index, isCompleted ->
            Pair(index + 1, isCompleted)
        }

        return dayRows
    }
}