package com.theo.habt.dataLayer.constants

import androidx.room.Embedded
import androidx.room.Relation
import com.theo.habt.dataLayer.localDb.Habit
import com.theo.habt.dataLayer.localDb.HabitCompletion

data class HabitWithCompletions(
    @Embedded
    val habit: Habit,

    @Relation(
        parentColumn = "id",
        entityColumn = "habit_id"
    )
    val completions: List<HabitCompletion>
)