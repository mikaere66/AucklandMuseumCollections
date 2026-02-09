package com.michaelrmossman.aucklandmuseum3api.ui.help

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat.getDrawable
import com.michaelrmossman.aucklandmuseum3api.R
import com.michaelrmossman.aucklandmuseum3api.enum.Collection
import com.michaelrmossman.aucklandmuseum3api.enum.CollectionDocHeritage
import com.michaelrmossman.aucklandmuseum3api.enum.CollectionHumanHistory
import com.michaelrmossman.aucklandmuseum3api.enum.CollectionNatSciences
import com.michaelrmossman.aucklandmuseum3api.navigation.CurrentScreen
import com.michaelrmossman.aucklandmuseum3api.objects.HelpSectionObject
import com.michaelrmossman.aucklandmuseum3api.ui.components.DynamicActionMenu
import com.michaelrmossman.aucklandmuseum3api.ui.components.TwoLineAppBar
import com.michaelrmossman.aucklandmuseum3api.util.DialogUtils.IconsLegendDialog
import com.michaelrmossman.aucklandmuseum3api.util.IconUtils.getCollectionIconId

@SuppressLint("LocalContextResourcesRead")
@Composable
fun HelpScreen(
    currentScreenItems: List<CurrentScreen>,
    onClickBackButton: () -> Unit,
    @StringRes stringId: Int,
    modifier: Modifier = Modifier
) {
    val columnVerticalPadding = dimensionResource(
        R.dimen.padding_small
    )
    val columnVerticalSpacing = dimensionResource(
        R.dimen.spacing_vertical_small
    )
    val context = LocalContext.current

    val helpSectionHeaders = stringArrayResource(R.array.help_section_headers)

    val helpSectionDrawableLists = mutableListOf<List<Drawable?>>()
    listOf(
        R.array.help_section_main_drawable_ids
    ).forEach { drawableIds ->
        val drawableResources = mutableListOf<Drawable?>()

        val typedArray = context.resources.obtainTypedArray(drawableIds)
        for (i in 0 until typedArray.length()) {
            drawableResources.add(typedArray.getDrawable(i))
        }
        typedArray.recycle()

        helpSectionDrawableLists.add(drawableResources)
    }

    val helpSectionStringLists = mutableListOf<List<String>>()
    val helpSectionStringArrays = context.resources.obtainTypedArray(
        R.array.help_section_string_arrays
    )
    for (i in 0 until helpSectionStringArrays.length()) {
        val strings = mutableListOf<String>()
        helpSectionStringArrays.getTextArray(i).map { charSequence ->
            strings.add(charSequence.toString())
        }
        helpSectionStringLists.add(strings)
    }
    helpSectionStringArrays.recycle()

    val mainSectionDrawableIds = currentScreenItems.map { currentScreen ->
        currentScreen.drawableId
    }
    val mainSectionDrawables = mainSectionDrawableIds.map { drawableId ->
        getDrawable(context, drawableId)
    }
    helpSectionDrawableLists.add(mainSectionDrawables)

    val mainSectionIntroIds = currentScreenItems.map { currentScreen ->
        currentScreen.introStringId
    }
    val mainSectionTitleIds = currentScreenItems.map { currentScreen ->
        currentScreen.titleStringId
    }
    val mainSectionStrings = mutableListOf<String>()
    for (i in 0 until currentScreenItems.size) {
        mainSectionStrings.add(stringResource(
            R.string.help_section_format, // HTML
            stringResource(mainSectionTitleIds[i]),
            stringResource(mainSectionIntroIds[i])
        ))
    }
    helpSectionStringLists.add(mainSectionStrings)

    val helpSectionObjects = mutableListOf<HelpSectionObject>()
    helpSectionDrawableLists.forEachIndexed { index, helpSectionDrawables ->
        helpSectionObjects.add(
            HelpSectionObject(
                helpSectionDrawables = helpSectionDrawables,
                helpSectionHeader = helpSectionHeaders[index],
                helpSectionStrings = helpSectionStringLists[index]
            )
        )
    }

    val lazyListState = rememberLazyListState()
    val menuDrawables = listOf(
        Collection.CollectionDocHeritage,
        Collection.CollectionHumanHistory,
        Collection.CollectionNatSciences
    ).map { collection ->
        getCollectionIconId(collection)
    }
    val sampleEnumEntries: List<Iterable<Collection>> = listOf(
        CollectionDocHeritage.entries,
        CollectionHumanHistory.entries,
        CollectionNatSciences.entries
    )
    val sampleMenuLabels = stringArrayResource(
        R.array.help_sample_menu_items
    )
    val sampleMenuBooleans = List(sampleMenuLabels.size) {
        sampleEnumEntries.size == sampleMenuLabels.size
    }
    var showSampleMenu by remember { mutableIntStateOf(-1) }
    if (showSampleMenu != -1) {
        IconsLegendDialog(
            entries = sampleEnumEntries[showSampleMenu],
            onClickConfirm = { showSampleMenu = -1 },
            title = sampleMenuLabels[showSampleMenu]
        )
    }

    Scaffold(
        topBar = {
            TwoLineAppBar(
                actions = {
                    if (sampleMenuBooleans.all { true }) {
                        DynamicActionMenu(
                            isEnabled = sampleMenuBooleans,
                            menuDrawables = menuDrawables,
                            menuLabels = sampleMenuLabels.toList(),
                            onClickActions = (
                                0 until sampleMenuLabels.size
                            ).map { index ->
                                { showSampleMenu = index }
                            }
                        )
                    }
                },
                onClickBackButton = { onClickBackButton() },
                stringId = R.string.app_name,
                subtitle = stringResource(stringId)
            )
        }
    ) { contentPadding ->

        LazyColumn(
            contentPadding = contentPadding,
            modifier = modifier
                .fillMaxSize()
                .padding(vertical = columnVerticalPadding),
            state = lazyListState,
            verticalArrangement = Arrangement.spacedBy(
                columnVerticalSpacing
            )
        ) {
            itemsIndexed(
                items = helpSectionObjects
            ) { index, helpSectionObject ->

                HelpSection(
                    helpSection = helpSectionObject
                )
            }
        }
    }
}