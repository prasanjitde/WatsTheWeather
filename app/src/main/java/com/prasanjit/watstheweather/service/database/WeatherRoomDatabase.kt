package com.prasanjit.watstheweather.service.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.prasanjit.watstheweather.service.dao.WeatherInfoDao
import com.prasanjit.watstheweather.service.model.WeatherInfo
import com.prasanjit.watstheweather.utilities.MainDataConverter
import com.prasanjit.watstheweather.utilities.WeatherDataConverter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Created by Prasanjit on 2019-06-29.
 * This is the backend. The database. This used to be done by the OpenHelper.
 * The fact that this has very few comments emphasizes its coolness.
*/
@Database(entities = [WeatherInfo::class], version = 1, exportSchema = false)
@TypeConverters(WeatherDataConverter::class, MainDataConverter::class)
abstract class WeatherRoomDatabase : RoomDatabase() {
    abstract fun weatherInfoDao(): WeatherInfoDao

    companion object {
        @Volatile
        private var INSTANCE: WeatherRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): WeatherRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WeatherRoomDatabase::class.java,
                    "word_database"
                )
                    // Wipes and rebuilds instead of migrating if no Migration object.
                    // Migration is not part of this codelab.
                    .fallbackToDestructiveMigration()
                    .addCallback(Companion.WeatherDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

        private class WeatherDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            /**
             * Override the onOpen method to populate the database.
             * For this sample, we clear the database every time it is created or opened.
             */
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                // If you want to keep the data through app restarts,
                // comment out the following line.
                INSTANCE?.let { database ->
                    scope.launch {
                        populateDatabase(database.weatherInfoDao())
                    }
                }
            }
        }

        /**
         * Populate the database in a new coroutine.
         * If you want to start with more words, just add them.
         */
        suspend fun populateDatabase(weatherInfoDao: WeatherInfoDao) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
            // weatherInfoDao.deleteAll()
            Log.d("DB ", Thread.currentThread().name)
            // weatherInfoDao.deleteAll()
        }
    }
}