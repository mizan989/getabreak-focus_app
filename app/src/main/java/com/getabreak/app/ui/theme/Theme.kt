package com.getabreak.app.ui.theme

import android.graphics.Color
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.LocalActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

/**
 * Break Dark Color Scheme.
 * Restrained, monochromatic palette: black background, subtle neutral surfaces, crisp white text.
 */
private val DarkColorScheme = darkColorScheme(
    primary = BreakDarkTextPrimary,
    onPrimary = BreakDarkBackground,
    primaryContainer = BreakDarkSurfaceElevated,
    onPrimaryContainer = BreakDarkTextPrimary,
    secondary = BreakDarkTextSecondary,
    onSecondary = BreakDarkBackground,
    secondaryContainer = BreakDarkSurfaceSubtle,
    onSecondaryContainer = BreakDarkTextPrimary,
    tertiary = BreakDarkTextSecondary,
    onTertiary = BreakDarkBackground,
    tertiaryContainer = BreakDarkSurfaceSubtle,
    onTertiaryContainer = BreakDarkTextPrimary,
    background = BreakDarkBackground,
    onBackground = BreakDarkTextPrimary,
    surface = BreakDarkSurface,
    onSurface = BreakDarkTextPrimary,
    surfaceVariant = BreakDarkSurfaceSubtle,
    onSurfaceVariant = BreakDarkTextSecondary,
    surfaceContainer = BreakDarkSurface,
    surfaceContainerLow = BreakDarkBackground,
    surfaceContainerHigh = BreakDarkSurfaceElevated,
    surfaceContainerHighest = BreakDarkSurfaceElevated,
    outline = BreakDarkDivider,
    outlineVariant = BreakDarkDivider,
)

/**
 * Break Light Color Scheme.
 * Restrained, monochromatic palette: clean white background, neutral gray surfaces, deep charcoal text.
 */
private val LightColorScheme = lightColorScheme(
    primary = BreakLightTextPrimary,
    onPrimary = BreakLightBackground,
    primaryContainer = BreakLightSurfaceElevated,
    onPrimaryContainer = BreakLightTextPrimary,
    secondary = BreakLightTextSecondary,
    onSecondary = BreakLightBackground,
    secondaryContainer = BreakLightSurfaceSubtle,
    onSecondaryContainer = BreakLightTextPrimary,
    tertiary = BreakLightTextSecondary,
    onTertiary = BreakLightBackground,
    tertiaryContainer = BreakLightSurfaceSubtle,
    onTertiaryContainer = BreakLightTextPrimary,
    background = BreakLightBackground,
    onBackground = BreakLightTextPrimary,
    surface = BreakLightSurface,
    onSurface = BreakLightTextPrimary,
    surfaceVariant = BreakLightSurfaceSubtle,
    onSurfaceVariant = BreakLightTextSecondary,
    surfaceContainer = BreakLightSurface,
    surfaceContainerLow = BreakLightBackground,
    surfaceContainerHigh = BreakLightSurfaceElevated,
    surfaceContainerHighest = BreakLightSurfaceElevated,
    outline = BreakLightDivider,
    outlineVariant = BreakLightDivider,
)

/** Restrained corner radii for a calm, native Android feel. */
private val BreakShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(20.dp),
)

/**
 * Whether the app is in its dark theme: the user's choice, or the system's when they have not made
 * one.
 */
@Composable
fun isAppInDarkTheme(): Boolean {
    val mode by AppearanceSettings.themeMode.collectAsState()
    return when (mode) {
        AppearanceSettings.ThemeMode.SYSTEM -> isSystemInDarkTheme()
        AppearanceSettings.ThemeMode.LIGHT -> false
        AppearanceSettings.ThemeMode.DARK -> true
    }
}

/**
 * The app's theme. Uses a strict monochromatic color system.
 * By default, dynamicColor is false to preserve the intentional monochromatic aesthetic.
 */
@Composable
fun BreakTheme(
    darkTheme: Boolean = isAppInDarkTheme(),
    dynamicColor: Boolean = false,
    syncSystemBars: Boolean = true,
    content: @Composable () -> Unit
) {
    if (syncSystemBars) SystemBarsFollowTheme(darkTheme)
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
        typography = Typography,
        shapes = BreakShapes,
        content = content
    )
}

/** Backward compatibility alias for DetoxDroidTheme. */
@Composable
fun DetoxDroidTheme(
    darkTheme: Boolean = isAppInDarkTheme(),
    dynamicColor: Boolean = false,
    syncSystemBars: Boolean = true,
    content: @Composable () -> Unit
) = BreakTheme(darkTheme, dynamicColor, syncSystemBars, content)

/**
 * Re-applies the edge-to-edge setup of the hosting activity, if any, with the theme's own notion
 * of dark, so status bar icons stay readable on a dark app in a light system and the other way
 * round.
 */
@Composable
private fun SystemBarsFollowTheme(darkTheme: Boolean) {
    val activity = LocalActivity.current as? ComponentActivity ?: return
    LaunchedEffect(activity, darkTheme) {
        activity.enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT) { darkTheme },
            navigationBarStyle = SystemBarStyle.auto(LightScrim, DarkScrim) { darkTheme }
        )
    }
}

private val LightScrim = Color.argb(0xe6, 0xFF, 0xFF, 0xFF)
private val DarkScrim = Color.argb(0x80, 0x11, 0x11, 0x11)
