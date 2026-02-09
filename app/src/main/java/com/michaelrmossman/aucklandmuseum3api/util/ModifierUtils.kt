package com.michaelrmossman.aucklandmuseum3api.util

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.integerResource
import com.michaelrmossman.aucklandmuseum3api.R

/**
 * Custom modifiers used throughout the app
 */
object ModifierUtils {

    // https://developer.android.com/develop/ui/compose/custom-modifiers

    @Composable
    fun Modifier.downloading(isDownloading: Boolean): Modifier {
        val infiniteTransition = rememberInfiniteTransition(
            label = "Infinite Transition"
        )
        val angle by infiniteTransition.animateFloat(
            initialValue = 0F,
            targetValue = 360F,
            label = "Transition Angle",
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = integerResource(
                        id = R.integer.loading_anim_duration
                    ),
                    easing = LinearEasing
                )
            )
        )
        return this then Modifier.graphicsLayer {
            this.rotationZ = when (isDownloading) {
                true -> angle
                else -> 0F
            }
        }
    }
}