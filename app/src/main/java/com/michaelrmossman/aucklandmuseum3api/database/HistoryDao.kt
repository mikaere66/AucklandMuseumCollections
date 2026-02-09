package com.michaelrmossman.aucklandmuseum3api.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import com.michaelrmossman.aucklandmuseum3api.data.HistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {

    @Query("DELETE FROM $TABLE_NAME_HISTORY")
    suspend fun deleteAllHistoryItems(): Int

    @Query("""
        SELECT $COLUMN_NAME_HISTORY_ITEM
        FROM $TABLE_NAME_HISTORY
        WHERE $COLUMN_NAME_MEDIA
        = :media
        ORDER BY $COLUMN_NAME_HISTORY_ID
        DESC
    """)
    suspend fun getHistoryItemsByMedia(
        media: String
    ) : List<String>

    @Query("""
        SELECT COUNT(*)
        FROM $TABLE_NAME_HISTORY
    """)
    fun getHistoryItemsCount(): Flow<Int>

    @Query("""
        SELECT EXISTS
        (
            SELECT $COLUMN_NAME_HISTORY_ITEM,
                   $COLUMN_NAME_MEDIA
            FROM $TABLE_NAME_HISTORY
            WHERE $COLUMN_NAME_HISTORY_ITEM
                = LOWER(:item)
            AND $COLUMN_NAME_MEDIA
                = :media
            LIMIT 1
        )
    """)
    suspend fun historyItemExists(
        item: String, media: String
    ) : Boolean

    @Insert
    suspend fun insertHistoryItem(item: HistoryEntity)
}