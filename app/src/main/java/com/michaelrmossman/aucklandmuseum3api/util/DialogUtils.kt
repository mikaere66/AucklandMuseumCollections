package com.michaelrmossman.aucklandmuseum3api.util

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.RadioButtonChecked
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material.icons.outlined.SubdirectoryArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelrmossman.aucklandmuseum3api.R
import com.michaelrmossman.aucklandmuseum3api.MuseumApplication.Companion.instance
import com.michaelrmossman.aucklandmuseum3api.enum.Collection
import com.michaelrmossman.aucklandmuseum3api.enum.CollectionDocHeritage
import com.michaelrmossman.aucklandmuseum3api.enum.CollectionHumanHistory
import com.michaelrmossman.aucklandmuseum3api.enum.CollectionNatSciences
import com.michaelrmossman.aucklandmuseum3api.ui.theme.AucklandMuseumV3APITheme
import com.michaelrmossman.aucklandmuseum3api.util.DialogUtils.ConfirmDeleteAllFavesDialog
import com.michaelrmossman.aucklandmuseum3api.util.DialogUtils.ConfirmResetSettingsDialog
import com.michaelrmossman.aucklandmuseum3api.util.DialogUtils.IconsLegendDialog
import com.michaelrmossman.aucklandmuseum3api.util.DialogUtils.SelectDeptDialog
import com.michaelrmossman.aucklandmuseum3api.util.IconUtils.getCollectionIconId
import com.michaelrmossman.aucklandmuseum3api.util.TextUtils.fontDimensionResource

/**
 * Dialog utility functions used throughout the app
 */
object DialogUtils {

    // const val TAG = "DialogUtils"

