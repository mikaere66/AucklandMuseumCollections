package com.michaelrmossman.aucklandmuseum3api.model

import kotlinx.serialization.Serializable

@Serializable
data class OpacPersonField(

    val value: String = String(),
    val opacPersonFieldAttributes: List<OpacPersonFieldAttribute> = emptyList()
)