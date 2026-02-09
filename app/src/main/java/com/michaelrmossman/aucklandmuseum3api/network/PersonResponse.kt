package com.michaelrmossman.aucklandmuseum3api.network

import com.michaelrmossman.aucklandmuseum3api.model.OpacPerson

data class PersonResponse(

    val responseCode : Int = 0,

    val responseMessage: String? = null,

    val searchResult: OpacPerson? = null
)