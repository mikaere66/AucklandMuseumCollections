package com.michaelrmossman.aucklandmuseum3api.model

import kotlinx.serialization.Serializable

@Serializable
data class Image(

    val imageId: String = String(),
    val caption: String = String(),
    val imageAttributes: List<ImageAttribute> = emptyList(),
    val imageDerivatives: List<ImageDerivative> = emptyList(),
    val imageLabels: List<ImageLabel> = emptyList(),
    val cssColors: List<CssColor> = emptyList(),
    val rawColors: List<RawColor> = emptyList()
)