package com.theo.habt.presentation.homeScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.theo.habt.dataLayer.localDb.Habit
import com.theo.habt.dataLayer.localDb.HabitCompletion
import com.theo.habt.dataLayer.repositorys.RepositoryError
import com.theo.habt.dataLayer.repositorys.RoomDbRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Calendar
import java.util.TimeZone
import kotlin.collections.chunked
import kotlin.text.format

//
//var CompletedTaskList  = MutableStateFlow(
//
//    mutableListOf(
//        HabitCompletion(
//            id = 1, isCompleted = true, completionDate = 1731408750000L,
//            habitId = 2
//        ),   HabitCompletion(
//            id = 1, isCompleted = false, completionDate =1731408750000L,
//            habitId = 2
//        ),   HabitCompletion(
//            id = 1, isCompleted = true, completionDate = 1731408750000L,
//            habitId = 2
//        ),   HabitCompletion(
//            id = 1, isCompleted = true, completionDate = 1731408750000L,
//            habitId = 2
//        ),   HabitCompletion(
//            id = 1, isCompleted = true, completionDate = 1731408750000L,
//            habitId = 2
//        ),   HabitCompletion(
//            id = 1, isCompleted = true, completionDate = 1731408750000L,
//            habitId = 2
//        ),   HabitCompletion(
//            id = 1, isCompleted = true, completionDate = 1731508721000L,
//            habitId = 2
//        ),   HabitCompletion(
//            id = 1, isCompleted = true, completionDate = 1731708750000L,
//            habitId = 2
//        ),   HabitCompletion(
//            id = 1, isCompleted = true, completionDate = 1731908750000L,
//            habitId = 2
//        ),   HabitCompletion(
//            id = 1, isCompleted = false, completionDate =1731208750000L,
//            habitId = 2
//        ),   HabitCompletion(
//            id = 1, isCompleted = true, completionDate = 11739408750000L,
//            habitId = 2
//        ),   HabitCompletion(
//            id = 1, isCompleted = false, completionDate = 11731408000000L,
//            habitId = 2
//        ),   HabitCompletion(
//            id = 1, isCompleted = true, completionDate = 11731401750000L,
//            habitId = 2
//        )
//    )
//)
//


data class HabitWithCompletion (
    val habit: Habit?,
    val habitCompletions:  List<List<Pair<Int, Boolean>>>
)

data class HomeUiState(
    val habits : List<Habit?> = emptyList(),
    val habitsWithCompletions: List<HabitWithCompletion?>? = emptyList(),
    val isLoading : Boolean = false,
    val habitCompletionList: List<Boolean> = emptyList()

)



@HiltViewModel
class HomeViewModal  @Inject constructor( private val roomDbRepo: RoomDbRepo) : ViewModel() {

    private val _state =  MutableStateFlow(HomeUiState())
    val state =  _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 2000),
        initialValue = HomeUiState()
    )

    init{
        val dateTime = LocalDateTime.now()
        val instant = dateTime.atZone(ZoneId.systemDefault()).toInstant()
        val timestamp: Long = instant.toEpochMilli()
        Log.d("today's date" , "In long $timestamp")

        val dateTimeAgain = Instant.ofEpochMilli(timestamp)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
        Log.d("today's date" , "In formated  $dateTimeAgain")


        val dateCurr = LocalDate.now()
        Log.d("today's date" , "localDate  $dateCurr")

//        for (i in  1..10) {
//            markAsCompleted(
//                HabitCompletion(
//                    habitId = 1,
//                    completionDate = 4532 + i.toLong(),
//                    isCompleted = true
//                )
//            )
//        }

        getHabitWithCompletions()
        convertDate(20413L);

    }

    fun markAsCompleted(habitCompletion: HabitCompletion) {
        viewModelScope.launch {
            roomDbRepo.insertHabitCompletion(habitCompletion)
        }
    }



    fun convertDate(longDate : Long) {
        val epochDays = 20413L

        // Convert the Long back to a LocalDate object
        val date = LocalDate.ofEpochDay(epochDays)

        println("ISO Format (YYYY-MM-DD): $date") // Prints: 2025-11-20

        // For a more human-readable format
        val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
        val formattedDate = date.format(formatter)
        Log.d("Formatted","Formatted Date: $formattedDate") // Prints: November 20, 2025
    }



    fun getHabitWithCompletions(month: Int = LocalDate.now().month.value, year:Int  = LocalDate.now().month.value){
        viewModelScope.launch {
            roomDbRepo.getHabitWithCompletions().onSuccess { flow ->
                    flow.collect { value ->
                        val habitWithHeatList = mutableListOf<HabitWithCompletion>()

//                        Log.d("habit with completions","before collecting values ${ value.toString()}")

                        value?.forEach {
                            habitWithHeatList.add(HabitWithCompletion(it?.habit, habitCompletions = getHeatmapForMonthArray(it?.completions, year = year , month = month)))
//                            Log.d("habit with completions",it?.habit.toString());
                        }

                        _state.update {
                            it.copy(habitsWithCompletions = habitWithHeatList)
                        }
//                        Log.d("habit with completions","state after update "+_state.value.habitsWithCompletions.toString())
                    }

            }.onFailure { exception ->
                when(exception){
                    is RepositoryError.RoomErrors.FetchingFailed -> {
                        Log.d( "habitError",RepositoryError.RoomErrors.FetchingFailed().msg)
                        _state.update {
                            it.copy(isLoading = true)
                        }
                    }
                }
            }
        }
    }


    fun getHabits(){

        _state.update {
            it.copy(isLoading = true )
        }
        viewModelScope.launch {

             roomDbRepo.getAllHabits().onSuccess {  list ->

                 list.collect { habits ->
                     _state.update {
                         it.copy(habits = habits )
                     }
                 }

             }.onFailure { exception ->
                 when(exception){
                     is RepositoryError.RoomErrors.FetchingFailed -> {
                      Log.d( "habitError",RepositoryError.RoomErrors.FetchingFailed().msg)
                     }
                 }
             }
        }
    }









    fun getHeatmapForMonthArray(
        completedList: List<HabitCompletion?>? = null ,
        year: Int,
        month: Int
    ):  List<List<Pair<Int, Boolean>>> {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, 1)
        val maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        val daysArray: Array<Boolean> = Array(maxDays ){false}
        completedList?.forEach { habit ->
            if (habit?.completionDate != null) {

                val completionDate = LocalDate.ofEpochDay(habit.completionDate)
//                if (completionDate.year == year && completionDate.monthValue == month) {
                    val dayOfMonth = completionDate.dayOfMonth
                    Log.d("CompletionsHeat", completionDate.toString())

                    daysArray[dayOfMonth - 1] = true
//                }
            }
        }
        val dayRows = daysArray.mapIndexed { index, isCompleted ->
            Pair(index + 1, isCompleted)
        }.chunked(8)

        Log.d("CompletionsHeat", dayRows.toString())

        return dayRows
    }





}