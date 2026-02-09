package com.michaelrmossman.aucklandmuseum3api.state

sealed interface SearchUiState {
    data object Downloading: SearchUiState
    data object Error      : SearchUiState
    data object Forbidden  : SearchUiState // TODO
    data object GettingMore: SearchUiState
    data object Init       : SearchUiState
    data object NoResults  : SearchUiState
    data object NotFound   : SearchUiState
    data object Success    : SearchUiState
}