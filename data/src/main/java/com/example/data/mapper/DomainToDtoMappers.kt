package com.example.data.mapper

import com.example.data.dto.DayDto
import com.example.data.dto.ExerciseDto
import com.example.data.dto.NoteDto
import com.example.domain.entity.Day
import com.example.domain.entity.Exercise
import com.example.domain.entity.Note

fun Day.toDto(): DayDto {
    return DayDto(
        id = this.id,
        dateString = this.date.toString(),
        startTimeString = this.startTime?.toString(),
        endTimeString = this.endTime?.toString(),
        isWorkedOut = this.isWorkedOut,
        exercisesList = this.exercisesList.map { it.toDto() },
        notesList = this.notesList.map { it.toDto() }
    )
}

fun Exercise.toDto(): ExerciseDto = ExerciseDto(id, name, type.name, weight, repsCount)

fun Note.toDto(): NoteDto = NoteDto(id, content)