package com.michaelrmossman.aucklandmuseum3api.objects

import com.michaelrmossman.aucklandmuseum3api.enum.MediaType
import kotlinx.serialization.Serializable

@Serializable
data class ImageObjects(

    /* The list of ImageObject items */
    val imageObjects: List<ImageObject> = listOf(ImageObject()),
    /* Image in list that is clicked */
    val itemIndex: Int = 0,
    /* The original opecObject title */
    val itemTitle: String = "Saddle, Camel",
    /* Whether type Object or Person */
    val mediaType: MediaType = MediaType.Object
)