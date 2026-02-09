package com.michaelrmossman.aucklandmuseum3api.model

import kotlinx.serialization.Serializable

@Serializable
data class FacetLabelValue(

    val label: String = String(),
    val value: Int = 0
)