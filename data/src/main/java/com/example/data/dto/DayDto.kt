package com.example.data.dto

data class DayDto(
    val id: String = "",
    val dateString: String = "",
    val startTimeString: String? = null,
    val endTimeString: String? = null,
    val isWorkedOut: Boolean = false,
    val exercisesList: List<ExerciseDto> = emptyList(),
    val notesList: List<NoteDto> = emptyList()
)