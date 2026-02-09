package com.michaelrmossman.aucklandmuseum3api.ui.persons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.michaelrmossman.aucklandmuseum3api.R
import com.michaelrmossman.aucklandmuseum3api.model.OpacPerson
import com.michaelrmossman.aucklandmuseum3api.state.PersonsListState
import com.michaelrmossman.aucklandmuseum3api.state.SearchUiState
import com.michaelrmossman.aucklandmuseum3api.ui.components.ButtonWithIcon
import com.michaelrmossman.aucklandmuseum3api.util.ModifierUtils.downloading
import com.michaelrmossman.aucklandmuseum3api.util.ResourceUtils.downloadDrawableIds
import com.michaelrmossman.aucklandmuseum3api.util.ResourceUtils.downloadStringIds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonsSearchResults(
    contentPadding: PaddingValues,
    onClickDownloadMore: () -> Unit,
    onClickSearchResult: (OpacPerson) -> Unit,
    viewState: PersonsListState
) {
    val innerPaddingHorizontal = dimensionResource(R.dimen.padding_medium)
    val innerPaddingVertical = dimensionResource(R.dimen.padding_small)
    val lazyListState = rememberLazyListState()

    LazyColumn(
        state = lazyListState,
        verticalArrangement = Arrangement.spacedBy(
            dimensionResource(R.dimen.spacing_vertical_small)
        ),
        modifier = Modifier
            .fillMaxHeight()
            .padding(
                bottom = contentPadding.calculateBottomPadding()
            )
    ) {
        /* itemsIndexed is used, without the need for
           index, to ensure unique ids for lazyColumn */
        itemsIndexed(
            items = viewState.resultsList,
        ) { _, result ->

            PersonListItem(
                /* Modifier used by all [OpacPerson]s */
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        dimensionResource(R.dimen.padding_list_item)
                    ),
                onClickSearchResult = onClickSearchResult,
                paddingHorizontal = innerPaddingHorizontal,
                paddingVertical = innerPaddingVertical,
                result = result
            )
        }

        /* In case Manage Search used, and Search All selected */
        if (viewState.resultsList.isNotEmpty()) {
            val isDownloading =
                viewState.resultState is SearchUiState.GettingMore
            item(
                key = Int.MAX_VALUE
            ) {
                ButtonWithIcon(
                    drawableId = when (isDownloading) {
                        true -> downloadDrawableIds.first
                        else -> when (viewState.canDownload) {
                            true -> downloadDrawableIds.second
                            else -> downloadDrawableIds.third
                        }
                    },
                    isEnabled = (
                        viewState.canDownload
                        &&
                        !isDownloading
                    ),
                    /* Refer custom modifier in ModifierUtils */
                    modifier = Modifier.downloading(isDownloading),
                    onClickButton = onClickDownloadMore,
                    stringId = when (isDownloading) {
                        true -> downloadStringIds.first
                        else -> when (viewState.canDownload) {
                            true -> downloadStringIds.second
                            else -> downloadStringIds.third
                        }
                    }
                )
            }
        }
    }
}