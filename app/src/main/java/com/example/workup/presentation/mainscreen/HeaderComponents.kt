package com.example.workup.presentation.mainscreen

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workup.R
import com.example.domain.entity.Day
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun HeaderBlock(
    day: Day,
    streakCount: Int,
    selectedDate: LocalDate,
    trainedDates: Set<LocalDate>,
    onDateSelected: (LocalDate) -> Unit,
    onTimeChanged: (String, String, String, String) -> Unit,
    onSaveClicked: () -> Unit
) {
    val startH = day.startTime?.hour?.toString()?.padStart(2, '0') ?: "00"
    val startM = day.startTime?.minute?.toString()?.padStart(2, '0') ?: "00"
    val endH = day.endTime?.hour?.toString()?.padStart(2, '0') ?: "99"
    val endM = day.endTime?.minute?.toString()?.padStart(2, '0') ?: "99"

    Column(modifier = Modifier.fillMaxWidth()) {
        HeaderDates(
            selectedDate = selectedDate,
            trainedDates = trainedDates,
            onDateSelected = onDateSelected
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .paint(painterResource(id = R.drawable.ic_banner_title), contentScale = ContentScale.FillBounds),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "DAILY WORKOUT",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(start = 24.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_timer),
                    contentDescription = "Timer",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))

                TimeInputField(hour = startH, minute = startM) { h, m -> onTimeChanged(h, m, endH, endM) }

                Text(
                    text = " — ",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )

                TimeInputField(hour = endH, minute = endM) { h, m -> onTimeChanged(startH, startM, h, m) }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                SaveStreakButton(
                    isSaved = day.isWorkedOut,
                    onClick = { onSaveClicked() }
                )
                Text("STREAK: $streakCount", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(start = 8.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeaderDates(
    selectedDate: LocalDate,
    trainedDates: Set<LocalDate>,
    onDateSelected: (LocalDate) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
    )

    // Системный календарь
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val newDate = Instant.ofEpochMilli(millis).atZone(ZoneId.of("UTC")).toLocalDate()
                        onDateSelected(newDate)
                    }
                    showDatePicker = false
                }) { Text("OK", color = MaterialTheme.colorScheme.primary) }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel", color = MaterialTheme.colorScheme.primary)
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    val pastDaysCount = 7
    val pastDays = (pastDaysCount downTo 1).map { selectedDate.minusDays(it.toLong()) }
    val formatter = DateTimeFormatter.ofPattern("dd MMM. EEEE", Locale.ENGLISH)
    val formattedToday = selectedDate.format(formatter).lowercase(Locale.ENGLISH)

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy((-8).dp),
                modifier = Modifier
                    .weight(1f)
                    .horizontalScroll(rememberScrollState())
            ) {
                pastDays.forEachIndexed { index, date ->
                    val backgroundRes = when (index) {
                        0 -> R.drawable.ic_day_start
                        pastDays.lastIndex -> R.drawable.ic_day_end
                        else -> R.drawable.ic_day_middle
                    }
                    val dayStr = date.dayOfMonth.toString().padStart(2, '0')
                    val isTrained = trainedDates.contains(date)

                    val baseModifier = Modifier
                        .size(if (index == 0 || index == pastDays.lastIndex) 52.dp else 60.dp, 30.dp)
                        .clickable { onDateSelected(date) }

                    val paintedModifier = if (isTrained) {
                        baseModifier.paint(
                            painter = painterResource(id = backgroundRes),
                            contentScale = ContentScale.FillBounds,
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                        )
                    } else {
                        baseModifier
                    }

                    Box(
                        modifier = paintedModifier,
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = dayStr,
                            color = if (isTrained) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(
                                start = if (index == pastDays.lastIndex) 6.dp else 0.dp,
                                end = if (index == 0) 6.dp else 0.dp
                            )
                        )
                    }
                }
            }

            Text(
                text = formattedToday,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .offset(x = 6.dp)
                    .clickable { showDatePicker = true }
            )
        }
        Image(
            painter = painterResource(id = R.drawable.ic_today_line),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.width(200.dp).height(16.dp).align(Alignment.End).offset(x = 50.dp, y = (-14).dp)
        )
    }
}

@Composable
fun TimeInputField(
    hour: String,
    minute: String,
    onValueChange: (String, String) -> Unit
) {
    var textBuffer by remember { mutableStateOf("") }

    // Меняем фон в зависимости от того, ввели ли туда что-то
    val isUsed = textBuffer.isNotEmpty() && textBuffer != "0000"
    val bgColor = if (isUsed) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant

    Box(
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = hour, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Text(text = ":", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Text(text = minute, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }

        BasicTextField(
            value = textBuffer,
            onValueChange = { newValue ->
                val digits = newValue.filter { it.isDigit() }.takeLast(4)
                textBuffer = digits

                val padded = digits.padStart(4, '0')
                onValueChange(padded.substring(0, 2), padded.substring(2, 4))
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = TextStyle(color = Color.Transparent),
            cursorBrush = SolidColor(Color.Transparent),
            modifier = Modifier.matchParentSize()
        )
    }
}

@Composable
fun SaveStreakButton(
    isSaved: Boolean,
    onClick: () -> Unit
) {
    val transition = updateTransition(targetState = isSaved, label = "streakSaveTransition")

    val tint by transition.animateColor(
        transitionSpec = { tween(500) },
        label = "streakColor"
    ) { saved ->
        if (saved) Color(0xFF639922) else MaterialTheme.colorScheme.primary
    }

    val scale by transition.animateFloat(
        transitionSpec = { spring(dampingRatio = Spring.DampingRatioMediumBouncy) },
        label = "streakScale"
    ) { saved ->
        if (saved) 1.2f else 1f
    }

    val rotation by transition.animateFloat(
        transitionSpec = { tween(600, easing = FastOutSlowInEasing) },
        label = "streakRotation"
    ) { saved ->
        if (saved) 360f else 0f
    }

    Icon(
        painter = painterResource(id = R.drawable.ic_streak_arrow),
        contentDescription = "Save Workout",
        tint = tint,
        modifier = Modifier
            .size(36.dp)
            .scale(scale)
            .rotate(rotation)
            .clickable(enabled = !isSaved) { onClick() }
    )
}