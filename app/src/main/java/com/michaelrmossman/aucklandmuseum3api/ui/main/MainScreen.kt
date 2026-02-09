package com.michaelrmossman.aucklandmuseum3api.ui.main

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import com.michaelrmossman.aucklandmuseum3api.R
import com.michaelrmossman.aucklandmuseum3api.navigation.CurrentScreen
import com.michaelrmossman.aucklandmuseum3api.ui.components.ButtonWithIcon
import com.michaelrmossman.aucklandmuseum3api.ui.components.MainImageWithText
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    currentScreenItems: List<CurrentScreen>,
    onClickActions: List<() -> Unit>,
    screensEnabled: List<Boolean>,
    @StringRes stringId: Int,
    windowHeightSize: WindowHeightSizeClass,
    windowWidthSize: WindowWidthSizeClass,
    modifier: Modifier = Modifier
) {
    val buttonWidth = dimensionResource(R.dimen.button_width)
    val buttonWithIcon: (
        @Composable (CurrentScreen) -> Unit
    ) = { currentScreen ->
        val index = currentScreen.listIndex
        if (index != -1) {
            ButtonWithIcon(
                buttonWidth = buttonWidth,
                drawableId = currentScreen.drawableId,
                isEnabled = screensEnabled[index],
                onClickButton = onClickActions[index],
                stringId = currentScreen.titleStringId
            )
        }
    }
    val configuration = LocalConfiguration.current
    val horizontalAlignment = Alignment.CenterHorizontally
    val isLandscape = (
        configuration.orientation
        ==
        Configuration.ORIENTATION_LANDSCAPE
    )
    val largeScreenMainImageHeight = dimensionResource(
        R.dimen.small_screen_main_image_height
    )
    val largeScreenMainImageWidth = dimensionResource(
        R.dimen.large_screen_main_image_width
    )
    val scrollState = rememberScrollState()
    val verticalArrangement = Arrangement.SpaceEvenly

    val firstHalfEnd =
        ceil(currentScreenItems.size.div(2.0)).roundToInt()
    val secondHalfStart =
        floor(currentScreenItems.size.div(2.0)).roundToInt()
    val firstHalfScreens = currentScreenItems.subList(
        0,firstHalfEnd
    )
    val secondHalfScreens = currentScreenItems.subList(
        secondHalfStart, currentScreenItems.size
    )
    val twinColumnWithHalfButtons: (
        @Composable (List<CurrentScreen>) -> Unit
    ) = { currentScreens ->
        Column(
            modifier = modifier.fillMaxHeight(),
            verticalArrangement = verticalArrangement
        ) {
            currentScreens.forEach { currentScreen ->
                buttonWithIcon(currentScreen)
            }
        }
    }

    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = {
                MainImageWithText(
                    stringId = stringId,
                    modifier = when (windowWidthSize) {
                        WindowWidthSizeClass.Compact -> {
                            modifier.fillMaxWidth()
                        }
                        else -> when (windowWidthSize) {
                            WindowWidthSizeClass.Compact -> {
                                modifier.height(
                                    largeScreenMainImageHeight
                                )
                            }
                            else -> modifier.width(
                                largeScreenMainImageWidth
                            )
                        }
                    },
                    windowWidthSize = windowWidthSize
                )
            }
        )
    }) { contentPadding ->

        if (
            currentScreenItems.size == onClickActions.size
            &&
            onClickActions.size == screensEnabled.size
        ) {
            Column(
                horizontalAlignment = horizontalAlignment,
                modifier = modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                verticalArrangement = verticalArrangement
            ) {
                when (
                    isLandscape
                    ||
                    windowWidthSize != WindowWidthSizeClass.Compact
                ) {
                    true -> Row(
                        modifier = when (windowHeightSize) {
                            WindowHeightSizeClass.Compact -> {
                                modifier.fillMaxWidth()
                            }
                            else -> modifier.width(
                                largeScreenMainImageWidth
                            )
                        },
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        /* 1st half: Coll.Status, ByObject, ByPerson */
                        twinColumnWithHalfButtons(firstHalfScreens)
                        /* 2nd half: Bookmarks, Help|About, Settings */
                        twinColumnWithHalfButtons(secondHalfScreens)
                    }
                    else -> Column(
                        horizontalAlignment = horizontalAlignment,
                        modifier = modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState),
                        verticalArrangement = verticalArrangement
                    ) {
                        currentScreenItems.forEach { currentScreen ->
                            buttonWithIcon(currentScreen)
                        }
                    }
                }
            }
        }
    }
}