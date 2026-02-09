package com.michaelrmossman.aucklandmuseum3api.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.michaelrmossman.aucklandmuseum3api.R
import com.michaelrmossman.aucklandmuseum3api.ui.theme.AucklandMuseumV3APITheme

@Composable
fun DynamicActionMenu(
    isEnabled: List<Boolean>,
    menuLabels: List<String>,
    onClickActions: List<() -> Unit>,
    menuDrawables: List<Int>? = null,
    /* For preview only: refer EOF */
    isExpanded: Boolean = false
) {
    var expanded by remember { mutableStateOf(isExpanded) }

    IconButton(onClick = { expanded = true }) {
        Icon(
            Icons.Filled.MoreVert,
            contentDescription = stringResource(
                R.string.menu_toggle_desc
            )
        )
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        if (
            isEnabled.size == menuLabels.size
            &&
            menuLabels.size == onClickActions.size
        ) {
            onClickActions.forEachIndexed { index, onClickAction ->
                DropdownMenuItem(
                    enabled = isEnabled[index],
                    onClick = {
                        expanded = false
                        onClickAction()
                    },
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            menuDrawables?.let { drawables ->
                                Icon(
                                    painter = painterResource(
                                        drawables[index]
                                    ),
                                    contentDescription = null,
                                    modifier = Modifier.padding(
                                        end = dimensionResource(
                                            R.dimen.padding_small
                                        )
                                    )
                                )
                            }
                            Text(
                                menuLabels[index]
                            )
                        }
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingScreenPreview() {
    AucklandMuseumV3APITheme {
        DynamicActionMenu(
            isEnabled = listOf(
                true,
                true
            ),
            menuLabels = listOf(
                "Enabled",
                "Disabled"
            ),
            onClickActions = listOf({},{}),
            menuDrawables = listOf(
                R.drawable.outline_thumb_up_24,
                R.drawable.outline_thumb_down_24
            ),
            isExpanded = true
        )
    }
}