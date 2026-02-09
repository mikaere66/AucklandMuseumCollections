package com.michaelrmossman.aucklandmuseum3api.ui.favourites

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.michaelrmossman.aucklandmuseum3api.MuseumApplication
import com.michaelrmossman.aucklandmuseum3api.data.FaveEntity
import com.michaelrmossman.aucklandmuseum3api.data.FavouritesRepository
import com.michaelrmossman.aucklandmuseum3api.data.NetworkObjectsRepository
import com.michaelrmossman.aucklandmuseum3api.data.NetworkPersonsRepository
import com.michaelrmossman.aucklandmuseum3api.enum.SortFavesBy
import com.michaelrmossman.aucklandmuseum3api.state.FavesListState
import kotlinx.coroutines.launch
import java.io.IOException

class FavesViewModel(
    private val favesRepository  : FavouritesRepository,
    private val objectsRepository: NetworkObjectsRepository,
    private val personsRepository: NetworkPersonsRepository
) : ViewModel() {

    fun deleteAllFavourites() {
        viewModelScope.launch {
            favesRepository.deleteAllFavourites()
        }
    }

    fun deleteFave(favourite: FaveEntity) {
        viewModelScope.launch {
            favesRepository.deleteFave(fave = favourite)
        }
    }

    val favesListState: LiveData<FavesListState> =
        favesRepository.favesListState.asLiveData()

    val favesSortedBy: LiveData<Int> =
        favesRepository.favesSortedBy.asLiveData()

    fun getObject(
        objectId: String
    ) {
        favesRepository.setDownloadState(downloading = true)

        viewModelScope.launch {
            try {
                objectsRepository.getObjectSearchResultById(
                    id = objectId,
                    callback = { response ->
                        when (response.responseCode) {
                            200 -> response.searchResult?.let { result ->
                                // Log.d(TAG,result.opacObjectId.toString())
                                favesRepository.updateObjectResult(
                                    result = result
                                )
                            }
                            else -> favesRepository.updateUiState(
                                message = response.responseMessage,
                                resultCode = response.responseCode
                            )
                        }
                    }
                )

            } catch (exception: IOException) {
                Log.d(TAG,exception.toString())
            }
        }
    }

    fun getPerson(
        personId: String
    ) {
        favesRepository.setDownloadState(downloading = true)

        viewModelScope.launch {
            try {
                personsRepository.getPersonSearchResultById(
                    id = personId,
                    callback = { response ->
                        when (response.responseCode) {
                            200 -> response.searchResult?.let { result ->
                                // Log.d(TAG,result.opacPersonId.toString())
                                favesRepository.updatePersonResult(
                                    result = result
                                )
                            }
                            else -> favesRepository.updateUiState(
                                message = response.responseMessage,
                                resultCode = response.responseCode
                            )
                        }
                    }
                )

            } catch (exception: IOException) {
                Log.d(TAG,exception.toString())
            }
        }
    }

    fun resetState() = favesRepository.resetState()

    fun setFavesSortedBy(sortBy: SortFavesBy) {
        viewModelScope.launch {
            favesRepository.setFavesSortedBy(sortBy)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MuseumApplication)
                val favesRepository = application.container.favesRepository
                val objectsRepository =
                    application.container.networkObjectsRepository
                val personsRepository =
                    application.container.networkPersonsRepository
                FavesViewModel(
                    favesRepository, objectsRepository, personsRepository
                )
            }
        }
        const val TAG = "FavesViewModel"
    }
}