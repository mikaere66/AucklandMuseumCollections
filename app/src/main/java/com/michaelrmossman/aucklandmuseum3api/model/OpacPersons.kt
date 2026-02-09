package com.michaelrmossman.aucklandmuseum3api.model

import kotlinx.serialization.Serializable

@Serializable
data class OpacPersons(

    val facetsCollection: FacetsCollection = FacetsCollection(),
    val opacPersons: List<OpacPerson> = emptyList(),
    val totalPersons: Int = 0
)