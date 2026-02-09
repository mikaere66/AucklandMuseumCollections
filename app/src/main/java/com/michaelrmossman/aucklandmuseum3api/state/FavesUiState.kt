package com.michaelrmossman.aucklandmuseum3api.state

sealed interface FavesUiState {
    data object Downloading: FavesUiState
//    data object EmptyList  : FavesUiState
    data object Error      : FavesUiState
    data object Forbidden  : FavesUiState
    data object NotFound   : FavesUiState
    data object Success    : FavesUiState
}