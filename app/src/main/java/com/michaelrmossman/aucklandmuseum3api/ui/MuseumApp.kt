package com.michaelrmossman.aucklandmuseum3api.ui

import android.graphics.Bitmap
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.michaelrmossman.aucklandmuseum3api.R
import com.michaelrmossman.aucklandmuseum3api.enum.MediaType
import com.michaelrmossman.aucklandmuseum3api.navigation.CurrentScreen
import com.michaelrmossman.aucklandmuseum3api.ui.components.StatusBottomSheet
import com.michaelrmossman.aucklandmuseum3api.ui.content.ContentPlaceholder
import com.michaelrmossman.aucklandmuseum3api.ui.favourites.FavesListScreen
import com.michaelrmossman.aucklandmuseum3api.ui.favourites.SinglePersonScreen
import com.michaelrmossman.aucklandmuseum3api.ui.favourites.SingleObjectScreen
import com.michaelrmossman.aucklandmuseum3api.ui.help.HelpScreen
import com.michaelrmossman.aucklandmuseum3api.ui.images.ImagesScreen
import com.michaelrmossman.aucklandmuseum3api.ui.main.MainScreen
import com.michaelrmossman.aucklandmuseum3api.ui.main.MainViewModel
import com.michaelrmossman.aucklandmuseum3api.ui.objects.ObjectDetailsScreen
import com.michaelrmossman.aucklandmuseum3api.ui.objects.ObjectsSearchScreen
import com.michaelrmossman.aucklandmuseum3api.ui.persons.PersonDetailsScreen
import com.michaelrmossman.aucklandmuseum3api.ui.persons.PersonsSearchScreen
import com.michaelrmossman.aucklandmuseum3api.ui.settings.SettingsScreen

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun MuseumApp(
    onSaveImageToFile: (Bitmap, String) -> Unit,
    windowWidthSize: WindowWidthSizeClass,
    windowHeightSize: WindowHeightSizeClass
) {

    val mainViewModel: MainViewModel = viewModel(
        factory = MainViewModel.Factory
    )

    val adaptiveInfo = currentWindowAdaptiveInfo()
    val directive = remember(adaptiveInfo) {
        calculatePaneScaffoldDirective(adaptiveInfo)
            .copy(horizontalPartitionSpacerSize = 0.dp)
    }
    val listDetailStrategy = rememberListDetailSceneStrategy<NavKey>(
        directive = directive
    )
    val faveCount = mainViewModel.faveCount.observeAsState(initial = 0)
    var showBottomSheet by remember { mutableStateOf(false) }
    if (showBottomSheet) {
        StatusBottomSheet(
            onDismissRequest = { showBottomSheet = false }
        )
    }

    /* For the MainScreen content */
    val currentScreenItems = listOf(
        CurrentScreen.CollectionDummy,
        CurrentScreen.ObjectsSearch,
        CurrentScreen.PersonsSearch,
        CurrentScreen.FavesScreen,
        CurrentScreen.HelpScreen,
        CurrentScreen.SettingsScreen
    )
    val onClickActions: List<() -> Unit> = listOf(
        { showBottomSheet = true },
        { mainViewModel.put(CurrentScreen.ObjectsSearch) },
        { mainViewModel.put(CurrentScreen.PersonsSearch) },
        { mainViewModel.put(CurrentScreen.FavesScreen) },
        { mainViewModel.put(CurrentScreen.HelpScreen) },
        { mainViewModel.put(CurrentScreen.SettingsScreen) }
    )
    val screensEnabled = currentScreenItems.map { screen ->
        when (screen == CurrentScreen.FavesScreen) {
            true -> (faveCount.value > 0)
            else -> true
        }
    }

    NavDisplay(
        backStack = mainViewModel.backStack,
        onBack = { mainViewModel.pop() },
        sceneStrategy = listDetailStrategy,

        entryProvider = entryProvider {
            /* Entries in alphabetical order, by CurrentScreen key */

            entry<CurrentScreen.FavesScreen>(
                metadata = ListDetailSceneStrategy.listPane(
                    detailPlaceholder = {
                        ContentPlaceholder(
                            drawableId =
                                R.drawable.outline_bookmark_stacks_24,
                            mediaType = null
                        )
                    }
                )
            ) { currentScreen ->
                FavesListScreen(
                    onClickBackButton = { mainViewModel.pop() },
                    onClickFavourite = { fave, mediaType ->
                        when (mediaType) {
                            MediaType.Object -> mainViewModel.put(
                                CurrentScreen.FaveObject(
                                    fave = fave
                                )
                            )
                            MediaType.Person -> mainViewModel.put(
                                CurrentScreen.FavePerson(
                                    fave = fave
                                )
                            )
                        }
                    }
                )
            }
            entry<CurrentScreen.FaveObject>(
                metadata = ListDetailSceneStrategy.detailPane()
            ) { currentScreen ->
                SingleObjectScreen(
                    favourite = currentScreen.fave,
                    onClickBackButton = { mainViewModel.pop() },
                    onClickImage = { imageObjects ->
                        mainViewModel.put(
                            CurrentScreen.ImagesScreen(
                                imageObjects = imageObjects
                            )
                        )
                    },
                    stringId = currentScreen.titleStringId,
                    windowSize = windowWidthSize
                )
            }
            entry<CurrentScreen.FavePerson>(
                metadata = ListDetailSceneStrategy.detailPane()
            ) { currentScreen ->
                SinglePersonScreen(
                    favourite = currentScreen.fave,
                    onClickBackButton = { mainViewModel.pop() },
                    onClickImage = { imageObjects ->
                        mainViewModel.put(
                            CurrentScreen.ImagesScreen(
                                imageObjects = imageObjects
                            )
                        )
                    },
                    stringId = currentScreen.titleStringId,
                    windowSize = windowWidthSize
                )
            }

            entry<CurrentScreen.HelpScreen> { currentScreen ->
                HelpScreen(
                    currentScreenItems = currentScreenItems,
                    onClickBackButton = { mainViewModel.pop() },
                    stringId = currentScreen.titleStringId
                )
            }

            entry<CurrentScreen.ImagesScreen> { currentScreen ->
                ImagesScreen(
                    images = currentScreen.imageObjects,
                    onClickBackButton = { mainViewModel.pop() },
                    onSaveImageToFile = onSaveImageToFile,
                    stringId = currentScreen.titleStringId
                )
            }

            entry<CurrentScreen.MainScreen> { currentScreen ->
                MainScreen(
                    currentScreenItems = currentScreenItems,
                    onClickActions = onClickActions,
                    screensEnabled = screensEnabled,
                    stringId = currentScreen.titleStringId,
                    windowHeightSize = windowHeightSize,
                    windowWidthSize = windowWidthSize
                )
            }

            entry<CurrentScreen.ObjectDetails>(
                metadata = ListDetailSceneStrategy.detailPane()
            ) { currentScreen ->
                ObjectDetailsScreen(
                    onClickBackButton = {
                        when (
                            windowWidthSize == WindowWidthSizeClass.Compact
                        ) {
                            true -> mainViewModel.pop()
                            /* Note home instead of pop : to
                               skip all backstack entries */
                            else -> mainViewModel.home()
                        }
                    },
                    onClickImage = { imageObjects ->
                        mainViewModel.put(
                            CurrentScreen.ImagesScreen(
                                imageObjects = imageObjects
                            )
                        )
                    },
                    result = currentScreen.`object`,
                    stringId = currentScreen.titleStringId,
                    windowSize = windowWidthSize
                )
            }
            entry<CurrentScreen.ObjectsSearch>(
                metadata = ListDetailSceneStrategy.listPane(
                    detailPlaceholder = {
                        ContentPlaceholder(
                            drawableId =
                                R.drawable.outline_category_search_24,
                            mediaType = MediaType.Object
                        )
                    }
                )
            ) { currentScreen ->
                ObjectsSearchScreen(
                    drawableId = currentScreen.drawableId,
                    onClickBackButton = { mainViewModel.pop() },
                    onClickSearchResult = { isSubsequent, result ->
                        mainViewModel.replace(
                            CurrentScreen.ObjectDetails(
                                `object` = result
                            ),
                            isSubsequent
                        )
                    },
                    windowHeightSize = windowHeightSize,
                    windowWidthSize = windowWidthSize
                )
            }

            entry<CurrentScreen.PersonDetails>(
                metadata = ListDetailSceneStrategy.detailPane()
            ) { currentScreen ->
                PersonDetailsScreen(
                    onClickBackButton = {
                        when (
                            windowWidthSize == WindowWidthSizeClass.Compact
                        ) {
                            true -> mainViewModel.pop()
                            /* Note home instead of pop : to
                               skip all backstack entries */
                            else -> mainViewModel.home()
                        }
                    },
                    onClickImage = { imageObjects ->
                        mainViewModel.put(
                            CurrentScreen.ImagesScreen(
                                imageObjects = imageObjects
                            )
                        )
                    },
                    result = currentScreen.person,
                    stringId = currentScreen.titleStringId,
                    windowSize = windowWidthSize
                )
            }
            entry<CurrentScreen.PersonsSearch>(
                metadata = ListDetailSceneStrategy.listPane(
                    detailPlaceholder = {
                        ContentPlaceholder(
                            drawableId =
                                R.drawable.outline_person_24,
                            mediaType = MediaType.Person
                        )
                    }
                )
            ) { currentScreen ->
                PersonsSearchScreen(
                    drawableId = currentScreen.drawableId,
                    onClickBackButton = { mainViewModel.pop() },
                    onClickSearchResult = { isSubsequent, result ->
                        mainViewModel.replace(
                            CurrentScreen.PersonDetails(
                                person = result
                            ),
                            isSubsequent
                        )
                    },
                    windowHeightSize = windowHeightSize,
                    windowWidthSize = windowWidthSize
                )
            }

            entry<CurrentScreen.SettingsScreen> { currentScreen ->
                SettingsScreen(
                    onClickBackButton = { mainViewModel.pop() },
                    stringId = currentScreen.titleStringId
                )
            }
        }
    )
}