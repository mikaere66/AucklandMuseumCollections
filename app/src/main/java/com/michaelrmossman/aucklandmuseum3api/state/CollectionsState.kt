package com.michaelrmossman.aucklandmuseum3api.state

import com.michaelrmossman.aucklandmuseum3api.model.OpacObject
import com.michaelrmossman.aucklandmuseum3api.model.OpacPerson

data class CollectionsState(
    val responseObj: String? = null,
    val responsePer: String? = null,
    val objectCount: Int = 0,
    val personCount: Int = 0,
    val objectState: CollectionUiState = CollectionUiState.Downloading,
    val personState: CollectionUiState = CollectionUiState.Downloading
)