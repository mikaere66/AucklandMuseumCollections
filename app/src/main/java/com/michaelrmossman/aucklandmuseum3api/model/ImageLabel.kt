package com.michaelrmossman.aucklandmuseum3api.model

import kotlinx.serialization.Serializable

@Serializable
data class ImageLabel(

    val count: Int = 0,
    val imageLabelId: Int = 0,
    val imageLabel: String = String(),
    val score: Int = 0,
    val status: String = String()
)