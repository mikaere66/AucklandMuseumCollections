package com.michaelrmossman.aucklandmuseum3api.ui.components

import androidx.annotation.StringRes
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleLineAppBar(
    @StringRes stringId: Int
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor =
                MaterialTheme.colorScheme.primaryContainer,
            titleContentColor =
                MaterialTheme.colorScheme.onPrimaryContainer
        ),
        title = {
            if (stringId != 0) {
                Text(stringResource(stringId))
            }
        }
    )
}