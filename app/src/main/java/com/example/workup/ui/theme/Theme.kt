package com.example.workup.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = WorkupPrimary,
    secondary = WorkupSecondary,
    background = WorkupBackground,
    surface = WorkupInputUsed,         // фон заполненного инпута
    surfaceVariant = WorkupElementsBG, // фон пустого инпута
    onPrimary = WorkupTextOnSurfaces   // текст на баннерах
)

private val DarkColorScheme = darkColorScheme(
    primary = WorkupDarkPrimary,
    secondary = WorkupDarkSecondary,
    background = WorkupDarkBackground,
    surface = WorkupDarkInputUsed,
    surfaceVariant = WorkupDarkElementsBG,
    onPrimary = WorkupDarkTextOnSurfaces
)

@Composable
fun WorkUpTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}