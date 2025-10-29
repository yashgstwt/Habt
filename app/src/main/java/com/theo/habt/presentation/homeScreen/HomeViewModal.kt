package com.theo.habt.presentation.homeScreen

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.theo.habt.dataLayer.localDb.Habit
import com.theo.habt.dataLayer.repositorys.RoomDbRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModal  @Inject constructor( private val roomDbRepo: RoomDbRepo) : ViewModel() {


    fun markAsCompleted(){


    }

     fun createNewHabit(habit : Habit){

            try {
                roomDbRepo.insertHabit(habit)
            }catch (e: SQLiteConstraintException) {
                // This habit name already exists!
                // Show a Toast, a Snackbar, or update a UI state variable with an error message.
                Log.e( "Room","A habit with this name already exists.")
            }


    }


}