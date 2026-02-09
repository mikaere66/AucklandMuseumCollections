package com.michaelrmossman.aucklandmuseum3api.ui.favourites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.michaelrmossman.aucklandmuseum3api.R
import com.michaelrmossman.aucklandmuseum3api.data.FaveEntity
import com.michaelrmossman.aucklandmuseum3api.enum.MediaType

@Composable
fun FavesList(
    contentPadding: PaddingValues,
    favourites: List<FaveEntity>,
    onClickFavourite: (FaveEntity, MediaType) -> Unit,
    // onLongClickFavourite: (AMapMarker) -> Unit,
    onClickToggleFavourite: (FaveEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val columnVerticalPadding = dimensionResource(
        R.dimen.padding_small
    )
    val columnVerticalSpacing = dimensionResource(
        R.dimen.spacing_vertical_small
    )
    val lazyListState = rememberLazyListState()
    val textHorizontalPadding = dimensionResource(R.dimen.padding_nano)
    val textVerticalPadding = dimensionResource(R.dimen.padding_small)

    LazyColumn(
        contentPadding = contentPadding,
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = columnVerticalPadding),
        state = lazyListState,
        verticalArrangement = Arrangement.spacedBy(
            columnVerticalSpacing
        )
    ) {
        itemsIndexed(
            items = favourites
        ) { _, fave ->

            FaveListItem(
                fave = fave,
                /* Modifier used by all [Text] composables */
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        bottom = textVerticalPadding,
                        end = textHorizontalPadding,
                        start = textHorizontalPadding
                    ),
                onClickFavourite = onClickFavourite,
                // onLongClickFavourite = onLongClickFavourite,
                onClickToggleFavourite = {
                    onClickToggleFavourite(fave)
                }
            )
        }
    }
}