package com.michaelrmossman.aucklandmuseum3api.model

import kotlinx.serialization.Serializable

@Serializable
data class Relationship(

    val relationshipId: String = String(),
    val relatedRecordType: String = String(),
    val relatedRecords: List<RelatedRecord> = emptyList(),
    val totalRelatedRecords: Int = 0
)