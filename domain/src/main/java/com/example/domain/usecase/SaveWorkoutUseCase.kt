package com.example.domain.usecase

import com.example.domain.entity.Day
import com.example.domain.repository.WorkoutRepository

class SaveWorkoutUseCase(private val repository: WorkoutRepository) {
    suspend operator fun invoke(day: Day) {
        repository.saveWorkout(day)
    }
}