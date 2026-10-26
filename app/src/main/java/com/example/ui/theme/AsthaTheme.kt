package com.example.ui.theme

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// ============================================================================
// 1. ASTHA OS CLAYMORPHISM COLOR PALETTE
// ============================================================================
object AsthaPalette {
    // Primary Signature Hues
    val WarmIvory = Color(0xFFFAF8F3)          // Primary App Canvas Background
    val PrimaryLavender = Color(0xFFB8A6FF)     // Soft Luminescent Lavender
    val DeepPurple = Color(0xFF7561C9)          // Rich Tactile Action Purple
    val PaleLavenderSurface = Color(0xFFEEE9FF) // Tactile Inset & Container Surface

    // Accents & Nature Moods
    val SoftMint = Color(0xFFDDF2E5)            // Calming Mint Green Container
    val SageAccent = Color(0xFF85BE9B)          // Growth, Focus & Success Sage
    val Peach = Color(0xFFFFE6D6)               // Energy & Joy Peach Accent
    val SkyBlue = Color(0xFFDFECFA)             // Serene Focus Sky Blue
    val CoralAccent = Color(0xFFD9777E)         // Warm Alert / Distraction Reset Accent
    val AmberAccent = Color(0xFFDE9858)         // Warm Focus Warning / Pause Accent
    val CoralAction = Color(0xFFFF725E)         // Signature Screenshot Coral Action Button
    val CoralLight = Color(0xFFFFEDE9)          // Soft Coral Container
    val Butter = Color(0xFFFFF7DC)              // Soft Butter Pastel
    val ButterText = Color(0xFF8A6C18)          // Butter Card Text
    val LavenderCard = Color(0xFFF1ECFF)        // Pastel Lavender Card
    val MintCard = Color(0xFFE4F7EC)            // Pastel Mint Card
    val PeachCard = Color(0xFFFFEEE3)           // Pastel Peach Card
    val SkyBlueCard = Color(0xFFE4F0FD)         // Pastel Sky Blue Card
    val RoseCard = Color(0xFFFFEAEB)            // Pastel Rose Card
    val RecessedBg = Color(0xFFF3EFE9)          // Recessed Clay Container Surface

    // Clay Surfaces & Typography
    val CardSurface = Color(0xFFFFFFFF)         // Pure Warm White Card Top Surface
    val CardSurfaceSubtle = Color(0xFFF9F7F2)   // Secondary Layered Card
    val PrimaryText = Color(0xFF292735)         // Deep Charcoal High-Contrast Body
    val SecondaryText = Color(0xFF716E7D)       // Muted Slate Supporting Text

    // Clay Physical Light & Shadow Tokens
    val SpecularTopHighlight = Color(0x99FFFFFF) // Top specular highlight edge (light bounce)
    val SpecularSubtleHighlight = Color(0x4DFFFFFF)
    val AmbientShadow = Color(0x1A292735)       // Soft ambient room occlusion
    val ElevatedShadow = Color(0x247561C9)      // Colored lavender/purple depth bloom
    val ClayBorderSubtle = Color(0xFFECE7DF)    // Soft clay edge definition
    val ClayBorderHighlight = Color(0x80FFFFFF)

    // Dark Mode Palette (Calm Evening Focus)
    val DarkBackground = Color(0xFF16151A)
    val DarkCardSurface = Color(0xFF222028)
    val DarkElevatedSurface = Color(0xFF2C2A34)
    val DarkTextPrimary = Color(0xFFF4F1EA)
    val DarkTextSecondary = Color(0xFFB8B3C2)
    val DarkPrimaryLavender = Color(0xFFB8A6FF)
    val DarkPrimaryContainer = Color(0xFF3C3566)
    val DarkSageAccent = Color(0xFF88BD9B)
    val DarkSageContainer = Color(0xFF254230)
}

// ============================================================================
// 2. PILLOWY CORNER RADII DEFINITIONS
// ============================================================================
@Immutable
data class AsthaCornerRadii(
    val extraSmall: Dp = 10.dp,   // Badges, micro tags
    val small: Dp = 14.dp,        // Chips, small input fields
    val medium: Dp = 18.dp,       // Standard buttons, small cards
    val cardPillowy: Dp = 26.dp,  // Primary pillowy clay cards
    val heroPillowy: Dp = 30.dp,  // Hero Next Move card, Focus timer card
    val modalSheet: Dp = 28.dp    // Bottom sheets, top navigation rounds
)

