package com.michaelrmossman.aucklandmuseum3api.network

import com.michaelrmossman.aucklandmuseum3api.model.OpacObjects

data class ObjectsResponse(

    val responseCode : Int = 0,

    val responseMessage: String? = null,

    val searchResults: OpacObjects? = null
)