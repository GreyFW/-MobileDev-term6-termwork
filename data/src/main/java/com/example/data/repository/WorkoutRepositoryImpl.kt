package com.example.data.repository

import com.example.domain.entity.Day
import com.example.domain.repository.WorkoutRepository
import java.time.LocalDate
import javax.inject.Inject

class WorkoutRepositoryImpl @Inject constructor(
    //  FirebaseFirestore
) : WorkoutRepository {
    override suspend fun getWorkoutForDate(date: LocalDate): Day? { TODO() }
    override suspend fun saveWorkout(day: Day) { TODO() }
    override suspend fun getCurrentStreak(): Int { TODO() }
    override suspend fun getUserLevel(): Int { TODO() }
}