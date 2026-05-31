package com.example.domain.entities

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