package com.getabreak.app.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * A very subtle, restrained liquid-glass surface modifier for overlapping surfaces
 * (e.g., top bars, bottom sheets, floating actions).
 *
 * Characteristics:
 * - Nearly opaque neutral surface (~0.94 alpha)
 * - Subtle 1px neutral border
 * - Restrained corner radius
 * - No glossy gradients, glowing edges, or heavy blur.
 */
@Composable
fun Modifier.subtleGlassSurface(
    shape: Shape = RoundedCornerShape(12.dp),
    borderWidth: Dp = 1.dp,
    alpha: Float = 0.94f,
): Modifier {
    val dark = isAppInDarkTheme()
    val surfaceColor = if (dark) BreakDarkSurface else BreakLightSurface
    val borderColor = if (dark) BreakDarkBorder else BreakLightBorder

    return this
        .clip(shape)
        .background(surfaceColor.copy(alpha = alpha), shape)
        .border(borderWidth, borderColor, shape)
}

/**
 * Color value for subtle glass background.
 */
@Composable
fun subtleGlassBackgroundColor(alpha: Float = 0.94f): Color {
    val dark = isAppInDarkTheme()
    return if (dark) BreakDarkSurface.copy(alpha = alpha) else BreakLightSurface.copy(alpha = alpha)
}

/**
 * Color value for subtle glass border.
 */
@Composable
fun subtleGlassBorderColor(): Color {
    val dark = isAppInDarkTheme()
    return if (dark) BreakDarkBorder else BreakLightBorder
}
