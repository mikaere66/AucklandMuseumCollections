package com.michaelrmossman.aucklandmuseum3api.ui.persons

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.michaelrmossman.aucklandmuseum3api.R
import com.michaelrmossman.aucklandmuseum3api.enum.MediaType
import com.michaelrmossman.aucklandmuseum3api.model.OpacPerson
import com.michaelrmossman.aucklandmuseum3api.state.SearchUiState
import com.michaelrmossman.aucklandmuseum3api.ui.EmptySearchScreen
import com.michaelrmossman.aucklandmuseum3api.ui.ErrorScreen
import com.michaelrmossman.aucklandmuseum3api.ui.ForbiddenScreen
import com.michaelrmossman.aucklandmuseum3api.ui.InitScreen
import com.michaelrmossman.aucklandmuseum3api.ui.LoadingScreen
import com.michaelrmossman.aucklandmuseum3api.ui.NotFoundScreen
import com.michaelrmossman.aucklandmuseum3api.ui.components.SearchBoxSuggestions
import com.michaelrmossman.aucklandmuseum3api.ui.components.SearchBoxWithButton
import com.michaelrmossman.aucklandmuseum3api.ui.components.SingleActionMenu
import com.michaelrmossman.aucklandmuseum3api.ui.components.TwoLineAppBar
import com.michaelrmossman.aucklandmuseum3api.util.EmojiInputFilter
import com.michaelrmossman.aucklandmuseum3api.util.TextUtils.getAppSubtitle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonsSearchScreen(
    @DrawableRes drawableId: Int,
    onClickBackButton: () -> Unit,
    onClickSearchResult: (Boolean, OpacPerson) -> Unit,
    windowHeightSize: WindowHeightSizeClass,
    windowWidthSize: WindowWidthSizeClass
) {
    var filteredSuggestions by remember { mutableStateOf(emptyList<String>()) }
    val keyboardController = LocalSoftwareKeyboardController.current
    var isSubsequent by rememberSaveable { mutableStateOf(false) }
    val personsViewModel: PersonsViewModel = viewModel(
        factory = PersonsViewModel.Factory
    )
    /* Used to either populate, or tear down, history */
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    lifecycleOwner.lifecycle.addObserver(personsViewModel)

    val onClickSearchButton = { query: String ->
        keyboardController?.hide()
        filteredSuggestions = emptyList()
        personsViewModel.getPersonSearchResults(
            /* For a NEW query, startFrom will
               be reset to its default of 0 */
            query = query
        )
    }

    val viewState by personsViewModel.resultsListState.collectAsState()
    var searchQuery by rememberSaveable { mutableStateOf(viewState.searchQuery) }

    Scaffold(
        topBar = {
            TwoLineAppBar(
                actions = {
                    SingleActionMenu(
                        isEnabled = viewState.resultsList.isNotEmpty(),
                        itemStringId = R.string.menu_clear_search,
                        onSingleItemClick = {
                            searchQuery = String()
                            personsViewModel.resetListState()
                        }
                    )
                },
                onClickBackButton = onClickBackButton,
                stringId = R.string.nav_persons_2,
                subtitle = getAppSubtitle(
                    viewState.resultsList.size,
                    viewState.resultCount,
                    R.string.app_subtitle
                )
            )
        }
    ) { contentPadding ->

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                dimensionResource(R.dimen.spacing_vertical_small)
            ),
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = contentPadding.calculateTopPadding()
                )
        ) {
            SearchBoxWithButton(
                isEnabled = (
                    viewState.resultState != SearchUiState.Downloading
                ),
                onClickClearButton = {
                    personsViewModel.cancelDownloadState()
                    filteredSuggestions = emptyList()
                    searchQuery = String()
                },
                onClickSearchButton = onClickSearchButton,
                onTextChanged = { query ->
                    val filteredQuery = EmojiInputFilter().filter(
                        query,0,query.length,
                        null,0,0
                    )
                    searchQuery = filteredQuery
                    filteredSuggestions = when (filteredQuery.isBlank()) {
                        true -> emptyList()
                        else -> viewState.historyList.filter { historyItem ->
                            historyItem.contains(
                                filteredQuery, ignoreCase = true
                            )
                        }
                    }
                },
                searchQuery = searchQuery,
            )

            Box(
                modifier = Modifier.padding(
                    horizontal = dimensionResource(R.dimen.padding_small)
                )
            ) {
                /* Need to specify fillMaxSize each time, so that
                   preview screens in AndroidStudio fit better */
                when (viewState.resultState) {
                    is SearchUiState.Downloading -> LoadingScreen(
                        modifier = Modifier.fillMaxSize(),
                        stringId = R.string.loading_anim
                    )
                    is SearchUiState.Error -> ErrorScreen(
                        errorMessage = viewState.responseMsg,
                        modifier = Modifier.fillMaxSize(),
                        retryAction = {
                            personsViewModel.getPersonSearchResults(
                                /* startFrom would have been applied on the
                                   first attempt, whatever the use case */
                                query = viewState.searchQuery
                            )
                        }
                    )
                    is SearchUiState.Forbidden -> ForbiddenScreen(
                        modifier = Modifier.fillMaxSize()
                    )
                    is SearchUiState.Init -> InitScreen(
                        drawableId = drawableId,
                        mediaType = MediaType.Person,
                        modifier = Modifier.fillMaxSize(),
                        stringId = R.string.details_message_coll_persons,
                        windowHeightSize = windowHeightSize,
                        windowWidthSize = windowWidthSize
                    )
                    is SearchUiState.NoResults -> EmptySearchScreen(
                        modifier = Modifier.fillMaxSize(),
                        query = viewState.searchQuery
                    )
                    is SearchUiState.NotFound -> NotFoundScreen(
                        modifier = Modifier.fillMaxSize(),
                        query = viewState.searchQuery
                    )
                    is SearchUiState.Success, SearchUiState.GettingMore -> {
                        PersonsSearchResults(
                            contentPadding = contentPadding,
                            onClickDownloadMore = {
                                personsViewModel.getPersonSearchResults(
                                    query = viewState.searchQuery,
                                    startFrom = viewState.resultsList.size
                                )
                            },
                            onClickSearchResult = { result ->
                                onClickSearchResult(
                                    isSubsequent, result
                                )
                                isSubsequent = true
                            },
                            // onLongClickSearchResult = onLongClickSearchResult,
                            viewState = viewState
                        )
                    }
                }

                if (filteredSuggestions.isNotEmpty()) {
                    SearchBoxSuggestions(
                        filteredSuggestions = filteredSuggestions,
                        onClickSuggestion = { suggestion ->
                            onClickSearchButton(suggestion)
                            searchQuery = suggestion
                            /* Clear filtered suggestions
                               when suggestion clicked */
                            filteredSuggestions = emptyList()
                        }
                    )
                }

            }
        }
    }
}