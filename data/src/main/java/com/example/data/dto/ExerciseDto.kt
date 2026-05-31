package com.example.data.dto

data class ExerciseDto(
    val id: String = "",
    val name: String = "",
    val type: String = "", // Строка для Enum
    val weight: Float = 0f,
    val repsCount: List<Int> = emptyList()
)