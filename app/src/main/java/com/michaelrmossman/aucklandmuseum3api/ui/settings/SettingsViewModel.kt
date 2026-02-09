package com.michaelrmossman.aucklandmuseum3api.ui.settings

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
import com.michaelrmossman.aucklandmuseum3api.data.HistoryRepository
import com.michaelrmossman.aucklandmuseum3api.data.SettingsRepository
import com.michaelrmossman.aucklandmuseum3api.state.SettingsUiState
import com.michaelrmossman.aucklandmuseum3api.util.DEBUG_SHOW_ADDITIONAL_MESSAGES
import com.michaelrmossman.aucklandmuseum3api.util.SETTING_COMMON_NUM_RESULTS
import com.michaelrmossman.aucklandmuseum3api.util.SETTING_COMMON_SAVE_HISTORY
import com.michaelrmossman.aucklandmuseum3api.util.SETTING_SAVE_FAVOURITES_DATE
import com.michaelrmossman.aucklandmuseum3api.util.SETTING_COMMON_SORT_ORDER
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val historyRepository : HistoryRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _settingsUiState by lazy { MutableStateFlow(SettingsUiState()) }
    val settingsUiState: StateFlow<SettingsUiState> = _settingsUiState

    /* Live Data */

    val resultsSize: LiveData<Int> = settingsRepository.resultsSize.asLiveData()

    val qtyHistory: LiveData<Int> = historyRepository.getHistoryItemsCount().asLiveData()

    val saveFaveDate: LiveData<Int> = settingsRepository.saveFaveDate.asLiveData()

    val saveHistory: LiveData<Int> = settingsRepository.saveHistory.asLiveData()

    val sortOrder: LiveData<Int> = settingsRepository.sortOrder.asLiveData()

    // val zoomFullImage: LiveData<Int> = settingsRepository.zoomFullImage.asLiveData()

    /* Restore Defaults */

    fun clearHistoryItems() {
        viewModelScope.launch {
            val deleted = historyRepository.deleteAllHistoryItems()
            _settingsUiState.update { currentState ->
                currentState.copy(
                    historyItemsDeleted = deleted
                )
            }
        }
    }

    fun resetAllSettings() {
        viewModelScope.launch {
            val result = settingsRepository.resetAllSettings()
            if (DEBUG_SHOW_ADDITIONAL_MESSAGES) {
                Log.d(TAG,"All settings ($result)")
            }
        }
    }

    fun resetHistoryState() {
        _settingsUiState.update { currentState ->
            currentState.copy(
                historyItemsDeleted = 0
            )
        }
    }

    /* Save Settings */

    fun saveSetting(settingId: String, setting: Int) {
        viewModelScope.launch {
            val result = settingsRepository.saveSetting(
                settingId = settingId,
                setting   = setting
            )
            if (DEBUG_SHOW_ADDITIONAL_MESSAGES) {
                Log.d(TAG,"$settingId: $setting ($result)")
            }
        }
    }

    fun setResultsSize(index: Int) {
        saveSetting(SETTING_COMMON_NUM_RESULTS, index)
        settingsRepository.setResultsSize(index)
    }

    fun setSaveFaveDate(saveDate: Int) {
        saveSetting(SETTING_SAVE_FAVOURITES_DATE, saveDate)
        settingsRepository.setSaveFaveDate(saveDate)
    }

    fun setSaveHistory(save: Int) {
        saveSetting(SETTING_COMMON_SAVE_HISTORY, save)
        settingsRepository.setSaveHistory(save)
    }

    fun setSortOrder(order: Int) {
        saveSetting(SETTING_COMMON_SORT_ORDER, order)
        settingsRepository.setSortOrder(order)
    }

//    fun setZoomFullImage(zoomImage: Int) {
//        saveSetting(SETTING_ZOOM_IMAGE_FULL_SCRN, zoomImage)
//        settingsRepository.setZoomFullImage(zoomImage)
//    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MuseumApplication)
                val historyRepository = application.container.historyRepository
                val settingsRepository = application.container.settingsRepository
                SettingsViewModel(historyRepository, settingsRepository)
            }
        }
        private const val TAG = "SettingsViewModel"
    }
}