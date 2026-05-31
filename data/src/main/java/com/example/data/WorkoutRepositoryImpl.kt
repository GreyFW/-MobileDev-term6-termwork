package com.example.data

import com.example.data.db.Converters
import com.example.data.db.DayEntity
import com.example.data.db.WorkoutDao
import com.example.domain.entity.Day
import com.example.domain.repository.WorkoutRepository
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

class WorkoutRepositoryImpl @Inject constructor(
    private val dao: WorkoutDao
) : WorkoutRepository {

    override suspend fun getWorkoutForDate(date: LocalDate): Day? {
        val entity = dao.getDayByDate(date.toString()) ?: return null
        return Day(
            id = entity.date,
            date = LocalDate.parse(entity.date),
            startTime = entity.startTime?.let { LocalTime.parse(it) },
            endTime = entity.endTime?.let { LocalTime.parse(it) },
            isWorkedOut = entity.isWorkedOut,
            exercisesList = Converters().toExerciseList(entity.exercisesJson),
            notesList = Converters().toNoteList(entity.notesJson)
        )
    }

    override suspend fun saveWorkout(day: Day) {
        val entity = DayEntity(
            date = day.date.toString(),
            startTime = day.startTime?.toString(),
            endTime = day.endTime?.toString(),
            isWorkedOut = day.isWorkedOut,
            exercisesJson = Converters().fromExerciseList(day.exercisesList),
            notesJson = Converters().fromNoteList(day.notesList)
        )
        dao.insertDay(entity)
    }

    // Заглушки для статистики
    override suspend fun getCurrentStreak(): Int = 15
    override suspend fun getUserLevel(): Int = 5
}