package com.example.workup.presentation.mainscreen

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workup.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun HeaderBlock() {
    var isWorkoutSaved by remember { mutableStateOf(false) }
    var streakCount by remember { mutableIntStateOf(15) }
    var timeInput by remember { mutableStateOf("00:00") }

    Column(modifier = Modifier.fillMaxWidth()) {
        // Блок с датами
        HeaderDates()

        // Синий баннер DAILY WORKOUT
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .paint(
                    painter = painterResource(id = R.drawable.ic_banner_title),
                    contentScale = ContentScale.FillBounds,
                ),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "DAILY WORKOUT",
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(start = 24.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Блок времени и стрика
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Часы и время
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_timer),
                    contentDescription = "Timer",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    BasicTextField(
                        value = timeInput,
                        onValueChange = { if (it.length <= 5) timeInput = it },
                        textStyle = TextStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                        modifier = Modifier.width(60.dp)
                    )
                }
            }

            // Кнопка сохранения и стрик
            Row(verticalAlignment = Alignment.CenterVertically) {
                SaveStreakButton(
                    isSaved = isWorkoutSaved,
                    onClick = {
                        isWorkoutSaved = true
                        streakCount++
                    }
                )

                Text(
                    text = "STREAK: $streakCount",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

@Composable
fun HeaderDates() {
    val today = LocalDate.now()
    // Получаем 4 предыдущих дня
    val pastDays = (4 downTo 1).map { today.minusDays(it.toLong()) }

    val formatter = DateTimeFormatter.ofPattern("dd MMM. EEEE", Locale.ENGLISH)
    val formattedToday = today.format(formatter).lowercase(Locale.ENGLISH)

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            // Квадратики прошлых дней
            Row(
                horizontalArrangement = Arrangement.spacedBy((-8).dp),
                modifier = Modifier.weight(1f)
            ) {
                pastDays.forEachIndexed { index, date ->
                    val backgroundRes = when (index) {
                        0 -> R.drawable.ic_day_start
                        3 -> R.drawable.ic_day_end
                        else -> R.drawable.ic_day_middle
                    }

                    val dayStr = date.dayOfMonth.toString().padStart(2, '0')

                    Box(
                        modifier = Modifier
                            .size(if (index == 1 || index == 2) 60.dp else 52.dp, 30.dp)
                            .paint(
                                painter = painterResource(id = backgroundRes),
                                contentScale = ContentScale.FillBounds,
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = dayStr,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(
                                start = if (index == 3) 6.dp else 0.dp,
                                end = if (index == 0) 6.dp else 0.dp
                            )
                        )
                    }
                }
            }

            // Сегодняшняя дата
            Text(
                text = formattedToday,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.offset(x = 6.dp)
            )
        }

        // Линия под сегодняшней датой
        Image(
            painter = painterResource(id = R.drawable.ic_today_line),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .width(200.dp)
                .height(16.dp)
                .align(Alignment.End)
                .offset(x = 50.dp, y = (-14).dp)
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
        if (saved) Color(0xFF639922) else MaterialTheme.colorScheme.primary // Зеленый при успехе
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