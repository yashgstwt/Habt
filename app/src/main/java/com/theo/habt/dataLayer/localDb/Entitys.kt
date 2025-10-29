package com.theo.habt.dataLayer.localDb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date




@Entity(
    tableName = "habits" ,
    indices = [Index(value = ["name"], unique = true)]
)
data class Habit(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "color_hex")
    val colorHex: String,

    @ColumnInfo(name = "icon_name")
    val iconName: String,

    @ColumnInfo(name = "creation_date")
    val creationDate: Long = 1L

)


@Entity(
    tableName = "habit_completions",
    foreignKeys = [
        ForeignKey(
            entity = Habit::class,
            parentColumns = ["id"],
            childColumns = ["habit_id"],
            onDelete = ForeignKey.CASCADE // If a habit is deleted, its completion records are also deleted.
        )
    ],
    indices = [
        Index(value = ["habit_id", "completion_date"], unique = true)
    ]
)
data class HabitCompletion(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "habit_id")
    val habitId: Int,

    @ColumnInfo(name = "completion_date")
    val completionDate: Long ,// Storing date as Long (timestamp) for easier querying

    @ColumnInfo(name="is_completed")
    val isCompleted : Boolean

)