package com.example.workup.presentation.mainscreen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workup.R
import com.example.domain.entity.EquipmentType
import com.example.domain.entity.Exercise
import androidx.compose.ui.platform.LocalFocusManager

@Composable
fun ExerciseListSection(
    exercises: List<Exercise>,
    onAddExercise: () -> Unit,
    onExerciseUpdated: (Exercise) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        exercises.forEach { exercise ->
            ExerciseInputRow(
                exercise = exercise,
                onExerciseChanged = onExerciseUpdated
            )
        }

        Button(
            onClick = onAddExercise,
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
fun ExerciseInputRow(
    exercise: Exercise,
    onExerciseChanged: (Exercise) -> Unit
) {
    var currentRepInput by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    val weightStr = if (exercise.weight == 0f) "" else {
        if (exercise.weight % 1 == 0f) exercise.weight.toInt().toString() else exercise.weight.toString()
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 0.dp, bottomEnd = 12.dp, bottomStart = 0.dp),
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
                // ВЕРХНИЙ РЯД
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BasicTextField(
                        value = exercise.name,
                        onValueChange = { onExerciseChanged(exercise.copy(name = it)) },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                        textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold),
                        modifier = Modifier.weight(1f),
                        decorationBox = { innerTextField ->
                            if (exercise.name.isEmpty()) Text("Exercise name", color = MaterialTheme.colorScheme.secondary)
                            innerTextField()
                        }
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    EquipmentSelector(
                        equipment = exercise.type,
                        onSelect = { onExerciseChanged(exercise.copy(type = it)) }
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    BasicTextField(
                        value = weightStr,
                        onValueChange = { newWeight ->
                            val floatWeight = newWeight.toFloatOrNull() ?: 0f
                            onExerciseChanged(exercise.copy(weight = floatWeight))
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                        textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.primary),
                        modifier = Modifier.width(40.dp),
                        decorationBox = { innerTextField ->
                            if (weightStr.isEmpty()) Text("0", color = MaterialTheme.colorScheme.secondary)
                            innerTextField()
                        }
                    )
                    Text("kg", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(12.dp))

                // НИЖНИЙ РЯД С СЕТАМИ
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    exercise.repsCount.forEach { rep -> SetBubble(reps = rep.toString()) }

                    val isRepUsed = currentRepInput.isNotEmpty()
                    val repBgColor = if (isRepUsed) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant

                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(repBgColor, RoundedCornerShape(8.dp))
                            .border(1.dp, MaterialTheme.colorScheme.secondary, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        BasicTextField(
                            value = currentRepInput,
                            onValueChange = { newValue ->
                                if (newValue.endsWith(" ")) {
                                    val digit = newValue.trim().toIntOrNull()
                                    if (digit != null) {
                                        onExerciseChanged(exercise.copy(repsCount = exercise.repsCount + digit))
                                        currentRepInput = ""
                                    }
                                } else {
                                    currentRepInput = newValue.filter { it.isDigit() }
                                    if (currentRepInput.length >= 2) {
                                        val digit = currentRepInput.toIntOrNull()
                                        if (digit != null) {
                                            onExerciseChanged(exercise.copy(repsCount = exercise.repsCount + digit))
                                            currentRepInput = ""
                                        }
                                    }
                                }
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    val digit = currentRepInput.toIntOrNull()
                                    if (digit != null) {
                                        onExerciseChanged(exercise.copy(repsCount = exercise.repsCount + digit))
                                        currentRepInput = ""
                                    }
                                    focusManager.clearFocus()
                                }
                            ),
                            textStyle = TextStyle(
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            ),
                            singleLine = true,
                            modifier = Modifier.wrapContentSize()
                        )
                    }
                }
            }
        }
    }
}


// Компонент выбора иконки
@Composable
fun EquipmentSelector(equipment: EquipmentType?, onSelect: (EquipmentType) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.clickable { expanded = true }) {
        if (equipment == null) {
            Text("EQP", color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold)
        } else {
            when (equipment) {
                EquipmentType.DB -> Icon(painterResource(id = R.drawable.ic_dumbbells), null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
                EquipmentType.BB -> Icon(painterResource(id = R.drawable.ic_barbell), null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
                EquipmentType.FW -> Text("FW", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                EquipmentType.P -> Text("P", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            EquipmentType.entries.forEach { eqpType ->
                DropdownMenuItem(
                    text = { Text(text = eqpType.label, color = MaterialTheme.colorScheme.primary) },
                    onClick = { onSelect(eqpType); expanded = false }
                )
            }
        }
    }
}

@Composable
fun SetBubble(reps: String) {
    var isCompleted by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (isCompleted) 1.1f else 1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow), label = "")
    val bgColor by animateColorAsState(if (isCompleted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface, label = "")
    val textColor by animateColorAsState(if (isCompleted) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.primary, label = "")

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
        Text(text = reps, color = textColor, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}