package com.michaelrmossman.aucklandmuseum3api.model

import kotlinx.serialization.Serializable

@Serializable
data class Facet(

    val facetId  : String = String(),
    val facet    : String = String(),
    val facetType: String = String(),
    val facetLabelValues: List<FacetLabelValue> = emptyList(),
    val totalLabelValues: Int = 0
)