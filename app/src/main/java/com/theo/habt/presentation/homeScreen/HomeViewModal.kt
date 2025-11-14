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
import java.time.LocalDateTime
import java.time.ZoneId


data class HomeUiState(
    val habits : List<Habit?> = emptyList(),
    val isLoading : Boolean = false
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


    }
    fun markAsCompleted(habitCompletion: HabitCompletion) {
        viewModelScope.launch {
            roomDbRepo.insertHabitCompletion(habitCompletion)
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


    fun updateHabitsList(allHabits: List<Habit?>) {


    }


}