    @Composable
    fun CommonSimpleDialog(
        onClickConfirm: () -> Unit,
        onClickDismiss: () -> Unit,
        @StringRes confirmId: Int,
        @StringRes dismissId: Int,
        @StringRes textId: Int,
        @StringRes titleId: Int,
        modifier: Modifier = Modifier
    ) {
        AlertDialog(
            modifier = modifier,
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the
                // dialog or on the back button. If you want to disable
                // that functionality, simply use empty onDismissRequest
                onClickDismiss()
            },
            title = {
                Text(text = stringResource(titleId).plus("?"))
            },
            text = {
                Text(
                    text = stringResource(textId).fromHtml(),
                    textAlign = TextAlign.Justify
                )
            },
            dismissButton = {
                TextButton (
                    onClick = { onClickDismiss() }
                ) {
                    Text(text = stringResource(dismissId))
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { onClickConfirm() }
                ) {
                    Text(text = stringResource(confirmId))
                }
            }
        )
    }

    @Composable
    fun ConfirmDeleteAllFavesDialog(
        onClickConfirm: () -> Unit,
        onClickDismiss: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        CommonSimpleDialog(
            confirmId = R.string.dialog_confirm,
            dismissId = R.string.dialog_deny,
            modifier = modifier,
            onClickConfirm = onClickConfirm,
            onClickDismiss = onClickDismiss,
            textId = R.string.faves_message,
            titleId = R.string.menu_faves_delete_all
        )
    }

    @Composable
    fun ConfirmResetSettingsDialog(
        onClickConfirm: () -> Unit,
        onClickDismiss: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        CommonSimpleDialog(
            confirmId = R.string.dialog_confirm,
            dismissId = R.string.dialog_deny,
            modifier = modifier,
            onClickConfirm = onClickConfirm,
            onClickDismiss = onClickDismiss,
            textId = R.string.settings_reset_message,
            titleId = R.string.menu_reset_settings
        )
    }

    @Composable
    private fun IconsBasicItem(
        drawableId: Int,
        horizontalPadding: Dp,
        index: Int,
        text: String,
        verticalPadding: Dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(
                    drawableId
                ),
                contentDescription = null,
                modifier = Modifier.padding(
                    end = 0.dp,
                    start = horizontalPadding,
                    top = when (index) {
                        0 -> 0.dp
                        else -> verticalPadding
                    }
                )
            )
            Text(
                text = text,
                modifier = Modifier.padding(
                    end = 0.dp,
                    start = horizontalPadding,
                    top = when (index) {
                        0 -> 0.dp
                        else -> verticalPadding
                    }
                )
            )
        }
    }

    @Composable
    fun IconsLegendBasic(
        onClickConfirm: () -> Unit,
        numRelatedRecords: Int,
        modifier: Modifier = Modifier
    ) {
        val drawableIds = instance.resources.obtainTypedArray(
            R.array.basic_icon_drawable_ids
        )
        val stringIds = instance.resources.obtainTypedArray(
            R.array.basic_icon_string_ids
        )
        /* To avoid error, abort if array sizes do NOT match */
        if (drawableIds.length() != stringIds.length()) {
            drawableIds.recycle()
            stringIds.recycle()
        } else {
            val horizontalPadding = dimensionResource(
                R.dimen.padding_dialog_horizontal
            )
            val title = stringResource(R.string.basic_icon_legend_title)
            val verticalPadding = dimensionResource(
                R.dimen.padding_icon_dialog_vert
            )
            AlertDialog(
                modifier = modifier,
                onDismissRequest = {
                    onClickConfirm() /* Refer note at BOF */
                },
                title = {
                    Text(
                        fontSize = fontDimensionResource(
                            R.dimen.font_size_dialog_title
                        ),
                        text = title.fromHtml()
                    )
                },
                text = {
                    /* There's probably a fancier way to achieve this, but
                       doing it this way ensures we end up with an integer */
                    val count = drawableIds.length().plus(
                        stringIds.length()
                    ).div(2)

                    Column {
                        for (i in 0 until count) {
                            val drawableId = drawableIds.getResourceId(
                                i,0
                            )
                            val stringId = stringIds.getResourceId(
                                i,0
                            )
                            if (drawableId != 0 && stringId != 0) {
                                IconsBasicItem(
                                    drawableId = drawableId,
                                    horizontalPadding = horizontalPadding,
                                    index = i,
                                    text = when (i) {
                                        0 -> pluralStringResource(
                                            stringId,
                                            numRelatedRecords
                                        )
                                        else -> stringResource(
                                            stringId
                                        )
                                    },
                                    verticalPadding = verticalPadding
                                )
                            }
                            if (i == count.minus(1)) {
                                drawableIds.recycle()
                                stringIds.recycle()
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton (
                        onClick = { onClickConfirm() }
                    ) {
                        Text(
                            text = stringResource(
                                R.string.dialog_got_it
                            )
                        )
                    }
                }
            )
        }
    }

    @Composable
    fun IconsLegendDialog(
        entries: Iterable<Collection>,
        onClickConfirm: () -> Unit,
        title: String,
        modifier: Modifier = Modifier,
        parentCollection: Collection? = null
    ) {
        val hierarchyIconAlpha = 0.5F
        val hierarchyIconColor = colorResource(
            R.color.icon_disabled
        )
        val horizontalPadding = dimensionResource(
            R.dimen.padding_dialog_horizontal
        )
        val verticalPadding = dimensionResource(
            R.dimen.padding_icon_dialog_vert
        )
        AlertDialog(
            modifier = modifier,
            onDismissRequest = {
                onClickConfirm() /* Refer note at BOF */
            },
            title = {
                Text(
                    fontSize = fontDimensionResource(
                        R.dimen.font_size_dialog_title
                    ),
                    text = title.fromHtml()
                )
            },
            text = {
                Column {
                    parentCollection?.let { collection ->
                        IconsLegendItem(
                            collection = collection,
                            hierarchyIconAlpha = hierarchyIconAlpha,
                            hierarchyIconColor = hierarchyIconColor,
                            horizontalPadding = horizontalPadding,
                            index = 0,
                            isParent = true,
                            verticalPadding = verticalPadding
                        )
                    }
                    entries.forEachIndexed { index, collection ->
                        IconsLegendItem(
                            collection = collection,
                            hierarchyIconAlpha = hierarchyIconAlpha,
                            hierarchyIconColor = hierarchyIconColor,
                            horizontalPadding = horizontalPadding,
                            index = when (parentCollection) {
                                null -> index
                                else -> index.plus(1)
                            },
                            isParent = false,
                            showHierarchy = (parentCollection != null),
                            verticalPadding = verticalPadding
                        )
                    }
                }
            },
            confirmButton = {
                TextButton (
                    onClick = { onClickConfirm() }
                ) {
                    Text(
                        text = stringResource(
                            R.string.dialog_got_it
                        )
                    )
                }
            }
        )
    }

    @Composable
    private fun IconsLegendItem(
        collection: Collection,
        hierarchyIconAlpha: Float,
        hierarchyIconColor: Color,
        horizontalPadding: Dp,
        index: Int,
        isParent: Boolean,
        verticalPadding: Dp,
        showHierarchy: Boolean = false
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showHierarchy) {
                Icon(
                    imageVector = Icons.Outlined.SubdirectoryArrowRight,
                    contentDescription = null,
                    modifier = Modifier.padding(
                        end = 0.dp,
                        start = horizontalPadding,
                        top = when (index) {
                            0 -> 0.dp
                            else -> verticalPadding
                        }
                    ),
                    tint = hierarchyIconColor.copy(
                        alpha = hierarchyIconAlpha
                    )
                )
            }
            Icon(
                painter = painterResource(
                    getCollectionIconId(collection)
                ),
                contentDescription = null,
                modifier = Modifier.padding(
                    end = 0.dp,
                    start = horizontalPadding,
                    top = when (index) {
                        0 -> 0.dp
                        else -> verticalPadding
                    }
                )
            )
            Text(
                text = when (isParent) {
                    true -> stringResource(
                        R.string.icons_legend_collection,
                        collection.humanReadable()
                    )
                    else -> collection.humanReadable()
                },
                modifier = Modifier.padding(
                    end = 0.dp,
                    start = horizontalPadding,
                    top = when (index) {
                        0 -> 0.dp
                        else -> verticalPadding
                    }
                )
            )
        }
    }

    @Composable
    fun SelectDeptDialog(
        currentSelection: Collection?,
        entries: Iterable<Collection>,
        onClickConfirm: (Collection?) -> Unit,
        onClickDismiss: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        val horizontalPadding = dimensionResource(
            R.dimen.padding_dialog_horizontal
        )
        val lastIndex = entries.count().minus(1)
        val verticalPadding = dimensionResource(
            R.dimen.padding_dept_dialog_vert
        )
        AlertDialog(
            modifier = modifier,
            onDismissRequest = {
                onClickDismiss() /* Refer note at BOF */
            },
            title = {
                Text(
                    fontSize = fontDimensionResource(
                        R.dimen.font_size_dialog_title
                    ),
                    text = stringResource(
                        R.string.search_dept,
                        entries.first().parent.humanReadable()
                    ).fromHtml()
                )
            },
            text = {
                Column {
                    SelectDeptItem(
                        collection = entries.first().parent,
                        horizontalPadding = horizontalPadding,
                        index = -1,
                        isSelected = (
                            currentSelection == entries.first().parent
                        ),
                        lastIndex = lastIndex,
                        onClickConfirm = onClickConfirm,
                        text = stringResource(
                            R.string.search_all_dept,
                            entries.first().parent.humanReadable()
                        ),
                        verticalPadding = verticalPadding
                    )
                    entries.forEachIndexed { index, collection ->
                        SelectDeptItem(
                            collection = collection,
                            horizontalPadding = horizontalPadding,
                            index = index,
                            isSelected = (currentSelection == collection),
                            lastIndex = lastIndex,
                            onClickConfirm = onClickConfirm,
                            text = collection.humanReadable(),
                            verticalPadding = verticalPadding
                        )
                    }
                }
            },
            confirmButton = {
                TextButton (
                    onClick = { onClickDismiss() }
                ) {
                    Text(text = stringResource(R.string.dialog_cancel))
                }
            }
        )
    }

    @Composable
    private fun SelectDeptItem(
        collection: Collection?,
        horizontalPadding: Dp,
        index: Int,
        isSelected: Boolean,
        lastIndex: Int,
        onClickConfirm: (Collection?) -> Unit,
        text: String,
        verticalPadding: Dp
    ) {
        Row(
            modifier = Modifier.clickable {
                onClickConfirm(collection)
            },
            verticalAlignment = Alignment.CenterVertically
        ) {
            collection?.let { coll ->
                Icon(
                    contentDescription = null,
                    painter = painterResource(
                        getCollectionIconId(coll)
                    ),
                    modifier = Modifier.padding(
                        bottom = when (index) {
                            lastIndex -> 0.dp
                            else -> verticalPadding
                        },
                        end = horizontalPadding,
                        start = horizontalPadding,
                        top = when (index) {
                            -1 -> 0.dp
                            else -> verticalPadding
                        }
                    )
                )
            }
            Icon(
                contentDescription = null,
                imageVector = when (isSelected) {
                    true -> Icons.Outlined.RadioButtonChecked
                    else -> Icons.Outlined.RadioButtonUnchecked
                },
                modifier = Modifier.padding(
                    bottom = when (index) {
                        lastIndex -> 0.dp
                        else -> verticalPadding
                    },
                    end = horizontalPadding,
                    start = horizontalPadding,
                    top = when (index) {
                        -1 -> 0.dp
                        else -> verticalPadding
                    }
                )
            )
            Text(
                text = text,
                modifier = Modifier.padding(
                    bottom = when (index) {
                        lastIndex -> 0.dp
                        else -> verticalPadding
                    },
                    top = when (index) {
                        -1 -> 0.dp
                        else -> verticalPadding
                    }
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConfirmDeletePreview() {
    AucklandMuseumV3APITheme {
        ConfirmDeleteAllFavesDialog(
            onClickConfirm = {},
            onClickDismiss = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ConfirmResetPreview() {
    AucklandMuseumV3APITheme {
        ConfirmResetSettingsDialog(
            onClickConfirm = {},
            onClickDismiss = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CollectionsIconsLegendPreview() {
    AucklandMuseumV3APITheme {
        IconsLegendDialog(
            entries = listOf(
                Collection.CollectionDocHeritage,
                Collection.CollectionHumanHistory,
                Collection.CollectionNatSciences
            ),
            onClickConfirm = {},
            title = stringResource(
                R.string.icons_legend_title,
                stringResource(
                    R.string.icons_legend_parent
                )
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DocHeritageIconsLegendPreview() {
    AucklandMuseumV3APITheme {
        IconsLegendDialog(
            entries = CollectionDocHeritage.entries,
            onClickConfirm = {},
            parentCollection = Collection.CollectionDocHeritage.parent,
            title = stringResource(
                R.string.icons_legend_title,
                Collection.CollectionDocHeritage.humanReadable()
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SelectDocHeritageDeptPreview() {
    AucklandMuseumV3APITheme {
        SelectDeptDialog(
            currentSelection = CollectionDocHeritage.MuseumArchives,
            entries = CollectionDocHeritage.entries,
            onClickConfirm = {},
            onClickDismiss = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HumanHistoryIconsLegendPreview() {
    AucklandMuseumV3APITheme {
        IconsLegendDialog(
            entries = CollectionHumanHistory.entries,
            onClickConfirm = {},
            parentCollection = Collection.CollectionHumanHistory.parent,
            title = stringResource(
                R.string.icons_legend_title,
                Collection.CollectionHumanHistory.humanReadable()
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SelectHumanHistoryDeptPreview() {
    AucklandMuseumV3APITheme {
        SelectDeptDialog(
            currentSelection = CollectionHumanHistory.AppliedArtsAndDesign,
            entries = CollectionHumanHistory.entries,
            onClickConfirm = {},
            onClickDismiss = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NatSciencesIconsLegendPreview() {
    AucklandMuseumV3APITheme {
        IconsLegendDialog(
            entries = CollectionNatSciences.entries,
            onClickConfirm = {},
            parentCollection = Collection.CollectionNatSciences.parent,
            title = stringResource(
                R.string.icons_legend_title,
                Collection.CollectionNatSciences.humanReadable()
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SelectNatSciencesDeptPreview() {
    AucklandMuseumV3APITheme {
        SelectDeptDialog(
            currentSelection = CollectionNatSciences.LandMammals,
            entries = CollectionNatSciences.entries,
            onClickConfirm = {},
            onClickDismiss = {}
        )
    }
}