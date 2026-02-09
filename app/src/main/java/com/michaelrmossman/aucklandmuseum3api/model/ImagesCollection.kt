package com.michaelrmossman.aucklandmuseum3api.model

import kotlinx.serialization.Serializable

@Serializable
data class ImagesCollection(

    val images: List<Image> = emptyList(),
    val totalImages: Int = 0
)