package com.michaelrmossman.aucklandmuseum3api.objects

import kotlinx.serialization.Serializable

@Serializable
data class ImageObject(

    val imageId: String = "236967",
    val identifier: String = "MEDIUM",
    val url: String = "https://collection-api.aucklandmuseum.com/records/images/medium/32140/05c5bb0506a3369eb021c817762376a94b0b571d.jpg",
    val width: Int = 400,
    val height: Int = 390
)