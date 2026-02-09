package com.michaelrmossman.aucklandmuseum3api.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material.icons.outlined.BookmarkRemove
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.michaelrmossman.aucklandmuseum3api.R
import com.michaelrmossman.aucklandmuseum3api.enum.MediaType
import com.michaelrmossman.aucklandmuseum3api.util.fromHtml

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToggleFaveContent(
    isFavourite: Boolean,
    mediaType: MediaType,
    onDismissRequest: () -> Unit,
    onToggleFave: () -> Unit,
    title: String,
    modifier: Modifier = Modifier
) {
    val headerText = stringResource(R.string.menu_toggle_fave)
    val iconLargePadding = dimensionResource(R.dimen.padding_great)
    val iconSize = dimensionResource(R.dimen.icon_size_small)
    val sheetState = rememberModalBottomSheetState()
    val horizontalPadding = dimensionResource(R.dimen.padding_medium)
    val textVerticalPadding = dimensionResource(R.dimen.padding_small)
    val rowVerticalPadding = dimensionResource(R.dimen.padding_small)
    val verticalSpacing = dimensionResource(R.dimen.spacing_vertical_small)

    ModalBottomSheet(
        onDismissRequest = { onDismissRequest() },
        sheetState = sheetState
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(verticalSpacing),
            modifier = modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorScheme.inverseOnSurface
                )
        ) {
            Row(
                modifier = Modifier.padding(
                    vertical = rowVerticalPadding
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    headerText,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(
                            horizontal = horizontalPadding
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
            Column(
                /* Width of Text will define width of Button */
                modifier = Modifier.width(IntrinsicSize.Max)
            ) {
                Text(
                    modifier = Modifier.padding(
                        horizontal = horizontalPadding,
                        vertical = textVerticalPadding
                    ),
                    text = stringResource(
                        when (isFavourite) {
                            true -> R.string.fave_is_favourite_message
                            else -> R.string.fave_not_favourite_message
                        },
                        mediaType.name,
                        title
                    ).fromHtml() // Note html
                )
                Button(
                    modifier = Modifier.padding(
                        horizontal = horizontalPadding,
                        vertical = rowVerticalPadding
                    ),
                    onClick = { onToggleFave() }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = when (isFavourite) {
                                true -> {
                                    Icons.Outlined.BookmarkRemove
                                }
                                else -> {
                                    Icons.Outlined.BookmarkAdd
                                }
                            },
                            modifier = modifier.padding(
                                horizontal = dimensionResource(
                                    R.dimen.padding_medium
                                )
                            ),
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.weight(0.2F))
                        Text(text = stringResource(when(isFavourite) {
                            true -> R.string.faves_remove_desc
                            else -> R.string.faves_add_desc
                        }))
                        Spacer(modifier = Modifier.weight(0.8F))
                    }
                }
            }
        }
    }
}