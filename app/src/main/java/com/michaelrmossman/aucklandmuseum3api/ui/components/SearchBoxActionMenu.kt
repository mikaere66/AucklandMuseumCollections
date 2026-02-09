package com.michaelrmossman.aucklandmuseum3api.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.michaelrmossman.aucklandmuseum3api.R
import com.michaelrmossman.aucklandmuseum3api.enum.Collection

/* Is leading icon for SearchBoxWithButton */
@Composable
fun SearchBoxActionMenu(
    onClickManageSearch: (Collection?) -> Unit,
    currentCollection: Collection?,
    modifier: Modifier = Modifier
) {
    SearchMenuContent(
        drawableId = R.drawable.outline_manage_search_24,
        modifier = modifier,
        onClickIconsLegend = null,
        onClickManageSearch = onClickManageSearch,
        stringId = R.string.search_all_coll,
        currentCollection = currentCollection
    )
}