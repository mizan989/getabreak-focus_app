package com.getabreak.app.ui.theme

import androidx.annotation.ColorRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.colorResource

/**
 * Break monochromatic brand tone:
 * A subtle, restrained container and high-contrast content color.
 * Replaces loud saturated rainbow cards with calm, readable neutral tones.
 */
class BrandTone(val container: Color, val content: Color)

@Composable
fun brandTone(@ColorRes colorRes: Int): BrandTone {
    val rawColor = colorResource(id = colorRes)
    val dark = isAppInDarkTheme()

    // Create calm, muted tints that maintain semantic distinction without bright neon look
    val container = if (dark) {
        lerp(BreakDarkSurfaceElevated, rawColor, 0.15f)
    } else {
        lerp(BreakLightSurfaceElevated, rawColor, 0.12f)
    }

    val content = if (dark) {
        lerp(rawColor, BreakDarkTextPrimary, 0.50f)
    } else {
        lerp(rawColor, BreakLightTextPrimary, 0.55f)
    }

    return BrandTone(container = container, content = content)
}
