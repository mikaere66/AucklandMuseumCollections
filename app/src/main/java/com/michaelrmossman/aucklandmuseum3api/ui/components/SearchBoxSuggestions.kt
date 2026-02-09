package com.michaelrmossman.aucklandmuseum3api.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.michaelrmossman.aucklandmuseum3api.R

/* AutoComplete suggestions for SearchBoxWithButton */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBoxSuggestions(
    filteredSuggestions: List<String>,
    onClickSuggestion: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val lazyBackgroundAlpha = 0.92F
    val lazyHorizontalPadding = dimensionResource(R.dimen.padding_quantum)
    val lazyVerticalPadding = dimensionResource(R.dimen.padding_small)
    val filteredVerticalPadding = dimensionResource(R.dimen.padding_large)

    LazyColumn(
        /* Limit height of suggestions list */
        modifier = modifier
            .background(
                MaterialTheme.colorScheme.surface.copy(
                    alpha = lazyBackgroundAlpha
                )
            )
            /* clip must come before border */
            .clip(RoundedCornerShape(dimensionResource(
                R.dimen.card_corner_shape
            )))
            .border(
                width = dimensionResource(
                    R.dimen.auto_complete_border_width
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            .heightIn(max = dimensionResource(
                R.dimen.auto_complete_height
            ))
            .padding(
                horizontal = lazyHorizontalPadding,
                vertical = lazyVerticalPadding
            )
    ) {
        itemsIndexed(filteredSuggestions) { index, suggestion ->

            Text(
                text = suggestion,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onClickSuggestion(suggestion) }
                    .padding(bottom = when (index) {
                        filteredSuggestions.lastIndex -> 0.dp
                        else -> filteredVerticalPadding
                    })
            )
        }
    }
}