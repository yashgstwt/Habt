package com.theo.habt.presentation.homeScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.theo.habt.Util.getCurrentDateInLong
import com.theo.habt.dataLayer.constants.HabitWithStatus
import com.theo.habt.dataLayer.localDb.Habit
import com.theo.habt.dataLayer.localDb.HabitCompletion
import com.theo.habt.dataLayer.repositorys.RepositoryError
import com.theo.habt.dataLayer.repositorys.RoomDbRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Calendar
import kotlin.collections.chunked


data class HabitWithCompletion(
    val habit: Habit?,
    val habitCompletions: List<List<Pair<Int, Boolean>>>
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

    fun markAsCompleted(habitCompletion: HabitCompletion) {
        viewModelScope.launch {
            roomDbRepo.insertHabitCompletion(habitCompletion)
        }
    }


    fun getHabitWithCompletions(
        month: Int = LocalDate.now().month.value,
        year: Int = LocalDate.now().year
    ) {
        viewModelScope.launch {
            roomDbRepo.getHabitWithCompletions().onSuccess { flow ->
                flow.collect { value ->
                    val habitWithHeatList = mutableListOf<HabitWithCompletion>()

                    value?.forEach {
                        habitWithHeatList.add(
                            HabitWithCompletion(
                                it?.habit,
                                habitCompletions = getHeatmapForMonthArray(
                                    it?.completions,
                                    year = year,
                                    month = month
                                )
                            )
                        )
                    }

                    _state.update {
                        it.copy(habitsWithCompletions = habitWithHeatList)
                    }
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
    ): List<List<Pair<Int, Boolean>>> {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, 1)
        var maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        maxDays /= interval

        val daysArray: Array<Boolean> = Array(maxDays) { false }
        completedList?.forEach { habit ->
            if (habit?.completionDate != null) {

                val completionDate = LocalDate.ofEpochDay(habit.completionDate)
                if (completionDate.year == year && completionDate.monthValue == month) {
                    val dayOfMonth = completionDate.dayOfMonth
                    daysArray[dayOfMonth - (1 + (interval-1))] = true
                }
            }
        }
        val dayRows = daysArray.mapIndexed { index, isCompleted ->
            Pair(index + 1, isCompleted)
        }.chunked(8)

        return dayRows
    }
}