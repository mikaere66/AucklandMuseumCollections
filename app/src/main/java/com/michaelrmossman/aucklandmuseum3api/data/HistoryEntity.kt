package com.michaelrmossman.aucklandmuseum3api.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.michaelrmossman.aucklandmuseum3api.database.COLUMN_NAME_HISTORY_ID
import com.michaelrmossman.aucklandmuseum3api.database.COLUMN_NAME_HISTORY_ITEM
import com.michaelrmossman.aucklandmuseum3api.database.COLUMN_NAME_MEDIA
import com.michaelrmossman.aucklandmuseum3api.database.TABLE_NAME_HISTORY

@Entity(tableName = TABLE_NAME_HISTORY) // ∞
data class HistoryEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_NAME_HISTORY_ID)
    val historyId: Int,

    @ColumnInfo(name = COLUMN_NAME_MEDIA)
    val media: String,

    @ColumnInfo(name = COLUMN_NAME_HISTORY_ITEM)
    val historyItem: String
)