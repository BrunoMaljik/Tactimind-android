package com.example.tactimind.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val TactimindDarkColorScheme = darkColorScheme(
    primary = TactimindBlue,
    onPrimary = Color.White,
    primaryContainer = TactimindBlueContainer,
    onPrimaryContainer = TactimindText,

    secondary = TactimindPurple,
    onSecondary = Color.White,
    secondaryContainer = TactimindPurpleContainer,
    onSecondaryContainer = TactimindText,

    tertiary = TactimindGreen,
    onTertiary = TactimindBackground,

    background = TactimindBackground,
    onBackground = TactimindText,

    surface = TactimindSurface,
    onSurface = TactimindText,

    surfaceVariant = TactimindSurfaceVariant,
    onSurfaceVariant = TactimindMutedText,

    outline = TactimindOutline,

    error = TactimindError,
    onError = TactimindBackground
)

private val TactimindShapes = Shapes(
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp)
)

@Composable
fun TactimindTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = TactimindDarkColorScheme,
        typography = Typography,
        shapes = TactimindShapes,
        content = content
    )
}