package com.example.workup.presentation.mainscreen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun MainScreen() {
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(800)
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
                item {
                    HeaderBlock()
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
                    ExerciseListSection()
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
                    NotesListSection()
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
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(25f, 15f), 0f)
        )
    }
}