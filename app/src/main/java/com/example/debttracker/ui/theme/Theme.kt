package com.example.debttracker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


private val LightColors = lightColorScheme(
    tertiaryContainer = PaidGreenLight,
    onTertiaryContainer = PaidGreenOnLight,

    primary = BluePrimary,

    onPrimary = LightSurface,

    primaryContainer = Color(0xFFE0E7FF),

    onPrimaryContainer = Color(0xFF1E1B4B),

    background = LightBackground,

    onBackground = TextPrimaryLight,

    surface = LightSurface,

    onSurface = TextPrimaryLight,

    surfaceVariant = LightSurfaceVariant,

    onSurfaceVariant = TextSecondaryLight,

    outline = DividerLight
)



private val DarkColors = darkColorScheme(
    tertiaryContainer = PaidGreenDark,
    onTertiaryContainer = PaidGreenOnDark,

    primary = BluePrimaryDark,

    onPrimary = Color(0xFF1E1B4B),

    primaryContainer = Color(0xFF3730A3),

    onPrimaryContainer = Color(0xFFE0E7FF),

    background = DarkBackground,

    onBackground = TextPrimaryDark,

    surface = DarkSurface,

    onSurface = TextPrimaryDark,

    surfaceVariant = DarkSurfaceVariant,

    onSurfaceVariant = TextSecondaryDark,

    outline = DividerDark
)


private val AppTypography = Typography(

    headlineLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp,
        lineHeight = 36.sp
    ),

    titleLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    ),

    titleMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 17.sp
    ),

    bodyLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 16.sp
    ),

    bodyMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp
    ),

    labelLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp
    ),

    labelSmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 12.sp
    )
)


@Composable
fun DebtTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    val colors = if (darkTheme) {
        DarkColors
    } else {
        LightColors
    }

    MaterialTheme(
        colorScheme = colors,
        typography = AppTypography,
        content = content
    )
}