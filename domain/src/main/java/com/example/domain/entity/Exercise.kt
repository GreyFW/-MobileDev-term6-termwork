package com.example.domain.entity

enum class EquipmentType(val label: String) {
    DB("Dumbbell"),
    BB("Barbell"),
    FW("Bodyweight"),
    P("Plate")
}

data class Exercise(
    val id: String,
    val name: String,
    val type: EquipmentType,
    val weight: Float,
    val repsCount: List<Int>
)