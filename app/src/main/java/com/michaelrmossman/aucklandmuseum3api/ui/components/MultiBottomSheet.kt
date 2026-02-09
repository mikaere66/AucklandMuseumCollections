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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import com.michaelrmossman.aucklandmuseum3api.R
import com.michaelrmossman.aucklandmuseum3api.enum.MediaType
import com.michaelrmossman.aucklandmuseum3api.model.OpacObject
import com.michaelrmossman.aucklandmuseum3api.ui.objects.ObjectMultiText
import com.michaelrmossman.aucklandmuseum3api.util.IDENTIFIER_TAXONOMIC

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultiBottomSheet(
    headerText: AnnotatedString,
    onDismissRequest: () -> Unit,
    values: List<String>,
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
    val verticalSpacing = dimensionResource(R.dimen.spacing_vertical_small)

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
                items = values
            ) { _, value ->

                MultiCard(
                    value = value,
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