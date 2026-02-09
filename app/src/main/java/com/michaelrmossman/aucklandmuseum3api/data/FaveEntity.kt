package com.michaelrmossman.aucklandmuseum3api.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.michaelrmossman.aucklandmuseum3api.database.COLUMN_NAME_FAVE_ADDED
import com.michaelrmossman.aucklandmuseum3api.database.COLUMN_NAME_FAVE_TITLE
import com.michaelrmossman.aucklandmuseum3api.database.COLUMN_NAME_MEDIA
import com.michaelrmossman.aucklandmuseum3api.database.TABLE_NAME_FAVOURITE
import com.michaelrmossman.aucklandmuseum3api.enum.MediaType
import java.util.concurrent.TimeUnit
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = TABLE_NAME_FAVOURITE) // ∞
data class FaveEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    val id: Int,

    @ColumnInfo(name = COLUMN_NAME_FAVE_ADDED)
    val added: Long,

    /* Only for use in HrefSingleViewModel */
    @ColumnInfo
    val isFave: Boolean,

    /* OpacObject id is actually an integer :
     * since (in a query) it's just sent as a
     * string anyway, just store as string */
    @ColumnInfo
    val itemId: String,

    @ColumnInfo(name = COLUMN_NAME_MEDIA)
    val media: String,

    @ColumnInfo
    val subtitle: String,

    @ColumnInfo(name = COLUMN_NAME_FAVE_TITLE)
    val title: String
) {

    companion object {
        fun getTestFavourites(): List<FaveEntity> {
            val testFaves = mutableListOf<FaveEntity>()
            val testItemIds = listOf("1030121","8473","15677","650711")
            val testMediaTypes = listOf(
                MediaType.Object.name,
                MediaType.Person.name,
                MediaType.Person.name,
                MediaType.Object.name
            )
            val testTitles = listOf(
                "Blue penguin, Hen Island",
                "Edmund Percival Hillary",
                "Olaf Petersen",
                "Saddle, Camel"
            )
            val testSubtitles = listOf(
                "Photography",
                "Individual",
                "Individual",
                "HistoryWar History"
            )
            for (i in 0..3) {
                testFaves.add(
                    FaveEntity(
                        id = 0,
                        /* 14 hours ago, then 14 hours before that */
                        added = System.currentTimeMillis().minus(
                            TimeUnit.HOURS.toMillis(
                                14.times(i.plus(1)).toLong()
                            )
                        ),
                        isFave = true,
                        itemId = testItemIds[i],
                        media = testMediaTypes[i],
                        subtitle = testSubtitles[i],
                        title = testTitles[i]
                    )
                )
            }
            return testFaves
        }
    }
}