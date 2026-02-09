package com.michaelrmossman.aucklandmuseum3api.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import com.michaelrmossman.aucklandmuseum3api.R
import com.michaelrmossman.aucklandmuseum3api.enum.MediaType
import com.michaelrmossman.aucklandmuseum3api.util.MUSEUM_RELATED_OBJECT
import com.michaelrmossman.aucklandmuseum3api.util.MUSEUM_RELATED_PERSON
import com.michaelrmossman.aucklandmuseum3api.util.MUSEUM_RELATED_URL

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RelatedBottomSheet(
    headerText: AnnotatedString,
    relatedRecords: List<Triple<String, String, String>>,
    onDismissRequest: () -> Unit,
    /* Modifier used by all Text items */
    modifier: Modifier = Modifier
) {
    val iconLargePadding = dimensionResource(R.dimen.padding_great)
    val iconSize = dimensionResource(R.dimen.icon_size_small)
    val lazyListState = rememberLazyListState()
    val rowHorizontalPadding = dimensionResource(R.dimen.padding_small)
    val rowVerticalPadding = dimensionResource(R.dimen.padding_small)
    val sheetState = rememberModalBottomSheetState()
    val textPaddingHorizontal = dimensionResource(R.dimen.padding_medium)
    val textPaddingVertical = dimensionResource(R.dimen.padding_small)
    val uriHandler = LocalUriHandler.current
    val verticalSpacing = dimensionResource(R.dimen.spacing_vertical_small)

    val onRelatedClick = { isObject: Boolean, relatedId: String ->
        val uri = String.format(
            MUSEUM_RELATED_URL,
            when (isObject) {
                true -> MUSEUM_RELATED_OBJECT
                else -> MUSEUM_RELATED_PERSON
            },
            relatedId
        )
        uriHandler.openUri(uri)
    }

    ModalBottomSheet(
        onDismissRequest = { onDismissRequest() },
        sheetState = sheetState
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(verticalSpacing),
            state = lazyListState,
            modifier = modifier
                .padding(horizontal = rowHorizontalPadding)
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.inverseOnSurface)
        ) {
            stickyHeader {
                Row(
                    modifier = Modifier
                        .background(color =
                            MaterialTheme.colorScheme.inverseOnSurface
                        )
                        .padding(
                            vertical = rowVerticalPadding
                        ),
                        verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        headerText,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(
                                horizontal = textPaddingHorizontal
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
            }
            itemsIndexed(
                items = relatedRecords
            ) { _, related ->

                RelatedCard(
                    onRelatedClick = {
                        onRelatedClick(
                            (
                                related.first
                                ==
                                MediaType.Object.toString().lowercase()
                            ),
                            related.second
                        )
                    },
                    recordType = related.first,
                    relatedId = related.second,
                    title = related.third,
                    paddingHorizontal = textPaddingHorizontal,
                    paddingVertical = textPaddingVertical,
                    /* Modifier used by Text items */
                    modifier = modifier
                        .weight(1F)
                        .padding(
                            bottom = textPaddingVertical
                        )
                )
            }
        }
    }
}