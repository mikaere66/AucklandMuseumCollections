package com.michaelrmossman.aucklandmuseum3api.data

import com.michaelrmossman.aucklandmuseum3api.database.SettingsDao
import com.michaelrmossman.aucklandmuseum3api.util.SETTING_COMMON_NUM_RESULTS
import com.michaelrmossman.aucklandmuseum3api.util.SETTING_COMMON_SAVE_HISTORY
import com.michaelrmossman.aucklandmuseum3api.util.SETTING_COMMON_SORT_ORDER
import com.michaelrmossman.aucklandmuseum3api.util.SETTING_SAVE_FAVOURITES_DATE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

class SettingsRepository(
    private val settingsDao: SettingsDao
) {

    suspend fun resetAllSettings(): Int {
        val settings = SettingEntity.getSettings()
        val result = settingsDao.updateSettings(settings)

        setResultsSize(index = 0)
        setSaveHistory(save = 0)

        return result
    }

    private val _resultsSize = MutableStateFlow(
        settingsDao.getSettingByIdFlow(
            settingId = SETTING_COMMON_NUM_RESULTS
        )
    )
    val resultsSize: Flow<Int>
        get() = _resultsSize.value
    fun setResultsSize(index: Int) {
        _resultsSize.value = flowOf(index)
    }

    private val _saveFaveDate = MutableStateFlow(
        settingsDao.getSettingByIdFlow(
            settingId = SETTING_SAVE_FAVOURITES_DATE
        )
    )
    val saveFaveDate: Flow<Int>
        get() = _saveFaveDate.value
    fun setSaveFaveDate(saveDate: Int) {
        _saveFaveDate.value = flowOf(saveDate)
    }

    private val _saveHistory = MutableStateFlow(
        settingsDao.getSettingByIdFlow(
            settingId = SETTING_COMMON_SAVE_HISTORY
        )
    )
    val saveHistory: Flow<Int>
        get() = _saveHistory.value
    fun setSaveHistory(save: Int) {
        _saveHistory.value = flowOf(save)
    }

    private val _sortOrder = MutableStateFlow(
        settingsDao.getSettingByIdFlow(
            settingId = SETTING_COMMON_SORT_ORDER
        )
    )
    val sortOrder: Flow<Int>
        get() = _sortOrder.value
    fun setSortOrder(order: Int) {
        _sortOrder.value = flowOf(order)
    }

    suspend fun saveSetting(
        settingId: String, setting: Int
    ) : Long {
        val settingEntity = SettingEntity(
            settingId = settingId,
            setting = setting
        )
        /* Note upsert : in case new Setting
           is added after production/install */
        return settingsDao.upsertSetting(settingEntity)
    }

//    private val _zoomFullImage = MutableStateFlow(
//        settingsDao.getSettingByIdFlow(
//            settingId = SETTING_ZOOM_IMAGE_FULL_SCRN
//        )
//    )
//    val zoomFullImage: Flow<Int>
//        get() = _zoomFullImage.value
//    fun setZoomFullImage(zoomImage: Int) {
//        _zoomFullImage.value = flowOf(zoomImage)
//    }
}