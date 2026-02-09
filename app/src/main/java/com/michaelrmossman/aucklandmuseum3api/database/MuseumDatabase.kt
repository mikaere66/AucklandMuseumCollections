package com.michaelrmossman.aucklandmuseum3api.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.michaelrmossman.aucklandmuseum3api.data.FaveEntity
import com.michaelrmossman.aucklandmuseum3api.data.HistoryEntity
import com.michaelrmossman.aucklandmuseum3api.data.SettingEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        FaveEntity::class,
        HistoryEntity::class,
        SettingEntity::class
    ],
    exportSchema = EXPORT_SCHEMA,
    version = DATABASE_VERSION
)
abstract class MuseumDatabase: RoomDatabase() {

    abstract fun favouritesDao(): FavesDao
    abstract fun historyDao()   : HistoryDao
    abstract fun settingsDao()  : SettingsDao

    companion object {
        @Volatile
        private var instance: MuseumDatabase? = null

        fun getDatabase(context: Context): MuseumDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    MuseumDatabase::class.java,
                    DATABASE_NAME
                )
                // .fallbackToDestructiveMigration() // TODO
                .addCallback(object: Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Populate the database on the I/O thread
                        CoroutineScope(Dispatchers.IO).launch {
                            instance?.favouritesDao()?.insertFaves(
                                faves = FaveEntity.getTestFavourites()
                            )
                            instance?.settingsDao()?.insertSettings(
                                settings = SettingEntity.getSettings()
                            )
                        }
                    }
                })
                .build()
                .also { instance = it}
            }
        }
    }
}