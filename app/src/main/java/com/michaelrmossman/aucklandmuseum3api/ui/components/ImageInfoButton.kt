package com.michaelrmossman.aucklandmuseum3api.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.michaelrmossman.aucklandmuseum3api.R

@Composable
fun ImageInfoButton(
    onClickInfoButton: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = { onClickInfoButton() }
    ) {
        Icon(
            contentDescription = stringResource(
                R.string.image_info_desc
            ),
            imageVector = Icons.Outlined.Info,
            modifier = modifier
        )
    }
}