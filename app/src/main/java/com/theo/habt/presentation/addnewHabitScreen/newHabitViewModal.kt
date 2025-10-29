package com.theo.habt.presentation.addnewHabitScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.theo.habt.dataLayer.localDb.Habit
import com.theo.habt.dataLayer.repositorys.RoomDbRepo
import com.theo.habt.ui.theme.colorPallet
import java.time.LocalTime
import javax.inject.Inject

class NewHabitViewModal @Inject constructor( private val roomDbRepo: RoomDbRepo ) : ViewModel() {

    var habitName:Habit  by  mutableStateOf(Habit(
        name = "",
        colorHex = colorPallet[0].toString(),
        iconName = TODO(),
        creationDate = TODO(),
    ))

    val time  = LocalTime.now()
    fun insertHabit (habit: Habit) {
        roomDbRepo.insertHabit(habit)
    }


}