val DefaultAsthaCornerRadii = AsthaCornerRadii()

val AsthaM3Shapes = Shapes(
    extraSmall = RoundedCornerShape(DefaultAsthaCornerRadii.extraSmall),
    small = RoundedCornerShape(DefaultAsthaCornerRadii.small),
    medium = RoundedCornerShape(DefaultAsthaCornerRadii.medium),
    large = RoundedCornerShape(DefaultAsthaCornerRadii.cardPillowy),
    extraLarge = RoundedCornerShape(DefaultAsthaCornerRadii.heroPillowy)
)

// ============================================================================
// 3. PHYSICAL CLAY ELEVATION & SHADOW SYSTEM
// ============================================================================
@Immutable
data class AsthaElevations(
    val flat: Dp = 0.dp,
    val pressed: Dp = 1.dp,        // Inset / depressed tactile feedback
    val chip: Dp = 2.dp,           // Selectable clay pill chip
    val cardDefault: Dp = 4.5.dp,  // Floating pillowy card
    val cardHero: Dp = 6.5.dp,     // Central Next Move hero card
    val floatingBar: Dp = 10.dp    // Bottom navigation bar, floating dialogs
)

val DefaultAsthaElevations = AsthaElevations()

@Immutable
data class AsthaShadowColors(
    val ambient: Color = AsthaPalette.AmbientShadow,
    val coloredBloom: Color = AsthaPalette.ElevatedShadow,
    val topSheen: Color = AsthaPalette.SpecularTopHighlight,
    val border: Color = AsthaPalette.ClayBorderSubtle
)

val DefaultAsthaShadowColors = AsthaShadowColors()

// ============================================================================
// 4. EXTENDED ASTHA THEME DATA
// ============================================================================
@Immutable
data class AsthaColors(
    val warmIvory: Color = AsthaPalette.WarmIvory,
    val primaryLavender: Color = AsthaPalette.PrimaryLavender,
    val deepPurple: Color = AsthaPalette.DeepPurple,
    val paleLavenderSurface: Color = AsthaPalette.PaleLavenderSurface,
    val softMint: Color = AsthaPalette.SoftMint,
    val sageAccent: Color = AsthaPalette.SageAccent,
    val peach: Color = AsthaPalette.Peach,
    val skyBlue: Color = AsthaPalette.SkyBlue,
    val cardSurface: Color = AsthaPalette.CardSurface,
    val primaryText: Color = AsthaPalette.PrimaryText,
    val secondaryText: Color = AsthaPalette.SecondaryText,
    val amberAccent: Color = AsthaPalette.AmberAccent,
    val isDark: Boolean = false
)

val LocalAsthaColors = staticCompositionLocalOf { AsthaColors() }
val LocalAsthaRadii = staticCompositionLocalOf { DefaultAsthaCornerRadii }
val LocalAsthaElevations = staticCompositionLocalOf { DefaultAsthaElevations }
val LocalAsthaShadowColors = staticCompositionLocalOf { DefaultAsthaShadowColors }

// Accessor Object for Astha Theme properties
object AsthaTheme {
    val colors: AsthaColors
        @Composable
        @ReadOnlyComposable
        get() = LocalAsthaColors.current

    val radii: AsthaCornerRadii
        @Composable
        @ReadOnlyComposable
        get() = LocalAsthaRadii.current

    val elevations: AsthaElevations
        @Composable
        @ReadOnlyComposable
        get() = LocalAsthaElevations.current

    val shadows: AsthaShadowColors
        @Composable
        @ReadOnlyComposable
        get() = LocalAsthaShadowColors.current
}

// ============================================================================
// 5. MATERIAL 3 COLOR SCHEMES
// ============================================================================
private val AsthaLightColorScheme = lightColorScheme(
    primary = AsthaPalette.DeepPurple,
    onPrimary = Color.White,
    primaryContainer = AsthaPalette.PaleLavenderSurface,
    onPrimaryContainer = AsthaPalette.DeepPurple,
    secondary = AsthaPalette.SageAccent,
    onSecondary = Color.White,
    secondaryContainer = AsthaPalette.SoftMint,
    onSecondaryContainer = Color(0xFF244F34),
    tertiary = AsthaPalette.Peach,
    onTertiary = Color(0xFF8B3A14),
    tertiaryContainer = AsthaPalette.Peach,
    onTertiaryContainer = Color(0xFF7B3F1D),
    background = AsthaPalette.WarmIvory,
    onBackground = AsthaPalette.PrimaryText,
    surface = AsthaPalette.CardSurface,
    onSurface = AsthaPalette.PrimaryText,
    surfaceVariant = AsthaPalette.PaleLavenderSurface,
    onSurfaceVariant = AsthaPalette.SecondaryText,
    outline = AsthaPalette.ClayBorderSubtle,
    outlineVariant = Color(0xFFF2EEE7)
)

