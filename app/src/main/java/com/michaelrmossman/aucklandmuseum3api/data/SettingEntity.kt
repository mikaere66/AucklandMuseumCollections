package com.michaelrmossman.aucklandmuseum3api.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.michaelrmossman.aucklandmuseum3api.MuseumApplication.Companion.instance
import com.michaelrmossman.aucklandmuseum3api.R
import com.michaelrmossman.aucklandmuseum3api.database.COLUMN_NAME_SETTING
import com.michaelrmossman.aucklandmuseum3api.database.COLUMN_NAME_SETTING_ID
import com.michaelrmossman.aucklandmuseum3api.database.TABLE_NAME_SETTING
import com.michaelrmossman.aucklandmuseum3api.util.SETTING_COMMON_NUM_RESULTS
import com.michaelrmossman.aucklandmuseum3api.util.SETTING_COMMON_SAVE_HISTORY
import com.michaelrmossman.aucklandmuseum3api.util.SETTING_COMMON_SORT_ORDER
import com.michaelrmossman.aucklandmuseum3api.util.SETTING_SAVE_FAVOURITES_DATE
import com.michaelrmossman.aucklandmuseum3api.util.SETTING_SORT_FAVOURITES_BY

@Entity(tableName = TABLE_NAME_SETTING) // 6
data class SettingEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = COLUMN_NAME_SETTING_ID)
    val settingId: String,

    @ColumnInfo(name = COLUMN_NAME_SETTING)
    val setting: Int
) {

    companion object {
        fun getSettings(): List<SettingEntity> {
            val settingIds = listOf(
                SETTING_COMMON_NUM_RESULTS,
                SETTING_SAVE_FAVOURITES_DATE,
                SETTING_COMMON_SAVE_HISTORY,
                SETTING_COMMON_SORT_ORDER,
                SETTING_SORT_FAVOURITES_BY,
                // SETTING_ZOOM_IMAGE_FULL_SCRN
            )
            return settingIds.map { settingId ->
                SettingEntity(
                    settingId = settingId,
                    /* Options for num results are
                       4,8,12 and default is 12 */
                    setting = when (settingIds.indexOf(settingId)) {
                        0 -> instance.resources.getStringArray(
                            R.array.settings_num_results
                        ).lastIndex
                        else -> 0
                    }
                )
            }
        }
    }
}