package com.michaelrmossman.aucklandmuseum3api.model

import kotlinx.serialization.Serializable

@Serializable
data class OpacObjectField(

    val value: String = String(),
    val opacObjectFieldAttributes: List<OpacObjectFieldAttribute> = emptyList()
)