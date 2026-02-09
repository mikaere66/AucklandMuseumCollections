package com.michaelrmossman.aucklandmuseum3api.ui.main

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.michaelrmossman.aucklandmuseum3api.MuseumApplication
import com.michaelrmossman.aucklandmuseum3api.data.FavouritesRepository
import com.michaelrmossman.aucklandmuseum3api.data.NetworkObjectsRepository
import com.michaelrmossman.aucklandmuseum3api.data.NetworkPersonsRepository
import com.michaelrmossman.aucklandmuseum3api.enum.SortOpecBy
import com.michaelrmossman.aucklandmuseum3api.objects.ImageObjects
import com.michaelrmossman.aucklandmuseum3api.navigation.CurrentScreen
import com.michaelrmossman.aucklandmuseum3api.state.CollectionUiState
import com.michaelrmossman.aucklandmuseum3api.state.CollectionsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

/* Used for Navigation3 app navigation */
class MainViewModel(
    favesRepository: FavouritesRepository,
    private val objectsRepository : NetworkObjectsRepository,
    private val personsRepository : NetworkPersonsRepository,
    // settingsRepository: SettingsRepository
) : ViewModel() {

    // HelpScreen | FavesScreen | MainScreen
    val backStack = mutableStateListOf<CurrentScreen>(
        CurrentScreen.MainScreen
//        ImagesScreen(
//            imageObjects = ImageObjects()
//        )
    )

    val faveCount = favesRepository.faveCount.asLiveData()

    private fun getDefaultSortOrder(): String {
        return SortOpecBy.entries[0].toString()
    }

    fun home() {
        backStack.forEach { screen ->
            backStack.removeIf { screen ->
                screen != CurrentScreen.MainScreen
            }
        }
    }

    fun pop() {
        backStack.removeLastOrNull()
    }

    fun put(currentScreen: CurrentScreen) {
        backStack.add(currentScreen)
    }

    fun replace(
        currentScreen: CurrentScreen,
        isSubsequent: Boolean
    ) {
        backStack.add(currentScreen)
        if (isSubsequent) {
            val index = backStack.indexOf(currentScreen)
            if (index != -1) {
                backStack.removeAt(index.minus(1))
            }
        }
    }

    private val _resultsListState by lazy {
        MutableStateFlow(CollectionsState())
    }
    val resultsListState: StateFlow<CollectionsState>
        get() = _resultsListState

    fun retrieveObjectsCount() {
        _resultsListState.update { currentState ->
            currentState.copy(
                objectState = CollectionUiState.Downloading
            )
        }
        viewModelScope.launch {
            try {
                objectsRepository.getObjectSearchResults(
                    startFrom = 0,
                    sortOrder = getDefaultSortOrder(),
                    query = String(),
                    facets = null,
                    callback = { response ->
                        when (response.responseCode) {
                            200 -> {
                                response.searchResults?.let { results ->
                                    when (results.totalObjects) {
                                        0    -> updateEmptyObjects()
                                        else -> updateObjectsList(
                                            totalObjects = results.totalObjects,
                                        )
                                    }
                                }
                            }
                            else -> updateObjUiState(
                                message = response.responseMessage,
                                resultCode = response.responseCode
                            )
                        }
                    }
                )

            } catch (exception: IOException) {
                _resultsListState.update { currentState ->
                    currentState.copy(
                        objectCount = 0,
                        objectState = CollectionUiState.Error
                    )
                }
                Log.d(TAG,exception.toString())
            }
        }
    }

    fun retrievePersonsCount() {
        _resultsListState.update { currentState ->
            currentState.copy(
                personState = CollectionUiState.Downloading
            )
        }
        viewModelScope.launch {
            try {
                personsRepository.getPersonSearchResults(
                    startFrom = 0,
                    sortOrder = getDefaultSortOrder(),
                    query = String(),
                    callback = { response ->
                        when (response.responseCode) {
                            200 -> {
                                response.searchResults?.let { results ->
                                    when (results.totalPersons) {
                                        0    -> updateEmptyPersons()
                                        else -> updatePersonsList(
                                            totalPersons = results.totalPersons,
                                        )
                                    }
                                }
                            }
                            else -> updatePerUiState(
                                message = response.responseMessage,
                                resultCode = response.responseCode
                            )
                        }
                    }
                )

            } catch (exception: IOException) {
                _resultsListState.update { currentState ->
                    currentState.copy(
                        personCount = 0,
                        personState = CollectionUiState.Error
                    )
                }
                Log.d(TAG,exception.toString())
            }
        }
    }

    fun updateEmptyObjects() { // 200, but NO results
        _resultsListState.update { currentState ->
            currentState.copy(
                objectCount = 0,
                objectState = CollectionUiState.NoResult
            )
        }
    }

    fun updateEmptyPersons() { // 200, but NO results
        _resultsListState.update { currentState ->
            currentState.copy(
                personCount = 0,
                personState = CollectionUiState.NoResult
            )
        }
    }

    fun updateObjectsList( // 200 (success) code
        totalObjects: Int
    ) {
        _resultsListState.update { currentState ->
            currentState.copy(
                objectCount = totalObjects,
                objectState = CollectionUiState.Success
            )
        }
    }

    fun updateObjUiState( // non-200 (error) code
        message: String?,
        resultCode: Int
    ) {
        _resultsListState.update { currentState ->
            currentState.copy(
                responseObj = message,
                objectCount = 0,
                objectState = when (resultCode) {
                    401  -> CollectionUiState.Forbidden
                    // 404  -> CollectionUiState.NotFound
                    else -> CollectionUiState.Error
                }
            )
        }
    }

    fun updatePersonsList( // 200 (success) code
        totalPersons: Int
    ) {
        _resultsListState.update { currentState ->
            currentState.copy(
                personCount = totalPersons,
                personState = CollectionUiState.Success
            )
        }
    }

    fun updatePerUiState( // non-200 (error) code
        message: String?,
        resultCode: Int
    ) {
        _resultsListState.update { currentState ->
            currentState.copy(
                responsePer = message,
                personCount = 0,
                personState = when (resultCode) {
                    401  -> CollectionUiState.Forbidden
                    // 404  -> CollectionUiState.NotFound
                    else -> CollectionUiState.Error
                }
            )
        }
    }

//    val zoomFullImage: LiveData<Int> =
//        settingsRepository.zoomFullImage.asLiveData()

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MuseumApplication)
                val favesRepository = application.container.favesRepository
                val objectsRepository =
                    application.container.networkObjectsRepository
                val personsRepository =
                    application.container.networkPersonsRepository
                // val settingsRepository = application.container.settingsRepository
                MainViewModel(
                    favesRepository, objectsRepository,
                    personsRepository // , settingsRepository
                )
            }
        }
        const val TAG = "MainViewModel"
    }
}