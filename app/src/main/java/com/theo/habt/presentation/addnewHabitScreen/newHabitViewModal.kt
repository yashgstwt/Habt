package com.theo.habt.presentation.addnewHabitScreen

import android.util.Log
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.theo.habt.dataLayer.constants.INITAL_ICON_ID
import com.theo.habt.dataLayer.localDb.Habit
import com.theo.habt.dataLayer.localDb.NextHabitSchedule
import com.theo.habt.dataLayer.repositorys.RepositoryError
import com.theo.habt.dataLayer.repositorys.RoomDbRepo
import com.theo.habt.ui.theme.INITIAL_COLOR
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
import java.util.Calendar
import javax.inject.Inject
import kotlin.time.Clock
import kotlin.time.ExperimentalTime


data class NewHabitUiState(
    val name: String = "gym",
    val colorArgb: Int = INITIAL_COLOR.toArgb(),
    val icon: String = INITAL_ICON_ID,
    val time: LocalTime? = null,
    val showSuccessMessage: Boolean = false,
    val interval: Int = 1
)

@HiltViewModel
class NewHabitViewModal @Inject constructor(private val roomDbRepo: RoomDbRepo) : ViewModel() {

    init {
        val currentDate = LocalDate.now()
        Log.d("todayTime", currentDate.toString())
    }
    private var _habit = MutableStateFlow(NewHabitUiState())

    val habit = _habit.stateIn(
        scope = viewModelScope,
        initialValue = NewHabitUiState(),
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 2000)
    )
    private var _showAlert = MutableStateFlow<Boolean>(false);
    val showAlert = _showAlert.asStateFlow()
    private var _alertMessage = MutableStateFlow<String>("")
    var alertMessage = _alertMessage.asStateFlow()

    fun updateShowSuccessMessage(value: Boolean) {
        _habit.update {
            it.copy(showSuccessMessage = value)
        }
    }

    fun updateShowAlert(value: Boolean) {
        _showAlert.value = value
    }

    fun updateAlertMessage(value: String) {
        _alertMessage.value = value
    }

    fun updateName(name: String) {
        _habit.update { _habit.value.copy(name = name) }
    }

    fun updateInterval(interval: Int) {
        _habit.update {
            it.copy(interval = interval)
        }
    }

    fun updateColor(color: Int) {
        _habit.update { _habit.value.copy(colorArgb = color) }
    }

    fun updateIcon(iconId: String) {
        _habit.update { _habit.value.copy(icon = iconId) }
    }

    fun updateReminderTime(hour: Int, min: Int) {
        _habit.update {
            Log.d(
                "notificationMsg",
                "inside updateReminderTime() update { : ${LocalTime.of(hour, min)} }"
            )
            _habit.value.copy(time = LocalTime.of(hour, min))

        }

        Log.d("notificationMsg", "inside updateReminderTime : ${habit.value.time} ")
    }

    @OptIn(ExperimentalTime::class)
    fun insertHabit() {

        val trimmedName = _habit.value.name.trim()
        if (trimmedName.isEmpty()) {
            updateAlertMessage("Habit name cannot be empty.")
            updateShowAlert(true)
            return
        }
        viewModelScope.launch(Dispatchers.IO) {

            val habit = Habit(
                name = _habit.value.name,
                colorArgb = _habit.value.colorArgb,
                icon = _habit.value.icon,
                reminder = _habit.value.time,
                creationDate = Clock.System.now().epochSeconds,
                interval = _habit.value.interval
            )
            roomDbRepo.insertHabit(habit).onSuccess {

                updateShowSuccessMessage(true)
                setHabitCompletionNextDate(_habit.value.name)

            }.onFailure { error ->
                when (error) {
                    is RepositoryError.RoomErrors.NameAlreadyExist -> {
                        updateAlertMessage("${_habit.value.name} already exist.")
                        updateShowAlert(true)
                    }
                }
            }
        }
    }

    fun setHabitCompletionNextDate(name : String ){
        viewModelScope.launch {
            var id: Int? = null
            try {
                id = roomDbRepo.getHabitByName(name)
                id?.let {
                    roomDbRepo.insertNextHabitSchedule(NextHabitSchedule(
                        habitID = id,
                        date = LocalDate.now()
                    ))
                }
            }catch (e : Exception){
                Log.e("LOG", e.toString())
            }
        }

    }
}