package com.michaelrmossman.aucklandmuseum3api.network

import com.michaelrmossman.aucklandmuseum3api.model.OpacObject

data class ObjectResponse(

    val responseCode : Int = 0,

    val responseMessage: String? = null,

    val searchResult: OpacObject? = null
)