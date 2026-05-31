package com.example.workup.presentation.mainscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.Day
import com.example.domain.entity.EquipmentType
import com.example.domain.entity.Exercise
import com.example.domain.entity.Note
import com.example.domain.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: WorkoutRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _currentDay = MutableStateFlow(createEmptyDay(LocalDate.now()))
    val currentDay: StateFlow<Day> = _currentDay.asStateFlow()

    private val _streak = MutableStateFlow(0)
    val streak = _streak.asStateFlow()

    init {
        loadDayData(LocalDate.now())
    }

    private fun loadDayData(date: LocalDate) {
        viewModelScope.launch {
            _isLoading.value = true

            _streak.value = repository.getCurrentStreak()

            val savedDay = repository.getWorkoutForDate(date)
            if (savedDay != null) {
                _currentDay.value = savedDay
            } else {
                _currentDay.value = createEmptyDay(date)
            }

            _isLoading.value = false
        }
    }

    fun updateTime(startH: String, startM: String, endH: String, endM: String) {
        _currentDay.update { day ->
            day.copy(
                startTime = tryParseTime(startH, startM),
                endTime = tryParseTime(endH, endM)
            )
        }
    }

    fun addExercise() {
        val newExercise = Exercise(
            id = UUID.randomUUID().toString(),
            name = "",
            type = EquipmentType.FW,
            weight = 0f,
            repsCount = emptyList()
        )
        _currentDay.update { day -> day.copy(exercisesList = day.exercisesList + newExercise) }
    }

    fun updateExercise(updatedExercise: Exercise) {
        _currentDay.update { day ->
            day.copy(exercisesList = day.exercisesList.map {
                if (it.id == updatedExercise.id) updatedExercise else it
            })
        }
    }

    fun addNote() {
        val newNote = Note(id = UUID.randomUUID().toString(), content = "")
        _currentDay.update { day -> day.copy(notesList = day.notesList + newNote) }
    }

    fun updateNote(noteId: String, newContent: String) {
        _currentDay.update { day ->
            day.copy(notesList = day.notesList.map {
                if (it.id == noteId) it.copy(content = newContent) else it
            })
        }
    }

    fun saveWorkout() {
        viewModelScope.launch {
            _currentDay.update { it.copy(isWorkedOut = true) }

            repository.saveWorkout(_currentDay.value)

            _streak.update { it + 1 }
        }
    }

    private fun createEmptyDay(date: LocalDate) = Day(
        id = date.toString(),
        date = date,
        startTime = null,
        endTime = null,
        isWorkedOut = false,
        exercisesList = listOf(
            Exercise(UUID.randomUUID().toString(), "", EquipmentType.FW, 0f, emptyList())
        ),
        notesList = listOf(
            Note(UUID.randomUUID().toString(), "")
        )
    )

    private fun tryParseTime(h: String, m: String): LocalTime? {
        return try {
            LocalTime.of(h.toInt(), m.toInt())
        } catch (e: Exception) { null }
    }
}