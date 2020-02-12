package com.prasanjit.watstheweather.service.model

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Created by Prasanjit on 2019-07-09.
 * example for using embedded & relation
 */
data class AllWeather(
    @Embedded
    val weatherInfo: WeatherInfo,

    @Relation(parentColumn = "id",
        entityColumn = "id")

    val weather : List<Weather>

)