package com.michaelrmossman.aucklandmuseum3api.ui.favourites

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.michaelrmossman.aucklandmuseum3api.R
import com.michaelrmossman.aucklandmuseum3api.data.FaveEntity
import com.michaelrmossman.aucklandmuseum3api.enum.MediaType
import com.michaelrmossman.aucklandmuseum3api.enum.SortFavesBy
import com.michaelrmossman.aucklandmuseum3api.state.FavesListState
import com.michaelrmossman.aucklandmuseum3api.state.FavesUiState
import com.michaelrmossman.aucklandmuseum3api.ui.EmptyFaves
import com.michaelrmossman.aucklandmuseum3api.ui.LoadingScreen
import com.michaelrmossman.aucklandmuseum3api.ui.components.SingleActionMenu
import com.michaelrmossman.aucklandmuseum3api.ui.components.SortByActionMenu
import com.michaelrmossman.aucklandmuseum3api.ui.components.TwoLineAppBar
import com.michaelrmossman.aucklandmuseum3api.util.DialogUtils.ConfirmDeleteAllFavesDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavesListScreen(
    onClickBackButton: () -> Unit,
    onClickFavourite: (FaveEntity, MediaType) -> Unit,
    // onLongClickFavourite: (AMapMarker) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: FavesViewModel = viewModel(factory = FavesViewModel.Factory)
    val favesSortedBy = viewModel.favesSortedBy.observeAsState(initial = 0)
    val favourites by viewModel.favourites.observeAsState(initial = emptyList())
    var showRemoveAllDialog by remember { mutableStateOf(false) }
    val viewState by viewModel.favesListState.observeAsState(
        initial = FavesListState()
    )

    Scaffold(
        topBar = {
            TwoLineAppBar(
                actions = {
                    SortByActionMenu(
                        isEnabled = favourites.size > 1,
                        onSortByDateClick = { viewModel.setFavesSortedBy(
                            SortFavesBy.Date
                        )},
                        onSortByNameClick = { viewModel.setFavesSortedBy(
                            SortFavesBy.Name
                        )},
                        onSortByTypeClick = { viewModel.setFavesSortedBy(
                            SortFavesBy.Type
                        )},
                        sortedBy = SortFavesBy.entries[favesSortedBy.value]
                    )
                    SingleActionMenu(
                        isEnabled = favourites.isNotEmpty(),
                        itemStringId = R.string.menu_faves_delete_all,
                        onSingleItemClick = { showRemoveAllDialog = true }
                    )
                },
                onClickBackButton = onClickBackButton,
                stringId = R.string.app_name,
                subtitle = stringResource(
                    R.string.faves_subtitle,
                    favourites.size
                )
            )
        }
    ) { contentPadding ->

        when (favourites.isEmpty()) {

            true -> when (viewState.listState) {
                FavesUiState.Downloading -> LoadingScreen(
                    modifier = Modifier.fillMaxSize(),
                    stringId = R.string.loading_database
                )
                else -> EmptyFaves(
                    drawableId = R.drawable.outline_heart_broken_24,
                    stringId = R.string.favourites_empty,
                    modifier = Modifier.fillMaxSize()
                )
            }
            else -> FavesList(
                contentPadding = contentPadding,
                favourites = favourites,
                modifier = modifier.padding(
                    start = dimensionResource(R.dimen.padding_medium),
                    end = dimensionResource(R.dimen.padding_medium)
                ),
                onClickFavourite = onClickFavourite,
                // onLongClickFavourite = onLongClickFavourite,
                onClickToggleFavourite = { favourite ->
                    viewModel.deleteFave(favourite)
                }
            )
        }

        if (showRemoveAllDialog) {
            ConfirmDeleteAllFavesDialog(
                onClickConfirm = {
                    showRemoveAllDialog = false
                    viewModel.deleteAllFavourites()
                    /* Quit on remove all faves */
                    onClickBackButton()
                },
                onClickDismiss = { showRemoveAllDialog = false }
            )
        }
    }
}