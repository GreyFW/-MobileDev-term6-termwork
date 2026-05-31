package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_days")
data class DayEntity(
    @PrimaryKey val date: String,
    val startTime: String?,
    val endTime: String?,
    val isWorkedOut: Boolean,
    val exercisesJson: String,
    val notesJson: String
)