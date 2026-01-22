package com.theo.habt.presentation.addnewHabitScreen

import android.util.Log
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.theo.habt.dataLayer.constants.INITAL_ICON_ID
import com.theo.habt.dataLayer.constants.habitIcons
import com.theo.habt.dataLayer.localDb.Habit
import com.theo.habt.dataLayer.localDb.HabitCompletion
import com.theo.habt.dataLayer.repositorys.RepositoryError
import com.theo.habt.dataLayer.repositorys.RoomDbRepo
import com.theo.habt.ui.theme.INITIAL_COLOR
import com.theo.habt.ui.theme.colorPallet
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.util.Date
import javax.inject.Inject
import kotlin.time.Clock
import kotlin.time.ExperimentalTime


data class NewHabitUiState (
    val name: String = "gym",
    val colorArgb: Int = INITIAL_COLOR.toArgb(),
    val icon: String = INITAL_ICON_ID,
    val time: LocalTime? = null,
    val showSuccessMessage: Boolean = false,
)

@HiltViewModel
class NewHabitViewModal @Inject constructor( private val roomDbRepo: RoomDbRepo ) : ViewModel() {

    private var _habit  = MutableStateFlow(NewHabitUiState())

    val habit = _habit.stateIn(scope = viewModelScope , initialValue = NewHabitUiState(), started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 2000))
    private var _showAlert  = MutableStateFlow<Boolean>(false);
    val showAlert = _showAlert.asStateFlow()

    private var _alertMessage = MutableStateFlow<String>("")
    var alertMessage  = _alertMessage.asStateFlow()

    fun updateShowSuccessMessage(value : Boolean){
        _habit.update {
            it.copy(showSuccessMessage = value)
        }
    }
    fun updateShowAlert (value: Boolean){
        _showAlert.value = value
    }
    fun updateAlertMessage (value: String){
        _alertMessage.value = value
    }
    fun updateName (name : String){
        _habit.update {_habit.value.copy(name= name )}
    }
    fun updateColor (color : Int){
        _habit.update {_habit.value.copy(colorArgb = color )}
    }
    fun updateIcon (iconId : String){
        _habit.update { _habit.value.copy(icon = iconId )}
    }
    fun updateReminderTime (hour : Int , min :Int){
        _habit.update {
            Log.d("notificationMsg",  "inside updateReminderTime() update { : ${LocalTime.of(hour , min)} }" )
            _habit.value.copy(time = LocalTime.of(hour , min) )

        }

        Log.d("notificationMsg",  "inside updateReminderTime : ${habit.value.time} " )
    }



    fun insertHabitCompletion(entry : HabitCompletion){
        viewModelScope.launch {
            roomDbRepo.insertHabitCompletion(entry)
        }

    }


    @OptIn(ExperimentalTime::class)
    fun insertHabit () {

        val trimmedName = _habit.value.name.trim()
        if (trimmedName.isEmpty()) {
            updateAlertMessage("Habit name cannot be empty.")
            updateShowAlert(true)
            return
        }
            viewModelScope.launch(Dispatchers.IO){

                val habit =  Habit(name = _habit.value.name, colorArgb = _habit.value.colorArgb , icon = _habit.value.icon , reminder = _habit.value.time, creationDate = Clock.System.now().epochSeconds )
                roomDbRepo.insertHabit(habit)
                    .onSuccess {
                        updateShowSuccessMessage(true)
                    }
                    .onFailure { error ->
                        when(error){
                           is RepositoryError.RoomErrors.NameAlreadyExist -> {
                              updateAlertMessage( "${_habit.value.name} already exist.")
                              updateShowAlert(true)
                           }
                       }
                   }

            }

    }

}