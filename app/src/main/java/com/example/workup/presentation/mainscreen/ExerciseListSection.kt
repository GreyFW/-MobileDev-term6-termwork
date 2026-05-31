package com.example.workup.presentation.mainscreen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.entity.ExerciseType

@Composable
fun ExerciseListSection() {
    var exercisesCount by remember { mutableIntStateOf(1) }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        repeat(exercisesCount) {
            ExerciseInputRow()
        }

        Button(
            onClick = { exercisesCount++ },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                contentColor = MaterialTheme.colorScheme.primary
            ),
            shape = RoundedCornerShape(12.dp),
            elevation = ButtonDefaults.buttonElevation(0.dp)
        ) {
            Text("+ ADD EXERCISE", style = MaterialTheme.typography.labelMedium)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ExerciseInputRow() {
    var name by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var currentRepInput by remember { mutableStateOf("") }
    var sets by remember { mutableStateOf(listOf<String>()) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(6.dp)
                    .background(MaterialTheme.colorScheme.primary)
            )

            Column(modifier = Modifier.padding(12.dp)) {
                // Верхний ряд: Название, Тип, Вес
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BasicTextField(
                        value = name,
                        onValueChange = { name = it },
                        textStyle = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.weight(1f),
                        decorationBox = { innerTextField ->
                            if (name.isEmpty()) Text("Exercise name", color = MaterialTheme.colorScheme.secondary)
                            innerTextField()
                        }
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    BasicTextField(
                        value = weight,
                        onValueChange = { weight = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.primary),
                        modifier = Modifier.width(40.dp),
                        decorationBox = { innerTextField ->
                            if (weight.isEmpty()) Text("0", color = MaterialTheme.colorScheme.secondary)
                            innerTextField()
                        }
                    )
                    Text("kg", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(12.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    sets.forEach { rep ->
                        SetBubble(reps = rep)
                    }

                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.Transparent, RoundedCornerShape(8.dp))
                            .border(1.dp, MaterialTheme.colorScheme.secondary, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        BasicTextField(
                            value = currentRepInput,
                            onValueChange = {
                                currentRepInput = it
                                if (it.length >= 2) {
                                    sets = sets + it
                                    currentRepInput = ""
                                }
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            textStyle = TextStyle(
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            ),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SetBubble(reps: String) {
    var isCompleted by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isCompleted) 1.1f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "setScale"
    )
    val bgColor by animateColorAsState(
        targetValue = if (isCompleted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
        label = "setColor"
    )
    val textColor by animateColorAsState(
        targetValue = if (isCompleted) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.primary,
        label = "setTextColor"
    )

    Box(
        modifier = Modifier
            .size(40.dp)
            .scale(scale)
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
            .clickable { isCompleted = !isCompleted },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = reps,
            color = textColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}