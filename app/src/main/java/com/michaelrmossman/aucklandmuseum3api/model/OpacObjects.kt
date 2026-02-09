package com.michaelrmossman.aucklandmuseum3api.model

import kotlinx.serialization.Serializable

@Serializable
data class OpacObjects(

    val facetsCollection: FacetsCollection = FacetsCollection(),
    val opacObjects: List<OpacObject> = emptyList(),
    val totalObjects: Int = 0
)