package com.michaelrmossman.aucklandmuseum3api.ui.objects

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.michaelrmossman.aucklandmuseum3api.MuseumApplication
import com.michaelrmossman.aucklandmuseum3api.data.FavouritesRepository
import com.michaelrmossman.aucklandmuseum3api.data.HistoryRepository
import com.michaelrmossman.aucklandmuseum3api.data.NetworkObjectsRepository
import com.michaelrmossman.aucklandmuseum3api.data.SettingsRepository
import com.michaelrmossman.aucklandmuseum3api.enum.Collection
import com.michaelrmossman.aucklandmuseum3api.enum.MediaType
import com.michaelrmossman.aucklandmuseum3api.enum.SortOpecBy
import com.michaelrmossman.aucklandmuseum3api.model.OpacObject
import com.michaelrmossman.aucklandmuseum3api.state.ObjectsListState
import com.michaelrmossman.aucklandmuseum3api.state.ObjectsListState.Companion.START_FROM
import com.michaelrmossman.aucklandmuseum3api.state.SearchUiState
import com.michaelrmossman.aucklandmuseum3api.util.facets
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.IOException

class ObjectsViewModel(
    private val favesRepository   : FavouritesRepository,
    private val historyRepository : HistoryRepository,
    private val networkRepository : NetworkObjectsRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel(), DefaultLifecycleObserver {

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        /* Perform actions when associated UI component resumes.
           Refer to *ListScreen[s] for actual lifecycleOwner. In
           this case, either populate, or tear down, history */
        setHistory()
    }

    fun cancelDownloadState() {
        if (
            resultsListState.value.resultState
            ==
            SearchUiState.Downloading
        ) {
            _resultsListState.update { currentState ->
                currentState.copy(
                    resultState = SearchUiState.Init,
                    searchQuery = String()
                )
            }
        }
    }

    fun getObjectSearchResults(
        query: String,
        startFrom: Int = 0
    ) {
        val facets = resultsListState.value.collections.facets()
        val searchQuery = query.trim()
        _resultsListState.update { currentState ->
            currentState.copy(
                resultCount = when (startFrom) {
                    0    -> 0
                    else -> currentState.resultCount
                },
                resultsList = when (startFrom) {
                    0    -> emptyList()
                    else -> currentState.resultsList
                },
                resultState = when (startFrom) {
                    0    -> SearchUiState.Downloading
                    else -> SearchUiState.GettingMore
                },
                searchQuery = searchQuery
            )
        }
        viewModelScope.launch {
            val sortOrder =
                SortOpecBy.entries[settingsRepository.sortOrder.first()]
            try {
                networkRepository.getObjectSearchResults(
                    startFrom = startFrom,
                    sortOrder = sortOrder.toString(),
                    query = searchQuery,
                    facets = facets,
                    callback = { response ->
                        when (response.responseCode) {
                            200 -> {
                                response.searchResults?.let { results ->
                                    when (results.totalObjects) {
                                        0    -> updateEmptyList()
                                        else -> updateResultsList(
                                            totalObjects = results.totalObjects,
                                            resultsList = results.opacObjects,
                                            startFrom = startFrom
                                        )
                                    }
                                }
                            }
                            else -> updateUiState(
                                message = response.responseMessage,
                                resultCode = response.responseCode
                            )
                        }
                    }
                )

            } catch (exception: IOException) {
                _resultsListState.update { currentState ->
                    currentState.copy(
                        resultsList = emptyList(),
                        resultState = SearchUiState.Error
                    )
                }
                Log.d(TAG,exception.toString())
            }
        }
    }

    suspend fun isFavourite(
        itemId: String
    ) : Boolean = favesRepository.isFavourite(
        itemId = itemId,
        itemType = MediaType.Object
    )

    fun manageSearch(collection: Collection?) {
        _resultsListState.update { currentState ->
            currentState.copy(
                collections = when (collection) {
                    /* Clear / reset (search all) */
                    null -> emptyList()
                    /* Set to a SINGLE collection */
                    Collection.CollectionDocHeritage,
                    Collection.CollectionHumanHistory,
                    Collection.CollectionNatSciences -> {
                        listOf(collection)
                    }
                    /* or collection + department */
                    else -> listOf(
                        collection.parent,
                        collection
                    )
                }
            )
        }
    }

    fun resetErrorState() {
        _resultsListState.update { currentState ->
            currentState.copy(
                resultState = SearchUiState.Init
            )
        }
    }

    fun resetListState() {
        /* Reset all EXCEPT collections|historyList */
        val collections = resultsListState.value.collections
//        with(resultsListState.value.collections) {
//            when (size) {
//                0 -> emptyList()
//                else -> this
//            }
//        }
        val historyList = resultsListState.value.historyList
//        with(resultsListState.value.historyList) {
//            when (size) {
//                0 -> emptyList()
//                else -> this
//            }
//        }
        _resultsListState.value = ObjectsListState()
        _resultsListState.update { currentState ->
            currentState.copy(
                collections = collections,
                historyList = historyList
            )
        }
    }

    private val _resultsListState by lazy {
        MutableStateFlow(ObjectsListState())
    }
    val resultsListState: StateFlow<ObjectsListState>
        get() = _resultsListState

    fun setHistory() {
        viewModelScope.launch {
            _resultsListState.update { currentState ->
                currentState.copy(
                    historyList = when (
                        settingsRepository.saveHistory.first()
                    ) {
                        0 -> emptyList()
                        else -> historyRepository.getHistoryItemsByMediaType(
                            media = MediaType.Object
                        )
                    }
                )
            }
        }
    }

    suspend fun toggleFavourite(
        isFavourite: Boolean,
        itemId: String,
        itemType: MediaType,
        subtitle: String,
        title: String
    ) : Int {
        val result = viewModelScope.async {
            when (isFavourite) {
                /* By default, REMOVING an item from DB returns num rows
                   affected, whereas inserting item returns the new item
                   ID, but -1 if an error. Here, we add one if inserting
                   item, meaning (in BOTH cases) error has occurred if 0 */
                true -> favesRepository.deleteFaveByIdAndType(
                    itemId = itemId,
                    itemType = itemType
                )
                else -> favesRepository.insertFave(
                    itemId = itemId,
                    itemType = itemType,
                    subtitle = subtitle,
                    title = title
                ).toInt().plus(1)
            }
        }
        return result.await()
    }

    fun updateEmptyList() { // 200, but NO results
        _resultsListState.update { currentState ->
            currentState.copy(
                resultCount = 0,
                resultsList = emptyList(),
                resultState = SearchUiState.NoResults
            )
        }
    }

    fun updateResultsList( // 200 (success) code
        totalObjects: Int,
        resultsList: List<OpacObject>,
        startFrom: Int
    ) {
        _resultsListState.update { currentState ->
            currentState.copy(
                resultCount = totalObjects,
                resultsList = when (startFrom) {
                    0 -> resultsList
                    else -> currentState.resultsList.plus(resultsList)
                },
                resultState = SearchUiState.Success
            )
        }
        /* Now that "list" & "count" are updated, update "canDownload" */
        _resultsListState.update { currentState ->
            currentState.copy(
                canDownload = (currentState.resultCount
                > /* Debug json search */
                currentState.resultsList.size.plus(START_FROM)),
            )
        }

        viewModelScope.launch {
            /* Save query to database, if user has enabled history option
               & the query (together with mediaType) not already in DB */
            if (settingsRepository.saveHistory.first() == 1) {
                historyRepository.insertHistoryItem(
                    item = resultsListState.value.searchQuery,
                    media = MediaType.Object
                )
                if (!resultsListState.value.historyList.contains(
                    _resultsListState.value.searchQuery
                )) {
                    _resultsListState.update { currentState ->
                        currentState.copy(
                            historyList = currentState.historyList.plus(
                                currentState.searchQuery
                            )
                        )
                    }
                }
            }
        }
    }

    fun updateUiState( // non-200 (error) code
        message: String?,
        resultCode: Int
    ) {
        _resultsListState.update { currentState ->
            currentState.copy(
                responseMsg = message,
                resultCount = 0,
                resultsList = emptyList(),
                resultState = when (resultCode) {
                    401  -> SearchUiState.Forbidden
                    404  -> SearchUiState.NotFound
                    else -> SearchUiState.Error
                }
            )
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MuseumApplication)
                val favesRepository = application.container.favesRepository
                val historyRepository = application.container.historyRepository
                val networkRepository =
                    application.container.networkObjectsRepository
                val settingsRepository = application.container.settingsRepository
                ObjectsViewModel(
                    favesRepository, historyRepository,
                    networkRepository, settingsRepository
                )
            }
        }
        const val TAG = "ObjectsViewModel"
    }
}