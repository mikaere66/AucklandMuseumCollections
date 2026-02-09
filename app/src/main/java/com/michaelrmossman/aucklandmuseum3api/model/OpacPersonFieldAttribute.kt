package com.michaelrmossman.aucklandmuseum3api.model

import kotlinx.serialization.Serializable

@Serializable
data class OpacPersonFieldAttribute(

    val key: String = String(),
    val value: String = String()
)