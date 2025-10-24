package com.insurtech.kanguro.shared.ui.composables

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun ConfettiAnimation(
    modifier: Modifier = Modifier,
    isActive: Boolean,
    colors: List<Color> = listOf(
        Color.Red,
        Color.Green,
        Color.Blue,
        Color.Yellow,
        Color.Magenta
    )
) {
    val particles = remember { mutableStateListOf<ConfettiParticle>() }

    LaunchedEffect(isActive) {
        if (isActive) {
            particles.clear()
            repeat(100) { // Number of confetti particles
                particles.add(
                    ConfettiParticle(
                        x = Random.nextFloat(),
                        y = Random.nextFloat(),
                        rotation = Random.nextFloat() * 360f,
                        speed = Random.nextFloat() * 4f + 2f,
                        color = colors.random()
                    )
                )
            }
        } else {
            particles.clear()
        }
    }

    // Animate the particles
    val infiniteTransition = rememberInfiniteTransition()
    val animationProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing)
        )
    )

    Box(modifier = modifier.fillMaxWidth()) {
        Canvas(modifier = modifier.fillMaxWidth()) {
            particles.forEach { particle ->
                val yOffset = animationProgress * size.height
                val xOffset = animationProgress * (particle.x * size.width + (particle.speed * yOffset * 0.1f))

                rotate(particle.rotation) {
                    drawCircle(
                        color = particle.color,
                        radius = 4.dp.toPx(),
                        center = Offset(xOffset, yOffset)
                    )
                }
            }
        }
    }
}

data class ConfettiParticle(
    val x: Float,
    val y: Float,
    val rotation: Float,
    val speed: Float,
    val color: Color
)
