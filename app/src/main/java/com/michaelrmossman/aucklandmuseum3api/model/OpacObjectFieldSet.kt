package com.michaelrmossman.aucklandmuseum3api.model

import kotlinx.serialization.Serializable

@Serializable
data class OpacObjectFieldSet(

    val identifier: String = String(),
    val opacObjectFields: List<OpacObjectField> = emptyList()
)