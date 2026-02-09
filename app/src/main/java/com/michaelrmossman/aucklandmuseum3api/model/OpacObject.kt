package com.michaelrmossman.aucklandmuseum3api.model

import kotlinx.serialization.Serializable

@Serializable
data class OpacObject(

    val opacObjectId: Int = 0,
    val objectSearchScore: Double = 0.0,
    val opacCollection: OpacCollection = OpacCollection(),
    val opacObjectFieldSets: List<OpacObjectFieldSet> = emptyList(),
    val relationshipsCollection: RelationshipsCollection =
        RelationshipsCollection(),
    val imagesCollection: ImagesCollection = ImagesCollection(),
    val slug: String = String()
)