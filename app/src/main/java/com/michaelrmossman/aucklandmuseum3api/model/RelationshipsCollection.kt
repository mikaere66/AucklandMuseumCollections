package com.michaelrmossman.aucklandmuseum3api.model

import kotlinx.serialization.Serializable

@Serializable
data class RelationshipsCollection(

    val relationships: List<Relationship> = emptyList(),
    val totalRelationships: Int = 0
)