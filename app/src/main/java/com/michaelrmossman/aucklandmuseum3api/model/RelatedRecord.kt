package com.michaelrmossman.aucklandmuseum3api.model

import kotlinx.serialization.Serializable

@Serializable
data class RelatedRecord(

    val relatedRecordId: String = String(),
    val title: String = String(),
    val image: Image = Image(),
    val slug: String = String(),
    val relatedRecordAttributes: List<RelatedRecordAttribute> = emptyList()
)