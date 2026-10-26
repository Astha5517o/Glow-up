package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable

/**
 * Backward-compatible wrapper delegating to AsthaTheme claymorphic design system.
 */
@Composable
fun GlowUpTheme(
    themeMode: String = "LIGHT",
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    AsthaTheme(
        themeMode = themeMode,
        darkTheme = darkTheme,
        content = content
    )
}