private val AsthaDarkColorScheme = darkColorScheme(
    primary = AsthaPalette.DarkPrimaryLavender,
    onPrimary = Color(0xFF1F1A42),
    primaryContainer = AsthaPalette.DarkPrimaryContainer,
    onPrimaryContainer = AsthaPalette.PaleLavenderSurface,
    secondary = AsthaPalette.DarkSageAccent,
    onSecondary = Color(0xFF152B1D),
    secondaryContainer = AsthaPalette.DarkSageContainer,
    onSecondaryContainer = AsthaPalette.SoftMint,
    tertiary = Color(0xFFF0A981),
    onTertiary = Color(0xFF47210B),
    background = AsthaPalette.DarkBackground,
    onBackground = AsthaPalette.DarkTextPrimary,
    surface = AsthaPalette.DarkCardSurface,
    onSurface = AsthaPalette.DarkTextPrimary,
    surfaceVariant = AsthaPalette.DarkElevatedSurface,
    onSurfaceVariant = AsthaPalette.DarkTextSecondary,
    outline = Color(0xFF433F4D),
    outlineVariant = Color(0xFF322F3B)
)

// ============================================================================
// 6. MAIN ASTHA THEME COMPOSABLE
// ============================================================================
@Composable
fun AsthaTheme(
    themeMode: String = "LIGHT", // LIGHT, DARK, SYSTEM
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val isDark = when (themeMode.uppercase()) {
        "DARK" -> true
        "LIGHT" -> false
        else -> darkTheme
    }

    val colorScheme = if (isDark) AsthaDarkColorScheme else AsthaLightColorScheme
    val asthaColors = AsthaColors(isDark = isDark)
    val asthaShadows = if (isDark) {
        AsthaShadowColors(
            ambient = Color(0x33000000),
            coloredBloom = Color(0x33282245),
            topSheen = Color(0x1FFFFFFF),
            border = Color(0xFF363340)
        )
    } else {
        DefaultAsthaShadowColors
    }

    CompositionLocalProvider(
        LocalAsthaColors provides asthaColors,
        LocalAsthaRadii provides DefaultAsthaCornerRadii,
        LocalAsthaElevations provides DefaultAsthaElevations,
        LocalAsthaShadowColors provides asthaShadows
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            shapes = AsthaM3Shapes,
            content = content
        )
    }
}



// ============================================================================
// 7. CLAYMORPHIC MODIFIER EXTENSIONS
// ============================================================================

/**
 * Applies a physical, pillowy clay surface effect with ambient soft shadow
 * and top specular sheen.
 */
fun Modifier.claymorphicSurface(
    shape: Shape = RoundedCornerShape(26.dp),
    backgroundColor: Color = AsthaPalette.CardSurface,
    borderColor: Color = AsthaPalette.ClayBorderSubtle,
    elevation: Dp = 4.5.dp
): Modifier = this
    .shadow(
        elevation = elevation,
        shape = shape,
        ambientColor = AsthaPalette.AmbientShadow,
        spotColor = AsthaPalette.ElevatedShadow
    )
    .background(backgroundColor, shape)
    .clip(shape)

/**
 * Interactive clay button behavior with smooth spring scale and tactile depression.
 */
@Composable
fun Modifier.clayClickable(
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(24.dp),
    onClick: () -> Unit
): Modifier {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val animatedScale by animateFloatAsState(
        targetValue = if (isPressed && enabled) 0.965f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "clay_click_scale"
    )

    return this
        .graphicsLayer {
            scaleX = animatedScale
            scaleY = animatedScale
        }
        .clip(shape)
        .clickable(
            interactionSource = interactionSource,
            indication = null,
            enabled = enabled,
            onClick = onClick
        )
}
