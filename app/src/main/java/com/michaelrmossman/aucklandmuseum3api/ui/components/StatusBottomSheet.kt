package com.michaelrmossman.aucklandmuseum3api.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.michaelrmossman.aucklandmuseum3api.R
import com.michaelrmossman.aucklandmuseum3api.enum.MediaType
import com.michaelrmossman.aucklandmuseum3api.state.CollectionUiState
import com.michaelrmossman.aucklandmuseum3api.ui.main.MainViewModel
import com.michaelrmossman.aucklandmuseum3api.util.fromHtml

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatusBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val columnPaddingHorizontal = dimensionResource(R.dimen.padding_small)
    var downloadedReqd by rememberSaveable { mutableStateOf(true) }
    val headerText = stringResource(R.string.bottom_sheet_status).fromHtml()
    val headerPaddingHorizontal = dimensionResource(R.dimen.padding_small)
    val iconLargePadding = dimensionResource(R.dimen.padding_great)
    val iconSize = dimensionResource(R.dimen.icon_size_small)
    val innerPaddingHorizontal = dimensionResource(R.dimen.padding_large)
    val innerPaddingVertical = dimensionResource(R.dimen.padding_small)
    val mainViewModel: MainViewModel = viewModel(
        factory = MainViewModel.Factory
    )
    val rowPaddingHorizontal = dimensionResource(R.dimen.padding_medium)
    val sheetState = rememberModalBottomSheetState()
    val textPaddingHorizontal = dimensionResource(R.dimen.padding_mini)
    val verticalSpacing = dimensionResource(R.dimen.spacing_vertical_small)
    val viewState by mainViewModel.resultsListState.collectAsState()

    LaunchedEffect(key1 = downloadedReqd) {
        if (downloadedReqd) {
            mainViewModel.retrieveObjectsCount()
            mainViewModel.retrievePersonsCount()
            downloadedReqd = false
        }
    }

    ModalBottomSheet(
        onDismissRequest = { onDismissRequest() },
        sheetState = sheetState
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(verticalSpacing),
            modifier = modifier
                .background(color = MaterialTheme.colorScheme.inverseOnSurface)
                .fillMaxSize()
                .padding(horizontal = columnPaddingHorizontal)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.inverseOnSurface
                    )
                    .padding(rowPaddingHorizontal)
            ) {
                Text(
                    headerText,
                    modifier = Modifier
                        .padding(
                            horizontal = headerPaddingHorizontal
                        )
                        .weight(1F)
                )
                IconButton(
                    modifier = Modifier
                        .padding(horizontal = iconLargePadding)
                        .size(iconSize),
                    onClick = { onDismissRequest() }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = stringResource(
                            R.string.bottom_sheet_dismiss
                        )
                    )
                }
            }

            /* Width of first Text will define width of second */
            Column(
                modifier = Modifier
                    .padding(
                        horizontal = innerPaddingHorizontal,
                        vertical = innerPaddingVertical
                    )
                    .width(IntrinsicSize.Max),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End
            ) {

                AcquiringStatus(
                    isDownloading = (
                        viewState.objectState
                        ==
                        CollectionUiState.Downloading
                    ),
                    isError = (
                        viewState.objectState
                        ==
                        CollectionUiState.Error
                    ),
                    itemCount = viewState.objectCount, /* 1,033,213 */
                    mediaType = MediaType.Object,
                    modifier = Modifier.padding(
                        horizontal = textPaddingHorizontal
                    ),
                    onDownloadClick = { downloadedReqd = true },
                    textAlign = TextAlign.Start
                )

                AcquiringStatus(
                    isDownloading = (
                        viewState.personState
                        ==
                        CollectionUiState.Downloading
                    ),
                    isError = (
                        viewState.personState
                        ==
                        CollectionUiState.Error
                    ),
                    itemCount = viewState.personCount, /* 98,009 */
                    mediaType = MediaType.Person,
                    modifier = Modifier.padding(
                        horizontal = textPaddingHorizontal
                    ),
                    onDownloadClick = { downloadedReqd = true },
                    textAlign = TextAlign.End
                )
            }
        }
    }
}