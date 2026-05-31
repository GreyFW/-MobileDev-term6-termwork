package com.example.data.mapper

import com.example.data.dto.ExerciseDto
import com.example.domain.entity.Exercise
import com.example.domain.entity.ExerciseType

fun ExerciseDto.toDomain(): Exercise {
    return Exercise(
        id = this.id,
        name = this.name,
        type = ExerciseType.valueOf(this.type),
        weight = this.weight,
        repsCount = this.repsCount
    )
}