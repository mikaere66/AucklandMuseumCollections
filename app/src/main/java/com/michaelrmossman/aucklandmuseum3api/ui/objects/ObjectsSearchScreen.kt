package com.michaelrmossman.aucklandmuseum3api.ui.objects

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
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.michaelrmossman.aucklandmuseum3api.R
import com.michaelrmossman.aucklandmuseum3api.enum.Collection
import com.michaelrmossman.aucklandmuseum3api.enum.CollectionDocHeritage
import com.michaelrmossman.aucklandmuseum3api.enum.CollectionHumanHistory
import com.michaelrmossman.aucklandmuseum3api.enum.CollectionNatSciences
import com.michaelrmossman.aucklandmuseum3api.enum.MediaType
import com.michaelrmossman.aucklandmuseum3api.model.OpacObject
import com.michaelrmossman.aucklandmuseum3api.state.SearchUiState
import com.michaelrmossman.aucklandmuseum3api.ui.EmptySearchScreen
import com.michaelrmossman.aucklandmuseum3api.ui.ErrorScreen
import com.michaelrmossman.aucklandmuseum3api.ui.ForbiddenScreen
import com.michaelrmossman.aucklandmuseum3api.ui.InitScreen
import com.michaelrmossman.aucklandmuseum3api.ui.LoadingScreen
import com.michaelrmossman.aucklandmuseum3api.ui.NotFoundScreen
import com.michaelrmossman.aucklandmuseum3api.ui.components.AdvancedSearchMenu
import com.michaelrmossman.aucklandmuseum3api.ui.components.SearchBoxSuggestions
import com.michaelrmossman.aucklandmuseum3api.ui.components.SearchBoxWithButton
import com.michaelrmossman.aucklandmuseum3api.ui.components.SingleActionMenu
import com.michaelrmossman.aucklandmuseum3api.ui.components.TwoLineAppBar
import com.michaelrmossman.aucklandmuseum3api.util.CollectionUtils.getCollectionEntries
import com.michaelrmossman.aucklandmuseum3api.util.DialogUtils.SelectDeptDialog
import com.michaelrmossman.aucklandmuseum3api.util.EmojiInputFilter
import com.michaelrmossman.aucklandmuseum3api.util.DialogUtils.IconsLegendDialog
import com.michaelrmossman.aucklandmuseum3api.util.TextUtils.getAppSubtitle
import com.michaelrmossman.aucklandmuseum3api.util.humanReadable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ObjectsSearchScreen(
    @DrawableRes drawableId: Int,
    onClickBackButton: () -> Unit,
    onClickSearchResult: (Boolean, OpacObject) -> Unit,
    windowHeightSize: WindowHeightSizeClass,
    windowWidthSize: WindowWidthSizeClass
) {
    var filteredSuggestions by remember { mutableStateOf(emptyList<String>()) }
    val keyboardController = LocalSoftwareKeyboardController.current
    var isSubsequent by rememberSaveable { mutableStateOf(false) }
    val objectsViewModel: ObjectsViewModel = viewModel(
        factory = ObjectsViewModel.Factory
    )
    /* Used to either populate, or tear down, history */
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    lifecycleOwner.lifecycle.addObserver(objectsViewModel)

    val onClickSearchButton = { query: String ->
        keyboardController?.hide()
        filteredSuggestions = emptyList()
        objectsViewModel.getObjectSearchResults(
            /* For a NEW query, startFrom will
               be reset to its default of 0 */
            query = query
        )
    }
    val viewState by objectsViewModel.resultsListState.collectAsState()

    var searchQuery by rememberSaveable { mutableStateOf(viewState.searchQuery) }
    var showDeptCollection by remember { mutableStateOf<Collection?>(null) }
    showDeptCollection?.let { collection ->
        SelectDeptDialog(
            currentSelection = with (viewState.collections) {
                when (size) {
                    0 -> null
                    else -> this[lastIndex]
                }
            },
            entries = getCollectionEntries(collection),
            onClickConfirm = { collection ->
                objectsViewModel.manageSearch(collection)
                showDeptCollection = null
            },
            onClickDismiss = { showDeptCollection = null }
        )
    }

    var showIconsDialog by remember { mutableStateOf(false) }
    if (showIconsDialog) {
        val entries = getCollectionEntries(
            with (viewState.collections) {
                when (size) {
                    0 -> null
                    else -> this[lastIndex]
                }
            }
        )
        IconsLegendDialog(
            entries = entries,
            parentCollection = when (viewState.collections.size) {
                0 -> null
                else -> viewState.collections.first()
            },
            onClickConfirm = { showIconsDialog = false },
            title = stringResource(
                R.string.icons_legend_title,
                when (viewState.collections.size) {
                    0 -> stringResource(R.string.icons_legend_parent)
                    else -> entries.first().parent.humanReadable()
                }
            )
        )
    }

    Scaffold(
        topBar = {
            TwoLineAppBar(
                /* Similar to SearchBoxActionMenu, which selects one of
                   three COLLECTIONS, selecting from AdvancedSearchMenu
                   shows dialog asking for department WITHIN collection */
                actions = {
                    AdvancedSearchMenu(
                        currentResultsSize = viewState.resultsList.size,
                        onClickClearSearch = {
                            searchQuery = String()
                            objectsViewModel.resetListState()
                        },
                        onClickIconsLegend = { showIconsDialog = true },
                        onClickManageSearch = { collection ->
                            if (collection == null) { /* Reset */
                                objectsViewModel.manageSearch(collection)
                            }
                            showDeptCollection = collection
                        },
                        currentCollection = when (
                            viewState.collections.size
                        ) {
                            0 -> null
                            else -> viewState.collections[
                                viewState.collections.lastIndex
                            ]
                        }
                    )
                },
                onClickBackButton = onClickBackButton,
                stringId = R.string.nav_objects_2,
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
                    objectsViewModel.cancelDownloadState()
                    filteredSuggestions = emptyList()
                    searchQuery = String()
                },
                onClickManageSearch = { collection ->
                    /* showDeptCollection not relevant here */
                    objectsViewModel.manageSearch(collection = collection)
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
                currentCollection = when (viewState.collections.size) {
                    0 -> null
                    else -> viewState.collections[0]
                }
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
                            objectsViewModel.getObjectSearchResults(
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
                        currentCollection = with (viewState.collections) {
                            when (size) {
                                0 -> null
                                else -> this[lastIndex]
                            }
                        },
                        drawableId = drawableId,
                        mediaType = MediaType.Object,
                        modifier = Modifier.fillMaxSize(),
                        stringId = R.string.details_message_coll_objects,
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
                        ObjectsSearchResults(
                            contentPadding = contentPadding,
                            onClickDownloadMore = {
                                objectsViewModel.getObjectSearchResults(
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