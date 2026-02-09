package com.michaelrmossman.aucklandmuseum3api.ui.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ClearAll
import androidx.compose.material.icons.outlined.Info
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
import com.michaelrmossman.aucklandmuseum3api.R
import com.michaelrmossman.aucklandmuseum3api.enum.Collection
import com.michaelrmossman.aucklandmuseum3api.util.humanReadable

/* Is leading icon for SearchBoxWithButton */
@Composable
fun SearchMenuContent(
    @DrawableRes drawableId: Int,
    onClickManageSearch: (Collection?) -> Unit,
    @StringRes stringId: Int,
    currentCollection: Collection?,
    modifier: Modifier = Modifier,
    currentResultsSize: Int = 0,
    onClickClearSearch: (() -> Unit?)? = null,
    onClickIconsLegend: (() -> Unit?)? = null
) {
    /* We can't simply use Collection.entries,
       because Collection is an interface */
    val collections = listOf(
        Collection.CollectionDocHeritage,
        Collection.CollectionHumanHistory,
        Collection.CollectionNatSciences
    )
    var expanded by remember { mutableStateOf(false) }
    val paddingHorizontal = dimensionResource(
        R.dimen.padding_dialog_horizontal
    )
    /* Note dialog vertical padding not used: instead top
       & bottom each have half (8dp) its total padding */
    val paddingVertical = dimensionResource(
        R.dimen.padding_dept_dialog_vert
    )
    val radioButtonModifier = Modifier.padding(
        horizontal = paddingHorizontal,
        vertical = paddingVertical
    )

    IconButton(
        onClick = {
            expanded = true
        },
        /* Modifier same as trailing icon */
        modifier = modifier
    ) {
        Icon(
            contentDescription = stringResource(
                R.string.manage_search_desc
            ),
            painter = painterResource(drawableId)
        )
    }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        DropdownMenuItem(
            text = {
                RadioButtonWithText(
                    isSelected = (currentCollection == null),
                    modifier = radioButtonModifier,
                    text = stringResource(stringId)
                )
            },
            onClick = {
                expanded = false
                onClickManageSearch(null) /* Search all */
            },
            enabled = (currentCollection != null)
        )
        collections.forEach { collection ->
            DropdownMenuItem(
                text = {
                    RadioButtonWithText(
                        isSelected = (collection == currentCollection?.parent),
                        modifier = radioButtonModifier,
                        text = collection.humanReadable()
                    )
                },
                onClick = {
                    expanded = false
                    onClickManageSearch(collection)
                }
            )
        }
        /* These 2 only for AdvancedSearchMenu()
           i.e. not for SearchBoxActionMenu() */
        onClickIconsLegend?.let { onClick ->
            DropdownMenuItem(
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            contentDescription = null,
                            imageVector = Icons.Outlined.Info,
                            modifier = radioButtonModifier
                        )
                        Text(
                            stringResource(R.string.menu_icons_legend),
                            modifier = radioButtonModifier
                        )
                    }
                },
                onClick = {
                    expanded = false
                    onClick()
                }
            )
        }
        onClickClearSearch?.let { onClick ->
            DropdownMenuItem(
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            contentDescription = null,
                            imageVector = Icons.Outlined.ClearAll,
                            modifier = radioButtonModifier
                        )
                        Text(
                            stringResource(R.string.menu_clear_search),
                            modifier = radioButtonModifier
                        )
                    }
                },
                onClick = {
                    expanded = false
                    onClick()
                },
                enabled = (currentResultsSize != 0)
            )
        }
    }
}