package com.prasanjit.watstheweather.service.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.prasanjit.watstheweather.service.model.WeatherInfo

/**
 * Created by Prasanjit on 2019-06-29.
 * Queries for weather table is done here
 */
@Dao
interface WeatherInfoDao {
    @Query("SELECT * from weather_info_table where name = :name")
    fun getWeatherInfo(name: String): LiveData<WeatherInfo>

    // suspend will do this insertion in a separate thread
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(weatherInfo: WeatherInfo): Long

    @Query("DELETE FROM weather_info_table")
    fun deleteAll()
}