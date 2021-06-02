package io.kekasquad.kyeue.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = teal300,
    primaryVariant = teal300Dark,
    secondary = teal300,
    onPrimary = Color.Black,
    onSecondary = Color.Black
)

@SuppressLint("ConflictingOnColor")
private val LightColorPalette = lightColors(
    primary = teal700,
    primaryVariant = teal700Light,
    secondary = teal700,
    background = backgroundLight,
    surface = surfaceLight,
    error = errorLight,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onError = Color.White
)

@Composable
fun KyeueTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = if (isDarkTheme) DarkColorPalette else LightColorPalette

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}