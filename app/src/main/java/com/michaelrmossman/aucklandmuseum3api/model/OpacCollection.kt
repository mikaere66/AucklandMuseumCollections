package com.michaelrmossman.aucklandmuseum3api.model

import kotlinx.serialization.Serializable

@Serializable
data class OpacCollection(

    val opacCollectionId: Int = 0,
    val collectionName: String = String()
)