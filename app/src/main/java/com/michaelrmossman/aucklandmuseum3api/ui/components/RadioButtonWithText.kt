package com.michaelrmossman.aucklandmuseum3api.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.RadioButtonChecked
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun RadioButtonWithText(
    isSelected: Boolean,
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            contentDescription = null,
            imageVector = when (isSelected) {
                true -> Icons.Outlined.RadioButtonChecked
                else -> Icons.Outlined.RadioButtonUnchecked
            },
            modifier = modifier
        )
        Text(
            modifier = modifier,
            text = text
        )
    }
}