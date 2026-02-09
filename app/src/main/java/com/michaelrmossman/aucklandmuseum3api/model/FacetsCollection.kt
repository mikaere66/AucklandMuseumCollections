package com.michaelrmossman.aucklandmuseum3api.model

import kotlinx.serialization.Serializable

@Serializable
data class FacetsCollection(

    val facets: List <Facet> = emptyList(),
    val totalFacets: Int = 0
)