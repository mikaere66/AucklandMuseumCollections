package com.michaelrmossman.aucklandmuseum3api.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.michaelrmossman.aucklandmuseum3api.R
import com.michaelrmossman.aucklandmuseum3api.enum.Collection
import com.michaelrmossman.aucklandmuseum3api.util.IconUtils.getCollectionIconId

/* Is leading icon for SearchBoxWithButton */
@Composable
fun AdvancedSearchMenu(
    onClickManageSearch: (Collection?) -> Unit,
    currentCollection: Collection?,
    modifier: Modifier = Modifier,
    currentResultsSize: Int = 0,
    onClickClearSearch: (() -> Unit?)? = null,
    onClickIconsLegend: (() -> Unit?)? = null
) {
    SearchMenuContent(
        currentResultsSize = currentResultsSize,
        drawableId = when (currentCollection) {
            null -> R.drawable.outline_more_vert_24
            else -> getCollectionIconId(currentCollection)
        },
        modifier = modifier,
        onClickClearSearch = onClickClearSearch,
        onClickIconsLegend = onClickIconsLegend,
        onClickManageSearch = onClickManageSearch,
        stringId = when (currentCollection) {
            null -> R.string.search_all_on
            else -> R.string.search_all_coll
        },
        currentCollection = currentCollection
    )
}