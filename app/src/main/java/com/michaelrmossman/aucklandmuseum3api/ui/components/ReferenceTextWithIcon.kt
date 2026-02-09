package com.michaelrmossman.aucklandmuseum3api.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import com.michaelrmossman.aucklandmuseum3api.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReferenceTextWithIcon(
    isEnabled: Boolean,
    onClickReferences: () -> Unit,
    referenceText: AnnotatedString,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = referenceText,
            modifier = modifier.clickable(
                enabled = isEnabled
            ) {
                onClickReferences()
            }
            .weight(1F)
        )
        IconButton(
            enabled = isEnabled,
            onClick = onClickReferences
        ) {
            Icon(
                contentDescription = stringResource(
                    R.string.bottom_sheet_desc
                ),
                painter = painterResource(
                    R.drawable.outline_bottom_panel_open_24
                )
            )
        }
    }
}