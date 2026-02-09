package com.michaelrmossman.aucklandmuseum3api.state

import com.michaelrmossman.aucklandmuseum3api.enum.Collection
import com.michaelrmossman.aucklandmuseum3api.model.OpacPerson

data class PersonsListState(
    val canDownload: Boolean = false,
    // val collection : Collection? = null,
    val historyList: List<String> = emptyList(),
    val resultCount: Int = 0,
    val responseMsg: String? = null,
    val resultsList: List<OpacPerson> = emptyList(),
    val resultState: SearchUiState = SearchUiState.Init,
    val searchQuery: String = String()
) {
    companion object {
        const val START_FROM = 0
    }
}