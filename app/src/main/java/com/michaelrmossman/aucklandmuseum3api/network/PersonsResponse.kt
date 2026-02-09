package com.michaelrmossman.aucklandmuseum3api.network

import com.michaelrmossman.aucklandmuseum3api.model.OpacPersons

data class PersonsResponse(

    val responseCode : Int = 0,

    val responseMessage: String? = null,

    val searchResults: OpacPersons? = null
)