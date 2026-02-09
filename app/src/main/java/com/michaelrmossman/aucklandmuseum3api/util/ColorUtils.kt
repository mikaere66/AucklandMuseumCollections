package com.michaelrmossman.aucklandmuseum3api.util

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.michaelrmossman.aucklandmuseum3api.R

/**
 * Custom color utilities used throughout the app
 */
object ColorUtils {

    @Composable
    fun getEmptyListColor(): Color {
        return colorResource(R.color.empty_list)
    }
}