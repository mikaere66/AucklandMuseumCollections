package com.michaelrmossman.aucklandmuseum3api.model

import kotlinx.serialization.Serializable

@Serializable
data class RelatedRecordAttribute(

    val identifier: String = String(),
    val label: String = String(),
    val values: List<String> = emptyList()
)