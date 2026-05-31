package com.example.data.mapper

import com.example.data.dto.DayDto
import com.example.domain.entity.Day
import java.time.LocalDate
import java.time.LocalTime

fun DayDto.toDomain(): Day {
    return Day(
        id = this.id,
        date = LocalDate.parse(this.dateString),
        startTime = this.startTimeString?.let { LocalTime.parse(it) },
        endTime = this.endTimeString?.let { LocalTime.parse(it) },
        isWorkedOut = this.isWorkedOut,
        exercisesList = this.exercisesList.map { it.toDomain() },
        notesList = this.notesList.map { it.toDomain() }
    )
}