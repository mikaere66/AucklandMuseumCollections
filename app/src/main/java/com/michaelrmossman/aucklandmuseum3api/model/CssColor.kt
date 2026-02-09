package com.michaelrmossman.aucklandmuseum3api.model

import kotlinx.serialization.Serializable

@Serializable
data class CssColor(

    val colorGroup: String = String(),
    val colorName: String = String(),
    val formattedColorName: String = String(),
    val hex: String = String(),
    val hue: Int = 0,
    val lightness: Int = 0,
    val presence: Int = 0,
    val rank: Int = 0,
    val saturation: Int = 0
)