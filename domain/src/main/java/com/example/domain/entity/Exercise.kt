package com.example.domain.entity

enum class ExerciseType {
    DB,  // Dumbbell
    BB,  // Barbell
    FW,  // Free Weight
    P    // Plate
}

data class Exercise(
    val id: String,
    val name: String,
    val type: ExerciseType,
    val weight: Float,
    val repsCount: List<Int>
)