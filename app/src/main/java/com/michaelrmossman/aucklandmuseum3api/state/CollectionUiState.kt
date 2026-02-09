package com.michaelrmossman.aucklandmuseum3api.state

sealed interface CollectionUiState {
    data object Downloading: CollectionUiState
    data object Error      : CollectionUiState
    data object Forbidden  : CollectionUiState
    data object NoResult   : CollectionUiState
    data object Success    : CollectionUiState
}