package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val LightColorScheme = lightColorScheme(
    primary = SoftLavender,
    onPrimary = Color.White,
    primaryContainer = SoftLavenderLight,
    onPrimaryContainer = SoftLavenderDark,
    secondary = SoftSageGreen,
    onSecondary = Color.White,
    secondaryContainer = SoftSageLight,
    onSecondaryContainer = SoftSageDark,
    tertiary = WarmPeachHighlight,
    onTertiary = Color.White,
    tertiaryContainer = WarmPeachLight,
    onTertiaryContainer = WarmPeachDark,
    background = WarmOffWhite,
    onBackground = DarkCharcoal,
    surface = PureWhiteCard,
    onSurface = DarkCharcoal,
    surfaceVariant = SoftCreamSurface,
    onSurfaceVariant = MutedCharcoal,
    outline = Color(0xFFDAD4CC),
    outlineVariant = Color(0xFFEBE6DF)
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimaryLavender,
    onPrimary = Color(0xFF1F1A42),
    primaryContainer = DarkPrimaryContainer,
    onPrimaryContainer = SoftLavenderLight,
    secondary = DarkSecondarySage,
    onSecondary = Color(0xFF152B1D),
    secondaryContainer = DarkSecondaryContainer,
    onSecondaryContainer = SoftSageLight,
    tertiary = Color(0xFFF0A981),
    onTertiary = Color(0xFF47210B),
    tertiaryContainer = Color(0xFF613115),
    onTertiaryContainer = WarmPeachLight,
    background = DarkBackground,
    onBackground = DarkTextPrimary,
    surface = DarkCardSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkElevatedSurface,
    onSurfaceVariant = DarkTextSecondary,
    outline = Color(0xFF433F4D),
    outlineVariant = Color(0xFF322F3B)
)

val GlowUpShapes = Shapes(
    extraSmall = RoundedCornerShape(10.dp),
    small = RoundedCornerShape(14.dp),
    medium = RoundedCornerShape(18.dp),
    large = RoundedCornerShape(22.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

@Composable
fun GlowUpTheme(
    themeMode: String = "LIGHT", // LIGHT, DARK, SYSTEM
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val useDark = when (themeMode.uppercase()) {
        "DARK" -> true
        "LIGHT" -> false
        else -> darkTheme
    }
    val colorScheme = if (useDark) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = GlowUpShapes,
        content = content
    )
}
