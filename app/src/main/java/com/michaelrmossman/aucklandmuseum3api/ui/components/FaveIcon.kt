package com.michaelrmossman.aucklandmuseum3api.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material.icons.filled.BookmarkRemove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.michaelrmossman.aucklandmuseum3api.R

@Composable
fun FaveIcon(
    isFave: Boolean,
    onClickToggleFavourite: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true
) {
    IconButton(
        enabled = isEnabled,
        onClick = { onClickToggleFavourite() }
    ) {
        Icon(
            imageVector = when (isFave) {
                true -> Icons.Filled.BookmarkRemove
                else -> Icons.Outlined.BookmarkAdd
            },
            contentDescription = stringResource(when (isFave) {
                true -> R.string.faves_add_desc
                else -> R.string.faves_remove_desc
            }),
            modifier = modifier
        )
    }
}