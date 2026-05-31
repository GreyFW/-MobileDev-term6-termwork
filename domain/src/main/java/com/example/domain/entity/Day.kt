package com.example.domain.entity

import java.time.LocalDate
import java.time.LocalTime

data class Day(
    val id: String,
    val date: LocalDate,
    val startTime: LocalTime?,
    val endTime: LocalTime?,
    val isWorkedOut: Boolean,
    val exercisesList: List<Exercise>,
    val notesList: List<Note>
)