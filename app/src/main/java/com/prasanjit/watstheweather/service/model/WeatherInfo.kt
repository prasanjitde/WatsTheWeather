package com.prasanjit.watstheweather.service.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Prasanjit on 2019-06-10.
 */
@Entity(tableName = "weather_info_table")
data class WeatherInfo(
    // required
    @PrimaryKey(autoGenerate = true)
    val weatherId: Int,

    val id: Int?,
    val name: String?,

    val weather: List<Weather>,

    val main: Main
)