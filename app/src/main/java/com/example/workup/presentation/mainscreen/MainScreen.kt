package com.example.workup.presentation.mainscreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun MainScreen(
    // iewModel через Hilt
) {
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(800) // Имитация запроса к БД
        isLoading = false
    }

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
                // ЗАГОЛОВОК
                item {
                    HeaderBlock()
                    Spacer(modifier = Modifier.height(32.dp))
                }

                // БЛОК УПРАЖНЕНИЙ
                item {
                    Text(
                        text = "EXERCISES",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    ExerciseListSection()
                    Spacer(modifier = Modifier.height(32.dp))
                }

                // БЛОК ЗАМЕТОК
                item {
                    Text(
                        text = "NOTES",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    NotesListSection()
                }
            }
        }
    }
}