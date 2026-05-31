package com.example.data.mapper

import com.example.data.dto.ExerciseDto
import com.example.domain.entity.Exercise
import com.example.domain.entity.EquipmentType

fun ExerciseDto.toDomain(): Exercise {
    return Exercise(
        id = this.id,
        name = this.name,
        type = EquipmentType.valueOf(this.type),
        weight = this.weight,
        repsCount = this.repsCount
    )
}