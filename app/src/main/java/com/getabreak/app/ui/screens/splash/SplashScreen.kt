package com.getabreak.app.ui.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.getabreak.app.R
import kotlinx.coroutines.delay

private val SplashBackground = Color(0xFF121212)
private val CreditTextColor = Color(0xFF858585)

/**
 * Launch screen matching the reference target design with centered getabreak logo
 * and two-line developer credit at Y ≈ 73.8% of total screen height.
 */
@Composable
fun SplashScreen(
    stayOnSplash: Boolean = false,
    onDismiss: () -> Unit
) {
    if (!stayOnSplash) {
        LaunchedEffect(Unit) {
            delay(1200L)
            onDismiss()
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(SplashBackground)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onDismiss()
            }
    ) {
        val logoSize = maxWidth * 0.68f
        val screenHeight = maxHeight

        // Centered logo matching native splash screen proportions (X: 50%, Y: 50%)
        Image(
            painter = painterResource(id = R.drawable.break_splash_symbol),
            contentDescription = "getabreak Logo",
            modifier = Modifier
                .align(Alignment.Center)
                .size(logoSize)
        )

        // Developer credit block centered at X ≈ 50% width and Y ≈ 73.8% height
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = screenHeight * 0.738f - 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            @Suppress("DEPRECATION")
            val creditTextStyle = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = CreditTextColor,
                letterSpacing = 0.4.em,
                textAlign = TextAlign.Center,
                lineHeight = 15.sp,
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                )
            )

            Text(
                text = "DEVELOPED BY",
                style = creditTextStyle
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "MD MIZAN",
                style = creditTextStyle
            )
        }
    }
}
