package com.example.myapplication.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Typography
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = GreenPrimary,
    secondary = YellowSecondary
)
private val DarkColors = darkColorScheme(
    primary = GreenPrimary,
    secondary = YellowSecondary
)

@Composable
fun MyApplicationTheme(content: @Composable () -> Unit) {
    val darkTheme = isSystemInDarkTheme()
    val colors = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colors,
        typography = Typography(),
        content = content
    )
}
