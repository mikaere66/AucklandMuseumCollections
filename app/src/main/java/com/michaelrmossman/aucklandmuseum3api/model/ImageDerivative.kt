package com.michaelrmossman.aucklandmuseum3api.model

import kotlinx.serialization.Serializable

@Serializable
data class ImageDerivative(

    val identifier: String = String(),
    val url: String = String(),
    val width: Int = 0,
    val height: Int = 0
)