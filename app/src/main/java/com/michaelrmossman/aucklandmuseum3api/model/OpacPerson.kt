package com.michaelrmossman.aucklandmuseum3api.model

import kotlinx.serialization.Serializable

@Serializable
data class OpacPerson(

    val opacPersonId: String = String(),
    val personSearchScore: Double = 0.0,
    val opacPersonFieldSets: List<OpacPersonFieldSet> = emptyList(),
    val relationshipsCollection: RelationshipsCollection =
        RelationshipsCollection(),
    val imagesCollection: ImagesCollection = ImagesCollection(),
    val slug: String = String()
)