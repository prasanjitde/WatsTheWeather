package com.prasanjit.watstheweather.service.model

import androidx.annotation.NonNull
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.prasanjit.watstheweather.utilities.WeatherDataConverter

/**
 * Created by Prasanjit on 2019-06-10.
 */
@Entity(tableName = "weather_info_table")
data class WeatherInfo(
    // required
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val weatherId: Int,

    val id: Int?,
    val name: String?,

    val weather: List<Weather>,

    val main: Main
)