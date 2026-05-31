package com.example.workup.presentation.mainscreen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val currentDay by viewModel.currentDay.collectAsState()
    val streak by viewModel.streak.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val trainedDates by viewModel.trainedDates.collectAsState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp),
                contentPadding = PaddingValues(vertical = 24.dp)
            ) {
                item {
                    HeaderBlock(
                        day = currentDay,
                        streakCount = streak,
                        selectedDate = selectedDate,
                        trainedDates = trainedDates,
                        onDateSelected = { viewModel.setDate(it) },
                        onTimeChanged = { sH, sM, eH, eM -> viewModel.updateTime(sH, sM, eH, eM) },
                        onSaveClicked = { viewModel.saveWorkout() }
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                }

                item {
                    DashedDivider(color = MaterialTheme.colorScheme.secondary)
                    Spacer(modifier = Modifier.height(24.dp))
                }

                item {
                    Text(
                        text = "EXERCISES",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    ExerciseListSection(
                        exercises = currentDay.exercisesList,
                        onAddExercise = { viewModel.addExercise() },
                        onExerciseUpdated = { viewModel.updateExercise(it) }
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }

                item {
                    DashedDivider(color = MaterialTheme.colorScheme.secondary)
                    Spacer(modifier = Modifier.height(24.dp))
                }

                item {
                    Text(
                        text = "NOTES",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    NotesListSection(
                        notes = currentDay.notesList,
                        onAddNote = { viewModel.addNote() },
                        onNoteUpdated = { id, content -> viewModel.updateNote(id, content) }
                    )
                }
            }
        }
    }
}

@Composable
fun DashedDivider(modifier: Modifier = Modifier, color: Color) {
    Canvas(modifier = modifier.fillMaxWidth().height(2.dp)) {
        drawLine(
            color = color,
            start = Offset(0f, size.height / 2),
            end = Offset(size.width, size.height / 2),
            strokeWidth = 2.dp.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)
        )
    }
}