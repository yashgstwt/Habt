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

data class HabitCompletionJoin(
    @Embedded
    val habit: Habit,

    // We use a prefix so Room doesn't get confused by two "id" columns
    @Embedded(prefix = "comp_")
    val completion: HabitCompletion?
)



data class HabitWithStatus(
    @Embedded
    val habit: Habit,
    val isCompleted: Boolean
)