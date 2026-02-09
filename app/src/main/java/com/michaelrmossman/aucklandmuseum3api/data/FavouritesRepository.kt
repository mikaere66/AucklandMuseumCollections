package com.michaelrmossman.aucklandmuseum3api.data

import com.michaelrmossman.aucklandmuseum3api.database.FavesDao
import com.michaelrmossman.aucklandmuseum3api.database.SettingsDao
import com.michaelrmossman.aucklandmuseum3api.enum.MediaType
import com.michaelrmossman.aucklandmuseum3api.enum.SortFavesBy
import com.michaelrmossman.aucklandmuseum3api.model.OpacObject
import com.michaelrmossman.aucklandmuseum3api.model.OpacPerson
import com.michaelrmossman.aucklandmuseum3api.state.FavesListState
import com.michaelrmossman.aucklandmuseum3api.state.FavesUiState
import com.michaelrmossman.aucklandmuseum3api.util.SETTING_SAVE_FAVOURITES_DATE
import com.michaelrmossman.aucklandmuseum3api.util.SETTING_SORT_FAVOURITES_BY
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class FavouritesRepository(
    private val favouritesDao: FavesDao,
    private val settingsDao: SettingsDao
) {

    suspend fun deleteAllFavourites(): Int =
        favouritesDao.deleteAllFavourites()

    suspend fun deleteFave(fave: FaveEntity): Int =
        favouritesDao.deleteFave(fave = fave)

    suspend fun deleteFaveByIdAndType(
        itemId: String, itemType: MediaType
    ) : Int = favouritesDao.deleteFaveByIdAndType(
        itemId = itemId, itemType = itemType.toString()
    )

    val faveCount: Flow<Int> = favouritesDao.getFaveCount()

    private val _favesListState by lazy {
        MutableStateFlow(FavesListState())
    }
    val favesListState: StateFlow<FavesListState>
        get() = _favesListState

    private val _favesSortedBy = MutableStateFlow(
        settingsDao.getSettingByIdFlow(
            settingId = SETTING_SORT_FAVOURITES_BY
        )
    )
    val favesSortedBy: Flow<Int>
        get() = _favesSortedBy.value

    private val _favesTimestamp = MutableStateFlow(
        settingsDao.getSettingByIdFlow(
            settingId = SETTING_SAVE_FAVOURITES_DATE
        )
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getAllFavourites(): Flow<List<FaveEntity>> =
        _favesSortedBy.flatMapLatest { sortedBy ->
            _favesTimestamp.flatMapLatest { timed ->

                val saveDateTime = timed.first()
                val sortFavesBy = SortFavesBy.entries[sortedBy.first()]

                favouritesDao.getFavesFlow().map { faves ->

                    faves.sortedBy { fave ->

                        setDownloadState(downloading = false)

                        when (sortFavesBy) {
                            SortFavesBy.Date -> when (saveDateTime) {
                                1 -> fave.added.toString()
                                else -> fave.id.toString()
                            }
                            SortFavesBy.Name -> fave.title
                            SortFavesBy.Type -> fave.media
                        }
                    }
                }
            }
        }

    suspend fun isFavourite(
        itemId: String, itemType: MediaType
    ) : Boolean = favouritesDao.isFavourite(
        itemId = itemId, itemType = itemType.toString()
    )

//    suspend fun insertFave(fave: FaveEntity): Long =
//        favouritesDao.insertFave(fave = fave)

    suspend fun insertFave(
        itemId: String,
        itemType: MediaType,
        subtitle: String,
        title: String,
        /* Only for use in FavesViewModel */
        isFave: Boolean = true
    ) : Long {
        val saveDate = settingsDao.getSettingById(
            settingId = SETTING_SAVE_FAVOURITES_DATE
        )
        val faveEntity = FaveEntity(
            id = 0,
            added = when (saveDate == 1) {
                true -> System.currentTimeMillis()
                else -> 0L
            },
            isFave = isFave,
            itemId = itemId,
            media = itemType.toString(),
            subtitle = subtitle,
            title = title
        )
        return favouritesDao.insertFave(faveEntity)
    }

    fun setDownloadState(downloading: Boolean) {
        _favesListState.update { currentState ->
            currentState.copy(
                listState = when (downloading) {
                    true -> FavesUiState.Downloading
                    else -> FavesUiState.Success
                }
            )
        }
    }

    suspend fun setFavesSortedBy(sortBy: SortFavesBy) {
        val settingEntity = SettingEntity(
            settingId = SETTING_SORT_FAVOURITES_BY,
            setting = sortBy.ordinal
        )
        if (settingsDao.updateSetting(settingEntity) > 0) {
            _favesSortedBy.value = flowOf(sortBy.ordinal)
        }
    }

    fun resetState() {
        _favesListState.value = FavesListState()
    }

    fun updateObjectResult( // 200 (success) code
        result: OpacObject
    ) {
        _favesListState.update { currentState ->
            currentState.copy(
                favObject = result,
                listState = FavesUiState.Success
            )
        }
    }

    fun updatePersonResult( // 200 (success) code
        result: OpacPerson
    ) {
        _favesListState.update { currentState ->
            currentState.copy(
                favPerson = result,
                listState = FavesUiState.Success
            )
        }
    }

    fun updateUiState( // non-200 (error) code
        message: String?,
        resultCode: Int
    ) {
        // Log.d("HEY",resultCode.toString())
        _favesListState.update { currentState ->
            currentState.copy(
                errorMess = message,
                favObject = null,
                favPerson = null,
                listState = when (resultCode) {
                    401 -> FavesUiState.Forbidden
                    404 -> FavesUiState.NotFound
                    else -> FavesUiState.Error
                }
            )
        }
    }
}