package com.michaelrmossman.aucklandmuseum3api.ui.favourites

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.michaelrmossman.aucklandmuseum3api.R
import com.michaelrmossman.aucklandmuseum3api.data.FaveEntity
import com.michaelrmossman.aucklandmuseum3api.enum.MediaType
import com.michaelrmossman.aucklandmuseum3api.objects.ImageObjects
import com.michaelrmossman.aucklandmuseum3api.state.FavesListState
import com.michaelrmossman.aucklandmuseum3api.state.FavesUiState
import com.michaelrmossman.aucklandmuseum3api.ui.EmptySearchScreen
import com.michaelrmossman.aucklandmuseum3api.ui.ErrorScreen
import com.michaelrmossman.aucklandmuseum3api.ui.ForbiddenScreen
import com.michaelrmossman.aucklandmuseum3api.ui.LoadingScreen
import com.michaelrmossman.aucklandmuseum3api.ui.NotFoundScreen
import com.michaelrmossman.aucklandmuseum3api.ui.components.TwoLineAppBar
import com.michaelrmossman.aucklandmuseum3api.ui.persons.PersonDetailsCard
import com.michaelrmossman.aucklandmuseum3api.util.titleText

/* Will download a single OpacPerson, upon
   clicking favourite MediaType.Person */
@Composable
fun SinglePersonScreen(
    favourite: FaveEntity,
    onClickBackButton: () -> Unit,
    onClickImage: (ImageObjects) -> Unit,
    @StringRes stringId: Int,
    windowSize: WindowWidthSizeClass
) {
    var downloadedReqd by rememberSaveable { mutableStateOf(true) }
    val viewModel: FavesViewModel = viewModel(factory = FavesViewModel.Factory)
    val snackbarHostState = remember { SnackbarHostState() }
    // val viewState by viewModel.favesListState.collectAsState()
    val viewState by viewModel.favesListState.observeAsState(
        initial = FavesListState()
    )

    LaunchedEffect(key1 = Unit) {
        if (
            viewState.favPerson != null
            &&
            (viewState.favPerson?.opacPersonId.toString()
            !=
            favourite.itemId)
        ) {
            viewModel.resetState()
            downloadedReqd = true
        }
    }

    LaunchedEffect(key1 = downloadedReqd) {
        if (downloadedReqd) {
            viewModel.getPerson(favourite.itemId)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TwoLineAppBar(
                actions = { /* TODO */ },
                onClickBackButton = onClickBackButton,
                stringId = when (windowSize) {
                    WindowWidthSizeClass.Compact -> {
                        R.string.nav_faves_2
                    }
                    /* Pass zero as titleId for larger screens,
                       to indicate NO navigation or title text */
                    else -> 0
                },
                subtitle = stringResource(
                    stringId,
                    favourite.media
                )
            )
        }
    ) { contentPadding ->

        /* Column not reqd, but to approximate
           SearchBoxWithContent pos|layout */
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            when (viewState.listState) {
                is FavesUiState.Downloading -> LoadingScreen(
                    modifier = Modifier.fillMaxSize(),
                    stringId = R.string.loading_anim
                )
                is FavesUiState.Error -> ErrorScreen(
                    errorMessage = viewState.errorMess,
                    modifier = Modifier.fillMaxSize(),
                    retryAction = {
                        viewModel.getPerson(favourite.itemId)
                    }
                )
                is FavesUiState.Forbidden -> ForbiddenScreen(
                    modifier = Modifier.fillMaxSize()
                )
                is FavesUiState.NotFound -> NotFoundScreen(
                    modifier = Modifier.fillMaxSize(),
                    query = favourite.title
                )
                is FavesUiState.Success -> {
                    viewState.favPerson?.let { person ->
                        val titlesFlattened = person.titleText()
                        PersonDetailsCard(
                            contentPadding = contentPadding,
                            fromFavourites = true,
                            onClickImage = { index, images ->
                                onClickImage(
                                    ImageObjects(
                                        imageObjects = images,
                                        itemIndex = index,
                                        itemTitle = titlesFlattened,
                                        mediaType = MediaType.Person
                                    )
                                )
                            },
                            result = person,
                            titlesFlattened = titlesFlattened,
                            windowSize = windowSize,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = dimensionResource(
                                        R.dimen.padding_details_item_hori
                                    ),
                                    vertical = dimensionResource(
                                        R.dimen.padding_details_item_vert
                                    )
                                )
                        )
                        downloadedReqd = false
                    }
                }
            }
        }
    }
}