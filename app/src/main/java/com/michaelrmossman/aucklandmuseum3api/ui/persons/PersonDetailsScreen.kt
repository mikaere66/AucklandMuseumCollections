package com.michaelrmossman.aucklandmuseum3api.ui.persons

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.michaelrmossman.aucklandmuseum3api.R
import com.michaelrmossman.aucklandmuseum3api.enum.MediaType
import com.michaelrmossman.aucklandmuseum3api.model.OpacPerson
import com.michaelrmossman.aucklandmuseum3api.objects.ImageObjects
import com.michaelrmossman.aucklandmuseum3api.state.SearchUiState
import com.michaelrmossman.aucklandmuseum3api.ui.components.DownloadButton
import com.michaelrmossman.aucklandmuseum3api.ui.components.DynamicActionMenu
import com.michaelrmossman.aucklandmuseum3api.ui.components.TwoLineAppBar
import com.michaelrmossman.aucklandmuseum3api.util.DialogUtils.IconsLegendBasic
import com.michaelrmossman.aucklandmuseum3api.util.SEPARATOR_RECORD
import com.michaelrmossman.aucklandmuseum3api.util.TextUtils.getAppSubtitle
import com.michaelrmossman.aucklandmuseum3api.util.relatedRecords
import com.michaelrmossman.aucklandmuseum3api.util.subtitleText
import com.michaelrmossman.aucklandmuseum3api.util.titleText

@Composable
fun PersonDetailsScreen(
    onClickBackButton: () -> Unit,
    onClickImage: (ImageObjects) -> Unit,
    result: OpacPerson,
    @StringRes stringId: Int,
    windowSize: WindowWidthSizeClass,
    /* Modifier used by all [OpacPerson]s */
    modifier: Modifier = Modifier
) {
    val personsViewModel: PersonsViewModel = viewModel(
        factory = PersonsViewModel.Factory
    )
    var isOnLastPage by rememberSaveable { mutableStateOf(false) }
    var scrollToIndex by rememberSaveable { mutableIntStateOf(-1) }
    val viewState by personsViewModel.resultsListState.collectAsState()

    val initialIndex = viewState.resultsList.indexOf(result)
    val isDownloading = viewState.resultState is SearchUiState.GettingMore
    val lastPage by remember { /* Must be remember */
        derivedStateOf { viewState.resultsList.lastIndex }
    }
    var pageIndex by rememberSaveable {
        mutableIntStateOf(initialIndex)
    }
    val pagerState = rememberPagerState(
        initialPage = pageIndex,
        pageCount = { viewState.resultsList.size }
    )
    var showBottomSheet by remember { mutableStateOf(false) }
    if (showBottomSheet) {
        val currentItem = viewState.resultsList[pageIndex]
        ToggleFaveBottomSheet(
            itemId = currentItem.opacPersonId,
            onDismissRequest = { showBottomSheet = false },
            subtitle = currentItem.subtitleText().joinToString(
                SEPARATOR_RECORD
            ),
            title = currentItem.titleText()
        )
    }
    var showIconsDialog by remember { mutableStateOf(false) }
    if (showIconsDialog) {
        val currentItem = viewState.resultsList[pageIndex]
        val relatedRecords =
            currentItem.relationshipsCollection.relatedRecords()
        IconsLegendBasic(
            onClickConfirm = { showIconsDialog = false },
            numRelatedRecords = relatedRecords.size
        )
    }

    LaunchedEffect(key1 = pagerState) {
        // Collect from the snapshotFlow reading the currentPage
        snapshotFlow { pagerState.currentPage }.collect { page ->
            /* Store current index as pageIndex, in case of
               activity restarted, i.e. screen rotation */
            pageIndex = page
            /* Enable the Download More icon if last page */
            isOnLastPage = (page == lastPage)
            /* Reset error state to reset red colour
               on DownloadButton icon, if applicable */
            if (viewState.resultState == SearchUiState.Error) {
                personsViewModel.resetErrorState()
            }
        }
    }

    LaunchedEffect(key1 = scrollToIndex) {
        if (scrollToIndex != -1) {
            pagerState.animateScrollToPage(
                scrollToIndex
            )
            scrollToIndex = -1
        }
    }

    Scaffold(
        topBar = {
            TwoLineAppBar(
                actions = {
                    if (windowSize == WindowWidthSizeClass.Compact) {
                        val isEnabled = List(size = 2){ true }
                        val menuLabels = listOf(
                            R.string.menu_toggle_fave,
                            R.string.menu_icons_legend
                        ).map { stringId -> stringResource(stringId) }
                        val onClickActions = listOf(
                            { showBottomSheet = true },
                            { showIconsDialog = true }
                        )
                        DownloadButton(
                            canDownloadMore = viewState.canDownload,
                            isDownloading = isDownloading,
                            isEnabled = (
                                viewState.canDownload
                                &&
                                !isDownloading
                                &&
                                isOnLastPage
                            ),
                            onClickDownloadButton = {
                                personsViewModel.getPersonSearchResults(
                                    query = viewState.searchQuery,
                                    startFrom = viewState.resultsList.size
                                )
                            },
                            onDownloadComplete = {
                                /* Disable download more icon
                                   IF download was successful */
                                isOnLastPage = (
                                    viewState.resultState
                                    ==
                                    SearchUiState.Error
                                )
                            }
                        )
                        DynamicActionMenu(
                            isEnabled = isEnabled,
                            menuLabels = menuLabels,
                            onClickActions = onClickActions
                        )
                    }
                },
                onClickBackButton = onClickBackButton,
                stringId = when (windowSize) {
                    WindowWidthSizeClass.Compact -> {
                        stringId
                    }
                    /* Pass zero as titleId for larger screens,
                       to indicate NO navigation or title text */
                    else -> 0
                },
                subtitle = getAppSubtitle(
                    viewState.resultsList.size,
                    viewState.resultCount,
                    R.string.app_subtitle
                )
            )
        }
    ) { contentPadding ->

        val content: (@Composable (OpacPerson) -> Unit) = { result ->
            val titlesFlattened = result.titleText()
            PersonDetailsCard(
                contentPadding = contentPadding,
                /* Modifier used by all [OpacPerson]s. Padding
                   is ADDED TO passed-in modifier's padding*/
                modifier = modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = dimensionResource(
                            R.dimen.padding_details_item_hori
                        ),
                        vertical = dimensionResource(
                            R.dimen.padding_details_item_vert
                        )
                    ),
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
                result = result,
                titlesFlattened = titlesFlattened,
                windowSize = windowSize
            )
        }

        when (windowSize == WindowWidthSizeClass.Compact) {
            true -> {
                HorizontalPager(
                    state = pagerState,
                    pageSpacing = dimensionResource(
                        R.dimen.page_spacing
                    ),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(contentPadding)
                ) { page ->
                    content(viewState.resultsList[page])
                }
            }
            else ->  if (initialIndex != -1) {
                content(viewState.resultsList[initialIndex])
            }
        }
    }
}