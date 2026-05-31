package com.example.domain.usecase

import com.example.domain.entity.Day
import com.example.domain.repository.WorkoutRepository
import java.time.LocalDate

class GetTodayWorkoutUseCase(private val repository: WorkoutRepository) {
    suspend operator fun invoke(date: LocalDate): Day? {
        return repository.getWorkoutForDate(date)
    }
}