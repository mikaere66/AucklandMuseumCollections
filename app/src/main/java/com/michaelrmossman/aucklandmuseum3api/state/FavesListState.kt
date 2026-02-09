package com.michaelrmossman.aucklandmuseum3api.state

import com.michaelrmossman.aucklandmuseum3api.model.OpacObject
import com.michaelrmossman.aucklandmuseum3api.model.OpacPerson

data class FavesListState(
    val errorMess: String? = null,
    val favObject: OpacObject? = null,
    val favPerson: OpacPerson? = null,
//    val favesList: List<FaveEntity> = emptyList(),
    val listState: FavesUiState = FavesUiState.Downloading
)