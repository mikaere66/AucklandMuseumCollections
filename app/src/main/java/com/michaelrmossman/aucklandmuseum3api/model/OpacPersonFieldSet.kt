package com.michaelrmossman.aucklandmuseum3api.model

import kotlinx.serialization.Serializable

@Serializable
data class OpacPersonFieldSet(

    val identifier: String = String(),
    val opacPersonFields: List<OpacPersonField> = emptyList()
)