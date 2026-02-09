package com.michaelrmossman.aucklandmuseum3api.state

import com.michaelrmossman.aucklandmuseum3api.enum.Collection
import com.michaelrmossman.aucklandmuseum3api.model.OpacObject

data class ObjectsListState(
    val canDownload: Boolean = false,
    val collections: List<Collection> = emptyList(),
    val historyList: List<String> = emptyList(),
    val resultCount: Int = 0,
    val responseMsg: String? = null,
    val resultsList: List<OpacObject> = emptyList(),
    val resultState: SearchUiState = SearchUiState.Init,
    val searchQuery: String = String()
) {
    companion object {
        const val START_FROM = 0
    }
}