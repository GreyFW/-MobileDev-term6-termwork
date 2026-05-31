package com.example.domain.repository

import com.example.domain.entity.Day
import java.time.LocalDate

interface WorkoutRepository {
    suspend fun getWorkoutForDate(date: LocalDate): Day?
    suspend fun saveWorkout(day: Day)
    suspend fun getCurrentStreak(): Int
    suspend fun getUserLevel(): Int
}